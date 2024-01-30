package com.example.comptest

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.comptest.ui.theme.CompTestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompTestTheme {
                Content()
            }
        }
    }
}

@Composable
fun Header() {
    Box(
        Modifier
            .height(160.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxHeight()
                .padding(start = 10.dp)
        ) {
            Image(
                painterResource(id = R.drawable.icont),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )
            Text(
                "니르",
                color = Color.White,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                "니르니르 니르",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}

data class NavEntry(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun ContentPreview() {
    CompTestTheme {
        Content()
    }
}

@Composable
fun HomeScreen() {
    CompTestTheme {
        Box(
            Modifier
                .background(Color.Red)
                .fillMaxSize()
        ) {
            Text("Hello")
        }
    }
}

typealias LangModule = String

data class Language(
    val code: String,
    val langName: String,
    val flag: Int,
    val modules: List<LangModule>
)

val languages = listOf(
    Language("ja", "Japanese", R.drawable.japan_flag,
        listOf("Hiragana", "Katakana", "Kanji", "Words")),
    Language("ko", "Korean", R.drawable.korea_flag,
        listOf("Hangul", "Words")),
)


@Preview(backgroundColor = 0xff00ff)
@Composable
fun Flag(lang: Language = languages[0], onClick: (() -> Unit) = {}) {
    Surface(
        onClick = onClick
    ) {
        Image(
            painterResource(id = lang.flag),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .wrapContentWidth()
                .width(180.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ModulesScreen(code: String = languages[0].code) {
    val lang = languages.first { it.code == code }
    CompTestTheme {
        Surface(Modifier.fillMaxSize()) {
            LazyColumn {
                items(lang.modules) {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(it)
                    }
                }
            }
        }
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LangScreen(navController: NavController) {
    val langs = languages
    CompTestTheme {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.FixedSize(180.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(20.dp)) {
                items(langs.size) {
                    Flag(langs[it]) {
                        navController.navigate("modules/${langs[it].code}")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val entries = listOf(
        NavEntry("Home", "home", Icons.Filled.AccountCircle),
        NavEntry("Languages", "lang", Icons.Filled.Call)
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
            ) {
                Header()

                LazyColumn {
                    items(entries) { e ->
                        NavigationDrawerItem(
                            icon = { Icon(e.icon, "tests") },
                            label = { Text(e.name) },
                            shape = RectangleShape,
                            selected = currentDestination?.hierarchy?.any { it.route == e.route } == true,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(e.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("My App")
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, "Menu")
                        }
                    }
                )
            }
        ) { it ->
            Box(Modifier.padding(it)) {
                NavHost(
                    navController,
                    startDestination = "lang"
                ) {
                    composable("home") {
                        HomeScreen()
                    }
                    composable("lang") {
                        LangScreen(navController)
                    }
                    composable("modules/{lang}") { backStackEntry ->
                        ModulesScreen(backStackEntry.arguments?.getString("lang")!!)
                    }
                }
            }
        }
    }
}