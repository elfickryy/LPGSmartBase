package id.lpgsmartbase.data.mapper

import id.lpgsmartbase.data.local.entity.CustomerEntity
import id.lpgsmartbase.domain.model.Customer

fun CustomerEntity.toDomain() = Customer(id, code, name, address, phone, active)
fun Customer.toEntity() = CustomerEntity(id, code, name, address, phone, isActive)
