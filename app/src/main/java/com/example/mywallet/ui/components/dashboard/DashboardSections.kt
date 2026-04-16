package com.example.mywallet.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mywallet.model.WalletSummary
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletCard
import com.example.mywallet.ui.theme.MyWalletDanger
import com.example.mywallet.ui.theme.MyWalletGreen
import com.example.mywallet.ui.theme.MyWalletPurple
import com.example.mywallet.ui.theme.MyWalletTextSecondary
import com.example.mywallet.ui.utils.formatRupiah
import com.example.mywallet.ui.utils.parseNominalRupiah

@Composable
fun DashboardHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MyWalletBlack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "User",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                Text(
                    text = "MyWallet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MyWalletBlack,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Financial Diary",
                    style = MaterialTheme.typography.bodySmall,
                    color = MyWalletTextSecondary
                )
            }
        }
    }
}

@Composable
fun WalletQuickSummary(
    summary: WalletSummary,
    onDepositClick: () -> Unit,
    onPayClick: () -> Unit,
    onEditLimitClick: () -> Unit
) {
    val progress = (summary.spentMonthly.toFloat() / summary.limitMonthly.toFloat()).coerceIn(0f, 1f)

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Saldo Tersedia",
                color = MyWalletTextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = formatRupiah(summary.balance),
                color = MyWalletBlack,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryBadge(
                    modifier = Modifier.weight(1f),
                    title = "Deposit",
                    amount = formatRupiah(summary.totalIncome),
                    color = MyWalletGreen,
                    icon = Icons.Filled.ArrowUpward,
                    onClick = onDepositClick
                )
                SummaryBadge(
                    modifier = Modifier.weight(1f),
                    title = "Bayar",
                    amount = formatRupiah(summary.totalExpense),
                    color = MyWalletDanger,
                    icon = Icons.Filled.ArrowDownward,
                    onClick = onPayClick
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Limit Bulanan",
                    color = MyWalletTextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(
                    onClick = onEditLimitClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Limit",
                        tint = MyWalletPurple,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(MyWalletCard)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(MyWalletPurple)
                )
            }
            Text(
                text = "Terpakai ${formatRupiah(summary.spentMonthly)} dari ${formatRupiah(summary.limitMonthly)}",
                color = MyWalletTextSecondary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun ActionButtons(
    onDepositClick: () -> Unit,
    onPayClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ActionButton(
            modifier = Modifier.weight(1f),
            title = "Deposit",
            tint = MyWalletGreen,
            onClick = onDepositClick
        )
        ActionButton(
            modifier = Modifier.weight(1f),
            title = "Bayar",
            tint = MyWalletDanger,
            onClick = onPayClick
        )
    }
}

@Composable
private fun SummaryBadge(
    modifier: Modifier = Modifier,
    title: String,
    amount: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(14.dp))
            }
            Column {
                Text(title, style = MaterialTheme.typography.labelMedium, color = MyWalletTextSecondary)
                Text(amount, style = MaterialTheme.typography.labelLarge, color = MyWalletBlack, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier,
    title: String,
    tint: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = tint.copy(alpha = 0.16f),
            contentColor = MyWalletBlack
        )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun EditLimitDialog(
    currentLimit: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var limitText by remember { mutableStateOf(currentLimit.toString()) }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Limit Bulanan", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = limitText,
                    onValueChange = { 
                        limitText = it
                        errorText = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Limit Baru") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errorText != null,
                    supportingText = { errorText?.let { Text(it) } }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsed = parseNominalRupiah(limitText)
                    parsed.fold(
                        onSuccess = { onConfirm(it) },
                        onFailure = { errorText = it.message }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MyWalletPurple)
            ) {
                Text("Simpan")
            }
        }
    )
}
