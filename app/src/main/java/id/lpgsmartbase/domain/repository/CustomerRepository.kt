package id.lpgsmartbase.domain.repository

import id.lpgsmartbase.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun observeActive(): Flow<List<CustomerEntity>>
    suspend fun add(customer: CustomerEntity): Long
}
