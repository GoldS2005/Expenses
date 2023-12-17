package com.example.expenses

import com.example.expenses.data.Joke
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET



interface JokeApiService {
    @GET("random_joke")
    suspend fun getRandomJoke(): Joke
}

object JokeApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://official-joke-api.appspot.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: JokeApiService = retrofit.create(JokeApiService::class.java)
}

suspend fun getJokeFromApi(): String {
    val jokeResponse = JokeApi.service.getRandomJoke()
    return "${jokeResponse.setup} ${jokeResponse.punchline}"
}



