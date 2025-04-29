package com.pankajkumarrout.bdmxa.ui.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pankajkumarrout.bdmxa.model.OrderInfo
import com.pankajkumarrout.bdmxa.network.OrderApiService
import com.pankajkumarrout.bdmxa.ui.login.LogUser
import kotlinx.coroutines.launch

object SelectedOrder {
    var order: OrderInfo? = null
}

class OrderViewModel: ViewModel() {
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf("")
    var orderInfoList: List<OrderInfo> by mutableStateOf(emptyList())

    private val orderApiService = OrderApiService()

    fun getOrderInfoList() {
        isLoading = true
        message = ""
        viewModelScope.launch {
            try {
                val response = orderApiService.orderInfoList(id = LogUser.presentUser?.companyId ?: 0)
                orderInfoList = response.reversed()
            } catch (e: Exception) {
                message = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}