package id.lpgsmartbase.data.repository

import id.lpgsmartbase.data.local.dao.CustomerDao
import id.lpgsmartbase.data.mapper.toDomain
import id.lpgsmartbase.data.mapper.toEntity
import id.lpgsmartbase.domain.model.Customer
import id.lpgsmartbase.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomerRepositoryImpl(private val dao: CustomerDao) : CustomerRepository {
    override fun observeActive(): Flow<List<Customer>> = dao.observeActive().map { customers -> customers.map { it.toDomain() } }
    override suspend fun add(customer: Customer): Long = dao.insert(customer.toEntity())
}
