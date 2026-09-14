package id.lpgsmartbase.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val V1_TO_V2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE stok ADD COLUMN hargaModal INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE stok ADD COLUMN hargaJual INTEGER NOT NULL DEFAULT 0")
            db.execSQL("CREATE TABLE transaksi_baru (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nomorNota TEXT NOT NULL, pelangganId INTEGER, jumlah INTEGER NOT NULL, hargaJual INTEGER NOT NULL DEFAULT 0, hargaModal INTEGER NOT NULL DEFAULT 0, total INTEGER NOT NULL, dibayar INTEGER NOT NULL, statusPembayaran TEXT NOT NULL DEFAULT 'BELUM_DIBAYAR', waktu INTEGER NOT NULL, catatan TEXT NOT NULL DEFAULT '')")
            db.execSQL("INSERT INTO transaksi_baru (id, nomorNota, pelangganId, jumlah, total, dibayar, waktu) SELECT id, nomorNota, pelangganId, jumlah, total, dibayar, waktu FROM transaksi")
            db.execSQL("DROP TABLE transaksi")
            db.execSQL("ALTER TABLE transaksi_baru RENAME TO transaksi")
            db.execSQL("CREATE TABLE IF NOT EXISTS mutasi_stok (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, jenis TEXT NOT NULL, perubahanIsi INTEGER NOT NULL, perubahanKosong INTEGER NOT NULL, tipe TEXT NOT NULL, referensi TEXT NOT NULL, waktu INTEGER NOT NULL, catatan TEXT NOT NULL)")
            db.execSQL("CREATE TABLE IF NOT EXISTS histori_jatah (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, pelangganId INTEGER NOT NULL, periode TEXT NOT NULL, tipe TEXT NOT NULL, jumlah INTEGER NOT NULL, referensi TEXT NOT NULL, waktu INTEGER NOT NULL)")
            db.execSQL("CREATE TABLE IF NOT EXISTS titip_tabung (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, pelangganId INTEGER NOT NULL, jumlahTitipan INTEGER NOT NULL, jumlahKosongMilikPelanggan INTEGER NOT NULL, jumlahIsiDiambil INTEGER NOT NULL, waktu INTEGER NOT NULL, catatan TEXT NOT NULL)")
        }
    }
    val V2_TO_V3 = object : Migration(2, 3) { override fun migrate(db: SupportSQLiteDatabase) { db.execSQL("ALTER TABLE transaksi ADD COLUMN jenisTransaksi TEXT NOT NULL DEFAULT 'PENJUALAN'"); db.execSQL("ALTER TABLE transaksi ADD COLUMN sumberTabung TEXT NOT NULL DEFAULT 'STOK'"); db.execSQL("CREATE TABLE IF NOT EXISTS saldo_titipan (pelangganId INTEGER NOT NULL PRIMARY KEY, saldo INTEGER NOT NULL, diperbaruiPada INTEGER NOT NULL)") } }
}
