package com.example.mywallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywallet.model.MainTab
import com.example.mywallet.ui.theme.MyWalletBlack
import com.example.mywallet.ui.theme.MyWalletPurple
import com.example.mywallet.ui.theme.MyWalletTextSecondary

@Composable
fun BottomNavigationBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.entries.forEach { tab ->
                val selected = tab == selectedTab
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = tab.icon(),
                        contentDescription = tab.title,
                        tint = if (selected) MyWalletPurple else MyWalletTextSecondary
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = tab.title,
                        color = if (selected) MyWalletBlack else MyWalletTextSecondary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 18.dp, height = 3.dp)
                            .clip(CircleShape)
                            .background(if (selected) MyWalletPurple else androidx.compose.ui.graphics.Color.Transparent)
                    )
                }
            }
        }
    }
}

private fun MainTab.icon(): ImageVector {
    return when (this) {
        MainTab.DASHBOARD -> Icons.Filled.Home
        MainTab.HISTORY -> Icons.Filled.ReceiptLong
        MainTab.REPORT -> Icons.Filled.PieChart
    }
}
