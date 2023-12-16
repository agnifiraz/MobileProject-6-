
package com.example.info_5126_mobile_dev_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserReadingPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerAdapter
    private val userKey: String = LoginActivity.userEmail.toString()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_reading_page)

        recyclerView = findViewById(R.id.recyclerViewReadingPage)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load data from Firestore when the page is opened
        val temp = db.collection("userData").document(userKey)
        val temp2 = temp.collection("clickedItems")
        temp2.get()
            .addOnSuccessListener { result ->
                val dataList = mutableListOf<Results>()
                for (document in result) {
                    if (document.id == "placeholder") {
                        continue
                    }

                    Log.d("test2", "${document.id} => ${document.data}")

                    val multimediaList = document.get("multimedia") as? List<Map<String, Any>> ?: emptyList()
                    val multimediaData = mutableListOf<Multimedia>()

                    for (multimediaMap in multimediaList) {
                        val multimedia = Multimedia(
                            caption = multimediaMap["caption"] as? String ?: "",
                            copyright = multimediaMap["copyright"] as? String ?: "",
                            url = multimediaMap["url"] as? String ?: ""
                        )
                        multimediaData.add(multimedia)
                    }

                    val results = Results(
                        section = document.getString("section") ?: "",
                        title = document.getString("title") ?: "",
                        byline = document.getString("byline") ?: "",
                        abstract = document.getString("abstract") ?: "",
                        subsection = document.getString("subsection") ?: "",
                        published_date = document.getString("publishedDate") ?: "",
                        image = document.getString("image")?:"",
                        multimedia = multimediaData
                    )
                    dataList.add(results)
                }

                // Initialize RecyclerView adapter and set it to RecyclerView
                recyclerViewAdapter = RecyclerAdapter(dataList)
                recyclerView.adapter = recyclerViewAdapter
            }
            .addOnFailureListener { exception ->
                Log.w("test2", "Error getting documents.", exception)
            }
    }

    fun onClickBack(view: View) {
        val intent = Intent(this, UserInputActivity::class.java)
        startActivity(intent)
    }

    fun onClickAbout(view: View) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }
}