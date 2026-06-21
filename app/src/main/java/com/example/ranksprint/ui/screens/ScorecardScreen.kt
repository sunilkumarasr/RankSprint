package com.example.ranksprint.ui.screens

import android.os.Build
import android.text.Html
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quiztech.model.LeaderBoardItem
import com.example.ranksprint.R
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import com.example.ranksprint.ui.viewmodels.ScorecardViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScorecardScreen(
    navController: NavController,
    productId: String,
    onNavigateBack: () -> Unit,
    viewModel: ScorecardViewModel = viewModel()
) {

    val context = LocalContext.current
    val scorecardData by viewModel.scorecardData.collectAsState()
    val winningData by viewModel.LeaderBoard.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(productId) {
        val userId = Utils.getData(
            context,
            "user_id",
            ""
        ).toString()

        Log.e("userId_", userId)
        Log.e("productId_", productId)
        if (userId.isNotEmpty()) {
            viewModel.fetchScorecard(userId = userId, productId = productId)
            viewModel.fetchWinningData(userId = userId, productId = productId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Scorecard",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
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

        when {

            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error ?: "",
                        color = Color.Red
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFF5F5F5))
                ) {

                    item {

                        ScoreHeader(
                            score = scorecardData?.scorePercentage
                                ?.toFloatOrNull()
                                ?.toInt()
                                ?.toString()
                                ?: "0"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ScoreTabs(
                            selectedTab = selectedTab,
                            onTabSelected = {
                                selectedTab = it
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    if (selectedTab == 0) {
                        //Question tab

                        item {

                            Text(
                                text = "📝 Question Analysis",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        scorecardData?.questions?.let { questions ->

                            items(questions.size) { index ->

                                val item = questions[index]

                                QuestionAnalysisCard(
                                    question = item.questionText.htmlToText(),
                                    userAnswer = item.selectedOption ?: "",
                                    correctAnswer = item.correctOption ?: "",
                                    explanation = item.explanation.htmlToText(),
                                    isCorrect = item.isCorrect == 1
                                )

                            }
                        }
                    } else {
                        //Winners tab
                        items(winningData) { item ->
                            LeaderBoardItemCard(item)
                        }
                    }
                }
            }
        }
    }
}

fun String?.htmlToText(): String {
    if (this.isNullOrEmpty()) return ""

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString()
    } else {
        Html.fromHtml(this).toString()
    }
}

@Composable
fun ScoreHeader(
    score: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFF5F5F5)
            )
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(180.dp)
                .background(
                    Color(0xFFD7E7FF),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(
                        Color(0xFF003BFF),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Your Score",
                        color = Color.White,
                        fontSize = 18.sp
                    )

                    Text(
                        text = "$score/100",
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White
    ) {

        Tab(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            text = {
                Text("Questions")
            }
        )

        Tab(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            text = {
                Text("Winner's")
            }
        )
    }
}

@Composable
fun QuestionAnalysisCard(
    question: String,
    userAnswer: String,
    correctAnswer: String,
    explanation: String,
    isCorrect: Boolean
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                text = question,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Row {
                Text(
                    "Your Answer: ",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Text(
                    userAnswer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text(
                    "Correct Answer: ",
                    color = Color(0xFF2EAD62),
                    fontSize = 13.sp
                )

                Text(
                    correctAnswer,
                    color = Color(0xFF2EAD62),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                color = if (isCorrect)
                    Color(0xFF2EAD62)
                else
                    Color.Red,
                shape = RoundedCornerShape(4.dp)
            ) {

                Text(
                    text = if (isCorrect) "Correct" else "Wrong",
                    modifier = Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 6.dp
                    ),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Explanation:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = explanation,
                fontSize = 15.sp
            )
        }
    }
}

//winning
@Composable
fun LeaderBoardItemCard(
    item: LeaderBoardItem
) {

    val bgColor = when (item.rank) {

        1 -> Color(0xFFFFF8E1)
        2 -> Color(0xFFFFFFFF)
        3 -> Color(0xFFFFFFFF)

        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            ),

        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            RankBadge(item.rank ?: 0)

            Spacer(modifier = Modifier.width(12.dp))

            AsyncImage(
                model = item.profileImage,
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_profile),
                placeholder = painterResource(R.drawable.ic_profile)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.name ?: "User",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = "Score: ${item.score}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            TrophyViews(item.rank ?: 0)
        }
    }
}

@Composable
fun RankBadge(rank: Int) {

    when (rank) {

        1 -> Text(
            text = "\uD83E\uDD47",
            fontSize = 35.sp
        )

        2 -> Text(
            text = "\uD83E\uDD48",
            fontSize = 35.sp
        )

        3 -> Text(
            text = "\uD83E\uDD49",
            fontSize = 35.sp
        )

        else -> {
            Box(
                modifier = Modifier.width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TrophyViews(rank: Int) {

    when (rank) {

        1, 2, 3 -> {

            val color = when (rank) {
                1 -> Color(0xFFFFC107) // Gold
                2 -> Color(0xFFBDBDBD) // Silver
                else -> Color(0xFFCD7F32) // Bronze
            }

            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(42.dp)
            )
        }

        else -> {

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}