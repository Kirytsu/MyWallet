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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mywallet.data.FinanceRepository
import com.example.mywallet.ui.components.TransactionCard
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletTextSecondary
import com.example.mywallet.ui.utils.formatDate

@Composable
fun HistoryScreen() {
    val allTransactions = FinanceRepository.allTransactions()
    val groupedByDate = allTransactions.groupBy { formatDate(it.timestampMillis) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Histori Transaksi",
                style = MaterialTheme.typography.titleLarge,
                color = MyWalletBlack,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Urut terbaru ke terlama",
                style = MaterialTheme.typography.bodySmall,
                color = MyWalletTextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        if (allTransactions.isEmpty()) {
            item {
                EmptyHistoryState()
            }
        } else {
            groupedByDate.forEach { (date, transactions) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.labelLarge,
                        color = MyWalletTextSecondary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(transactions, key = { it.id }) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onDeleteClick = {
                            FinanceRepository.deleteTransaction(transaction.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MyWalletTextSecondary.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Belum ada riwayat",
            style = MaterialTheme.typography.titleMedium,
            color = MyWalletTextSecondary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Transaksi yang Anda simpan akan muncul di sini.",
            style = MaterialTheme.typography.bodyMedium,
            color = MyWalletTextSecondary.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 32.dp, top = 4.dp, end = 32.dp)
        )
    }
}
