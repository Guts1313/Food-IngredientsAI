package com.example.test2.presentation.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.test2.presentation.n.activities.MainScreen
import com.google.firebase.analytics.FirebaseAnalytics


class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen() // Entry point to the app with navigation

        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Log an event to check if Firebase is working
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "test_id")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "test_name")
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "test_content")
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

    }
}

//@Composable
//fun chipSection(chips: List<String>) {
//    var selectedIndex by remember { mutableStateOf(0) }
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3),
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(15.dp))
//    ) {
//        items(chips.size) { index ->
//            Box(
//                contentAlignment = Alignment.Center,  // Center the content inside the Box
//                modifier = Modifier
//                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
//                    .clickable { selectedIndex = index }
//                    .clip(RoundedCornerShape(15.dp))
//                    .height(25.dp)
//                    .background(if (selectedIndex == index) Color.Blue else Color.DarkGray)
//            ) {
//                Text(
//                    text = chips[index],
//                    textAlign = TextAlign.Center,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    color = Color.White,
//                    modifier = Modifier.fillMaxWidth()  // Ensure the text fills the width of the Box
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun ImageCard(
//    painter: Painter, contentDescription: String, title: String, modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(25.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 35.dp)
//    ) {
//        // Make the image fill the entire card
//        Image(
//            painter = painter,
//            contentDescription = contentDescription,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.height(200.dp)  // Fill the entire card with the image
//        )
//
//        // Add semi-transparent background behind the text for better visibility
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(Color.Transparent, Color.Blue), startY = 300f
//                    )
//                )
//        ) {
//
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Brush.verticalGradient(listOf(Color.Red, Color.Blue))),
//                fontSize = TextUnit(14F, TextUnitType(14)),
//                text = contentDescription,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//
//
//        }





