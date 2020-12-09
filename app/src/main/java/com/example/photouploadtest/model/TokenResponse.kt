package com.example.photouploadtest.model


import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("type")
    val type: Int?
)