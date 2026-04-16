package com.example.mywallet.ui.components.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mywallet.model.ReportCategoryItem
import com.example.mywallet.ui.components.uiConfig
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletCard
import com.example.mywallet.ui.theme.MyWalletTextSecondary
import com.example.mywallet.ui.utils.formatRupiah

@Composable
fun DonutReportCard(
    reportItems: List<ReportCategoryItem>,
    totalFlow: Int,
    incomeTotal: Int,
    expenseTotal: Int
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (reportItems.isEmpty()) {
                EmptyReportState()
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ReportDonutChart(items = reportItems)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Total Transaksi",
                            color = MyWalletTextSecondary,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = formatRupiah(totalFlow),
                            color = MyWalletBlack,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            "Pemasukan",
                            color = MyWalletTextSecondary,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            formatRupiah(incomeTotal),
                            color = Color(0xFF2ECC71),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "Pengeluaran",
                            color = MyWalletTextSecondary,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            formatRupiah(expenseTotal),
                            color = Color(0xFFEF5350),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyReportState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.PieChart,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MyWalletTextSecondary.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Data laporan kosong",
            style = MaterialTheme.typography.titleSmall,
            color = MyWalletBlack,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Tidak ada transaksi pada periode ini untuk ditampilkan.",
            style = MaterialTheme.typography.bodySmall,
            color = MyWalletTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 24.dp, top = 4.dp, end = 24.dp)
        )
    }
}

@Composable
private fun ReportDonutChart(items: List<ReportCategoryItem>) {
    if (items.isEmpty()) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)) {
            drawCircle(
                color = MyWalletCard.copy(alpha = 0.3f),
                radius = (size.minDimension - 40f) / 2f,
                style = Stroke(width = 40f)
            )
        }
        return
    }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)) {
        val strokeWidth = 40f
        val gap = 4f
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        var startAngle = -90f

        items.forEach { item ->
            val color = item.category.uiConfig().color
            val sweep = ((item.percentage.coerceAtLeast(1) / 100f) * 360f) - gap
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                size = Size(diameter, diameter),
                topLeft = topLeft,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            startAngle += sweep + gap
        }
    }
}
