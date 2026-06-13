package com.example.ranksprint.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quiztech.model.MockList
import com.example.ranksprint.ui.components.BottomNavigationBar
import com.example.ranksprint.ui.viewmodels.SubCategoryTestsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCategoryTestsScreen(
    navController: NavController,
    subCategoryId: String,
    subCategoryName: String,
    onNavigateBack: () -> Unit,
    onNavigateToTestInstructions: (String) -> Unit,
    viewModel: SubCategoryTestsViewModel = viewModel()
) {
    val mockTests by viewModel.mockTests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(subCategoryId) {
        viewModel.fetchMockTestsBySubCategory(subCategoryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subCategoryName, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(mockTests.size) { index ->
                        TestListItem(mockTests[index], onNavigateToTestInstructions)
                    }
                }
            }
        }
    }
}

@Composable
fun TestListItem(mockTest: MockList, onNavigateToTestInstructions: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mockTest.title ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF004AAD),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This mock test helps you quickly assess your preparation.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TestInfoItem("Question Count: ${mockTest.questions ?: "0"}")
                TestInfoItem("Mark: ${mockTest.marks ?: "0"}")
                TestInfoItem("Duration: ${mockTest.pDuration ?: "0"} min")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onNavigateToTestInstructions(mockTest.id ?: "") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F469A))
            ) {
                Text(text = "Start Test", color = Color.White)
            }
        }
    }
}

@Composable
fun TestInfoItem(text: String) {
    Surface(
        color = Color(0xFFE3F2FD),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF004AAD),
            fontWeight = FontWeight.Bold
        )
    }
}

