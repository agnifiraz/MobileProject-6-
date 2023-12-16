package com.example.info_5126_mobile_dev_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.info_5126_mobile_dev_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewManager: RecyclerView.LayoutManager
    private  lateinit var sectionSpinnerInfo:String
    private lateinit var viewModel: MainViewModel
    var selectedTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        sectionSpinnerInfo = intent.getStringExtra(UserInputActivity.NAME_KEY).toString()

        // setup recyclerView
        recyclerViewManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = recyclerViewManager
        binding.recyclerView.setHasFixedSize(true)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val observer = Observer<APIFormat>{
            updateRecyclerView(it.results )
        }
        viewModel.model.observe(this, observer)

        if(sectionSpinnerInfo != null){
            println("check $sectionSpinnerInfo")
            selectedSection()
        }

        // Set up observer for availableSections LiveData
        viewModel.availableTitles.observe(this, Observer { titles ->
            // Update UI or take necessary action with the received data
            val userInputFragment: UserInputFragment? =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as UserInputFragment?
            println("select observer" + titles.toString())
            userInputFragment?.setSectionsSpinner(titles)
        })

        // Trigger data update in the ViewModel
        viewModel.updateData()


    }

    fun onPassingValue(TitleName: String) {
        selectedTitle = TitleName

        println("select onpass title: " + selectedTitle)
        println("select onpass section: " + sectionSpinnerInfo)


        // Update RecyclerView or trigger any necessary action here
        viewModel.updateData()
        sectionSpinnerInfo = ""

    }

    fun onClickSectionInfo(view: View) {
        sectionSpinnerInfo = ""
        viewModel.updateData()
    }

    private fun selectedSection(){
        viewModel.updateData()
    }
    private fun updateRecyclerView(results: List<Results>) {

//        if(selectedTitle.isNotEmpty()) {
//            println("select spin: title: $selectedTitle")
//            val filteredResults = results.filter { it.title.equals(selectedTitle, ignoreCase = true) }
//            binding.recyclerView.adapter = RecyclerAdapter(filteredResults, selectedTitle)
//        }
//        else{
//            println("select spin: section: $sectionSpinnerInfo")
//            val filteredResults = results.filter { it.section.equals(sectionSpinnerInfo, ignoreCase = true) }
//            binding.recyclerView.adapter = RecyclerAdapter(filteredResults, sectionSpinnerInfo)
//        }

        if(sectionSpinnerInfo.isNotEmpty()) {
            println("select spin: section: $sectionSpinnerInfo")
            val filteredResults = results.filter { it.section.equals(sectionSpinnerInfo, ignoreCase = true) }
            binding.recyclerView.adapter = RecyclerAdapter(filteredResults, sectionSpinnerInfo)

        }
        else{
            println("select spin: title: $selectedTitle")
            val filteredResults = results.filter { it.title.equals(selectedTitle, ignoreCase = true) }
            binding.recyclerView.adapter = RecyclerAdapter(filteredResults, selectedTitle)

        }




    }

    private fun getTitlesFromResults(results: List<Results>): List<String> {
        // Add a placeholder item at the beginning
        val titles = mutableListOf("Select a Title")
        titles.addAll(results.map { it.title })
        return titles
    }


    fun onClickBack(view: View) {
        val intent = Intent(this, UserInputActivity::class.java)
        startActivity(intent)
    }

    fun onClickAbout(view: View) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    fun onClickButton(view: View) {
        val intent = Intent(this, UserReadingPage::class.java)
        startActivity(intent)
    }

}