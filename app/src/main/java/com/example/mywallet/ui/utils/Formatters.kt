package com.example.mywallet.ui.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
    maximumFractionDigits = 0
}

private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
private val monthFormat = SimpleDateFormat("MMMM", Locale("id", "ID"))

fun formatRupiah(amount: Int): String = rupiahFormat.format(amount)

fun formatTransactionAmount(amount: Int, isIncome: Boolean): String {
    val signedAmount = if (isIncome) amount else -amount
    return if (signedAmount >= 0) {
        "+${rupiahFormat.format(signedAmount)}"
    } else {
        rupiahFormat.format(signedAmount)
    }
}

fun formatDate(timestampMillis: Long): String = dateFormat.format(Date(timestampMillis))

fun monthName(month: Int): String {
    val date = Calendar.getInstance().apply {
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, 1)
    }.time
    return monthFormat.format(date).replaceFirstChar { it.uppercase() }
}

fun monthShortName(month: Int): String {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")
    return months.getOrElse(month - 1) { "Jan" }
}
