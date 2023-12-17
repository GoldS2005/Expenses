@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenses.data.Category
import com.example.expenses.data.Expense
import com.example.expenses.model.AddCategoryScreen
import com.example.expenses.model.AddExpenseScreen
import com.example.expenses.model.ExpenseTrackerViewModel
import com.example.expenses.ui.theme.ExpensesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpensesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseManagerApp()

                }
            }
        }
    }
}

@Composable
fun ExpenseManagerApp() {
    var currentState by remember {
        mutableStateOf(1)
    }

    when(currentState) {
        1-> {
            MainScreen()
            Display1(Forward = {currentState = 2})
            Display2(Forward = {currentState = 3})
        }
        2-> {
            Column(modifier = Modifier, horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top) {
                Display3(Backward = {currentState = 1})
                AddExpenseScreen()
            }

        }
        3-> {
            Column(modifier = Modifier, horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top) {
                Display3(Backward = {currentState = 1})
                AddCategoryScreen()
            }


        }
    }



}



@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val expenseTrackerViewModel: ExpenseTrackerViewModel = viewModel()
    val expenses = expenseTrackerViewModel.expenses.value
    val categories = expenseTrackerViewModel.categories.value
    val removeExpense: (Expense) -> Unit = { expense ->
        expenseTrackerViewModel.removeExpense(expense)
    }
    val joke = remember { mutableStateOf("Загрузка шутки...") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    )   {

        ExpenseHistory(expenses, onDeleteExpense = removeExpense)
        Spacer(modifier = Modifier.height(16.dp))
        ExpenseStatistics(expenses, categories)

        }

    }



@Composable
fun ExpenseHistory(expenses: List<Expense>, modifier: Modifier = Modifier, onDeleteExpense: (Expense) -> Unit) {
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
fun ExpenseStatistics(expenses: List<Expense>, categories: List<Category>, modifier: Modifier = Modifier) {
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

@Composable
fun Display1 (Forward: () -> Unit) {
    Row(verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()) {
        Button(onClick = Forward) {
            Text(text = "Расход", textAlign = TextAlign.Center, modifier = Modifier.width(75.dp))

        }
    }

}
@Composable
fun Display2 (Forward: () -> Unit) {
    Row(verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()) {
        Button(onClick = Forward) {
            Text(text = "Категория", textAlign = TextAlign.Center, modifier = Modifier.width(75.dp))

        }
    }

}

@Composable
fun Display3 (Backward: () -> Unit) {
    Row(verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick =  Backward ) {
            Icon(imageVector =  Icons.Filled.ArrowBack, contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer)

        }

    }

}



@Preview(showBackground = true)
@Composable
fun ExpensesPreview() {
    ExpensesTheme {
        ExpenseManagerApp()

    }
}