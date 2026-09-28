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
    fun isValid(input: InputStream): Boolean = runCatching { DataInputStream(input).use { it.readUTF() == MAGIC && it.readInt() == VERSION && it.readLong() >= 0 } }.getOrDefault(false)
    fun backup(context: Context, database: LpgDatabase, output: OutputStream) {
        database.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close()
        val source = context.getDatabasePath(LpgDatabase.NAME)
        require(source.exists() && source.length() > 0) { "Database tidak tersedia" }
        DataOutputStream(output).use { out -> out.writeUTF(MAGIC); out.writeInt(VERSION); out.writeLong(source.length()); source.inputStream().use { it.copyTo(out) }; out.flush() }
    }
    fun restore(context: Context, database: LpgDatabase, input: InputStream) {
        val temp = File(context.filesDir, "restore-${LpgDatabase.NAME}")
        runCatching {
            DataInputStream(input).use { source ->
                require(source.readUTF() == MAGIC) { "Format backup tidak dikenal" }; require(source.readInt() == VERSION) { "Versi backup tidak didukung" }
                val size = source.readLong(); require(size in 1..100_000_000) { "Ukuran backup tidak valid" }
                temp.outputStream().use { target -> source.copyTo(target) }; require(temp.length() == size) { "Backup tidak lengkap" }
            }
            database.close(); context.deleteDatabase(LpgDatabase.NAME)
            val target = context.getDatabasePath(LpgDatabase.NAME); temp.copyTo(target, overwrite = true)
        }.onFailure { temp.delete(); throw it }
    }
}
