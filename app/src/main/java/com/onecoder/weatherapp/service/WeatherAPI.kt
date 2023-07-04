package com.onecoder.weatherapp5.service


import com.onecoder.weatherapp5.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query




interface WeatherAPI {
    @GET("data/2.5/weather?&units=metric&appid=please don't forget to enter your own api key here!!")
    fun getData(
        @Query("q") cityName:String
    ):Single<WeatherModel>
}