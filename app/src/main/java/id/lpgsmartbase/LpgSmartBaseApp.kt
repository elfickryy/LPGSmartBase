package id.lpgsmartbase

import android.app.Application
import androidx.room.Room
import id.lpgsmartbase.data.local.LpgDatabase
import id.lpgsmartbase.data.local.DatabaseMigrations

class LpgSmartBaseApp : Application() {
    val database: LpgDatabase by lazy { Room.databaseBuilder(this, LpgDatabase::class.java, LpgDatabase.NAME).addMigrations(DatabaseMigrations.V1_TO_V2, DatabaseMigrations.V2_TO_V3).build() }
}
