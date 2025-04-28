package com.pankajkumarrout.bdmxa.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pankajkumarrout.bdmxa.model.OrderInfo
import org.koin.compose.koinInject


@Composable
fun OrderScreen(navController: NavController) {
    val orderViewModel: OrderViewModel = koinInject()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp) // Padding around everything
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterStart)

            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }

            // Title (centered)
            Text(
                text = "Orders",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleSmall
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Order Screen",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun OrderInfoCard(order: OrderInfo) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("📦 TP: ${order.tpNumber}   (#${order.orderId})", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            Row {
                Text("🗓️ Date: ", fontWeight = FontWeight.SemiBold)
                Text(order.orderDate)
            }

            Row {
                Text("👤 Client: ", fontWeight = FontWeight.SemiBold)
                Text(order.clientName + " (#${order.clientId})")
            }

            Row {
                Text("  - ${order.mineralName} (${order.quantity} ${order.mineralUnit})")
            }

            Row {
                Text("🏭 Mines: ", fontWeight = FontWeight.SemiBold)
                Text("${order.minesName}, ${order.minesLocation}")

            }

            Row {
                Text("    ---> ")
                Text("🚚 Destination: ", fontWeight = FontWeight.SemiBold)
                Text("${order.destinationName}, ${order.destinationLocation}")
            }

        }
    }
}