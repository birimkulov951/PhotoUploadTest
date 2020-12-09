package ru.app.models.addfield


import com.google.gson.annotations.SerializedName

data class WorkingHourXX(
    @SerializedName("day")
    val day: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("start")
    val start: String
)