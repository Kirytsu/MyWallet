package com.example.mywallet.ui.components.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywallet.model.ReportCategoryItem
import com.example.mywallet.model.TransactionType
import com.example.mywallet.ui.components.uiConfig
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletTextSecondary
import com.example.mywallet.ui.utils.formatRupiah

@Composable
fun ReportCategoryGrid(reportItems: List<ReportCategoryItem>) {
    if (reportItems.isEmpty()) {
        EmptyCategoryState()
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        reportItems.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { item ->
                    ReportCategoryCard(
                        modifier = Modifier.weight(1f),
                        item = item
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun EmptyCategoryState() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = "Belum ada data untuk filter ini.",
            modifier = Modifier.padding(16.dp),
            color = MyWalletTextSecondary
        )
    }
}

@Composable
private fun ReportCategoryCard(
    modifier: Modifier,
    item: ReportCategoryItem
) {
    val ui = item.category.uiConfig()
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(ui.color.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ui.icon,
                        contentDescription = item.category.title,
                        tint = ui.color,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "${item.percentage}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = MyWalletBlack,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = formatRupiah(item.amount),
                style = MaterialTheme.typography.bodyLarge,
                color = MyWalletBlack,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.category.title,
                style = MaterialTheme.typography.bodySmall,
                color = MyWalletTextSecondary
            )
            Text(
                text = if (item.category.type == TransactionType.INCOME) "Pemasukan" else "Pengeluaran",
                style = MaterialTheme.typography.labelSmall,
                color = ui.color
            )
        }
    }
}
