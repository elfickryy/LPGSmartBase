package id.lpgsmartbase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.lpgsmartbase.data.local.dao.JatahDao
import id.lpgsmartbase.data.local.dao.KasDao
import id.lpgsmartbase.data.local.dao.PelangganDao
import id.lpgsmartbase.data.local.dao.SettingDao
import id.lpgsmartbase.data.local.dao.StokDao
import id.lpgsmartbase.data.local.dao.TransaksiDao
import id.lpgsmartbase.data.local.entity.JatahEntity
import id.lpgsmartbase.data.local.entity.KasEntity
import id.lpgsmartbase.data.local.entity.PelangganEntity
import id.lpgsmartbase.data.local.entity.SettingEntity
import id.lpgsmartbase.data.local.entity.StokEntity
import id.lpgsmartbase.data.local.entity.TransaksiEntity

@Database(entities = [PelangganEntity::class, StokEntity::class, TransaksiEntity::class, JatahEntity::class, KasEntity::class, SettingEntity::class], version = 1, exportSchema = true)
abstract class LpgDatabase : RoomDatabase() {
    abstract fun pelangganDao(): PelangganDao
    abstract fun stokDao(): StokDao
    abstract fun transaksiDao(): TransaksiDao
    abstract fun jatahDao(): JatahDao
    abstract fun kasDao(): KasDao
    abstract fun settingDao(): SettingDao

    companion object { const val DATABASE_NAME = "lpg_smartbase.db" }
}
