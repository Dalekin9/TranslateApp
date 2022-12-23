package com.example.translateapp.fragments.mainActivity.jeu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.databinding.FragmentJeuBinding
import com.example.translateapp.fragments.mainActivity.statistiques.StatistiquesViewModel

class JeuFragment : Fragment() {

    private var _binding: FragmentJeuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var dictionnaireViewModel: StatistiquesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dictionnaireViewModel = ViewModelProvider(this)[StatistiquesViewModel::class.java]


        _binding = FragmentJeuBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}