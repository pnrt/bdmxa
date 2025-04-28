package com.pankajkumarrout.bdmxa.model

import kotlinx.serialization.Serializable



@Serializable
data class User(
    val isAuthorized: Boolean,
    val companyId: Long?,
    val role: String?,
)

@Serializable
data class ForAuthentication(
    val username: String?,
    val password: String?
)