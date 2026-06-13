package com.example.ranksprint.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController

import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ranksprint.ui.viewmodels.MockTestViewModel

import com.example.quiztech.model.SubmitExamResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestEngineScreen(
    navController: NavController,
    testId: String,
    onNavigateBack: () -> Unit,
    onFinishTest: (SubmitExamResponse) -> Unit,
    viewModel: MockTestViewModel = viewModel()
) {
    val questions by viewModel.questions.collectAsState()
    val sections by viewModel.sections.collectAsState()
    val testDetails by viewModel.testDetails.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedOptions by viewModel.selectedOptions.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val submitSuccess by viewModel.submitSuccess.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit Exam") },
            text = { Text("Are you sure you want to exit the exam? Your progress will be lost.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onNavigateBack()
                }) {
                    Text("Exit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(testId) {
        viewModel.startTest(testId)
    }

    LaunchedEffect(submitSuccess) {
        submitSuccess?.let {
            onFinishTest(it)
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Back", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else if (questions.isNotEmpty()) {
                val currentQuestion = questions.getOrNull(currentQuestionIndex)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = testDetails?.title ?: "Test",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color(0xFF004AAD),
                                fontWeight = FontWeight.Bold
                            )
                            currentQuestion?.sectionName?.let {
                                Text(
                                    text = "Section: $it",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFFE5D10),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            color = Color(0xFF001D36),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = timeString, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section Tabs
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        sections.forEach { (sectionName, sectionQuestions) ->
                            item {
                                val isSelected = currentQuestion?.sectionName == sectionName
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        val firstQuestionOfSection = sectionQuestions.firstOrNull()
                                        val index = questions.indexOf(firstQuestionOfSection)
                                        if (index != -1) {
                                            viewModel.setCurrentQuestion(index)
                                        }
                                    },
                                    label = { Text(sectionName) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF004AAD),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Question: ${currentQuestionIndex + 1}/${questions.size}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF004AAD),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Quit",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Red,
                                        modifier = Modifier.clickable { showExitDialog = true }
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                currentQuestion?.let { q ->
                                    Text(
                                        text = android.text.Html.fromHtml(q.questionText ?: "", android.text.Html.FROM_HTML_MODE_LEGACY).toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    q.options?.forEach { option ->
                                        OptionItem(
                                            text = option.optionText ?: "",
                                            isSelected = selectedOptions[q.id] == option.id,
                                            onClick = { viewModel.selectOption(q.id!!, option.id!!) }
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (currentQuestionIndex > 0) Arrangement.SpaceEvenly else Arrangement.Center
                        ) {
                            if (currentQuestionIndex > 0) {
                                Button(
                                    onClick = { viewModel.previousQuestion() },
                                    modifier = Modifier.width(140.dp).height(48.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001D36))
                                ) {
                                    Text("Previous")
                                }
                            }
                            Button(
                                onClick = {
                                    if (currentQuestionIndex < questions.size - 1) {
                                        viewModel.nextQuestion()
                                    } else {
                                        viewModel.submitExam(testId)
                                    }
                                },
                                modifier = Modifier.width(140.dp).height(48.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE5D10))
                            ) {
                                Text(if (currentQuestionIndex == questions.size - 1) "Finish" else "Next")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Question Overview",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF004AAD),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OverviewLegend(Color.Blue, "Current")
                        OverviewLegend(Color(0xFF43A047), "Answered")
                        OverviewLegend(Color(0xFFFF6D00), "Review")
                        OverviewLegend(Color(0xFFE1E2EC), "Pending")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                        }
                        LazyRow(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(questions.size) { index ->
                                val qId = questions[index].id
                                val isCurrent = currentQuestionIndex == index
                                val isAnswered = selectedOptions.containsKey(qId)

                                val bgColor = when {
                                    isCurrent -> Color(0xFF004AAD)
                                    isAnswered -> Color(0xFF43A047)
                                    else -> Color(0xFFE1E2EC)
                                }
                                val textColor = if (isCurrent || isAnswered) Color.White else Color.Black

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(bgColor)
                                        .clickable { viewModel.setCurrentQuestion(index) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "${index + 1}", color = textColor, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                        }
                    }
                }
            } else if (!isLoading && questions.isEmpty() && error == null) {
                Text(
                    text = "No questions available for this test.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun OptionItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFFFF6D00) else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            ),
        color = if (isSelected) Color(0xFFFF6D00).copy(alpha = 0.1f) else Color.White,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = if (isSelected) Color(0xFFFF6D00) else Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFFF6D00))
            }
        }
    }
}

@Composable
fun OverviewLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}
