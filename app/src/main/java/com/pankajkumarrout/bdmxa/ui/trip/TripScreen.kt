package com.pankajkumarrout.bdmxa.ui.trip

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pankajkumarrout.bdmxa.model.Trip
import com.pankajkumarrout.bdmxa.ui.home.ConfirmationDialog
import com.pankajkumarrout.bdmxa.ui.home.formatCurrency
import com.pankajkumarrout.bdmxa.ui.login.LogUser
import com.pankajkumarrout.bdmxa.ui.order.SelectedOrder
import org.koin.compose.koinInject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TripScreen(navController: NavController) {
    val tripsViewModel: TripsViewModel = koinInject()

    LaunchedEffect(Unit) {
        if (tripsViewModel.tripList.isEmpty() && SelectedOrder.order != null) {
            tripsViewModel.getTripList(orderId = SelectedOrder.order!!.orderId)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
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
                text = "Tp number ${SelectedOrder.order?.tpNumber}",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleSmall
            )
        }
        var searchText by remember { mutableStateOf("")}
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
            verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it.uppercase() },
                    label = { Text("Search...") },
                    trailingIcon = {
                        IconButton(
                            onClick = {},
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .scale(.8f)

                )
                IconButton(onClick = {navController.navigate("addTrip")}, modifier = Modifier.padding(horizontal = 8.dp)) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add", tint = Color(0xFF2E7D32), modifier = Modifier.size(52.dp))
                }
            }
        }
        Column {
            if (tripsViewModel.isLoadingTrip) {
                CircularProgressIndicator()
            } else {
                if (tripsViewModel.tripList.isEmpty()) {
                    Text("No listed trips kindly add ➕")
                    Text(tripsViewModel.messageTrip)
                } else {
                    val tripListState = rememberLazyListState()

                    var scrollStarted by remember { mutableStateOf(false) }
                    if (tripListState.isScrollInProgress) {
                        scrollStarted = true
                    }

                    if (searchText.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                state = tripListState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                item {
                                    if (LogUser.presentUser?.role == "admin") {
                                        Column (

                                        ) {
                                            Text(text = "🔃 Total trips: ${tripsViewModel.tripList.count()}")
                                            Text(
                                                text = "🚛 Quantity(load/unload): " +
                                                        "${String.format("%.2f", tripsViewModel.tripList.sumOf { it.unloadQuantity }) } / " +
                                                        "${String.format("%.2f", tripsViewModel.tripList.sumOf { it.loadedQuantity })} " +
                                                        "(${String.format("%.2f", tripsViewModel.tripList.sumOf { it.unloadQuantity } - tripsViewModel.tripList.sumOf { it.loadedQuantity })})"
                                            )
                                            Text(text = "📜 Ordered Quantity: ${SelectedOrder.order?.quantity ?: 0} ${if (tripsViewModel.tripList.sumOf { it.unloadQuantity } < SelectedOrder.order?.quantity ?: 0.0) "🟢" else "🔴"}")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Divider()
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }

                                items(tripsViewModel.tripList) { item ->
                                    CardScreen(item, tripsViewModel, navController)
                                }
                            }
                        }

                    } else {
                        val filteredData = tripsViewModel.tripList.filter { trip ->
                            val searchLower = searchText.trim().lowercase()
                            val passNumberMatch = trip.passNumber.toString().contains(searchLower)
                            val vehicleNameMatch = trip.vehicleName.lowercase().contains(searchLower)
                            val ownerNameMatch = trip.ownerName.lowercase().contains(searchLower)
                            val driverNameMatch = trip.driverName.lowercase().contains(searchLower)

                            passNumberMatch || vehicleNameMatch || ownerNameMatch || driverNameMatch
                        }
                        Column {
                            LazyColumn(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                items(filteredData) { item ->
                                    CardScreen(item, tripsViewModel, navController)
                                }
                            }
                        }
                    }
                    Text(tripsViewModel.messageTrip)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CardScreen(item: Trip, tripsViewModel: TripsViewModel, navController: NavController) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(
            1.dp,
            borderColor(item.status)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                tripsViewModel.selectedTrip = item
                navController.navigate("strip")
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pass Number block
                Column(
                    modifier = Modifier
                        .width(50.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = item.passNumber.toString(), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Vehicle & General Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        item.vehicleName,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("Owner: ${item.ownerName}")
                    Text("Driver: ${item.driverName}")
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFE1F5FE),
                                shape = RoundedCornerShape(32.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("${item.loadedQuantity}")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(formatDateTime(item.startTime), fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text("      |")
                    Row {
                        Text("      | -> (Cash) ${formatCurrency(item.advCash)} | (HSD) ${formatCurrency(item.dieselUsed)}")
                    }
                    Text("      |")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFffb4ab),
                                shape = RoundedCornerShape(32.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("${item.unloadQuantity}")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(formatDateTime(item.endTime), fontSize = 14.sp)
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Remarks
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Remark: ${item.remarks} ", color = Color.Gray, fontSize = 14.sp)
                Text(if (item.loadedQuantity == item.unloadQuantity) "🟢" else "🔴")
            }
        }

    }
}

@Composable
fun SelectedTripList(navController: NavController) {
    val tripsViewModel: TripsViewModel = koinInject()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
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
                text = "Tp number ${SelectedOrder.order?.tpNumber} pass ${tripsViewModel.selectedTrip?.passNumber}",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleSmall
            )
        }
        LazyColumn {
            item {
                Column {
                    var showPass by remember { mutableStateOf(false) }
                    var showLoad by remember { mutableStateOf(false) }
                    var showCash by remember { mutableStateOf(false) }
                    var showHSD by remember { mutableStateOf(false) }
                    var showUnload by remember { mutableStateOf(false) }
                    Column {
                        Text("Details:")
                        Text("TP: ${SelectedOrder.order?.tpNumber}/${tripsViewModel.selectedTrip?.passNumber}")
                        Text("Vehicle number: ${tripsViewModel.selectedTrip?.vehicleName}")
                        Text("Owner: ${tripsViewModel.selectedTrip?.ownerName}")
                        Text("Driver: ${tripsViewModel.selectedTrip?.driverName}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Update options ~")
                    //----------Pass-----------------------
                    if (tripsViewModel.selectedTrip?.passNumber == 0L || LogUser.presentUser?.role == "admin") {
                        OutlinedButton(onClick = { showPass = !showPass }, modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Pass number: ${tripsViewModel.selectedTrip?.passNumber}")
                                Icon(imageVector = if (!showPass) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp, contentDescription = "")
                            }
                        }
                    }
                    if (showPass) {
                        var passNumber by remember { mutableStateOf("${tripsViewModel.selectedTrip?.passNumber}") }
                        var confirmPass by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = passNumber,
                            onValueChange = { passNumber = it },
                            label = { Text("Pass") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Button(
                            onClick = { confirmPass = true },
                            enabled = tripsViewModel.selectedTrip?.passNumber.toString() != passNumber
                        ) {
                            Text("Update")
                        }
                        if (confirmPass) {
                            ConfirmationDialog(
                                message = "Update pass!",
                                onConfirm = {
                                    tripsViewModel.updatePass(
                                        id = tripsViewModel.selectedTrip?.id ?: 0,
                                        pass = passNumber.toLong()
                                    )
                                    navController.popBackStack()
                                    confirmPass = false
                                },
                                onDismiss = {
                                    confirmPass = false
                                }
                            )
                        }
                    }
                    //----------Load--------------------------
                    if (tripsViewModel.selectedTrip?.loadedQuantity == 0.0 || LogUser.presentUser?.role == "admin") {
                        OutlinedButton(onClick = { showLoad = !showLoad }, modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Load quantity: ${tripsViewModel.selectedTrip?.loadedQuantity}")
                                Icon(imageVector = if (!showLoad) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp, contentDescription = "")
                            }
                        }
                    }
                    if (showLoad) {
                        var quantity by remember { mutableStateOf("${tripsViewModel.selectedTrip?.loadedQuantity}") }
                        var confirmQuantity by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantity") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        Button(
                            onClick = { confirmQuantity = true },
                            enabled = tripsViewModel.selectedTrip?.loadedQuantity.toString() != quantity
                        ) {
                            Text("Update")
                        }
                        if (confirmQuantity) {
                            ConfirmationDialog(
                                message = "Update load!",
                                onConfirm = {
                                    tripsViewModel.updateLoad(id = tripsViewModel.selectedTrip?.id ?: 0, quantity = quantity.toDouble())
                                    navController.popBackStack()
                                    confirmQuantity = false
                                },
                                onDismiss = {
                                    confirmQuantity = false
                                }
                            )
                        }
                    }
                    //----------Cash-----------------------------
                    if (tripsViewModel.selectedTrip?.advCash == 0.0 || LogUser.presentUser?.role == "admin") {
                        OutlinedButton(onClick = { showCash = !showCash }, modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Cash advance: ${formatCurrency(tripsViewModel.selectedTrip?.advCash ?: 0.0)}")
                                Icon(imageVector = if (!showCash) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp, contentDescription = "")
                            }
                        }
                    }
                    if (showCash) {
                        var amount by remember { mutableStateOf("${tripsViewModel.selectedTrip?.advCash}") }
                        var confirmCash by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Quantity") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        Button(
                            onClick = { confirmCash = true },
                            enabled = tripsViewModel.selectedTrip?.advCash.toString() != amount
                        ) {
                            Text("Update")
                        }
                        if (confirmCash) {
                            ConfirmationDialog(
                                message = "Update cash!",
                                onConfirm = {
                                    tripsViewModel.updateCash(id = tripsViewModel.selectedTrip?.id ?: 0, cash = amount.toDouble())
                                    navController.popBackStack()
                                    confirmCash = false
                                },
                                onDismiss = {
                                    confirmCash = false
                                }
                            )
                        }
                    }
                    //----------HSD-------------------------------
                    if (tripsViewModel.selectedTrip?.dieselUsed == 0.0 || LogUser.presentUser?.role == "admin") {
                        OutlinedButton(onClick = { showHSD = !showHSD }, modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("HSD: ${formatCurrency(tripsViewModel.selectedTrip?.dieselUsed ?: 0.0) }")
                                Icon(imageVector = if (!showHSD) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp, contentDescription = "")
                            }
                        }
                    }
                    if (showHSD) {
                        var amount by remember { mutableStateOf("${tripsViewModel.selectedTrip?.dieselUsed}") }
                        var confirmHSD by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Quantity") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        Button(
                            onClick = { confirmHSD = true },
                            enabled = tripsViewModel.selectedTrip?.dieselUsed.toString() != amount
                        ) {
                            Text("Update")
                        }
                        if (confirmHSD) {
                            ConfirmationDialog(
                                message = "Update HSD!",
                                onConfirm = {
                                    tripsViewModel.updateHSD(id = tripsViewModel.selectedTrip?.id ?: 0, amount = amount.toDouble())
                                    navController.popBackStack()
                                    confirmHSD = false
                                },
                                onDismiss = {
                                    confirmHSD = false
                                }
                            )
                        }
                    }
                    //----------Unload-------------------------
                    if (tripsViewModel.selectedTrip?.unloadQuantity == 0.0 || LogUser.presentUser?.role == "admin") {
                        OutlinedButton(onClick = { showUnload = !showUnload }, modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Unload quantity: ${tripsViewModel.selectedTrip?.unloadQuantity}")
                                Icon(imageVector = if (!showUnload) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp, contentDescription = "")
                            }
                        }
                    }
                    if (showUnload) {
                        var quantity by remember { mutableStateOf("${tripsViewModel.selectedTrip?.unloadQuantity}") }
                        var confirmUnload by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantity") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        Button(
                            onClick = { confirmUnload = true },
                            enabled = tripsViewModel.selectedTrip?.unloadQuantity.toString() != quantity
                        ) {
                            Text("Update")
                        }
                        if (confirmUnload) {
                            ConfirmationDialog(
                                message = "Update unload!",
                                onConfirm = {
                                    tripsViewModel.updateUnload(id = tripsViewModel.selectedTrip?.id ?: 0, quantity = quantity.toDouble())
                                    navController.popBackStack()
                                    confirmUnload = false
                                },
                                onDismiss = {
                                    confirmUnload = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

fun borderColor(status: String): Color {
    return when(status) {
        "scheduled" -> Color.White
        "load" -> Color(0xFFFFD700)
        "unload" -> Color(0xFF4CAF50)
        "success" -> Color(0xFF2E7D32)
        else -> Color.Black
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(isoString: String): String {
    val parsedDate = LocalDateTime.parse(isoString)
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy,hh:mm a")
    return parsedDate.format(formatter)
}