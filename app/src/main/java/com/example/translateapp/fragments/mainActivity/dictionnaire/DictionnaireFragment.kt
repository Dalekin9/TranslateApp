package com.example.translateapp.fragments.mainActivity.dictionnaire

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translateapp.RecycleAdapter
import com.example.translateapp.databinding.FragmentDashboardBinding

class DictionnaireFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var dictionnaireViewModel: DictionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dictionnaireViewModel = ViewModelProvider(this).get(DictionnaireViewModel::class.java)


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dictionnaireViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val adapter =  RecycleAdapter()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        dictionnaireViewModel.mots.observe(viewLifecycleOwner) {
            Log.i("INFO", "dans loadAll observer")
            adapter.motList = it
            adapter.notifyDataSetChanged()
        }

        dictionnaireViewModel.insertInfo.observe(viewLifecycleOwner) {
            Log.i("INFO", "dans insertInfo observer ${dictionnaireViewModel.insertInfo.value}")
            dictionnaireViewModel.mots.value?.let { it1 -> adapter.setNewPaysList(it1) }
            adapter.notifyDataSetChanged()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}