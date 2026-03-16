package com.example.newsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.presentation.navigation.Destination
import com.example.newsapp.presentation.navigation.FavoritesDestination
import com.example.newsapp.presentation.navigation.HomeDestination
import com.example.newsapp.presentation.screens.FavoritesScreen
import com.example.newsapp.presentation.screens.TopHeadlines
import com.example.newsapp.presentation.viewmodel.MainViewModel
import com.example.newsapp.presentation.viewmodel.ThemeViewModel
import com.example.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeViewModel: ThemeViewModel by viewModels()
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsStateWithLifecycle()
            NewsAppTheme(isDarkTheme) {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NewsApp(
                        navController = navController,
                        isDarkTheme = isDarkTheme,
                        toggleDarkTheme = { theme -> themeViewModel.toggleDarkTheme(theme) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun NewsApp(navController: NavHostController,
            isDarkTheme : Boolean,
            toggleDarkTheme : (Boolean) -> Unit,
            modifier: Modifier) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Dark Mode")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { toggleDarkTheme(!isDarkTheme) }
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigateSingleTopTo(FavoritesDestination) }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(width = 35.dp, height = 35.dp)
                )
            }
        }
        NavHost(navController, startDestination = HomeDestination,modifier = Modifier) {

            composable<HomeDestination> { backStackEntry ->
                TopHeadlines(
                    onFavoritesIconClick = {
                        navController.navigateSingleTopTo(
                            FavoritesDestination
                        )
                    }
                )
            }
            composable<FavoritesDestination>{
                FavoritesScreen()
            }

        }
    }

}


private fun NavHostController.navigateSingleTopTo(route: Destination) {
    this.navigate(route){
        launchSingleTop = true
        restoreState = true
        popUpTo(route)
    }
}


