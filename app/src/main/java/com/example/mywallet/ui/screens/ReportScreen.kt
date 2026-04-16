package com.example.mywallet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywallet.data.FinanceRepository
import com.example.mywallet.model.ReportPeriod
import com.example.mywallet.model.TransactionType
import com.example.mywallet.ui.components.report.DonutReportCard
import com.example.mywallet.ui.components.report.MonthSelector
import com.example.mywallet.ui.components.report.ReportCategoryGrid
import com.example.mywallet.ui.components.report.ReportPeriodSelector
import com.example.mywallet.ui.components.report.WeekFilterInfo
import com.example.mywallet.ui.components.report.YearSelector
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletTextSecondary
import com.example.mywallet.ui.utils.monthName

@Composable
fun ReportScreen() {
    val currentYear = FinanceRepository.currentYear()
    val currentMonth = FinanceRepository.currentMonth()

    var selectedPeriod by remember { mutableStateOf(ReportPeriod.MONTH) }
    var selectedMonth by remember { mutableIntStateOf(currentMonth) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }

    val filteredTransactions = FinanceRepository.filterTransactions(
        period = selectedPeriod,
        selectedMonth = selectedMonth,
        selectedYear = selectedYear
    )

    val reportItems = FinanceRepository.reportCategoryItems(filteredTransactions)
    val totalFlow = reportItems.sumOf { it.amount }

    val incomeTotal = filteredTransactions
        .filter { it.type == TransactionType.INCOME }
        .sumOf { it.amount }

    val expenseTotal = filteredTransactions
        .filter { it.type == TransactionType.EXPENSE }
        .sumOf { it.amount }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Laporan Keuangan",
                style = MaterialTheme.typography.titleLarge,
                color = MyWalletBlack,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = periodSubtitle(
                    period = selectedPeriod,
                    month = selectedMonth,
                    year = selectedYear
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MyWalletTextSecondary
            )
        }

        item {
            ReportPeriodSelector(
                selected = selectedPeriod,
                onSelected = { selectedPeriod = it }
            )
        }

        item {
            when (selectedPeriod) {
                ReportPeriod.WEEK -> {
                    WeekFilterInfo()
                }

                ReportPeriod.MONTH -> {
                    MonthSelector(
                        selectedMonth = selectedMonth,
                        onMonthSelected = { selectedMonth = it }
                    )
                }

                ReportPeriod.YEAR -> {
                    YearSelector(
                        years = FinanceRepository.availableYears(),
                        selectedYear = selectedYear,
                        onYearSelected = { selectedYear = it }
                    )
                }
            }
        }

        item {
            DonutReportCard(
                reportItems = reportItems,
                totalFlow = totalFlow,
                incomeTotal = incomeTotal,
                expenseTotal = expenseTotal
            )
        }

        item {
            Text(
                text = "Kategori Transaksi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MyWalletBlack
            )
        }
        item { ReportCategoryGrid(reportItems = reportItems) }
    }
}

private fun periodSubtitle(period: ReportPeriod, month: Int, year: Int): String {
    return when (period) {
        ReportPeriod.WEEK -> "Periode 7 hari terakhir"
        ReportPeriod.MONTH -> "Periode ${monthName(month)} $year"
        ReportPeriod.YEAR -> "Periode tahun $year"
    }
}
