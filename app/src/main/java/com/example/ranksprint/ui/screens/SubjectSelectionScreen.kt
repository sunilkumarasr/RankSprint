package com.example.ranksprint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectSelectionScreen(
    navController: NavController,
    categoryName: String,
    onNavigateBack: () -> Unit,
    onNavigateToInstructions: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color(0xFF004AAD),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Subjects", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SubjectSelectionCard(
                name = "Aptitude",
                questions = 40,
                marks = 40,
                bgColor = Color(0xFFFFF3E0),
                borderColor = Color.Transparent,
                onClick = { onNavigateToInstructions("aptitude") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SubjectSelectionCard(
                name = "Reasoning",
                questions = 30,
                marks = 30,
                bgColor = Color(0xFFF5F5F5),
                borderColor = Color.Transparent,
                onClick = { onNavigateToInstructions("reasoning") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SubjectSelectionCard(
                name = "English",
                questions = 30,
                marks = 30,
                bgColor = Color(0xFFE8EAF6),
                borderColor = Color(0xFFFF6D00),
                onClick = { onNavigateToInstructions("english") }
            )
        }
    }
}

@Composable
fun SubjectSelectionCard(
    name: String,
    questions: Int,
    marks: Int,
    bgColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF004AAD),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Questions : ", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$questions", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFFF6D00), fontWeight = FontWeight.Bold)
            }
            Row {
                Text(text = "Marks : ", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$marks", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF004AAD), fontWeight = FontWeight.Bold)
            }
        }
    }
}
