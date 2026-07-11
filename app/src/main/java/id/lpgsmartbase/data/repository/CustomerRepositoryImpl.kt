package id.lpgsmartbase.data.repository

import id.lpgsmartbase.data.local.dao.CustomerDao
import id.lpgsmartbase.data.local.entity.CustomerEntity
import id.lpgsmartbase.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow

class CustomerRepositoryImpl(private val dao: CustomerDao) : CustomerRepository {
    override fun observeActive(): Flow<List<CustomerEntity>> = dao.observeActive()
    override suspend fun add(customer: CustomerEntity): Long = dao.insert(customer)
}
