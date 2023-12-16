package com.example.info_5126_mobile_dev_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.info_5126_mobile_dev_project.databinding.ActivityUserInputBinding

class UserInputActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUserInputBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Set up observer for availableSections LiveData
        viewModel.availableSections.observe(this, Observer { sections ->
            // Update UI or take necessary action with the received data
            val userInputFragment: UserInputFragment? =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as UserInputFragment?
            userInputFragment?.setSectionsSpinner(sections)
        })

        // Trigger data update in the ViewModel
        viewModel.updateData()


    }

    fun onPassingValue(sectionName: String)
    {
        println(sectionName)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("section", sectionName)
        startActivity(intent)

    }

    companion object {
        const val NAME_KEY = "section"

    }

    fun onClickTitleButton(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    fun onClickBack(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun onClickAbout(view: View) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }
}