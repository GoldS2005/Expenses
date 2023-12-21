package com.example.expenses

import com.example.expenses.data.Joke
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET



private val BASE_URL = "https://official-joke-api.appspot.com/"

interface JokesApi {
    @GET("random_joke")
    suspend fun getrandomJoke(): Joke
}

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

val jokeApiService = retrofit.create(JokesApi::class.java)




