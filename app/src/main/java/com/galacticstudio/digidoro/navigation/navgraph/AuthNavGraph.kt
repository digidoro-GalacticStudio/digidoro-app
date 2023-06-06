package com.galacticstudio.digidoro.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.galacticstudio.digidoro.navigation.AUTH_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.SignUpScreen
import com.galacticstudio.digidoro.ui.forgotpassword.ForgotPasswordScreen
import com.galacticstudio.digidoro.ui.forgotpassword.VerifyAccountScreen
import com.galacticstudio.digidoro.ui.login.LoginScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
){
    navigation(
        startDestination = Screen.Login.route,
        route = AUTH_GRAPH_ROUTE
    ){
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.ForgotPassword.route
        ) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(
            route = Screen.VerifyAccount.route
        ) {
            VerifyAccountScreen(navController = navController)
        }
        composable(
            route = Screen.SignUp.route
        ) {
            SignUpScreen(navController = navController)
        }
    }
}