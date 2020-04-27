package com.company.prodemo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable


interface WeatherService {
    @GET("weather_mini")
    fun getMessage(@Query("city") city: String): Observable<WeatherEntity>
}