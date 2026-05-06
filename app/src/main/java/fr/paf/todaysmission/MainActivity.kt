package fr.paf.todaysmission

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import fr.paf.todaysmission.views.FriendScreen
import fr.paf.todaysmission.views.GroupScreen
import fr.paf.todaysmission.views.HomeScreen
import fr.paf.todaysmission.views.InviteScreen
import fr.paf.todaysmission.views.ListGroupScreen
import fr.paf.todaysmission.views.LoginScreen
import fr.paf.todaysmission.views.NotifyScreen
import fr.paf.todaysmission.views.ProofGroupScreen
import fr.paf.todaysmission.views.SettingsScreen
import fr.paf.todaysmission.views.UploadScreen

@AndroidEntryPoint
class   MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavHostController;
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }


        // pour savoir si un met la barre de navigation visible ou non
        when (navBackStackEntry?.destination?.route) {
            "home" -> {
                bottomBarState.value = true
            }

            "login" -> {
                bottomBarState.value = false
            }

            "groups" -> {
                bottomBarState.value = true
            }

            "settings" -> {
                bottomBarState.value = true
            }

            "group/{id}" -> {
                bottomBarState.value = false
            }

            "friends" -> {

            }
        }

        Scaffold(
            bottomBar = { BottomNavBar(navController, bottomBarState) }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("login") {
                    LoginScreen(
                        onSuccess = {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable("home") { HomeScreen(navController) }
                composable("groups") { ListGroupScreen(navController) }
                composable("settings") { SettingsScreen() }
                composable(
                    "group/{id}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.StringType
                            nullable = false
                        },
                    ))
                { entry ->
                    val id = entry.arguments?.getString("id") ?: "1"
                    GroupScreen(id, navController)
                }
                composable(
                    route = "upload/{id}/{groupId}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        },
                        navArgument("groupId") {
                            type = NavType.StringType
                        }
                    )
                ) { entry ->
                    val id = entry.arguments?.getString("id")
                    val groupId = entry.arguments!!.getString("groupId")!!
                    UploadScreen(id, groupId, navController)
                }
                    composable("friends") { FriendScreen(navController) }
                    composable("notif") { NotifyScreen(navController) }
                    composable("invite/{id}") { entry ->
                        val id = entry.arguments?.getString("id") ?: "1"
                        InviteScreen(id, navController)
                    }
                composable(
                    route = "group/uploads/{id}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        }
                    )
                ) { entry ->
                    val id = entry.arguments?.getString("id")!!
                    ProofGroupScreen(id, navController)
                }
                }
            }
        }

        @Composable
        fun BottomNavBar(navController: NavController, bottomBarState: MutableState<Boolean>) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            AnimatedVisibility(visible = bottomBarState.value,) {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = currentRoute == "home",
                        onClick = { navController.navigate("home") },
                        icon = { Icon(Icons.Outlined.Home, "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "groups",
                        onClick = { navController.navigate("groups") },
                        icon = { Icon(Icons.Outlined.Group, "Group") },
                        label = { Text("Group") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "settings",
                        onClick = { navController.navigate("settings") },
                        icon = { Icon(Icons.Outlined.Settings, "Settings") },
                        label = { Text("Settings") }
                    )
                }
            }
        }
    }
