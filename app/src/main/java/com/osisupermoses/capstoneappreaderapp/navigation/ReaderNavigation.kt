package com.osisupermoses.capstoneappreaderapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.osisupermoses.capstoneappreaderapp.screens.ReaderSplashScreen
import com.osisupermoses.capstoneappreaderapp.screens.details.BookDetailsScreen
import com.osisupermoses.capstoneappreaderapp.screens.home.HomeScreen
import com.osisupermoses.capstoneappreaderapp.screens.home.HomeScreenViewModel
import com.osisupermoses.capstoneappreaderapp.screens.login.LoginScreen
import com.osisupermoses.capstoneappreaderapp.screens.search.BooksSearchViewModel
import com.osisupermoses.capstoneappreaderapp.screens.search.SearchScreen
import com.osisupermoses.capstoneappreaderapp.screens.stats.StatsScreen
import com.osisupermoses.capstoneappreaderapp.screens.update.BookUpdateScreen

@ExperimentalComposeUiApi
@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = ReaderScreens.SplashScreen.name) {

        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderHomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<BooksSearchViewModel>()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }

        composable(ReaderScreens.ReaderStatsScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            StatsScreen(navController = navController, viewModel = homeViewModel)
        }

        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType // note: if arguments were of Integer type, it would NavType.IntType
        })) { backStackEntry -> backStackEntry.arguments?.getString("bookId").let {

                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
                arguments = listOf(navArgument("bookItemId") {
                    type = NavType.StringType
                })) { navBackStackEntry ->

            navBackStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navController, bookItemId = it.toString())
            }
        }
    }
}