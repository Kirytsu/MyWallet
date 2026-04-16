package com.example.mywallet.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mywallet.data.FinanceRepository
import com.example.mywallet.model.MainTab
import com.example.mywallet.ui.components.BottomNavigationBar
import com.example.mywallet.ui.components.TransactionAction
import com.example.mywallet.ui.components.TransactionActionSheet
import com.example.mywallet.ui.screens.DashboardScreen
import com.example.mywallet.ui.screens.HistoryScreen
import com.example.mywallet.ui.screens.ReportScreen
import com.example.mywallet.ui.utils.formatRupiah
import com.example.mywallet.ui.theme.MyWalletBackground

@Composable
fun MyWalletApp() {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.DASHBOARD) }
    var activeActionSheet by remember { mutableStateOf<TransactionAction?>(null) }
    var latestSheetMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(latestSheetMessage) {
        latestSheetMessage?.let {
            snackbarHostState.showSnackbar(it)
            latestSheetMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MyWalletBackground,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MyWalletBackground
            ) {
                when (selectedTab) {
                    MainTab.DASHBOARD -> DashboardScreen(onActionClick = { activeActionSheet = it })
                    MainTab.HISTORY -> HistoryScreen()
                    MainTab.REPORT -> ReportScreen()
                }
            }
        }

        activeActionSheet?.let { action ->
            key(action) {
                TransactionActionSheet(
                    action = action,
                    onDismiss = { activeActionSheet = null },
                    onSubmitSuccess = { draft ->
                        val saveResult = FinanceRepository.addTransaction(
                            amount = draft.amount,
                            category = draft.category,
                            note = draft.note
                        )

                        latestSheetMessage = saveResult.fold(
                            onSuccess = {
                                "${if (draft.action == TransactionAction.DEPOSIT) "Deposit" else "Bayar"} ${draft.category.title} berhasil disimpan: ${formatRupiah(draft.amount)}"
                            },
                            onFailure = {
                                "Gagal menyimpan transaksi: ${it.message ?: "unknown error"}"
                            }
                        )
                    }
                )
            }
        }
    }
}
