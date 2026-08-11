package id.lpgsmartbase.domain.model

data class Customer(
    val id: Long = 0,
    val code: String,
    val name: String,
    val address: String,
    val phone: String,
    val isActive: Boolean = true
)
