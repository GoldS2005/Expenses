@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
package com.example.expenses.model

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenses.data.Category
import com.example.expenses.data.Expense

class ExpenseTrackerViewModel: ViewModel() {
    private val _expenses = mutableStateOf(emptyList<Expense>())
    val expenses: State<List<Expense>> = _expenses

    private val _categories = mutableStateOf(
        listOf(Category("Питание"), Category("Развлечения"), Category("Транспорт"))
    )
    val categories: State<List<Category>> = _categories


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

@Composable
fun AddExpenseScreen() {
    val amountState = remember { mutableStateOf(0.0) }
    val categoryState = remember { mutableStateOf("") }
    val expenseViewModel: ExpenseTrackerViewModel = viewModel()
    val categories = expenseViewModel.categories.value
    val localFocusManager = LocalFocusManager.current

    Column {
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
            expenseViewModel.addExpense(
                Expense(
                    id = 1,
                    amount = amountState.value,
                    category = categoryState.value
                )
            )
            amountState.value = 0.0
            categoryState.value = ""
            localFocusManager.clearFocus()
        }) {
            Text("Сохранить")
        }
    }
}


@Composable
fun AddCategoryScreen() {
    val expenseViewModel: ExpenseTrackerViewModel = viewModel()
    val newCategoryState = remember { mutableStateOf("") }
    val categories = expenseViewModel.categories.value
    val localFocusManager = LocalFocusManager.current

    Column {
        TextField(
            value = newCategoryState.value,
            onValueChange = { newCategoryState.value = it },
            label = { Text("Новая категория") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            expenseViewModel.addCategory(Category(newCategoryState.value))
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
                IconButton(onClick = { expenseViewModel.removeCategory(category) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                }
            }
        }

    }
}






