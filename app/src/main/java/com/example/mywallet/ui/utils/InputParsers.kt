package com.example.mywallet.ui.utils

private val nonDigitRegex = Regex("[^0-9]")

fun parseNominalRupiah(input: String): Result<Int> {
    val cleaned = input
        .replace("Rp", "", ignoreCase = true)
        .replace(nonDigitRegex, "")

    if (cleaned.isBlank()) {
        return Result.failure(IllegalArgumentException("Nominal wajib diisi."))
    }

    if (cleaned.length > 12) {
        return Result.failure(IllegalArgumentException("Nominal terlalu besar."))
    }

    val parsed = cleaned.toLongOrNull()
        ?: return Result.failure(IllegalArgumentException("Format nominal tidak valid."))

    if (parsed <= 0L) {
        return Result.failure(IllegalArgumentException("Nominal harus lebih dari 0."))
    }

    if (parsed > Int.MAX_VALUE.toLong()) {
        return Result.failure(IllegalArgumentException("Nominal melebihi batas sistem."))
    }

    return Result.success(parsed.toInt())
}
