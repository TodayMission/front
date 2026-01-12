package fr.paf.todaysmission

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.paf.todaysmission.views.GroupScreen
import fr.paf.todaysmission.views.HomeScreen
import fr.paf.todaysmission.views.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = { BottomNavBar(navController) }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("home") { HomeScreen() }
                composable("group") { GroupScreen() }
                composable("settings") { SettingsScreen() }
            }
        }
    }

    @Composable
    fun BottomNavBar(navController: NavController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBar {
            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = { navController.navigate("home") },
                icon = { Icon(Icons.Default.Home, "Home") },
                label = { Text("Home") }
            )
            NavigationBarItem(
                selected = currentRoute == "group",
                onClick = { navController.navigate("group") },
                icon = { Icon(Icons.Default.Search, "Group") },
                label = { Text("Group") }
            )
            NavigationBarItem(
                selected = currentRoute == "settings",
                onClick = { navController.navigate("settings") },
                icon = { Icon(Icons.Default.Person, "Settings") },
                label = { Text("Settings") }
            )
        }
    }

//    @Composable
//    fun HomeScreen() {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Text("Écran d'accueil", style = MaterialTheme.typography.headlineMedium)
//        }
//    }
//
//    @Composable
//    fun SearchScreen() {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Text("Écran de recherche", style = MaterialTheme.typography.headlineMedium)
//        }
//    }
//
//    @Composable
//    fun ProfileScreen() {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Text("Écran de profil", style = MaterialTheme.typography.headlineMedium)
//        }
//    }
}
