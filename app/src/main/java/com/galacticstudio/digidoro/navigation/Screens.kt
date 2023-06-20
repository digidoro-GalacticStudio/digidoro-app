package com.galacticstudio.digidoro.navigation

const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"
const val HOME_GRAPH_ROUTE = "home"
const val TODO_GRAPH_ROUTE = "todo"
const val NOTE_GRAPH_ROUTE = "note"
const val POMODORO_GRAPH_ROUTE = "pomodoro"
const val ACCOUNT_GRAPH_ROUTE = "account"

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object Login: Screen(route = "login_screen")
    object SignUp: Screen(route = "sign_up_screen")
    object Todo: Screen(route = "todo_screen")
    object Note: Screen(route = "note_screen")
    object Pomodoro: Screen(route = "pomodoro_screen")
    object Account: Screen(route = "account_screen")
    object Ranking: Screen(route = "ranking_screen")
    object ForgotPassword: Screen(route = "forgot_password_screen")
    object VerifyAccount: Screen(route = "verify_account_screen")
}