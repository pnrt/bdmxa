package com.pankajkumarrout.bdmxa.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pankajkumarrout.bdmxa.model.OrderInfo
import com.pankajkumarrout.bdmxa.ui.home.ConfirmationDialog
import org.koin.compose.koinInject


@Composable
fun OrderScreen(navController: NavController) {
    val orderViewModel: OrderViewModel = koinInject()
    LaunchedEffect(Unit) {
        if (orderViewModel.orderInfoList.isEmpty()) {
            orderViewModel.getOrderInfoList()
        }
    }
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
                text = "🟢 Active Orders ${orderViewModel.orderInfoList.count()}",
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
            if (orderViewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                if (orderViewModel.orderInfoList.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("No active orders available..!")
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(onClick = {orderViewModel.getOrderInfoList()}) {
                            Text("🔃")
                        }
                    }
                } else {
                    LazyColumn {
                        items(orderViewModel.orderInfoList) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        SelectedOrder.order = item
                                        navController.navigate("trip")
                                    }
                            ) {
                                Column {
                                    OrderInfoCard(item)
                                }
                            }
                        }
                    }
                }
            }
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