package com.example.mywallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mywallet.data.FinanceRepository
import com.example.mywallet.ui.MyWalletApp
import com.example.mywallet.ui.theme.MyWalletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FinanceRepository.initialize(applicationContext)
        setContent {
            MyWalletTheme(darkTheme = false) {
                MyWalletApp()
            }
        }
    }
}
