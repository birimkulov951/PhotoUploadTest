package com.example.photouploadtest

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import ru.app.models.addfield.AddFieldResponse

interface ApiService {


    /**    POST   */
    @Multipart
    @POST("api/main/field/create/")
    fun createField(@Header("Authorization") token: String,
                 @Part("field_type") fieldType: RequestBody,
                 @Part("name") name: RequestBody,
                 @Part("price") price: RequestBody,
                 @Part("minimum_size") minimumSize: RequestBody,
                 @Part("maximum_size") maximumSIze: RequestBody,
                 @Part   images: List<MultipartBody.Part>,
                 @Part("location") location: RequestBody,
                 @Part("description") description: RequestBody,
                 @Part("number_of_players") numberOfPlayers: RequestBody,
                 @Part("has_parking") hasParking: RequestBody,
                 @Part("is_indoor") isIndoor: RequestBody,
                 @Part("has_showers") hasShowers: RequestBody,
                 @Part("has_locker_rooms") hasLockerRooms: RequestBody,
                 @Part("has_lights") hasLights: RequestBody,
                 @Part("has_rostrum") hasRostrum: RequestBody,
                 @Part("has_equipment") hasEquipment: RequestBody
    ): Call<AddFieldResponse>


}

