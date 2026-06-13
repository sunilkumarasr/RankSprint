package com.example.ranksprint.ui.screens

import android.util.Log
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ranksprint.ui.viewmodels.TestInstructionsViewModel
import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestInstructionsScreen(
    navController: NavController,
    testId: String,
    onNavigateBack: () -> Unit,
    onStartTest: () -> Unit,
    viewModel: TestInstructionsViewModel = viewModel()
) {
    var isConfirmed by remember { mutableStateOf(false) }
    val testDetails by viewModel.testDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(testId) {
        viewModel.fetchTestDetails(testId)
    }

    LaunchedEffect(error) {
        if (error == "NOT_LOGGED_IN") {
            navController.navigate(com.example.ranksprint.navigation.Screen.NotLoggedIn.route) {
                popUpTo("test_instructions/$testId") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Back", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Log.e("error","error $error")
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null&& error=="ALREADY_ATTEMPTED") {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (error == "ALREADY_ATTEMPTED") "You have already attempted this test." else error!!,
                        color = if (error == "ALREADY_ATTEMPTED") Color(0xFF004AAD) else Color.Red,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    if (error == "ALREADY_ATTEMPTED") {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                navController.navigate(com.example.ranksprint.navigation.Screen.Scorecard.createRoute(testId))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE5D10))
                        ) {
                            Text("View Scorecard")
                        }
                    }
                }
            } else if (testDetails != null) {
                val details = testDetails!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = details.title ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF004AAD),
                                fontWeight = FontWeight.Bold
                            )
                            if(!details.descriptions.isNullOrEmpty())
                            Spacer(modifier = Modifier.height(8.dp))
                            if(!details.descriptions.isNullOrEmpty())
                            AndroidView(
                                factory = { context ->
                                    WebView(context).apply {
                                        webViewClient = WebViewClient()
                                        settings.javaScriptEnabled = true

                                        // Disable copy/select
                                        isLongClickable = false
                                        setOnLongClickListener { true }

                                        setOnCreateContextMenuListener(null)

                                        setBackgroundColor(0) // Transparent
                                    }
                                },
                                update = { webView ->
                                    val htmlContent = """
                                <html>
                                <head>
                                <style>
                                    body {
                                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                                        font-size: 14px;
                                        line-height: 1;
                                        color: #616161;
                                        margin: 0;
                                        padding: 0;
                                        
                                        /* Disable text selection */
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;

    /* Disable copy/share menu on long press */
    -webkit-touch-callout: none;
                                    }
                                </style>
                                </head>
                                <body>
                                    ${details.descriptions ?: ""}
                                </body>
                                </html>
                            """.trimIndent()
                                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp, max = 400.dp)
                            )
                            if(!details.test_instructions.isNullOrEmpty())
                            Spacer(modifier = Modifier.height(8.dp))
                            if(!details.test_instructions.isNullOrEmpty())
                                Text(
                                    text = "Test Instructions",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color(0xFF004AAD),
                                    fontWeight = FontWeight.Bold
                                )
                            if(!details.test_instructions.isNullOrEmpty())
                            AndroidView(
                                factory = { context ->
                                    WebView(context).apply {
                                        webViewClient = WebViewClient()
                                        settings.javaScriptEnabled = true

                                        // Disable copy/select
                                        isLongClickable = false
                                        setOnLongClickListener { true }

                                        setOnCreateContextMenuListener(null)

                                        setBackgroundColor(0) // Transparent
                                    }
                                },
                                update = { webView ->
                                    val htmlContent = """
                                <html>
                                <head>
                                <style>
                                    body {
                                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                                        font-size: 14px;
                                        line-height: 1;
                                        color: #616161;
                                        margin: 0;
                                        padding: 0;
                                        
                                         /* Disable text selection */
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;

    /* Disable copy/share menu on long press */
    -webkit-touch-callout: none;
                                    }
                                </style>
                                </head>
                                <body>
                                    ${details.test_instructions ?: ""}
                                </body>
                                </html>
                            """.trimIndent()
                                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp, max = 400.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            InstructionRow("Questions :", (details.questions ?: "0"))
                            InstructionRow("Marks :", (details.marks ?: "0"))
                            InstructionRow("Time :", "${details.pDuration ?: "0"} minutes")
                           // InstructionRow("Rewards :", (details.rewards ?: "0"))
                            //InstructionRow("Test Type :", (details.testType ?: ""))
                        }
                    }

                    if (details.subjects.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Subjects :",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                details.subjects.forEach { subject ->
                                    SubjectRow(
                                        "${subject.subjectName ?: ""}:",
                                        (subject.question_count ?: 0).toString()
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                   /* Text(
                        text = "Instructions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF004AAD)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                setBackgroundColor(0) // Transparent
                            }
                        },
                        update = { webView ->
                            val htmlContent = """
                                <html>
                                <head>
                                <style>
                                    body {
                                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                                        font-size: 14px;
                                        line-height: 1.5;
                                        color: #616161;
                                        margin: 0;
                                        padding: 0;
                                    }
                                </style>
                                </head>
                                <body>
                                    ${details.descriptions ?: ""}
                                </body>
                                </html>
                            """.trimIndent()
                            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp, max = 400.dp)
                    )*/

                    Spacer(modifier = Modifier.height(24.dp))

                    val isEnrolled = details.isEnrolled == 1

                    if (isEnrolled) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = isConfirmed,
                                onCheckedChange = { isConfirmed = it }
                            )
                            Text(
                                text = "I confirm that I have read the instructions and agree to start the test.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onStartTest,
                            enabled = isConfirmed,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Start Test", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = {
                                navController.navigate(com.example.ranksprint.navigation.Screen.Subscription.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE5D10))
                        ) {
                            Text(
                                text = if (details.testType == "Free Test") "Enroll Now" else "Subscribe to Unlock",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InstructionRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFFF6D00), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SubjectRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF004AAD), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFFF6D00), fontWeight = FontWeight.Bold)
    }
}
