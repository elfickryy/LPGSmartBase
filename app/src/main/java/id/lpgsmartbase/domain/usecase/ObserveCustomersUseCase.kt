package id.lpgsmartbase.domain.usecase

import id.lpgsmartbase.domain.repository.CustomerRepository

class ObserveCustomersUseCase(private val repository: CustomerRepository) {
    operator fun invoke() = repository.observeActive()
}
