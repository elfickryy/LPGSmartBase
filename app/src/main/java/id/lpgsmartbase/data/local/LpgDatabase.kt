package id.lpgsmartbase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.lpgsmartbase.data.local.dao.*
import id.lpgsmartbase.data.local.entity.*

@Database(entities = [PelangganEntity::class, StokEntity::class, MutasiStokEntity::class, TransaksiEntity::class, JatahEntity::class, HistoriJatahEntity::class, TitipTabungEntity::class, SaldoTitipanEntity::class, KasEntity::class, SettingEntity::class], version = 3, exportSchema = true)
abstract class LpgDatabase : RoomDatabase() {
    abstract fun pelangganDao(): PelangganDao; abstract fun stokDao(): StokDao; abstract fun transaksiDao(): TransaksiDao; abstract fun jatahDao(): JatahDao; abstract fun kasDao(): KasDao; abstract fun settingDao(): SettingDao; abstract fun operasionalDao(): OperasionalDao
    companion object { const val NAME = "lpg_smartbase.db" }
}
