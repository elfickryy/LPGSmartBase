package id.lpgsmartbase.data.mapper

import id.lpgsmartbase.data.local.entity.PelangganEntity
import id.lpgsmartbase.domain.model.Pelanggan

fun PelangganEntity.toDomain() = Pelanggan(id, kode, nama, alamat, telepon, aktif)
fun Pelanggan.toEntity() = PelangganEntity(id, kode, nama, alamat, telepon, aktif)
