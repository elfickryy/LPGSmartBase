package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setting")
data class SettingEntity(@PrimaryKey val kunci: String, val nilai: String)
