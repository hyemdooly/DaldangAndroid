package io.feliz.android.daldang_android

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

data class SignInDTO(val StatusCode: Int, val Message: String)
data class SignUpDTO(val StatusCode: Int, val Message: String)

data class ItemFuture(val Date: String, val MaxTemp: Double, val MinTemp: Double, val Category: Int, val DayOfWeek: String)
data class ItemNow(val Date: String, val Temp: Double, val Category: Int, val DayOfWeek: String, val Hour: Int)
data class Weather(val Future: MutableList<ItemFuture>, val Now: MutableList<ItemNow>)
data class WeekWeatherDTO(val StatusCode: Int, val Weather: Weather, val Message: String)

data class ItemReport(val Red: Float, val Green: Float, val Blue: Float, val Brix: Float)
data class ReportDTO(val Report: MutableList<ItemReport>, val StatusCode: Int, val Message: String)

data class ItemPest(val Name: String, val RelationVideo: String)
data class PestDTO(val Message: String, val PestInfo: MutableList<ItemPest>, val StatusCode: Int)

data class ItemUser(val Orchard: String, val Location: String, val Kind: String, val Market: String, val MeasureCount: Int)
data class UserDTO(val Result: ItemUser, val StatusCode: Int, val Message: String)

data class ItemNowWeather(val Category: Int, val RainPercent: String, val Temp: String, val DayOfWeek: String)
data class NowWeatherDTO(val StatusCode: Int, val Weather: ItemNowWeather, val Message: String)

data class MeasureBrixDTO(val Red: Float, val Green: Float, val Blue: Float, val Brix: Float, val MeanBrix: Float, val Message: String, val StatusCode: Int)

data class ItemPrice(val Price: Int, val Date: String)
data class ApplePriceDTO(val StatusCode: Int, val Message: String, val Market: MutableList<ItemPrice>)

data class MarketListDTO(val StatusCode: Int, val Market: MutableList<String>)

// longitude=128.356235 latitude=36.089178
interface RetrofitService {

    @Multipart
    @POST("signIn")
    fun postSignIn(@Part("id") id: String, @Part("password") pw: String): Call<SignInDTO>

    @Multipart
    @POST("user")
    fun postSignUp(@Part("id") id: String, @Part("password") pw: String,
                   @Part("orchard") orchard: String, @Part("location") area: String,
                   @Part("kind") kind: String, @Part("membership") membership: String,
                   @Part("description") description: String, @Part("market") market: String,
                   @Part("latitude") latitude: Double, @Part("longitude") longitude:Double,
                   @Part profileImage: MultipartBody.Part?): Call<SignUpDTO>
    @Multipart
    @POST("sugarcontent")
    fun measureBrix(@Part("id") id: String, @Part profileImage: MultipartBody.Part): Call<MeasureBrixDTO>

    @GET("applePrice")
    fun getApplePrice(@Query("id") id: String): Call<ApplePriceDTO>

    @GET("user")
    fun getUser(@Query("id") id: String): Call<UserDTO>

    @GET("weekWeather")
    fun weekWeather(@Query("id") id: String): Call<WeekWeatherDTO>

    @GET("report")
    fun getReports(@Query("id") id: String): Call<ReportDTO>

    @GET("pest")
    fun getPest(): Call<PestDTO>

    @GET("nowWeather")
    fun getNowWeather(@Query("id") id: String): Call<NowWeatherDTO>

    @GET("marketList")
    fun getMarketList(): Call<MarketListDTO>
}