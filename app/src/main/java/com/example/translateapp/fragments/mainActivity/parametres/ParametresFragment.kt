package com.example.translateapp.fragments.mainActivity.parametres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.databinding.FragmentParamBinding

class ParametresFragment : Fragment() {

    private var _binding: FragmentParamBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var parametresViewModel: ParametresViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        parametresViewModel = ViewModelProvider(this).get(ParametresViewModel::class.java)


        _binding = FragmentParamBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}