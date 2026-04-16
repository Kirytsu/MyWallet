package com.example.mywallet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mywallet.model.FinanceCategory
import com.example.mywallet.ui.theme.MyWalletBlue
import com.example.mywallet.ui.theme.MyWalletDanger
import com.example.mywallet.ui.theme.MyWalletGreen
import com.example.mywallet.ui.theme.MyWalletOrange
import com.example.mywallet.ui.theme.MyWalletPurple
import com.example.mywallet.ui.theme.MyWalletYellow

data class CategoryUi(
    val color: Color,
    val icon: ImageVector
)

fun FinanceCategory.uiConfig(): CategoryUi {
    return when (this) {
        // Income categories
        FinanceCategory.SALARY -> CategoryUi(MyWalletGreen, Icons.Filled.AccountBalance)
        FinanceCategory.TRANSFER_IN -> CategoryUi(MyWalletBlue, Icons.Filled.Payments)
        FinanceCategory.BONUS -> CategoryUi(MyWalletPurple, Icons.Filled.Celebration)
        FinanceCategory.INCOME_OTHERS -> CategoryUi(MyWalletBlue, Icons.Filled.AttachMoney)

        // Expense categories
        FinanceCategory.FOOD_DRINK -> CategoryUi(MyWalletYellow, Icons.Filled.Fastfood)
        FinanceCategory.TRANSPORT -> CategoryUi(MyWalletBlue, Icons.Filled.DirectionsCar)
        FinanceCategory.SHOPPING -> CategoryUi(MyWalletOrange, Icons.Filled.ShoppingBag)
        FinanceCategory.BILL_PAYMENT -> CategoryUi(Color(0xFFEF5350), Icons.Filled.ReceiptLong)
        FinanceCategory.ENTERTAINMENT -> CategoryUi(MyWalletPurple, Icons.Filled.LiveTv)
        FinanceCategory.EXPENSE_OTHERS -> CategoryUi(MyWalletDanger, Icons.Filled.Widgets)
    }
}
