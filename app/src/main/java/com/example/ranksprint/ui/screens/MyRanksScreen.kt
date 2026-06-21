package com.example.ranksprint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quiztech.model.MyTestResultResponse
import com.example.quiztech.model.TestResultItem
import com.example.quiztech.model.TestSummary
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRanksScreen(
    navController: NavController,
    onNavigateBack: () -> Unit
) {

    val context = LocalContext.current

    var loading by remember { mutableStateOf(false) }

    var summary by remember {
        mutableStateOf<TestSummary?>(null)
    }

    var results by remember {
        mutableStateOf<List<TestResultItem>>(emptyList())
    }

    var message by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        val userId = Utils.getData(context, "user_id", "") as String

        loading = true

        ServiceManager.getDataManager().getMyTestResults(
            object : Callback<MyTestResultResponse> {

                override fun onResponse(
                    call: Call<MyTestResultResponse>,
                    response: Response<MyTestResultResponse>
                ) {

                    loading = false

                    if (response.isSuccessful &&
                        response.body() != null
                    ) {

                        val body = response.body()!!

                        message = body.message ?: ""

                        if (body.status == 1) {

                            summary = body.summary
                            results = body.results

                        } else {

                            results = emptyList()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<MyTestResultResponse>,
                    t: Throwable
                ) {

                    loading = false
                    message = t.message ?: ""
                }
            },
            userId
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Ranks")
                },
                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        if (loading) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(
                        start = 15.dp,
                        end = 15.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                summary?.let {
                    item {
                        SummaryCard(it)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "All Test Results",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(results) {
                    TestResultCard(it)
                }
            }
        }
    }
}

@Composable
fun SummaryCard(
    summary: TestSummary
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            SummaryItem(
                icon = Icons.Default.Description,
                iconBg = Color(0xFFE8F1FF),
                iconColor = Color(0xFF2962FF),
                title = "Total Tests",
                value = "${summary.totalTests}",
                valueColor = Color(0xFF2962FF)
            )

            SummaryItem(
                icon = Icons.Default.CheckCircle,
                iconBg = Color(0xFFE8F8EA),
                iconColor = Color(0xFF2E7D32),
                title = "Tests Attempted",
                value = "${summary.testsAttempted}",
                valueColor = Color(0xFF2E7D32)
            )

            SummaryItem(
                icon = Icons.Default.EmojiEvents,
                iconBg = Color(0xFFF3E8FF),
                iconColor = Color(0xFF9333EA),
                title = "Average Score",
                value = summary.averageScore ?: "0%",
                valueColor = Color(0xFF9333EA)
            )

            SummaryItem(
                icon = Icons.Default.TrendingUp,
                iconBg = Color(0xFFFFF3E0),
                iconColor = Color(0xFFFF9800),
                title = "Best Score",
                value = summary.bestScore ?: "0%",
                valueColor = Color(0xFFFF9800)
            )
        }
    }
}

@Composable
fun SummaryItem(
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    title: String,
    value: String,
    valueColor: Color
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    iconBg,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = valueColor,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun TestResultCard(
    item: TestResultItem
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        Color(0xFFE8F1FF),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.title?.firstOrNull()?.uppercase() ?: "T",
                    color = Color(0xFF2962FF),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.title ?: "",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.DateRange,
                        null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        item.attemptedDate ?: "",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        Icons.Default.AccessTime,
                        null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "${item.duration} min",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFE8F8EA),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        )
                ) {

                    Text(
                        "Full Test",
                        color = Color(0xFF2E7D32),
                        fontSize = 9.sp
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularScore(
                    item.scorePercentage?.toFloat() ?: 0f
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    item.score ?: "",
                    color = getScoreColor(
                        item.scorePercentage?.toFloat() ?: 0f
                    ),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun CircularScore(
    percentage: Float
) {

    val scoreColor = getScoreColor(percentage)

    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 3.dp,
            color = scoreColor,
            trackColor = Color(0xFFE5E5E5)
        )

        Text(
            "${percentage.toInt()}%",
            fontWeight = FontWeight.Bold,
            color = scoreColor,
            fontSize = 8.sp
        )
    }
}

fun getScoreColor(
    percentage: Float
): Color {

    return when {
        percentage >= 75 -> Color(0xFF2E7D32)
        percentage >= 50 -> Color(0xFFFF9800)
        else -> Color(0xFFE53935)
    }
}