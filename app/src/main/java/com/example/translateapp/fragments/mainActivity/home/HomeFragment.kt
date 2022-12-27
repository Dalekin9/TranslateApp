package com.example.translateapp.fragments.mainActivity.home

import com.example.translateapp.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val dicoSpinner: Spinner = binding.dictionnary
        val dictionnaries = requireActivity().resources.getStringArray(R.array.spinner_entries).toMutableList()

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item, dictionnaries
        )

        dicoSpinner.adapter = adapter

        homeViewModel.dictionnaires.observe(viewLifecycleOwner){
            adapter.clear()
            adapter.add("Google")
            adapter.addAll(it.map { d -> d.url })
            dicoSpinner.adapter = adapter
        }

        val buttonSearch: Button = binding.searchBut
        buttonSearch.setOnClickListener(search)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    fun data(view: View) {
        val act = Intent(activity, DataActivity::class.java)
        startActivity(act)
    }
     */

    /**
     * ClickListener du bouton buttonSearch
     */
    private val search = View.OnClickListener() {
        val intent = Intent(Intent.ACTION_VIEW)
        val dico = binding.dictionnary.selectedItem.toString()
        val startLang = binding.languesourc.selectedItem.toString()
        val endLang = binding.languedest.selectedItem.toString()
        val mot = binding.word.text.toString()

        var url = "http://www.google.fr/search?q=traduction+$mot $endLang"

        homeViewModel.loadMot(mot, endLang)
        if(homeViewModel.certainsMots.value != null && homeViewModel.certainsMots.value!!.isNotEmpty()){   //Cas
            url = homeViewModel.certainsMots.value!![0].urlTransl
        }else{
            homeViewModel.loadDictionnaire(dico, startLang, endLang)
            if(homeViewModel.certainsDictionnaires.value != null  && homeViewModel.certainsDictionnaires.value!!.isNotEmpty()){
                url = homeViewModel.certainsDictionnaires.value!![0].url + mot
            }
        }
        // TODO val urlDico -> get url pour le dico selectionné

        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    fun searchK(view: View){
        val intent = Intent(Intent.ACTION_VIEW)
        val dico = binding.dictionnary.selectedItem.toString()
        val startLang = binding.languesourc.selectedItem.toString()
        val endLang = binding.languedest.selectedItem.toString()
        val mot = binding.word.text.toString()

        var url = "http://www.google.fr/search?q=traduction+$mot $endLang"

        homeViewModel.loadMot(mot, endLang)
        if(homeViewModel.certainsMots.value != null && homeViewModel.certainsMots.value!!.isNotEmpty()){   //Cas
            url = homeViewModel.certainsMots.value!![0].urlTransl
        }else{
            homeViewModel.loadDictionnaire(dico, startLang, endLang)
            if(homeViewModel.certainsDictionnaires.value != null  && homeViewModel.certainsDictionnaires.value!!.isNotEmpty()){
                url = homeViewModel.certainsDictionnaires.value!![0].url + mot
            }
        }
        // TODO val urlDico -> get url pour le dico selectionné

        intent.data = Uri.parse(url)
        startActivity(intent)
    }


}