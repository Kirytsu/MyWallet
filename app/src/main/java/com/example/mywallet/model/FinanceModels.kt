package com.example.mywallet.model

enum class MainTab(val title: String) {
    DASHBOARD("Dashboard"),
    HISTORY("Histori"),
    REPORT("Laporan")
}

enum class ReportPeriod(val title: String) {
    WEEK("Minggu"),
    MONTH("Bulan"),
    YEAR("Tahun")
}

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class FinanceCategory(
    val title: String,
    val type: TransactionType
) {
    // Pendapatan (Income)
    SALARY("Gaji", TransactionType.INCOME),
    TRANSFER_IN("Transfer Masuk", TransactionType.INCOME),
    BONUS("Bonus", TransactionType.INCOME),
    INCOME_OTHERS("Pendapatan Lain", TransactionType.INCOME),

    // Pengeluaran (Expense)
    FOOD_DRINK("Makan & Minum", TransactionType.EXPENSE),
    TRANSPORT("Transportasi", TransactionType.EXPENSE),
    SHOPPING("Belanja", TransactionType.EXPENSE),
    BILL_PAYMENT("Tagihan", TransactionType.EXPENSE),
    ENTERTAINMENT("Hiburan", TransactionType.EXPENSE),
    EXPENSE_OTHERS("Pengeluaran Lain", TransactionType.EXPENSE)
}

data class WalletTransaction(
    val id: Int,
    val title: String,
    val description: String,
    val amount: Int,
    val type: TransactionType,
    val category: FinanceCategory,
    val timestampMillis: Long
)

data class ReportCategoryItem(
    val category: FinanceCategory,
    val amount: Int,
    val percentage: Int
)

data class WalletSummary(
    val balance: Int,
    val totalIncome: Int,
    val totalExpense: Int,
    val limitMonthly: Int,
    val spentMonthly: Int
)
