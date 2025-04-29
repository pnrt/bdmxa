package com.pankajkumarrout.bdmxa.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pankajkumarrout.bdmxa.ui.login.LogUser
import com.pankajkumarrout.bdmxa.ui.login.LoginScreen
import com.pankajkumarrout.bdmxa.ui.order.OrderScreen
import com.pankajkumarrout.bdmxa.ui.trip.AddTripScreen
import com.pankajkumarrout.bdmxa.ui.trip.SelectedTripList
import com.pankajkumarrout.bdmxa.ui.trip.SelectionDialogForVehicleAndDriver
import com.pankajkumarrout.bdmxa.ui.trip.TripScreen
import java.text.NumberFormat
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController)}
        composable("order") { OrderScreen(navController) }
        composable("trip") { TripScreen(navController) }
        composable("strip") { SelectedTripList(navController) }
        composable("addTrip") { AddTripScreen(navController) }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        if (LogUser.presentUser?.role != "admin") {
            navController.navigate("order")
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(8.dp)
    ) {
        if (LogUser.presentUser?.role == "admin") {
            Column {
                Text("Hello, ${LogUser.presentUser?.role}")
                Button(onClick = {navController.navigate("order")}, modifier = Modifier.fillMaxWidth()) {
                    Text("Orders")
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String = "Are you sure?",
    message: String = "Do you want to proceed with this operation?",
    confirmButtonText: String = "Yes",
    cancelButtonText: String = "No",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(cancelButtonText)
            }
        }
    )
}

fun formatCurrency(amount: Double, locale: Locale = Locale("en", "IN")): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
    return currencyFormatter.format(amount)
}