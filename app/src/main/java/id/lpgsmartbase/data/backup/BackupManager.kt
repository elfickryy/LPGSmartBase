package id.lpgsmartbase.data.backup

import android.content.Context
import id.lpgsmartbase.data.local.LpgDatabase
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream

object BackupManager {
    private const val MAGIC = "LPGSMARTBASE"
    const val VERSION = 1
    fun isValid(input: InputStream): Boolean = runCatching {
        DataInputStream(input).use { source ->
            require(source.readUTF() == MAGIC && source.readInt() == VERSION)
            val size = source.readLong()
            require(size in 1..100_000_000)
            copyExactly(source, object : OutputStream() { override fun write(value: Int) {} }, size)
            require(source.read() == -1)
            true
        }
    }.getOrDefault(false)

    fun backup(context: Context, database: LpgDatabase, output: OutputStream) {
        database.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close()
        val source = context.getDatabasePath(LpgDatabase.NAME)
        require(source.exists() && source.length() > 0) { "Database tidak tersedia" }
        DataOutputStream(output).use { out -> out.writeUTF(MAGIC); out.writeInt(VERSION); out.writeLong(source.length()); source.inputStream().use { it.copyTo(out) }; out.flush() }
    }

    fun restore(context: Context, database: LpgDatabase, input: InputStream) {
        val staged = File(context.cacheDir, "restore-${LpgDatabase.NAME}.tmp")
        val target = context.getDatabasePath(LpgDatabase.NAME)
        val previous = File(target.parentFile, "${LpgDatabase.NAME}.previous")
        try {
            DataInputStream(input).use { source ->
                require(source.readUTF() == MAGIC) { "Format backup tidak dikenal" }
                require(source.readInt() == VERSION) { "Versi backup tidak didukung" }
                val size = source.readLong()
                require(size in 1..100_000_000) { "Ukuran backup tidak valid" }
                staged.outputStream().use { copyExactly(source, it, size) }
                require(source.read() == -1) { "Backup berisi data tidak valid" }
                require(staged.length() == size) { "Backup tidak lengkap" }
                require(staged.inputStream().use { databaseHeader(it) } == "SQLite format 3\u0000") { "Isi backup bukan database LPG SmartBase" }
            }
            database.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close()
            database.close()
            File("${target.path}-wal").delete(); File("${target.path}-shm").delete()
            previous.delete()
            val hadDatabase = target.exists()
            if (hadDatabase) require(target.renameTo(previous)) { "Database lama tidak dapat diamankan" }
            try {
                staged.copyTo(target, overwrite = false)
                previous.delete()
            } catch (error: Throwable) {
                target.delete()
                if (hadDatabase) previous.renameTo(target)
                throw error
            }
        } finally {
            staged.delete()
        }
    }

    private fun copyExactly(source: InputStream, target: OutputStream, size: Long) {
        var remaining = size
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (remaining > 0) {
            val read = source.read(buffer, 0, minOf(buffer.size.toLong(), remaining).toInt())
            require(read > 0) { "Backup tidak lengkap" }
            target.write(buffer, 0, read)
            remaining -= read
        }
    }

    private fun databaseHeader(input: InputStream): String {
        val bytes = ByteArray(16)
        var offset = 0
        while (offset < bytes.size) {
            val read = input.read(bytes, offset, bytes.size - offset)
            require(read > 0) { "Backup tidak lengkap" }
            offset += read
        }
        return String(bytes, Charsets.US_ASCII)
    }
}
