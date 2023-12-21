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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expenses.data.Category
import com.example.expenses.data.Expense
import com.example.expenses.data.Joke
import com.example.expenses.model.AddCategoryScreen
import com.example.expenses.model.AddExpenseScreen
import com.example.expenses.model.ExpenseTrackerViewModel
import com.example.expenses.ui.theme.ExpensesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    val navController = rememberNavController()
    val expenseTrackerViewModel: ExpenseTrackerViewModel = viewModel()

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                onCategoryClick = { navController.navigate("add_category_screen") },
                onExpenseClick = { navController.navigate("add_expense_screen") },
                expenses = expenseTrackerViewModel.expenses.value,
                categories = expenseTrackerViewModel.categories.value,
                removeExpense = { expense -> expenseTrackerViewModel.removeExpense(expense) }
            )
        }
        composable("add_category_screen") {
            AddCategoryScreen(
                onBackwardClick = { navController.popBackStack() },
                addCategory = { category -> expenseTrackerViewModel.addCategory(category) },
                categories = expenseTrackerViewModel.categories.value,
                removeCategory = { category ->  expenseTrackerViewModel.removeCategory(category)}

            )
        }
        composable("add_expense_screen") {
            AddExpenseScreen(
                onBackwardClick = { navController.popBackStack() },
                addExpense = { expense -> expenseTrackerViewModel.addExpense(expense) },
                categories = expenseTrackerViewModel.categories.value
            )
        }

    }

}

@Composable
fun DisplayJoke() {
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
}


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
    modifier: Modifier = Modifier) {

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

@Preview(showBackground = true)
@Composable
fun ExpensesPreview() {
    ExpensesTheme {
        ExpenseManagerApp()

    }
}