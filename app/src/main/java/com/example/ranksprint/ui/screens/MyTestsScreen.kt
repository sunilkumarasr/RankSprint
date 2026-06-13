package com.example.ranksprint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController
import com.example.quiztech.model.MyExamItem
import com.example.ranksprint.ui.viewmodels.MyTestsViewModel

@Composable
fun MyTestsScreen(
    navController: NavController,
    viewModel: MyTestsViewModel = viewModel()
) {
    val completedTests by viewModel.completedTests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val userId = Utils.getData(context, "user_id", "") as String
        if (userId.isNotEmpty()) {
            viewModel.fetchMyExams(userId)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "My Tests",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF004AAD),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (error != null) {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    if (completedTests.isEmpty()) {
                        Text(
                            text = "No completed tests found",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(completedTests) { test ->
                                CompletedTestItem(test) {
                                    navController.navigate("scorecard/${test.productId}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF004AAD) else Color(0xFFF5F5F5),
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        elevation = null,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun CompletedTestItem(test: MyExamItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = test.examName ?: "Mock Test",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF004AAD),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                val scoreDisplay = if (test.scorePercentage != null) {
                    "${test.scorePercentage?.substringBefore(".")}%"
                } else "N/A"
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = scoreDisplay,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = test.attemptedDate ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val total = test.totalQuestions?.substringBefore(".") ?: "0"
                val correct = test.correctAnswers?.substringBefore(".") ?: "0"
                val wrong = test.wrongAnswers?.toString()?.substringBefore(".") ?: "0"
                val left = test.unAttemptedQuestions?.toString()?.substringBefore(".") ?: "0"

                StatItem("Total", total)
                StatItem("Correct", correct, Color(0xFF2E7D32))
                StatItem("Wrong", wrong, Color(0xFFC62828))
                StatItem("Left", left, Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD))
            ) {
                Text(
                    text = "View Result",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, valueColor: Color = Color.Black) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}
