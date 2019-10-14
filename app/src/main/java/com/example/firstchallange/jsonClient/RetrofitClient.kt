package com.example.firstchallange.jsonClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient{
    companion object{

        fun getClient():Retrofit{
            return Retrofit.Builder()
                .baseUrl("http://starlord.hackerearth.com")
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}