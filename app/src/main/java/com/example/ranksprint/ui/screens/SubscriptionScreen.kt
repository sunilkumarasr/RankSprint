package com.example.ranksprint.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.viewmodels.SubscriptionViewModel
import com.example.ranksprint.ui.components.BottomNavigationBar
import androidx.navigation.NavController

import com.example.ranksprint.MainActivity
import com.example.ranksprint.PaymentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    navController: NavController,
    planId: String,
    onNavigateBack: () -> Unit,
    onPayNow: () -> Unit = {},
    viewModel: SubscriptionViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? MainActivity
    val plan by viewModel.selectedPlanDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(planId) {
        viewModel.fetchSubscriptionPlanDetail(planId)
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
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (isLoading && plan == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null && plan == null) {
                Text(
                    text = error ?: "Unknown Error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Subscription Details",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF004AAD),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = plan?.planName ?: "Plan Details",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    SubscriptionDetailRow("Plan Name:", plan?.planName ?: "N/A", Color(0xFF004AAD))
                    SubscriptionDetailRow("Price:", "₹${plan?.price ?: "N/A"}", Color(0xFFFF6D00))
                    SubscriptionDetailRow("Duration:", "${plan?.durationDays ?: "0"} Days", Color(0xFF004AAD))
                    SubscriptionDetailRow("Status:", plan?.status ?: "Active", Color(0xFF004AAD))
                    if (plan?.renewalDate != null) {
                        SubscriptionDetailRow("Renewal Date:", plan?.renewalDate ?: "", Color(0xFF00C853))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "What’s Included",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (plan?.included?.isNotEmpty() == true) {
                        plan?.included?.forEach { item ->
                            IncludedItem(item, Color(0xFFFF6D00))
                        }
                    } else if (plan?.description != null) {
                        Text(
                            text = android.text.Html.fromHtml(plan?.description ?: "", android.text.Html.FROM_HTML_MODE_LEGACY).toString(),
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    } else {
                        IncludedItem("Unlimited Test Attempts", Color(0xFFFF6D00))
                        IncludedItem("Access to All Categories", Color(0xFF004AAD))
                        IncludedItem("Daily Challenge Mode", Color(0xFF004AAD))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        Button(
                            onClick = {
                                val userId = Utils.getData(context, "user_id", "") as String
                                if (userId.isNotEmpty()) {
                                   val amountStr = plan?.price ?: "0"

// Use Regex to find the first sequence of digits
                                    val regex = Regex("\\d+")
                                    val match = regex.find(amountStr)
                                    val amount = match?.value?.toDoubleOrNull() ?: 0.0
                                    if (amount > 0) {
                                        // Initialize Payment Callbacks
                                        PaymentStatus.onPaymentSuccess = { paymentId ->
                                            viewModel.subscribePackage(userId, planId,paymentId!!) { success, message ->
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                if (success) {
                                                    onPayNow()
                                                }
                                            }
                                        }
                                        PaymentStatus.onPaymentError = { _, response ->
                                            Toast.makeText(context, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
                                        }

                                        activity?.startPayment(
                                            amount = amount,
                                            name = plan?.planName ?: "Subscription",
                                            description = "Plan: ${plan?.planName}"
                                        )
                                    } else {
                                        // If amount is 0 or invalid, maybe directly subscribe? 
                                        // But usually price is mandatory.
                                        Toast.makeText(context, "Invalid plan price", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE5D10))
                        ) {
                            Text(text = "Pay Now", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun SubscriptionDetailRow(label: String, value: String, valueColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(valueColor)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
fun IncludedItem(text: String, bulletColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(bulletColor)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
    }
}
