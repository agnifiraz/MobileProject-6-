package com.example.info_5126_mobile_dev_project

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.info_5126_mobile_dev_project.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    // Companion object to hold user email
    companion object {
        var userEmail: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.buttonLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.username.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email: String = binding.username.text.toString().trim { it <= ' ' }
                    val password: String = binding.password.text.toString().trim { it <= ' ' }

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success")
                                userEmail = auth.currentUser?.email

                                // Check if the user's email exists in "userData"
                                checkIfUserExists(email)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                }
            }
        }

        binding.textViewHere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun checkIfUserExists(email: String) {
        val userDocRef = db.collection("userData").document(email)

        userDocRef.get()
            .addOnCompleteListener { task: com.google.android.gms.tasks.Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        // User document exists, proceed to the next activity
                        val intent = Intent(this, UserInputActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("userid", auth.currentUser?.uid)
                        intent.putExtra("email", userEmail)
                        startActivity(intent)
                        finish()
                    } else {
                        // User document doesn't exist, create it
                        createUserDocument(email)
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.exception)
                }
            }
    }

    private fun createUserDocument(email: String) {
        val userDocRef = db.collection("userData").document(email)

        // Create an empty user document
        userDocRef.set(emptyMap<String, Any>())
            .addOnSuccessListener {
                Log.d("Firebase", "User document created successfully")

                // Add "clickedItems" subcollection to the user document
                addClickedItemsSubcollection(userDocRef)
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error creating user document", e)
            }
    }

    private fun addClickedItemsSubcollection(userDocRef: DocumentReference) {
        // Add "clickedItems" subcollection to the user document
        userDocRef.collection("clickedItems").document("placeholder")
            .set(emptyMap<String, Any>())
            .addOnSuccessListener {
                Log.d("Firebase", "ClickedItems subcollection added successfully")

                // Proceed to the next activity
                val intent = Intent(this, UserInputActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("userid", auth.currentUser?.uid)
                intent.putExtra("email", userEmail)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error adding ClickedItems subcollection", e)
            }
    }
}
