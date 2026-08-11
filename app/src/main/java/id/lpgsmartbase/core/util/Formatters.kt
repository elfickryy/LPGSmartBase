package id.lpgsmartbase.core.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toRupiah(): String = NumberFormat.getCurrencyInstance(Locale(AppConstants.CURRENCY_LOCALE)).format(this)
fun Long.toDisplayDate(): String = SimpleDateFormat(AppConstants.DATE_PATTERN, Locale(AppConstants.CURRENCY_LOCALE)).format(Date(this))
