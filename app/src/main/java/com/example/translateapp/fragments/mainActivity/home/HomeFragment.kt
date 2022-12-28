package com.example.translateapp.fragments.mainActivity.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.R
import com.example.translateapp.database.entity.Dictionnaire
import com.example.translateapp.databinding.FragmentHomeBinding
import com.example.translateapp.service.NotificationsService
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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

        val sourceSpinner = binding.languesourc
        val destSpinner = binding.languedest
        sourceSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                homeViewModel.loadSameLangDictionnaires(sourceSpinner.selectedItem.toString(),
                                                        destSpinner.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        destSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                homeViewModel.loadSameLangDictionnaires(sourceSpinner.selectedItem.toString(),
                    destSpinner.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }




        val dicoSpinner: Spinner = binding.dictionnary
        val dictionnaries = requireActivity().resources.getStringArray(R.array.spinner_entries).toMutableList()

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            dictionnaries
        )

        dicoSpinner.adapter = adapter

        homeViewModel.currentLangDictionnaires.observe(viewLifecycleOwner) {
            adapter.clear()
            adapter.add("Google")
            val names = getDicoNames(it)
            adapter.addAll(names)
            dicoSpinner.adapter = adapter
        }

        val buttonSearch: Button = binding.searchBut
        buttonSearch.setOnClickListener(search)

        /**
         * Permet d'obtenir l'accès aux notifications
         * Afin d'actualiser les données quand on supprime une notification
         */
        val enableds = context?.let { NotificationManagerCompat.getEnabledListenerPackages(it) }
        if (enableds != null) {
            var enable = false;
            for (value: String in enableds) {
                Log.i("VALUE", value)
                if (value == "com.example.translateapp") {
                    enable = true;
                }
            }
            if (!enable) {
                startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            }
        }


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

        if (savedInstanceState != null) {
            Log.i("SAVED", "dans savedInstance")
            binding.languesourc.setSelection(savedInstanceState.getInt("spinner1", 0))
            binding.languedest.setSelection(savedInstanceState.getInt("spinner2", 0))
            binding.dictionnary.setSelection(savedInstanceState.getInt("dico"))
            binding.word.setText(savedInstanceState.getString("mot"))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getDicoNames(dicos :List<Dictionnaire>) : List<String>{
        val urls = dicos.map { d -> d.url }
        val separatedUrls = urls.map{ u -> u.split("/") }
        val nameZone = separatedUrls.map{ s -> s[2]}
        val names = nameZone.map { z -> z.replaceBefore('.', "").substringAfter('.') }
        return names
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (_binding != null) {
            outState.putInt("spinner1", binding.languesourc.selectedItemPosition)
            outState.putInt("spinner2", binding.languedest.selectedItemPosition)
            outState.putInt("dico", binding.dictionnary.selectedItemPosition)
            outState.putString("mot", binding.word.text.toString())
        }
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

        if (mot.trim() == "") {
            val text = "Remplissez les champs !"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(requireContext(), text, duration)
            toast.show()
        } else {
            var url = "http://www.google.fr/search?q=traduction+$mot+$endLang"

            homeViewModel.loadMot(mot, endLang)
            if (homeViewModel.certainsMots.value != null && homeViewModel.certainsMots.value!!.isNotEmpty()) {   //Cas
                url = homeViewModel.certainsMots.value!![0].urlTransl
            } else {
                homeViewModel.loadDictionnaire(dico, startLang, endLang)
                if (homeViewModel.certainsDictionnaires.value != null && homeViewModel.certainsDictionnaires.value!!.isNotEmpty()) {
                    url = homeViewModel.certainsDictionnaires.value!![0].url + mot
                }
            }
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

    }

}