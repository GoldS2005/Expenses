@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expenses.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expenses.data.Category
import com.example.expenses.data.Expense


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    onBackwardClick: () -> Unit,
    addExpense: (Expense) -> Unit,
    categories: List<Category>) {
    val amountState = remember { mutableStateOf(0.0) }
    val categoryState = remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current

    Column {
        Row(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick =  onBackwardClick ) {
                Icon(imageVector =  Icons.Filled.ArrowBack, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryContainer)

            }

        }
        TextField(
            value = amountState.value.toString(),
            onValueChange = { amountState.value = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Сумма расхода") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Выберите категорию:")
        Spacer(modifier = Modifier.height(8.dp))
        categories.forEach { category ->
            Button(
                onClick = { categoryState.value = category.name },
                modifier = Modifier.padding(4.dp)) {
                Text(category.name)
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val newExpense = Expense(id = 1, amount = amountState.value, category = categoryState.value)
            addExpense(newExpense)
            amountState.value = 0.0
            categoryState.value = ""
            localFocusManager.clearFocus()
        }) {
            Text("Сохранить")
        }
    }
}