package io.feliz.android.daldang_android

import retrofit2.Call
import retrofit2.http.*



data class ItemDocuments(val address_name: String, val y: Double, val x: Double) // y : latitude, x : longitude
data class LocationDTO(val documents: MutableList<ItemDocuments>)

// longitude=128.356235 latitude=36.089178
interface KakaoRetrofitService {

    @Headers(
        "Authorization: KakaoAK " // input token
    )
    @GET("/v2/local/search/address.json")
    fun searchLocation(@Query("query") query: String): Call<LocationDTO>


}