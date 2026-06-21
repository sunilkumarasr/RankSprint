package com.example.ranksprint.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiztech.model.NotificationItem
import com.example.quiztech.model.NotificationResponse
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    var notifications by remember {
        mutableStateOf<List<NotificationItem>>(emptyList())
    }

    LaunchedEffect(Unit) {

        val userId = Utils.getData(context, "user_id", "") as String

        Log.e("Notification_", "userId = $userId")

        if (userId.isNotEmpty()) {

            isLoading = true

            ServiceManager.getDataManager().getNotifications(
                object : Callback<NotificationResponse> {

                    override fun onResponse(
                        call: Call<NotificationResponse>,
                        response: Response<NotificationResponse>
                    ) {

                        isLoading = false

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!

                            Log.e("Notification", "status=${body.status}")
                            Log.e("Notification", "message=${body.message}")
                            Log.e(
                                "Notification",
                                "size=${body.notifications.size}"
                            )

                            message = body.message ?: ""

                            if (body.status == 1) {
                                notifications = body.notifications
                            } else {
                                notifications = emptyList()
                            }

                        } else {
                            message = "Failed to load notifications"
                        }
                    }

                    override fun onFailure(
                        call: Call<NotificationResponse>,
                        t: Throwable
                    ) {

                        isLoading = false

                        Log.e(
                            "Notification",
                            "Error: ${t.message}"
                        )

                        message = t.message ?: "Something went wrong"
                    }
                },
                userId
            )
        } else {
            message = "User not found"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        when {

            isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            notifications.isEmpty() -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "🔔",
                            style = MaterialTheme.typography.headlineLarge
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = message.ifEmpty {
                                "No notifications found"
                            },
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(notifications) { notification ->

                        NotificationCard(
                            title = notification.title.orEmpty(),
                            message = notification.message.orEmpty(),
                            date = notification.createdAt.orEmpty()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    title: String,
    message: String,
    date: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

}