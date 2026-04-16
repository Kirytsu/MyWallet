package com.example.mywallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mywallet.model.FinanceCategory
import com.example.mywallet.model.TransactionType
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletCard
import com.example.mywallet.ui.theme.MyWalletDanger
import com.example.mywallet.ui.theme.MyWalletGreen
import com.example.mywallet.ui.theme.MyWalletTextSecondary
import com.example.mywallet.ui.utils.parseNominalRupiah

enum class TransactionAction {
    DEPOSIT,
    PAY
}

data class TransactionDraft(
    val amount: Int,
    val note: String,
    val category: FinanceCategory,
    val action: TransactionAction
)

@Composable
fun TransactionActionSheet(
    action: TransactionAction,
    onDismiss: () -> Unit,
    onSubmitSuccess: (TransactionDraft) -> Unit
) {
    val categories = if (action == TransactionAction.DEPOSIT) {
        FinanceCategory.entries.filter { it.type == TransactionType.INCOME }
    } else {
        FinanceCategory.entries.filter { it.type == TransactionType.EXPENSE }
    }

    var nominalText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var amountError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(action) {
        nominalText = ""
        noteText = ""
        selectedCategory = categories.first()
        amountError = null
    }

    fun validateAndSubmit() {
        val parsedAmountResult = parseNominalRupiah(nominalText)
        val normalizedNote = noteText.trim()

        amountError = parsedAmountResult.exceptionOrNull()?.message

        if (amountError == null) {
            onSubmitSuccess(
                TransactionDraft(
                    amount = parsedAmountResult.getOrThrow(),
                    note = normalizedNote,
                    category = selectedCategory,
                    action = action
                )
            )
            onDismiss()
        }
    }

    AlertDialog(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (action == TransactionAction.DEPOSIT) "Tambah Deposit" else "Bayar",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MyWalletBlack
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nominalText,
                    onValueChange = {
                        nominalText = it
                        if (amountError != null) {
                            amountError = parseNominalRupiah(it).exceptionOrNull()?.message
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nominal") },
                    placeholder = { Text("Contoh: 350000") },
                    isError = amountError != null,
                    supportingText = {
                        amountError?.let { Text(it, color = MyWalletDanger) }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Keterangan (opsional)") },
                    placeholder = { Text("Contoh: Tagihan listrik") },
                    singleLine = true
                )

                Text(
                    text = "Pilih Kategori",
                    style = MaterialTheme.typography.titleSmall,
                    color = MyWalletBlack,
                    fontWeight = FontWeight.SemiBold
                )

                CategoryChipRow(
                    categories = categories,
                    selected = selectedCategory,
                    onSelected = { selectedCategory = it }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        },
        confirmButton = {
            Button(
                onClick = { validateAndSubmit() },
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (action == TransactionAction.DEPOSIT) MyWalletGreen else MyWalletDanger,
                    contentColor = Color.White
                )
            ) {
                Text("Simpan", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun CategoryChipRow(
    categories: List<FinanceCategory>,
    selected: FinanceCategory,
    onSelected: (FinanceCategory) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    val isSelected = item == selected
                    val categoryUi = item.uiConfig()
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .background(
                                color = if (isSelected) categoryUi.color.copy(alpha = 0.16f) else MyWalletCard.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelected(item) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = categoryUi.icon,
                                contentDescription = null,
                                tint = if (isSelected) categoryUi.color else MyWalletTextSecondary.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) MyWalletBlack else MyWalletTextSecondary,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                if (rowItems.size < 2) {
                    repeat(2 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
