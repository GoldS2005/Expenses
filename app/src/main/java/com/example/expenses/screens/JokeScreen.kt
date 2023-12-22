package com.example.expenses.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expenses.data.Joke
import com.example.expenses.network.jokeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun JokeScreen(
    onMainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val joke = remember { mutableStateOf<Joke?>(null) }
    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                joke.value = jokeApiService.getrandomJoke()
            }
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    if (joke.value != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Шутка:")
            Spacer(modifier = Modifier.height(16.dp))
            Text(joke.value!!.setup)
            Spacer(modifier = Modifier.height(8.dp))
            Text(joke.value!!.punchline)
        }
    } else if (error.value != null) {
        Text(error.value!!)
        println(error.value!!)
    } else {
        CircularProgressIndicator()
    }
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onMainClick) {
            Text(text = "Войти", textAlign = TextAlign.Center, modifier = Modifier.width(75.dp))

        }


    }
}