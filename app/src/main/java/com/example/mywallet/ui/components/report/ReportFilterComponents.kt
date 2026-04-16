package com.example.mywallet.ui.components.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywallet.model.ReportPeriod
import com.example.mywallet.model.TransactionType
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletDanger
import com.example.mywallet.ui.theme.MyWalletGreen
import com.example.mywallet.ui.theme.MyWalletTextSecondary
import com.example.mywallet.ui.utils.monthShortName

@Composable
fun ReportPeriodSelector(
    selected: ReportPeriod,
    onSelected: (ReportPeriod) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ReportPeriod.entries.forEach { period ->
            val isSelected = period == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) MyWalletBlack else Color.Transparent)
                    .clickable { onSelected(period) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = period.title,
                    color = if (isSelected) Color.White else MyWalletTextSecondary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TransactionTypeSelector(
    selected: TransactionType?,
    onSelected: (TransactionType?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TypeChip(
            modifier = Modifier.weight(1f),
            title = "Pemasukan",
            isSelected = selected == TransactionType.INCOME,
            onClick = { 
                onSelected(if (selected == TransactionType.INCOME) null else TransactionType.INCOME) 
            },
            activeColor = MyWalletGreen
        )
        TypeChip(
            modifier = Modifier.weight(1f),
            title = "Pengeluaran",
            isSelected = selected == TransactionType.EXPENSE,
            onClick = { 
                onSelected(if (selected == TransactionType.EXPENSE) null else TransactionType.EXPENSE) 
            },
            activeColor = MyWalletDanger
        )
    }
}

@Composable
private fun TypeChip(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    activeColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) activeColor.copy(alpha = 0.12f) else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) activeColor else Color(0xFFE4E7EF),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) activeColor else MyWalletTextSecondary,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
fun WeekFilterInfo() {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = "Menampilkan transaksi dalam 7 hari terakhir.",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MyWalletTextSecondary
        )
    }
}

@Composable
fun MonthSelector(
    selectedMonth: Int,
    onMonthSelected: (Int) -> Unit
) {
    NumberChipRow(
        items = (1..12).toList(),
        selected = selectedMonth,
        onSelected = onMonthSelected,
        label = { monthShortName(it) }
    )
}

@Composable
fun YearSelector(
    years: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit
) {
    NumberChipRow(
        items = years,
        selected = selectedYear,
        onSelected = onYearSelected,
        label = { it.toString() }
    )
}

@Composable
private fun NumberChipRow(
    items: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    label: (Int) -> String
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            val isSelected = item == selected
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) MyWalletBlack else Color.White)
                    .border(1.dp, if (isSelected) MyWalletBlack else Color(0xFFE4E7EF), CircleShape)
                    .clickable { onSelected(item) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = label(item),
                    color = if (isSelected) Color.White else MyWalletTextSecondary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
