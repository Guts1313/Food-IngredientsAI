package com.example.test2.presentation.n.activities

import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test2.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignUpCard(
    onSignUp: (String, String, String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(25.dp))
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Color.Transparent.copy(0.9f),
                        Color.White.copy(0.2f),
                        Color.White.copy(0.2f),
                        Color.DarkGray.copy(0.2f),
                        Color.DarkGray.copy(0.2f),
                        Color.Transparent.copy(0.2f)
                    )
                )


            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .height(410.dp)
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color.Black,
                            Color.Yellow.copy(0.5f),
                            Color.Black
                        )
                    )
                )
                .clip(shape = RoundedCornerShape(25.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(0.92f),
            ), shape = RoundedCornerShape(25.dp)
        ) {

            Text(text = "Sign Up", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            // Name input
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.98f)
                    .padding(start = 6.dp, end = 5.dp)
                    .clip(shape = RoundedCornerShape(35.dp))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(Color.White, Color.Yellow, Color.White, Color.Yellow),
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color.Transparent.copy(0.6f),
                                Color.White.copy(0.3f),
                                Color.Transparent.copy(0.7f),
                                Color.Yellow.copy(0.3f),
                                Color.Transparent.copy(0.9f)

                            )
                        )

                    )
                    .height(55.dp)
            ) {


                TextField(
                    value = name,
                    onValueChange = { name = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Name icon",
                            tint = Color.White
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Red,
                        unfocusedTextColor = Color.Red,
                        focusedContainerColor = Color.Yellow.copy(0.19f),
                        unfocusedContainerColor = Color.Transparent.copy(0.75f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Red
                    ),

                    shape = RoundedCornerShape(25.dp),
                    label = {
                        Text(
                            "Name",
                            color = Color.White
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.98f)
                    .padding(start = 4.dp, top = 18.dp)
                    .border(
                        width = 1.5.dp,
                        shape = RoundedCornerShape(35.dp),
                        brush = Brush.linearGradient(
                            listOf(Color.White, Color.Yellow, Color.White, Color.Yellow),
                        )
                    )
                    .height(55.dp)
            ) {
                // Email input
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            "Email",
                            color = Color.White
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
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email icon",
                            tint = Color.White
                        )
                    },
                    shape = RoundedCornerShape(35.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 1.3.dp, top = 2.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.98f)
                    .padding(start = 4.dp, top = 18.dp)
                    .border(
                        width = 1.5.dp,
                        shape = RoundedCornerShape(35.dp),
                        brush = Brush.linearGradient(
                            listOf(Color.White, Color.Yellow, Color.White, Color.Yellow),
                        )
                    )
                    .height(55.dp)
            ) {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            "Password",
                            color = Color.White
                        )
                    },
                    shape = RoundedCornerShape(25.dp),
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
                            contentDescription = "Password Icon",
                            tint = Color.White
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 1.dp, end = 1.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 18.dp, end = 6.dp)
                    .border(
                        width = 1.5.dp,
                        shape = RoundedCornerShape(35.dp),
                        brush = Brush.linearGradient(
                            listOf(Color.White, Color.Yellow, Color.White, Color.Yellow),
                        )
                    )
                    .height(55.dp)
            ) {
                TextField(
                    value = age,
                    onValueChange = { age = it },
                    shape = RoundedCornerShape(25.dp),
                    label = { Text("Age", color = Color.White) },
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
                            imageVector = Icons.Default.Create,
                            contentDescription = "Age icon",
                            tint = Color.White
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            // Sign-Up Button
            Button(
                onClick = {
                    val ageInt = age.toIntOrNull() ?: 0 // Default to 0 if parsing fails
                    onSignUp(name, email, password, ageInt)
                },
                modifier = Modifier
                    .padding(top = 26.dp)
                    .border(
                        brush = Brush.linearGradient(
                            listOf(
                                Color.Yellow,
                                Color.Red,
                                Color.Yellow,
                                Color.Red,
                            )
                        ),
                        width = 1.5.dp,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .align(Alignment.CenterHorizontally), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent.copy(0.6f), Color.Red.copy(0.8f)
                )
            ) {
                Text(
                    text = "Sign Up",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

    }

}

 fun signUpUser(
    name: String,
    email: String,
    password: String,
    age: Int
) {
    val auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth instance
    val db = FirebaseFirestore.getInstance() // Initialize Firestore instance

    // Create user in Firebase Authentication
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get the newly created user ID
                val userId = auth.currentUser?.uid ?: ""

                // Create the user object to store in Firestore
                val newUser = User(
                    uid = userId,
                    name = name,
                    email = email,
                    age = age
                )

                // Add the user data to Firestore
                db.collection("users").document(userId).set(newUser)
                    .addOnSuccessListener {
                        Log.d("SignUp", "User successfully added to Firestore!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("SignUp", "Error adding user to Firestore", e)
                    }
            } else {
                // Handle errors in user creation
                Log.e("SignUp", "Error creating user", task.exception)
            }
        }
}

@Preview
@Composable
fun PreviewSignUp() {
    // Pass a lambda for the onSignUp function to mock the behavior in the preview
    SignUpCard(onSignUp = { name, email, password, age ->
        // Do nothing or print mock data for preview purposes
        println("Name: $name, Email: $email, Password: $password, Age: $age")
    })
}