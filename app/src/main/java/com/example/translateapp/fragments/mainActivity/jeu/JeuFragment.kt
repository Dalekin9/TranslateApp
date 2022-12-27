package com.example.translateapp.fragments.mainActivity.jeu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.database.Dao
import com.example.translateapp.database.DicoApplication
import com.example.translateapp.database.entity.Mot
import com.example.translateapp.databinding.FragmentJeuBinding
import kotlin.random.Random

class JeuFragment : Fragment() {

    private var _binding: FragmentJeuBinding? = null

    private val binding get() = _binding!!
    private lateinit var dao: Dao
    private var mot: Mot? = null
    private val listeMots = mutableListOf<Mot>()

    private val jeuModel by lazy { ViewModelProvider(requireActivity())[JeuModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentJeuBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dao = (requireActivity().application as DicoApplication).database.MyDao()

        val buttonSee = binding.answer
        buttonSee.setOnClickListener(voirLaReponse)

        val buttonNext = binding.next
        buttonNext.setOnClickListener(next)

        val wordToFind: TextView = binding.wordToFind
        val startLang: Spinner = binding.langue1
        val endLang: Spinner = binding.langue2

        startLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                updateData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        endLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                updateData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        updateData()

        jeuModel.listMots.observe(viewLifecycleOwner) {
            Log.i("JEUFRAGMENT", "observer jeu model")

            if (it.isNotEmpty()) {

                listeMots.clear()
                listeMots.addAll(it)

                Log.i("JEUFRAGMENT", "listeMots2: ${listeMots.size}")
                Log.i("JEUFRAGMENT", "jeu model listeMots: ${jeuModel.listMots.value?.size}")

                val i = Random.nextInt(listeMots.size)
                mot = listeMots[i]
                wordToFind.text = mot!!.word
                Log.i("JEUFRAGMENT", "listeMots2: ${listeMots.size}")
                Log.i("JEUFRAGMENT", "jeu model listeMots: ${jeuModel.listMots.value?.size}")

            }
            Log.i("JEUFRAGMENT", "listeMots1: ${listeMots.size}")

        }

        return root
    }

    fun updateData() {
        Log.i("JEUFRAGMENT", "update data")
        val startLang: Spinner = binding.langue1
        val endLang: Spinner = binding.langue2

        jeuModel.loadAllMotsNeedToBeLearnWithSpecificLanguages(
            true,
            startLang.selectedItem.toString(),
            endLang.selectedItem.toString()
        )
    }

    private val voirLaReponse = View.OnClickListener {
        Log.i("JEUFRAGMENT", "voir réponse")
        if (mot != null) {
            val translate = binding.translateToFind
            translate.text = mot!!.translation
        }
    }

    private val next = View.OnClickListener {
        Log.i("JEUFRAGMENT", "next")
        if (listeMots.isNotEmpty()) {
            val wordToFind: TextView = binding.wordToFind
            val translate = binding.translateToFind
            translate.text = ""
            val i = Random.nextInt(listeMots.size)
            mot = listeMots[i]
            wordToFind.text = mot!!.word
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}