package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.lpgsmartbase.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao interface PelangganDao { @Query("SELECT * FROM pelanggan WHERE aktif = 1 ORDER BY nama") fun observeAll(): Flow<List<PelangganEntity>>; @Insert suspend fun insert(data: PelangganEntity): Long; @Query("UPDATE pelanggan SET aktif = 0 WHERE id = :id") suspend fun nonaktifkan(id: Long) }
@Dao interface StokDao { @Query("SELECT * FROM stok ORDER BY jenis") fun observeAll(): Flow<List<StokEntity>>; @Query("SELECT * FROM stok WHERE jenis = :jenis") suspend fun get(jenis: String): StokEntity?; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(data: StokEntity) }
@Dao interface TransaksiDao { @Query("SELECT * FROM transaksi ORDER BY waktu DESC") fun observeAll(): Flow<List<TransaksiEntity>>; @Query("SELECT * FROM transaksi WHERE waktu BETWEEN :mulai AND :selesai ORDER BY waktu DESC") fun observeRentang(mulai: Long, selesai: Long): Flow<List<TransaksiEntity>>; @Insert suspend fun insert(data: TransaksiEntity): Long }
@Dao interface JatahDao { @Query("SELECT * FROM jatah WHERE periode = :periode") fun observePeriode(periode: String): Flow<List<JatahEntity>>; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(data: JatahEntity) }
@Dao interface KasDao { @Query("SELECT * FROM kas ORDER BY waktu DESC") fun observeAll(): Flow<List<KasEntity>>; @Insert suspend fun insert(data: KasEntity): Long }
@Dao interface SettingDao { @Query("SELECT * FROM setting WHERE kunci = :kunci") fun observe(kunci: String): Flow<SettingEntity?>; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(data: SettingEntity) }
@Dao interface OperasionalDao { @Insert suspend fun mutasi(data: MutasiStokEntity): Long; @Insert suspend fun historiJatah(data: HistoriJatahEntity): Long; @Insert suspend fun titip(data: TitipTabungEntity): Long; @Query("SELECT * FROM jatah WHERE pelangganId = :pelangganId AND periode = :periode") suspend fun jatah(pelangganId: Long, periode: String): JatahEntity?; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun simpanJatah(data: JatahEntity): Long; @Query("SELECT * FROM saldo_titipan WHERE pelangganId = :pelangganId") suspend fun saldoTitipan(pelangganId: Long): SaldoTitipanEntity?; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun simpanSaldoTitipan(data: SaldoTitipanEntity) }
