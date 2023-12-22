
package com.example.expenses.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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











