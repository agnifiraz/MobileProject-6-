package com.example.info_5126_mobile_dev_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserInputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserInputFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var button: Button? = null
    private var spinnerchecked: String? = null

    private var spinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_input, container, false)

        button = view.findViewById(R.id.buttonSectionInfo)
        spinner = view.findViewById(R.id.spinner)

        // Handle item selection
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Update spinnerchecked based on the selected item
                spinnerchecked = spinner?.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

//        button?.setOnClickListener {
//            (activity as UserInputActivity?)?.onPassingValue(spinnerchecked)
//            (activity as MainActivity?)?.onPassingValue(spinnerchecked)
//
//        }

        button?.setOnClickListener {
                val activity = activity
                if (activity is UserInputActivity) {
                    activity.onPassingValue(spinnerchecked!!)
                } else if (activity is MainActivity) {
                    activity.onPassingValue(spinnerchecked!!)
                }
        }


        return view
    }

//    fun setSectionsSpinner(sections: List<String>) {
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sections)
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        // Apply the adapter to the spinner
//        spinner?.adapter = adapter
//    }

    fun setSectionsSpinner(sections: List<String>) {
        // Add a default item to the beginning of the list
        val defaultItem = "Select something"
        val spinnerItems = mutableListOf(defaultItem)
        spinnerItems.addAll(sections)

        // Create an ArrayAdapter using the updated list of items
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner?.adapter = adapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserInputFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserInputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}