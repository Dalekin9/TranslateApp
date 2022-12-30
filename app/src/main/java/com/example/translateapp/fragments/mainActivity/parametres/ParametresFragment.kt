package com.example.translateapp.fragments.mainActivity.parametres

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.os.SystemClock
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.databinding.FragmentParamBinding
import com.example.translateapp.R
import com.example.translateapp.service.NotificationsService

class ParametresFragment : Fragment() {

    private var _binding: FragmentParamBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var parametresViewModel: ParametresViewModel

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        parametresViewModel = ViewModelProvider(this).get(ParametresViewModel::class.java)


        _binding = FragmentParamBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val heure = requireContext().getSharedPreferences("parametres", Context.MODE_PRIVATE).getInt("heure",8)
        val minute = requireContext().getSharedPreferences("parametres", Context.MODE_PRIVATE).getInt("minute",57)

        val timePicker = binding.timePicker
        timePicker.setIs24HourView(true)
        timePicker.hour = heure
        timePicker.minute = minute
        binding.saveButton.setOnClickListener {
            sauvegarderParam()
        }

        val nbNotifs = requireContext().getSharedPreferences("parametres", Context.MODE_PRIVATE).getInt("nbNotifs",10)
        binding.nbNotifsEditText.setText(nbNotifs.toString())
        binding.nbNotifsEditText.inputType = InputType.TYPE_CLASS_NUMBER

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun sauvegarderParam(){
        val timePicker = binding.timePicker
        val hours = timePicker.hour
        val minutes = timePicker.minute
        val sharedPrefs = requireContext().getSharedPreferences("parametres", Context.MODE_PRIVATE)
        sharedPrefs?.edit()?.apply{
            putInt("heure", hours)
            putInt("minute", minutes)
        }?.apply()

        val serviceIntent = Intent(requireContext(), NotificationsService::class.java)
        val pendingIntent = PendingIntent.getService(
            requireContext(),
            0,
            serviceIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        //amorcer Alarme
        val alarmManager =
            requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager


        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND, 0)
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        val newNbNotifs : Int = binding.nbNotifsEditText.text.toString().toInt()
        sharedPrefs.edit().putInt("nbNotifs", newNbNotifs).apply()

        Toast.makeText(context, "Paramètres sauvegardés", Toast.LENGTH_SHORT).show()
    }
}