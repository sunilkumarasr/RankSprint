package com.example.ranksprint.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quiztech.model.MockList
import com.example.ranksprint.ui.viewmodels.HomeSubCategoryItemsViewModel
import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController
import com.example.ranksprint.common.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSubCategoryItems(
    navController: NavController,
    categoryId: String,
    subCategoryItemName: String,
    onNavigateBack: () -> Unit,
    onNavigateToTestInstructions: (String) -> Unit,
    viewModel: HomeSubCategoryItemsViewModel = viewModel()
) {
    val mockTests by viewModel.mockTests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(categoryId) {
        Log.e("asdf_", "2")

        val userId = Utils.getData(context, "user_id", "") as String

        viewModel.fetchMockItemsTestsByCategory(categoryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subCategoryItemName, style = MaterialTheme.typography.titleMedium) },
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
                        SubCategoryItemsTestListItem(mockTests[index], onNavigateToTestInstructions)
                    }
                }
            }
        }
    }
}

@Composable
fun SubCategoryItemsTestListItem(mockTest: MockList, onNavigateToTestInstructions: (String) -> Unit) {
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
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToTestInstructions(mockTest.id ?: "") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${mockTest.short_descriptions }",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SubCategoryItemInfoRow("Question Count : ", mockTest.questions ?: "0")
                SubCategoryItemInfoRow("Duration : ", "${mockTest.pDuration ?: "0"} min")
                SubCategoryItemInfoRow("Mark : ", mockTest.marks ?: "0")
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
fun SubCategoryItemInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),

        ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Surface(
            color = Color(0xFFE3F2FD),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF004AAD),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SubCategoryItemInstructionRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

