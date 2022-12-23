package com.example.translateapp.fragments.mainActivity.jeu

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.translateapp.database.entity.Dictionnaire
import com.example.translateapp.databinding.ItemLayoutBinding

class RecycleAdapterDico: RecyclerView.Adapter<RecycleAdapterDico.VH>() {


    var motList: List<Dictionnaire> = listOf()

    fun setNewPaysList(liste: List<Dictionnaire>){
        motList = liste
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapterDico.VH {

        //créer View d'un élément de la liste à partir de fichier layout xml
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //creer le viewHolder
        val holder = VH(binding)
        return holder

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.mot = motList[position]

        holder.binding.motInit.text = holder.mot!!.url
        holder.binding.motTrad.text = holder.mot!!.idDico.toString()
        holder.binding.langueInit.text = holder.mot!!.startLanguage.toString()
        holder.binding.langueTrad.text = holder.mot!!.endLanguage.toString()

    }

    override fun getItemCount(): Int = motList.size

    //ViewHolder : ici il ne contient rien d'utile sauf une référence vers la View
    class VH( val binding : ItemLayoutBinding ) : RecyclerView.ViewHolder( binding.root ){
        var mot : Dictionnaire? = null
    }


}