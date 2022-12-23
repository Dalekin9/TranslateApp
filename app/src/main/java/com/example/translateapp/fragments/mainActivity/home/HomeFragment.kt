package com.example.translateapp.fragments.mainActivity.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.databinding.FragmentHomeBinding
import com.example.translateapp.service.NotificationsService
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var homeViewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.M)
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

        val buttonSearch: Button = binding.searchBut
        buttonSearch.setOnClickListener(search)

        /*
        * Lancement de l'alarme programmée pour 8h30
        */

        /* TODO ajoute en extra, le nb voulue grace a sharedPreferences */
        val serviceIntent = Intent(requireContext(), NotificationsService::class.java)
        val pendingIntent = PendingIntent.getService(
            requireContext(),
            0,
            serviceIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        /* calculer le moment de demarrage d'alarme */
        /* TODO voir comment modifier l'heure, surement via les sharedPreferences
        *   mais surtout voir si cela est pris en compte lors du changement ds les parametres */

        //amorcer Alarme
        val alarmManager =
            requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        /*
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 29)
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        */

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 15 * 1000,
            pendingIntent
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * ClickListener du bouton buttonSearch
     * lance une page web selon les langues de traduction et le mot voulu
     */
    private val search = View.OnClickListener {
        val intent = Intent(Intent.ACTION_VIEW)
        val dico = binding.dictionnary.selectedItem.toString()
        val startLang = binding.languesourc.selectedItem.toString()
        val endLang = binding.languedest.selectedItem.toString()
        val mot = binding.word.text.toString()

        var url = "http://www.google.fr/search?q=traduction+$mot $endLang"

        homeViewModel.loadMot(mot, endLang)
        if (homeViewModel.certainsMots.value != null && homeViewModel.certainsMots.value!!.isNotEmpty()) {   //Cas
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