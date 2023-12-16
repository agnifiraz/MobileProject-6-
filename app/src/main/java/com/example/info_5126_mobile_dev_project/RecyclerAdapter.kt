package com.example.info_5126_mobile_dev_project

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class RecyclerAdapter(private val dataSet: List<Results>, private val sectionFilter: String? = null) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private val userKey: String = LoginActivity.userEmail.toString()
    private val db = Firebase.firestore

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var textViewTitle: TextView
        var textViewByLine: TextView
        var textViewAbstract: TextView
        var textViewSubsection: TextView
        var textViewPublishedDate: TextView
        var imageView: ImageView

        init {
            textViewTitle = view.findViewById(R.id.textViewTitle)
            textViewByLine = view.findViewById(R.id.textViewByLine)
            textViewAbstract = view.findViewById(R.id.textViewAbstract)
            textViewSubsection = view.findViewById(R.id.textViewSubsection)
            textViewPublishedDate = view.findViewById(R.id.textViewPublished_date)
            imageView = view.findViewById(R.id.imageView)

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedItem = dataSet[position]

                saveDataToFirebase(clickedItem)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_item, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.textViewTitle.text = "Title: ${dataSet[position].title}"
        if(dataSet[position].byline.isNotBlank()) {
            val byLineText = if (dataSet[position].byline.trimStart().startsWith("by", ignoreCase = true)) {
                dataSet[position].byline.substringAfter(" ", "").trim()
            } else {
                dataSet[position].byline
            }
            viewHolder.textViewByLine.text = "By: $byLineText"
        }
        else
        {
            viewHolder.textViewByLine.text = "By: Unknown"
        }

        viewHolder.textViewAbstract.text = "Abstract: ${dataSet[position].abstract}"
        if(dataSet[position].subsection.isNotEmpty())
        {
            viewHolder.textViewSubsection.text = "Subsection: ${dataSet[position].subsection}"
        }
        else
        {
            viewHolder.textViewSubsection.text = "Subsection: N/A"
        }

        // Format the date
        viewHolder.textViewPublishedDate.text = try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dataSet[position].published_date)
            "Date: ${date?.let { outputFormat.format(it) }}"
        } catch (e: ParseException) {
            "Date: ${dataSet[position].published_date}" // Use the original date if parsing fails
        }

        if (dataSet[position].multimedia.isNotEmpty()) {
            // Load the image using Picasso from API data
            Picasso.get().load(dataSet[position].multimedia[0].url)
                .into(viewHolder.imageView)
        } else if (dataSet[position].image?.isNotEmpty() == true && dataSet[position].image!!.isNotBlank()) {
            // Load the image using Picasso from Firestore data
            Picasso.get().load(dataSet[position].image)
                .into(viewHolder.imageView)
        } else {
            // Handle the case when there is no image data
            Log.d("test3", "No image URL for position $position")
            // viewHolder.imageView.setImageResource(R.drawable.placeholder_image)
        }


    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun saveDataToFirebase(clickedItem: Results) {
        val userDocRef = db.collection("userData").document(userKey)
        val clicksCollectionRef = userDocRef.collection("clickedItems")

        val image = if (clickedItem.multimedia.isNotEmpty()) {
            clickedItem.multimedia[0].url
        } else {
            // Provide a default image URL or handle the case when there is no image
            // For now, I'm using an empty string as a default value. Adjust it as needed.
            ""
        }

        val data = hashMapOf(
            "title" to clickedItem.title,
            "byline" to clickedItem.byline,
            "abstract" to clickedItem.abstract,
            "subsection" to clickedItem.subsection,
            "publishedDate" to clickedItem.published_date,
            "image" to image,
            // Add more fields as needed
        )

        clicksCollectionRef.add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("Firebase", "Data successfully written to Firebase with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error writing data to Firebase", e)
            }
    }



//    private fun saveDataToFirebase(clickedItem: Results) {
//        val userDocRef = db.collection("userData").document(userKey)
//
//        val clicksCollectionRef = userDocRef.collection("clickedItems")
//
//        val data = hashMapOf(
//            "title" to clickedItem.title,
//            "byline" to clickedItem.byline,
//            "abstract" to clickedItem.abstract,
//            "subsection" to clickedItem.subsection,
//            "publishedDate" to clickedItem.published_date,
//            "image" to clickedItem.multimedia[0].url,
//            // Add more fields as needed
//        )
//
//        clicksCollectionRef.add(data)
//            .addOnSuccessListener { documentReference ->
//                Log.d("Firebase", "Data successfully written to Firebase with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w("Firebase", "Error writing data to Firebase", e)
//            }
//    }
}