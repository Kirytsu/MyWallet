package com.example.mywallet.data

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import com.example.mywallet.model.FinanceCategory
import com.example.mywallet.model.ReportCategoryItem
import com.example.mywallet.model.ReportPeriod
import com.example.mywallet.model.TransactionType
import com.example.mywallet.model.WalletSummary
import com.example.mywallet.model.WalletTransaction
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

object FinanceRepository {

    private const val PREF_NAME = "mywallet_diary_storage"
    private const val KEY_TRANSACTIONS = "transactions"
    private const val KEY_MONTHLY_LIMIT = "monthly_limit"
    private const val DEFAULT_LIMIT = 5_000_000

    private val allTransactionsInternal = mutableStateListOf<WalletTransaction>()
    private var nextIdState = mutableIntStateOf(1)
    private var monthlyLimitState = mutableIntStateOf(DEFAULT_LIMIT)
    private var appContext: Context? = null

    fun initialize(context: Context) {
        if (appContext != null) return
        appContext = context.applicationContext
        loadFromStorage()
    }

    fun allTransactions(): List<WalletTransaction> = allTransactionsInternal

    fun latestTransactions(limit: Int): List<WalletTransaction> =
        allTransactionsInternal.take(limit)

    fun walletSummaryForCurrentMonth(): WalletSummary {
        val month = currentMonth()
        val year = currentYear()
        val monthTransactions = filterTransactions(
            period = ReportPeriod.MONTH,
            selectedMonth = month,
            selectedYear = year
        )

        val totalIncome = monthTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

        val totalExpense = monthTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
        val limit = monthlyLimitState.intValue

        val balance = allTransactionsInternal.sumOf {
            if (it.type == TransactionType.INCOME) it.amount else -it.amount
        }

        return WalletSummary(
            balance = balance,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            limitMonthly = limit,
            spentMonthly = totalExpense
        )
    }

    fun filterTransactions(
        period: ReportPeriod,
        selectedMonth: Int,
        selectedYear: Int
    ): List<WalletTransaction> {
        val filtered = when (period) {
            ReportPeriod.WEEK -> {
                val now = System.currentTimeMillis()
                val weekAgo = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -6)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                allTransactionsInternal.filter { it.timestampMillis in weekAgo..now }
            }

            ReportPeriod.MONTH -> {
                allTransactionsInternal.filter {
                    val cal = Calendar.getInstance().apply { timeInMillis = it.timestampMillis }
                    val month = cal.get(Calendar.MONTH) + 1
                    val year = cal.get(Calendar.YEAR)
                    month == selectedMonth && year == selectedYear
                }
            }

            ReportPeriod.YEAR -> {
                allTransactionsInternal.filter {
                    val cal = Calendar.getInstance().apply { timeInMillis = it.timestampMillis }
                    cal.get(Calendar.YEAR) == selectedYear
                }
            }
        }

        return filtered.sortedByDescending { it.timestampMillis }
    }

    fun reportCategoryItems(transactions: List<WalletTransaction>): List<ReportCategoryItem> {
        val grouped = transactions.groupBy { it.category }
            .mapValues { (_, list) -> list.sumOf { it.amount.absoluteValue } }

        val total = grouped.values.sum().coerceAtLeast(1)

        return grouped
            .map { (category, amount) ->
                ReportCategoryItem(
                    category = category,
                    amount = amount,
                    percentage = ((amount.toFloat() / total.toFloat()) * 100f).roundToInt()
                )
            }
            .sortedByDescending { it.amount }
    }

    fun availableYears(): List<Int> {
        val currentYear = currentYear()
        val yearsFromData = allTransactionsInternal.map {
            Calendar.getInstance().apply { timeInMillis = it.timestampMillis }.get(Calendar.YEAR)
        }

        val minYear = (yearsFromData.minOrNull() ?: currentYear).coerceAtMost(currentYear)
        return (minYear..currentYear).toList()
    }

    fun currentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

    fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

    fun addTransaction(
        amount: Int,
        category: FinanceCategory,
        note: String,
        timestampMillis: Long = System.currentTimeMillis()
    ): Result<WalletTransaction> {
        return runCatching {
            require(amount > 0) { "Nominal harus lebih dari 0." }

            val normalizedNote = note.trim()
            val transaction = WalletTransaction(
                id = nextIdState.intValue++,
                title = category.title,
                description = normalizedNote,
                amount = amount,
                type = category.type,
                category = category,
                timestampMillis = timestampMillis
            )

            allTransactionsInternal.add(0, transaction)
            persistToStorage()
            transaction
        }
    }

    fun deleteTransaction(transactionId: Int): Result<Unit> {
        return runCatching {
            val index = allTransactionsInternal.indexOfFirst { it.id == transactionId }
            require(index >= 0) { "Transaksi tidak ditemukan." }

            allTransactionsInternal.removeAt(index)
            persistToStorage()
        }
    }

    fun updateMonthlyLimit(newLimit: Int): Result<Unit> {
        return runCatching {
            require(newLimit > 0) { "Limit harus lebih dari 0." }
            monthlyLimitState.intValue = newLimit
            persistToStorage()
        }
    }

    private fun loadFromStorage() {
        val context = appContext ?: return
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        
        monthlyLimitState.intValue = prefs.getInt(KEY_MONTHLY_LIMIT, DEFAULT_LIMIT)

        val raw = prefs.getString(KEY_TRANSACTIONS, null)
        if (raw.isNullOrBlank()) {
            allTransactionsInternal.clear()
            nextIdState.intValue = 1
            return
        }

        runCatching {
            val array = JSONArray(raw)
            val loaded = parseTransactionsFromJson(array)

            loaded.sortByDescending { it.timestampMillis }
            allTransactionsInternal.clear()
            allTransactionsInternal.addAll(loaded)
            nextIdState.intValue = (loaded.maxOfOrNull { it.id } ?: 0) + 1
        }.onFailure {
            allTransactionsInternal.clear()
            nextIdState.intValue = 1
        }
    }

    private fun persistToStorage() {
        val context = appContext ?: return
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        
        editor.putInt(KEY_MONTHLY_LIMIT, monthlyLimitState.intValue)

        val array = JSONArray()
        allTransactionsInternal.forEach { tx ->
            array.put(
                JSONObject().apply {
                    put("id", tx.id)
                    put("title", tx.title)
                    put("description", tx.description)
                    put("amount", tx.amount)
                    put("type", tx.type.name)
                    put("category", tx.category.name)
                    put("timestampMillis", tx.timestampMillis)
                }
            )
        }
        editor.putString(KEY_TRANSACTIONS, array.toString())
        editor.apply()
    }

    private fun parseTransactionsFromJson(array: JSONArray): MutableList<WalletTransaction> {
        val loaded = mutableListOf<WalletTransaction>()

        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            val category = parseCategory(item)
            val type = parseType(item)

            loaded += WalletTransaction(
                id = item.optInt("id", i + 1),
                title = item.optString("title", category.title),
                description = item.optString("description", ""),
                amount = item.optInt("amount", 0),
                type = type,
                category = category,
                timestampMillis = item.optLong("timestampMillis", System.currentTimeMillis())
            )
        }

        return loaded
    }

    private fun parseCategory(item: JSONObject): FinanceCategory {
        return runCatching {
            FinanceCategory.valueOf(item.getString("category"))
        }.getOrDefault(FinanceCategory.INCOME_OTHERS)
    }

    private fun parseType(item: JSONObject): TransactionType {
        return runCatching {
            TransactionType.valueOf(item.getString("type"))
        }.getOrDefault(TransactionType.INCOME)
    }
}
