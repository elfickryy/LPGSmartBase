package id.lpgsmartbase.domain.repository

import id.lpgsmartbase.domain.model.Pelanggan
import kotlinx.coroutines.flow.Flow

interface PelangganRepository {
    fun observeAktif(): Flow<List<Pelanggan>>
    suspend fun simpan(pelanggan: Pelanggan): Long
    suspend fun nonaktifkan(id: Long)
}
