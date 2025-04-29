package com.pankajkumarrout.bdmxa.ui.trip

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pankajkumarrout.bdmxa.model.Trip
import com.pankajkumarrout.bdmxa.model.TripDTO
import com.pankajkumarrout.bdmxa.model.VehicleDetails
import com.pankajkumarrout.bdmxa.network.TripApiService
import com.pankajkumarrout.bdmxa.network.VehicleApiService
import com.pankajkumarrout.bdmxa.ui.login.LogUser
import com.pankajkumarrout.bdmxa.ui.order.SelectedOrder
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class TripsViewModel: ViewModel() {
    var isLoadingTrip by mutableStateOf(false)
    var messageTrip by mutableStateOf("")
    var onSuccess by mutableStateOf(false)

    var tripList: List<Trip> by mutableStateOf(emptyList())
    val tripApiService = TripApiService()

    var selectedTrip: Trip? by mutableStateOf(null)

    fun getTripList(orderId: Long) {
        isLoadingTrip = true
        messageTrip = ""
        viewModelScope.launch(Dispatchers.IO) {
            try {
                selectedTrip = null
                val response = tripApiService.getTripsWithOrderId(orderId = orderId)
                tripList = response.reversed()
            } catch (e: Exception) {
                messageTrip = "Error: ${e.message}"
            } finally {
                isLoadingTrip = false
            }
        }
    }

    fun updatePass(id: Long, pass: Long) {
        isLoadingTrip = true
        messageTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripPass(pass = pass, id = id)
                if (response.status.isSuccess()) {
                    messageTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageTrip = "Error: ${e.message}"
            } finally {
                isLoadingTrip = false
            }
        }
    }

    fun updateLoad(id: Long, quantity: Double) {
        isLoadingTrip = true
        messageTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripLoad(quantity = quantity, id = id)
                if (response.status.isSuccess()) {
                    messageTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageTrip = "Error: ${e.message}"
            } finally {
                isLoadingTrip = false
            }
        }
    }

    fun updateCash(id: Long, cash: Double) {
        isLoadingTrip = true
        messageTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripCash(cash = cash, id = id)
                if (response.status.isSuccess()) {
                    messageTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageTrip = "Error: ${e.message}"
            } finally {
                isLoadingTrip = false
            }
        }
    }

    fun updateHSD(id: Long, amount: Double) {
        isLoadingTrip = true
        messageTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripHSD(amount = amount, id = id)
                if (response.status.isSuccess()) {
                    messageTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageTrip = "Error: ${e.message}"
            } finally {
                isLoadingTrip = false
            }
        }
    }

    fun updateUnload(id: Long, quantity: Double) {
        isLoadingTrip = true
        messageTrip = ""
        viewModelScope.launch {
            try {
                val response = tripApiService.updateTripUnload(quantity = quantity, id = id)
                if (response.status.isSuccess()) {
                    messageTrip = "Successfully updated ✅"
                    getTripList(SelectedOrder.order?.orderId ?: 0)
                } else {
                    messageTrip = "Error: Update ❌: ${response.body<Any>()}"
                }
            } catch (e: Exception) {
                messageTrip = "Error: ${e.message}"
            } finally {
                isLoadingTrip = false
            }
        }
    }

    private var vehicleApiService = VehicleApiService()

    var isLoadingVehicle by mutableStateOf(false)
    var messageVehicle by mutableStateOf("")
    var vehicleDetails by mutableStateOf<VehicleDetails?>(null)

    fun getVehicleDetails(vehicleNumber: String) {
        isLoadingVehicle = true
        messageVehicle = ""
        viewModelScope.launch {
            try {
                val response = vehicleApiService.getVehicleDetails(vehicleNumber)
                vehicleDetails = response
            } catch (e: Exception) {
                messageVehicle = "Error: ${e.message}"
            } finally {
                isLoadingVehicle = false
            }
        }
    }

    var isLoadingCreateTrip by mutableStateOf(false)
    var messageCreateTrip by mutableStateOf("")



    @RequiresApi(Build.VERSION_CODES.O)
    fun createTrip(companyId: Long, tpNumber: String, orderId: Long, vehicleId: Long, driverId: Long, passNumber: Long, loadQuantity: Double) {
        isLoadingCreateTrip = true
        messageCreateTrip = ""
        viewModelScope.launch {
            try {
                val insertTrip = TripDTO(companyId = companyId, tpNumber = tpNumber, orderId = orderId, vehicleId = vehicleId, driverId = driverId, remarks = "created at android by ${LogUser.presentUser?.role}", passNumber = passNumber, loadedQuantity = loadQuantity, startTime = LocalDateTime.now().toString(), endTime = LocalDateTime.now().toString())
                val response = tripApiService.createTrip(insertTrip)
                if (response.status.isSuccess()) {
                    messageCreateTrip = "Successfully created Trip ✅"
                    getTripList(orderId)
                } else {
                    messageCreateTrip = "Error: Creating Trip ❌"
                }
            } catch (e: Exception) {
                messageCreateTrip = "Error: ${e.message}"
            } finally {
                isLoadingCreateTrip = false
            }
        }
    }
}