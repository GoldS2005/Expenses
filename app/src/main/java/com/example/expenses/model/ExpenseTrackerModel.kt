@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
package com.example.expenses.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.expenses.data.Category
import com.example.expenses.data.Expense

class ExpenseTrackerViewModel: ViewModel() {
    private val _expenses = mutableStateOf(emptyList<Expense>())
    val expenses: MutableState<List<Expense>> = _expenses

    private val _categories = mutableStateOf(
        listOf(Category("Питание"), Category("Развлечения"), Category("Транспорт"))
    )
    val categories: MutableState<List<Category>> = _categories


    fun addExpense(expense: Expense) {
        _expenses.value += expense
    }

    fun addCategory(category: Category) {
        _categories.value += category
    }

    fun removeExpense(expense: Expense) {
        _expenses.value = _expenses.value.toMutableList().also { it.remove(expense) }
    }

    fun removeCategory(category: Category) {
        _categories.value = _categories.value.toMutableList().also { it.remove(category) }
    }
}

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







