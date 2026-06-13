package com.example.ranksprint.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.viewmodels.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPlansScreen(
    navController: NavController,
    onSubscribe: (String) -> Unit,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val plans by viewModel.plans.collectAsState()
    val activePlan by viewModel.activePlan.collectAsState()
    val isActivePlan by viewModel.isActivePlan.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val userId = Utils.getData(context, "user_id", "") as String
        viewModel.fetchSubscriptionPlans(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isActivePlan) "Active Plan" else "Subscription Plans", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    text = error ?: "Error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    /*Text(
                        text = if (isActivePlan) "Active Plan" else "Subscription Plans",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF004AAD),
                        fontWeight = FontWeight.Bold
                    )*/
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isActivePlan) "You have an active subscription." else "Unlock premium content with our flexible subscription plans.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF263238)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    if (isActivePlan && activePlan != null) {
                        ActivePlanCard(
                            title = activePlan?.planName ?: "",
                            price = "₹${activePlan?.price}",
                            expiryDate = activePlan?.expiryDate ?: "",
                            expiryDays = activePlan?.expiryDays ?: 0,
                            description = activePlan?.planDesc ?: ""
                        )
                    } else {
                        plans.forEach { plan ->
                            SubscriptionPlanCard(
                                title = plan.planTitle ?: "",
                                price = "₹${plan.price} / ${plan.durationDays} days",
                                description = plan.planDesc ?: "",
                                onClick = { onSubscribe(plan.id ?: "") }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ActivePlanCard(
    title: String,
    price: String,
    expiryDate: String,
    expiryDays: Int,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004AAD)
                )
                Text(
                    text = price,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFE5D10)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Expiry Date: $expiryDate ($expiryDays days left)",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(8.dp))
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
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
                                        line-height: 1.2;
                                        color: #616161;
                                        margin: 0;
                                        padding: 0;
                                    }
                                </style>
                                </head>
                                <body>
                                    $description
                                </body>
                                </html>
                            """.trimIndent()
                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                },
                modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
            )
        }
    }
}

@Composable
fun SubscriptionPlanCard(
    title: String,
    price: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004AAD)
                )
                Text(
                    text = price,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFE5D10)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
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
                                        line-height: 1.2;
                                        color: #616161;
                                        margin: 0;
                                        padding: 0;
                                    }
                                </style>
                                </head>
                                <body>
                                    $description
                                </body>
                                </html>
                            """.trimIndent()
                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                },
                modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD))
            ) {
                Text("Select Plan", color = Color.White)
            }
        }
    }
}
