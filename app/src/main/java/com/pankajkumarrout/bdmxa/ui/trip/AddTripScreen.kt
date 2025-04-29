package com.pankajkumarrout.bdmxa.ui.trip

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.pankajkumarrout.bdmxa.ui.home.ConfirmationDialog
import com.pankajkumarrout.bdmxa.ui.login.LogUser
import com.pankajkumarrout.bdmxa.ui.order.SelectedOrder
import org.koin.compose.koinInject


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTripScreen(navController: NavController) {
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
                text = "Tp number ${SelectedOrder.order?.tpNumber}  ➕",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleSmall
            )
        }
        Column {
            var selectVehicle by remember { mutableStateOf(false) }
            Button(onClick = { selectVehicle = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Select Vehicle")
            }
            if (selectVehicle) {
                SelectionDialogForVehicleAndDriver(onDismiss = { selectVehicle = false }, tripsViewModel)
            }
            Text("Details: ${if (tripsViewModel.vehicleDetails == null) "Select vehicle 👆" else ""}")
            if (tripsViewModel.vehicleDetails != null) {
                Column {
                    Text("Vehicle number: ${tripsViewModel.vehicleDetails?.vehicleNumber ?: ""}")
                    Text("Owner name: ${tripsViewModel.vehicleDetails?.ownerName ?: ""}")
                    Text("Driver name: ${tripsViewModel.vehicleDetails?.driverName ?: ""}")
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        var passNumber by remember { mutableStateOf("${if (LogUser.presentUser?.role == "admin") tripsViewModel.tripList.count() + 1 else ""}") }
                        var quantity by remember { mutableStateOf("") }
                        var confirmSaveTrip by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = passNumber,
                            onValueChange = {passNumber = it},
                            label = { Text("Pass number")},
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = quantity,
                            onValueChange = {quantity = it},
                            label = { Text("Load Quantity")},
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {confirmSaveTrip = true},
                            enabled = tripsViewModel.vehicleDetails != null && SelectedOrder.order != null && tripsViewModel.vehicleDetails!!.active != false && passNumber.isNotEmpty() && quantity.isNotEmpty()
                        ) {
                            Text("Generate trip")
                        }
                        if (confirmSaveTrip) {
                            ConfirmationDialog(
                                message = "Confirm assign vehicle ${tripsViewModel.vehicleDetails!!.vehicleNumber} --> ${passNumber}",
                                onDismiss = {confirmSaveTrip = false},
                                onConfirm = {
                                    tripsViewModel.createTrip(
                                        SelectedOrder.order?.companyId ?: 0,
                                        SelectedOrder.order?.tpNumber ?: "",
                                        SelectedOrder.order?.orderId ?: 0 ,
                                        tripsViewModel.vehicleDetails?.vehicleId ?: 0,
                                        tripsViewModel.vehicleDetails?.driverId ?: 0,
                                        passNumber = passNumber.toLong(),
                                        loadQuantity = quantity.toDouble()
                                    )
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectionDialogForVehicleAndDriver(
    onDismiss: () -> Unit,
    tripsViewModel: TripsViewModel
) {
    var searchVehicle by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss,

    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(500.dp)
        ) {
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = searchVehicle,
                            onValueChange = { searchVehicle = it.uppercase() },
                            label = { Text("Search vehicle...") },
                            trailingIcon = {
                                TextButton(
                                    onClick = {
                                        tripsViewModel.vehicleDetails = null
                                        tripsViewModel.getVehicleDetails(searchVehicle)
                                    },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    tripsViewModel.vehicleDetails = null
                                    tripsViewModel.getVehicleDetails(searchVehicle)
                                }
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(32.dp)
                        )
                        if (tripsViewModel.isLoadingVehicle) {
                            CircularProgressIndicator()
                        } else {
                            if (tripsViewModel.vehicleDetails == null) {
                                Text("No vehicle Listed!..")
                            } else {
                                Column(

                                ) {
                                    Card(
                                        //                            elevation = 4.dp,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text("🚛 Vehicle Details:")
                                            Text("  Name: ${tripsViewModel.vehicleDetails?.vehicleNumber ?: ""}")
                                            Text("  Model: ${tripsViewModel.vehicleDetails?.model ?: ""}")
                                            Text("  Fitness Valid: ${tripsViewModel.vehicleDetails?.fitnessValidTill ?: ""}")
                                            Text("  Insurance Valid: ${tripsViewModel.vehicleDetails?.insuranceValidTill ?: ""}")
                                            Text("  Status: ${tripsViewModel.vehicleDetails?.status ?: ""}")
                                        }
                                    }

                                    Card(
                                        //                            elevation = 4.dp,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            val context = LocalContext.current
                                            Text("🤵 Owner Details:")
                                            Text("  Name: ${tripsViewModel.vehicleDetails?.ownerName ?: ""}")
                                            Text(
                                                "  Phone: ${tripsViewModel.vehicleDetails?.ownerPhone ?: ""}",
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier.clickable {
                                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                                        data =
                                                            Uri.parse("tel:${tripsViewModel.vehicleDetails?.ownerPhone}")
                                                    }
                                                    context.startActivity(intent)
                                                }
                                            )
                                            Text("  Type: ${tripsViewModel.vehicleDetails?.ownerType ?: ""}")
                                        }
                                    }

                                    Card(
                                        //                            elevation = 4.dp,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            val context = LocalContext.current
                                            Text("👷 Driver Details:")
                                            Text("  Name: ${tripsViewModel.vehicleDetails?.driverName ?: " Add Driver"}")
                                            Text(
                                                "  Phone: ${tripsViewModel.vehicleDetails?.driverPhone ?: ""}",
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier.clickable {
                                                    val intent = Intent(Intent.ACTION_DIAL).apply {

                                                        data =
                                                            Uri.parse("tel:${tripsViewModel.vehicleDetails?.driverPhone}")
                                                    }
                                                    context.startActivity(intent)
                                                }
                                            )
                                            Text("  License valid: ${tripsViewModel.vehicleDetails?.licenseValidTill ?: ""}")
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Select")
                        }
                    }
                }
            }
        }
    }
}
