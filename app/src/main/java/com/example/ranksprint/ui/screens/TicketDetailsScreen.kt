package com.example.ranksprint.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiztech.model.TicketDetailsItem
import com.example.quiztech.model.TicketDetailsResponse
import com.example.quiztech.model.TicketReplyItem
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.ui.components.BottomNavigationBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailsScreen(
    ticketId: String,
    navController: NavController,
    onNavigateBack: () -> Unit
) {

    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    var ticket by remember {
        mutableStateOf<TicketDetailsItem?>(null)
    }

    var replies by remember {
        mutableStateOf<List<TicketReplyItem>>(emptyList())
    }

    LaunchedEffect(ticketId) {

        isLoading = true

        ServiceManager.getDataManager().getTicketDetails(
            object : Callback<TicketDetailsResponse> {

                override fun onResponse(
                    call: Call<TicketDetailsResponse>,
                    response: Response<TicketDetailsResponse>
                ) {

                    isLoading = false

                    if (response.isSuccessful && response.body() != null) {

                        val body = response.body()!!

                        message = body.message ?: ""

                        if (body.status == 1) {

                            ticket = body.ticket
                            replies = body.replies

                        } else {
                            message = body.message ?: "No details found"
                        }

                    } else {
                        message = "Failed to load ticket details"
                    }
                }

                override fun onFailure(
                    call: Call<TicketDetailsResponse>,
                    t: Throwable
                ) {

                    isLoading = false
                    message = t.message ?: "Something went wrong"
                }
            },
            ticketId
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Ticket Details")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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

            ticket == null -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(message)
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

                    item {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = ticket?.subject.orEmpty(),
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text = ticket?.message.orEmpty()
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text = "Status: ${ticket?.status.orEmpty()}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = ticket?.createdAt.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Replies",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(replies) { reply ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = reply.userType.orEmpty(),
                                    style = MaterialTheme.typography.titleSmall
                                )

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                Text(
                                    text = reply.message.orEmpty()
                                )

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                Text(
                                    text = reply.createdAt.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}