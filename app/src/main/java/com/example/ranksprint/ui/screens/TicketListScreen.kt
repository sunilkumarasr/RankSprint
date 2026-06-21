package com.example.ranksprint.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiztech.model.TicketsItem
import com.example.quiztech.model.TicketsResponse
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    navController: NavController,
    onViewAddTicketScreen: () -> Unit,
    onViewTicketDetailsScreen: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    var myRanksList by remember {
        mutableStateOf<List<TicketsItem>>(emptyList())
    }

    LaunchedEffect(Unit) {

        val userId = Utils.getData(context, "user_id", "") as String

        Log.e("userId_", "userId = $userId")

        if (userId.isNotEmpty()) {

            isLoading = true

            ServiceManager.getDataManager().getTickets(
                object : Callback<TicketsResponse> {

                    override fun onResponse(
                        call: Call<TicketsResponse>,
                        response: Response<TicketsResponse>
                    ) {

                        isLoading = false

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!

                            Log.e("getTickets_", "status=${body.status}")
                            Log.e("getTickets_", "message=${body.message}")
                            Log.e(
                                "getTickets_",
                                "size=${body.tickets.size}"
                            )

                            message = body.message ?: ""

                            if (body.status == 1) {
                                myRanksList = body.tickets
                            } else {
                                myRanksList = emptyList()
                            }

                        } else {
                            message = "Failed to load notifications"
                        }
                    }

                    override fun onFailure(
                        call: Call<TicketsResponse>,
                        t: Throwable
                    ) {

                        isLoading = false

                        Log.e(
                            "tickets",
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
                title = { Text("Tickets", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onViewAddTicketScreen()
                },
                modifier = Modifier.size(64.dp),
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Ticket"
                )
            }
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

            myRanksList.isEmpty() -> {

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
                            text = "\uD83C\uDF9F\uFE0F",
                            style = MaterialTheme.typography.headlineLarge
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = message.ifEmpty {
                                "No Ranks found"
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
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(myRanksList) { myRanksL ->

                        TicketListCard(
                            title = myRanksL.subject.orEmpty(),
                            message = myRanksL.message.orEmpty(),
                            date = myRanksL.createdAt.orEmpty(),
                            onClick = {
                                onViewTicketDetailsScreen(myRanksL.id.toString())
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TicketListCard(
    title: String,
    message: String,
    date: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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