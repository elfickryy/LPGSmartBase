package id.lpgsmartbase.utils
import java.text.NumberFormat
import java.util.Locale
fun Long.toRupiah(): String = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
