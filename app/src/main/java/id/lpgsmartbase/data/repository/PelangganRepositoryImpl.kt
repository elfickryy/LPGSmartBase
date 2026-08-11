package id.lpgsmartbase.data.repository
import id.lpgsmartbase.data.local.dao.PelangganDao
import id.lpgsmartbase.data.mapper.toDomain
import id.lpgsmartbase.data.mapper.toEntity
import id.lpgsmartbase.domain.model.Pelanggan
import id.lpgsmartbase.domain.repository.PelangganRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class PelangganRepositoryImpl(private val dao: PelangganDao) : PelangganRepository { override fun observeAll(): Flow<List<Pelanggan>> = dao.observeAll().map { it.map { entity -> entity.toDomain() } }; override suspend fun tambah(pelanggan: Pelanggan) = dao.insert(pelanggan.toEntity()) }
