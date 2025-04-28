package com.pankajkumarrout.bdmxa.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderInfo(
    val status: String,
    val companyId: Long,
    val tpNumber: String,
    val clientId: Long,
    val quantity: Double,
    val orderDate: String,
    val mineralId: Long,
    val ratePerTon: Double,
    val destinationId: Int,
    val minesName: String,
    val orderId: Long,
    val destinationName: String,
    val mineralName: String,
    val mineralUnit: String,
    val minesLocation: String,
    val clientName: String,
    val destinationLocation: String

)
