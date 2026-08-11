package id.lpgsmartbase.domain.repository
import id.lpgsmartbase.domain.model.Pelanggan
import kotlinx.coroutines.flow.Flow
interface PelangganRepository { fun observeAll(): Flow<List<Pelanggan>>; suspend fun tambah(pelanggan: Pelanggan): Long }
