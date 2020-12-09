package com.example.photouploadtest.model


import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("password")
    val password: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)