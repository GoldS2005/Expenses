package com.example.expenses.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenses.network.jokeApiService
import kotlinx.coroutines.launch

class GetJokesApiModel: ViewModel() {
    init {
        getrandomJoke()
    }

    private fun getrandomJoke() {
        viewModelScope.launch {
            val joke = jokeApiService.getrandomJoke()
        }
    }

}