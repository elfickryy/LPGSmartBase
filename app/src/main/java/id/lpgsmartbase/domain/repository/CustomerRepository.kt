package id.lpgsmartbase.domain.repository

import id.lpgsmartbase.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun observeActive(): Flow<List<Customer>>
    suspend fun add(customer: Customer): Long
}
