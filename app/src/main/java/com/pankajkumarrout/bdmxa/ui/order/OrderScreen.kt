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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pankajkumarrout.bdmxa.R
import com.pankajkumarrout.bdmxa.model.OrderInfo
import com.pankajkumarrout.bdmxa.ui.home.ConfirmationDialog
import com.pankajkumarrout.bdmxa.ui.login.LogUser
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
                    var searchList by remember { mutableStateOf("") }
                    val selectedList: List<OrderInfo> = if (searchList.isEmpty()) orderViewModel.orderInfoList else orderViewModel.orderInfoList.filter { it.tpNumber.lowercase().contains(searchList) }

                    val listFilter = listOf("active", "pending", "success", "cancelled", )
                    val listColor = listOf(Color.Green, Color.Yellow, Color.White, Color.Red)

                    var selectedFilter by remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = searchList,
                            onValueChange = {searchList = it},
                            label = { Text("Search...")},
                            singleLine = true,
                            trailingIcon = { IconButton(onClick = {}, modifier = Modifier.padding(8.dp)){ Icon(imageVector = Icons.Default.Search, contentDescription = "Search")} },
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier.scale(0.8f)
                        )
                        if (LogUser.presentUser?.role == "admin") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.filter_list_24px),
                                    contentDescription = "Filter",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable { selectedFilter = "" }
                                )
                                LazyRow(
                                ) {
                                    items(listFilter) { item ->
                                        Button(
                                            onClick = { selectedFilter = item },
                                            colors = ButtonColors(
                                                contentColor = if (item == selectedFilter) Color.White else Color.Black,
                                                containerColor = if (item == selectedFilter) Color.Black else Color.White,
                                                disabledContainerColor = Color.White,
                                                disabledContentColor = Color.Black
                                            ),
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            Text(
                                                item.replaceFirstChar { it.uppercaseChar() },
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    val selectedListFilter = if (selectedFilter.isEmpty()) selectedList else selectedList.filter { it.status == selectedFilter }
                    LazyColumn {
                        items(selectedListFilter) { item ->
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