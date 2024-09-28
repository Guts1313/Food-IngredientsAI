package com.example.test2.presentation.activities

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test2.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun GlassMorphismLoginPage(navController: NavController) {
    val auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth instance
    val context = LocalContext.current // For showing Toasts in the login process

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") } // To display error messages if login fails

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background GIF Image
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
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 50.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassMorphismCard(
                navController = navController,
                username = username,  // Pass the state down
                onUsernameChange = { username = it },  // Pass a lambda to update the state
                password = password,  // Pass the state down
                onPasswordChange = { password = it },  // Pass a lambda to update the state
                loginError = loginError,
                onLogin = { email, pass ->
                    // Firebase authentication for login
                    auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                Log.d("Login", "Navigating to recipe list")
                                // Navigate to the recipe list screen upon successful login
                                navController.navigate("recipe_list") {
                                    popUpTo("login") { inclusive = true } // Remove login from the back stack
                                }
                            } else {
                                loginError = task.exception?.localizedMessage ?: "Login failed"
                            }
                        }
                }
            )
        }
    }
}





@Composable
fun GlassMorphismCard(
    navController: NavController,
    username: String,
    onUsernameChange: (String) -> Unit,  // Callback for username
    password: String,
    onPasswordChange: (String) -> Unit,  // Callback for password
    loginError: String,
    onLogin: (String, String) -> Unit  // Function expects two parameters: email and password
) {
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(360.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(0.92f),
            contentColor = Color.White,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color.Transparent.copy(0.9f),
                            Color.White.copy(0.2f),
                            Color.White.copy(0.2f),
                            Color.DarkGray.copy(0.2f),
                            Color.DarkGray.copy(0.2f),
                            Color.Transparent.copy(0.2f),
                        ), start = Offset.Zero, end = Offset.Infinite, tileMode = TileMode.Clamp
                    )
                )
                .clip(RoundedCornerShape(25.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Error Message Display
            if (loginError.isNotEmpty()) {
                Text(
                    text = loginError,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Username TextField
            TextField(
                value = username,
                onValueChange = onUsernameChange,  // Use callback for updating username
                label = {
                    Text(
                        "Username",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Red,
                    unfocusedTextColor = Color.Red,
                    focusedContainerColor = Color.Yellow.copy(0.19f),
                    unfocusedContainerColor = Color.Transparent.copy(0.3f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp)),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person Icon",
                        tint = Color.Transparent.copy(0.94f)
                    )
                },
                textStyle = TextStyle(color = Color.Red),
            )

            // Password TextField
            TextField(
                value = password,
                onValueChange = onPasswordChange,  // Use callback for updating password
                label = {
                    Text(
                        "Password",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .clip(RoundedCornerShape(15.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Red,
                    unfocusedTextColor = Color.Red,
                    focusedContainerColor = Color.Yellow.copy(0.19f),
                    unfocusedContainerColor = Color.Transparent.copy(0.3f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Red
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color.Transparent.copy(0.94f)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.Red)
            )

            // Login Button
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        onLogin(username, password)  // Call the onLogin callback with username and password
                    }
                },
                modifier = Modifier
                    .height(65.dp)
                    .padding(top = 14.dp)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(listOf(Color.Red, Color.Yellow)),
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(0.6f), Color.Yellow.copy(0.9f)
                )
            ) {
                Text(
                    "Login",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Yellow
                )
            }

            // Row for "Sign up" and "Forgotten password?" at the bottom
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
            ) {
                // "Forgotten Password" Button (Left)
                Text(
                    text = "Forgot password?",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = Color.Red.copy(0.6f),
                    modifier = Modifier.padding(start = 8.dp)
                )

                // Spacer to push the "Sign up" to the right
                Spacer(modifier = Modifier.weight(1f))

                // "Sign up" Button (Right)
                Text(
                    text = "Sign up",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = Color.Yellow.copy(0.67f),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            // Navigate to the Sign-Up screen
                            navController.navigate("signup")
                        }
                )
            }
        }
    }
}




//@RequiresApi(Build.VERSION_CODES.P)
//@Preview
//@Composable
//fun PreviewGlassMorphismLoginPage() {
//    GlassMorphismLoginPage(navController = NavController(context = ))
//}


@RequiresApi(Build.VERSION_CODES.P)
private fun createAnimatedImageDrawableFromImageDecoder(
    context: Context,
    uri: Uri
): AnimatedImageDrawable {
    val source = ImageDecoder.createSource(context.contentResolver, uri)
    val drawable = ImageDecoder.decodeDrawable(source)
    return drawable as AnimatedImageDrawable
}