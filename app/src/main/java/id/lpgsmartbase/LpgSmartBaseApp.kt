package id.lpgsmartbase

import android.app.Application
import androidx.room.Room
import id.lpgsmartbase.data.local.LpgDatabase

class LpgSmartBaseApp : Application() {
    val database: LpgDatabase by lazy { Room.databaseBuilder(this, LpgDatabase::class.java, LpgDatabase.NAME).build() }
}
