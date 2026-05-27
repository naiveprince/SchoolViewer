package com.naiveprince.schoolviewer
import com.naiveprince.schoolviewer.ui.SchoolDetailScreen
import com.naiveprince.schoolviewer.R

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
// import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.naiveprince.schoolviewer.data.SchoolRepository
//import com.naiveprince.schoolviewer.model.SchoolV2Dto
import com.naiveprince.schoolviewer.model.SchoolV2Dto
import com.naiveprince.schoolviewer.network.ApiClient
import com.naiveprince.schoolviewer.ui.SchoolDetailScreen
import com.naiveprince.schoolviewer.ui.SchoolListViewModel
import com.naiveprince.schoolviewer.ui.SchoolListViewModelFactory
import com.naiveprince.schoolviewer.ui.theme.SchoolViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = SchoolRepository(ApiClient.schoolApi)

        setContent {
            SchoolViewerTheme {
                val viewModel: SchoolListViewModel = viewModel(
                    factory = SchoolListViewModelFactory(repository)
                )
                SchoolViewerApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolViewerApp(viewModel: SchoolListViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSchools()
    }

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            SchoolListScreen(
                viewModel = viewModel,
                onRefresh = { viewModel.refresh() },
                onSchoolClick = { school ->
                    navController.navigate("detail/${school.id}")
                }
            )
        }

        composable(
            route = "detail/{schoolId}",
            arguments = listOf(
                navArgument("schoolId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val schoolId = backStackEntry.arguments?.getLong("schoolId")
            val school = uiState.schools.find { it.id == schoolId }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(school?.schoolName ?: "Detail") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    SchoolDetailScreen(school = school)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolListScreen(
    viewModel: SchoolListViewModel,
    onRefresh: () -> Unit,
    onSchoolClick: (SchoolV2Dto) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {



        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "中学受験 英語入試情報",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0x33000000),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = onRefresh) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = Color(0xCCFFFFFF),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.query,
                            onValueChange = { viewModel.updateQuery(it) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("学校名で検索") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )


                    }
                }

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }

                    uiState.errorMessage != null -> {
                        Surface(
                            color = Color(0xD0FFFFFF),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text(
                                text = "Error: ${uiState.errorMessage}",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(uiState.filteredSchools) { school ->
                                SchoolCard(
                                    school = school,
                                    onClick = { onSchoolClick(school) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SchoolCard(
    school: SchoolV2Dto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xEAF7F7F2)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = school.schoolName ?: "(no name)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E2A1E)
            )

            Text(
                text = "区分: ${school.category ?: "-"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF405240)
            )

            Text(
                text = "試験日: ${school.examDates ?: "-"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF405240)
            )

            Text(
                text = "定員: ${school.capacity ?: "-"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF405240)
            )

            Text(
                text = "科目: ${school.subjects ?: "-"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF5A6B5A)
            )
        }
    }
}