package com.fcroce.easystores

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fcroce.easystores.data.getDatabase
import com.fcroce.easystores.screens.AddStore
import com.fcroce.easystores.screens.ScanItem
import com.fcroce.easystores.screens.Store
import com.fcroce.easystores.theme.EasyStoresTheme

enum class Screens(@param:StringRes val title: Int) {
    Store(title = R.string.screen_store),
    ScanItem(title = R.string.screen_scan_item),
    AddStore(title = R.string.screen_add_store),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasyStoresMainScreenBar(
    currentScreen: Screens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun EasyStoresMainScreen(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.Store.name
    )
    val db = getDatabase()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            EasyStoresMainScreenBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Store.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screens.Store.name) {
                Store(openScanItemScreen = {
                    navController.navigate(Screens.ScanItem.name)
                }, db)
            }

            composable(route = Screens.ScanItem.name) {
                ScanItem(returnToPreviousScreen = {
                    navController.popBackStack()
                })
            }

            composable(route = Screens.AddStore.name) {
                AddStore()
            }
        }
    }
}

@Preview
@Composable
fun PreviewEasyStoresMainScreen() {
    EasyStoresTheme {
        Surface {
            EasyStoresMainScreen()
        }
    }
}