package com.example.info_5126_mobile_dev_project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainViewModel : ViewModel() {
    var model = MutableLiveData<APIFormat>()
    var availableSections = MutableLiveData<List<String>>()
    var availableTitles = MutableLiveData<List<String>>()


    fun updateData(){
        CoroutineScope(Dispatchers.Main).launch {
            val request = getTopStoriesOfDifferentSection()
            if (request != null){
                model.value = request
                availableSections.value = request.results.map { it.section }.distinct()
                availableTitles.value = request.results.map { it.title }.distinct()

            }
            else {
                //binding.textViewCopyright.text = getString(R.string.nobooks)
            }
        }
    }

    private suspend fun getTopStoriesOfDifferentSection():APIFormat? {
        val defer = CoroutineScope(Dispatchers.IO).async {
            val url = URL("https://api.nytimes.com/svc/topstories/v2/arts.json?api-key=KG3L38cznGgoeNnRp6BiGf9AJLfprb17")
            val connection = url.openConnection() as HttpsURLConnection
            if(connection.responseCode == 200)
            {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, APIFormat::class.java)
                inputStreamReader.close()
                inputSystem.close()
                return@async request
            }
            else {
                return@async null
            }
        }
        return defer.await()
    }


}