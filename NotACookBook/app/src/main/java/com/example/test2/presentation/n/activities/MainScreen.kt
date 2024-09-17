package com.example.test2.presentation.n.activities

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test2.R
import com.example.test2.presentation.activities.GlassMorphismLoginPage
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        NavHost(navController = navController, startDestination = "login") {
            // Define the Login route
            composable("login") {
                GlassMorphismLoginPage(navController)
            }


            composable("signup") {
                Box(
                    modifier = Modifier.fillMaxSize()

                ) {
                    Image(
                        painter = rememberDrawablePainter(
                            drawable = LocalContext.current.getDrawable(R.drawable.cooking)
                        ),
                        contentDescription = "Background animation",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 180.dp, start = 2.dp)
                            .fillMaxWidth(0.99f),
                        contentAlignment = Alignment.Center

                    ) {
                        SignUpCard(onSignUp = { name, email, password, age ->
                            // Implement Firebase sign-up logic here
                            signUpUser(name, email, password, age) // Call the sign-up function

                            // After signing up, navigate back to the login page
                            navController.popBackStack()
                        })
                    }
                }
            }

        }
    }

}
