package com.example.expenses.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expenses.data.Category
import com.example.expenses.data.Expense

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: () -> Unit,
    onExpenseClick: () -> Unit,
    removeExpense: (Expense) -> Unit,
    expenses: List<Expense>,
    categories: List<Category>) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ExpenseHistory(expenses, onDeleteExpense = removeExpense)
        Spacer(modifier = Modifier.height(16.dp))
        ExpenseStatistics(expenses, categories)
    }
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onCategoryClick) {
            Text(text = "Категория", textAlign = TextAlign.Center, modifier = Modifier.width(75.dp))

        }


    }
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onExpenseClick) {
            Text(text = "Расход", textAlign = TextAlign.Center, modifier = Modifier.width(75.dp))

        }
    }
}


@Composable
fun ExpenseHistory(
    expenses: List<Expense>,
    modifier: Modifier = Modifier,
    onDeleteExpense: (Expense) -> Unit) {

    Column(modifier = modifier) {
        Text("История расходов:", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(expenses) { expense ->
                Row {
                    Text(" ${expense.amount},  ${expense.category}")
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { onDeleteExpense(expense) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                    }
                }

            }

        }

    }

}


@Composable
fun ExpenseStatistics(
    expenses: List<Expense>,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {

    val groupedExpenses = expenses.groupBy { it.category }

    Column {
        Text("Статистика расходов по категориям:", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        for (category in categories) {
            val expensesInCategory = groupedExpenses[category.name] ?: emptyList()
            Text("${category.name}: ${calculateTotalExpenses(expensesInCategory)}")
        }
    }
}

fun calculateTotalExpenses(expenses: List<Expense>): Double {
    var total = 0.0
    for (expense in expenses) {
        total += expense.amount
    }
    return total
}