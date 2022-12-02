package com.example.translateapp

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.translateapp.databinding.ItemLayoutBinding
import com.example.translateapp.database.entity.Mot

class RecycleAdapter: RecyclerView.Adapter<RecycleAdapter.VH>() {


    var motList: List<Mot> = listOf()

    fun setNewPaysList(liste: List<Mot>){
        motList = liste
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapter.VH {

        //créer View d'un élément de la liste à partir de fichier layout xml
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //creer le viewHolder
        val holder = VH(binding)
        return holder

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.mot = motList[position]

        holder.binding.motInit.text = holder.mot!!.word
        holder.binding.motTrad.text = holder.mot!!.translation
        holder.binding.langueInit.text = holder.mot!!.idMot.toString()
        holder.binding.langueTrad.text = holder.mot!!.dictionnary.toString()

    }

    override fun getItemCount(): Int = motList.size

    //ViewHolder : ici il ne contient rien d'utile sauf une référence vers la View
    class VH( val binding : ItemLayoutBinding ) : RecyclerView.ViewHolder( binding.root ){
        var mot : Mot? = null
    }


}