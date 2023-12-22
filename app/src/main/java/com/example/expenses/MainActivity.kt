@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expenses.model.ExpenseTrackerViewModel
import com.example.expenses.screens.CategoryScreen
import com.example.expenses.screens.ExpenseScreen
import com.example.expenses.screens.JokeScreen
import com.example.expenses.screens.MainScreen
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
    val navController = rememberNavController()
    val expenseTrackerViewModel: ExpenseTrackerViewModel = viewModel()

    NavHost(navController = navController, startDestination = "joke_screen") {
        composable("joke_screen") {
            JokeScreen(
                onMainClick = { navController.navigate("main_screen")}
            )


        }
        composable("main_screen") {
            MainScreen(
                onCategoryClick = { navController.navigate("category_screen") },
                onExpenseClick = { navController.navigate("expense_screen") },
                expenses = expenseTrackerViewModel.expenses.value,
                categories = expenseTrackerViewModel.categories.value,
                removeExpense = { expense -> expenseTrackerViewModel.removeExpense(expense) }
            )
        }
        composable("category_screen") {
            CategoryScreen(
                onBackwardClick = { navController.popBackStack() },
                addCategory = { category -> expenseTrackerViewModel.addCategory(category) },
                categories = expenseTrackerViewModel.categories.value,
                removeCategory = { category ->  expenseTrackerViewModel.removeCategory(category)}

            )
        }
        composable("expense_screen") {
            ExpenseScreen(
                onBackwardClick = { navController.popBackStack() },
                addExpense = { expense -> expenseTrackerViewModel.addExpense(expense) },
                categories = expenseTrackerViewModel.categories.value
            )
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