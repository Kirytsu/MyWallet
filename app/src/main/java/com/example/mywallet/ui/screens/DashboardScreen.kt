package com.example.mywallet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mywallet.data.FinanceRepository
import com.example.mywallet.ui.components.TransactionAction
import com.example.mywallet.ui.components.TransactionCard
import com.example.mywallet.ui.components.dashboard.ActionButtons
import com.example.mywallet.ui.components.dashboard.DashboardHeader
import com.example.mywallet.ui.components.dashboard.EditLimitDialog
import com.example.mywallet.ui.components.dashboard.WalletQuickSummary
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletTextSecondary

@Composable
fun DashboardScreen(
    onActionClick: (TransactionAction) -> Unit
) {
    val summary = FinanceRepository.walletSummaryForCurrentMonth()
    val latestTransactions = FinanceRepository.latestTransactions(limit = 6)
    var showEditLimit by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { DashboardHeader() }
        item { 
            WalletQuickSummary(
                summary = summary,
                onDepositClick = { onActionClick(TransactionAction.DEPOSIT) },
                onPayClick = { onActionClick(TransactionAction.PAY) },
                onEditLimitClick = { showEditLimit = true }
            ) 
        }
        item {
            ActionButtons(
                onDepositClick = { onActionClick(TransactionAction.DEPOSIT) },
                onPayClick = { onActionClick(TransactionAction.PAY) }
            )
        }
        item {
            Text(
                text = "Aktivitas Terbaru",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MyWalletBlack
            )
        }

        if (latestTransactions.isEmpty()) {
            item {
                EmptyTransactionsState()
            }
        } else {
            items(latestTransactions, key = { it.id }) { transaction ->
                TransactionCard(
                    transaction = transaction,
                    onDeleteClick = {
                        FinanceRepository.deleteTransaction(transaction.id)
                    }
                )
            }
        }
    }

    if (showEditLimit) {
        EditLimitDialog(
            currentLimit = summary.limitMonthly,
            onDismiss = { showEditLimit = false },
            onConfirm = { newLimit ->
                FinanceRepository.updateMonthlyLimit(newLimit)
                showEditLimit = false
            }
        )
    }
}

@Composable
private fun EmptyTransactionsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MyWalletTextSecondary.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Belum ada transaksi",
            style = MaterialTheme.typography.titleSmall,
            color = MyWalletTextSecondary,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Mulai catat pengeluaran atau pemasukan Anda hari ini.",
            style = MaterialTheme.typography.bodySmall,
            color = MyWalletTextSecondary.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 32.dp, top = 4.dp, end = 32.dp)
        )
    }
}
