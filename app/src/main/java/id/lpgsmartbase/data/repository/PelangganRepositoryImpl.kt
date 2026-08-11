package id.lpgsmartbase.data.repository

import id.lpgsmartbase.data.local.dao.PelangganDao
import id.lpgsmartbase.data.mapper.toDomain
import id.lpgsmartbase.data.mapper.toEntity
import id.lpgsmartbase.domain.model.Pelanggan
import id.lpgsmartbase.domain.repository.PelangganRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PelangganRepositoryImpl(private val dao: PelangganDao) : PelangganRepository {
    override fun observeAktif(): Flow<List<Pelanggan>> = dao.observeAktif().map { daftar -> daftar.map { it.toDomain() } }
    override suspend fun simpan(pelanggan: Pelanggan): Long = dao.insert(pelanggan.toEntity())
    override suspend fun nonaktifkan(id: Long) = dao.nonaktifkan(id)
}
