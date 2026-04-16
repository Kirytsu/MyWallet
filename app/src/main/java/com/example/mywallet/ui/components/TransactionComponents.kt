package com.example.mywallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mywallet.model.TransactionType
import com.example.mywallet.model.WalletTransaction
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletDanger
import com.example.mywallet.ui.theme.MyWalletGreen
import com.example.mywallet.ui.theme.MyWalletTextSecondary
import com.example.mywallet.ui.utils.formatDate
import com.example.mywallet.ui.utils.formatTransactionAmount

@Composable
fun TransactionCard(
    transaction: WalletTransaction,
    showDateInline: Boolean = false,
    onDeleteClick: (() -> Unit)? = null
) {
    var showDeleteAlert by remember { mutableStateOf(false) }
    val categoryUi = transaction.category.uiConfig()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(categoryUi.color.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = categoryUi.icon,
                        contentDescription = transaction.category.title,
                        tint = categoryUi.color
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = transaction.title,
                        color = MyWalletBlack,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (transaction.description.isNotBlank()) {
                        Text(
                            text = transaction.description,
                            color = MyWalletTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (showDateInline) {
                        Text(
                            text = formatDate(transaction.timestampMillis),
                            color = MyWalletTextSecondary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formatTransactionAmount(
                        amount = transaction.amount,
                        isIncome = transaction.type == TransactionType.INCOME
                    ),
                    color = if (transaction.type == TransactionType.INCOME) MyWalletGreen else MyWalletBlack,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (onDeleteClick != null) {
                    IconButton(
                        onClick = { showDeleteAlert = true },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Hapus transaksi",
                            tint = MyWalletDanger,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteAlert && onDeleteClick != null) {
        AlertDialog(
            onDismissRequest = { showDeleteAlert = false },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MyWalletDanger,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Hapus Transaksi?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MyWalletBlack,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Transaksi \"${transaction.title}\" akan dihapus permanen. Tindakan ini tidak dapat dibatalkan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MyWalletTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAlert = false }) {
                    Text("Batal", color = MyWalletTextSecondary)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick()
                        showDeleteAlert = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyWalletDanger,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Hapus", fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}
