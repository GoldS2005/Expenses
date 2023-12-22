

package com.example.expenses.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.expenses.data.Category


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onBackwardClick: () -> Unit,
    addCategory: (Category) -> Unit,
    removeCategory: (Category) -> Unit,
    categories: List<Category>) {

    val newCategoryState = remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current

    Column {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackwardClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryContainer
                )

            }

        }
        TextField(
            value = newCategoryState.value,
            onValueChange = { newCategoryState.value = it },
            label = { Text("Новая категория") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val newCategory = Category(newCategoryState.value)
            addCategory(newCategory)
            newCategoryState.value = ""
            localFocusManager.clearFocus()
        }) {
            Text("Добавить")
        }
        Spacer(modifier = Modifier.height(16.dp))
        categories.forEach { category ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(category.name)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { removeCategory(category) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                }
            }
        }

    }
}