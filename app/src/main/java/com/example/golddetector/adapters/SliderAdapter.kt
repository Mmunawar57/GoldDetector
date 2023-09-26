package com.example.golddetector.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.golddetector.R

class SliderAdapter(val list: List<AdapterItems>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivWalpaper = itemView.findViewById<ImageView>(R.id.iv_walpaper)
//        val txt = itemView.findViewById<TextView>(R.id.textviewhwt)
        fun onBind(position: Int) {

//            txt.text = list[position].text
            Glide.with(ivWalpaper)
                .load(list[position].img)
                .into(ivWalpaper)
        }
    }
//    private class diffutilcallback:DiffUtil.ItemCallback<Int>(){
//
//        override fun areItemsTheSame(oldItem: Int, newItem: Int)=oldItem==newItem
//
//        override fun areContentsTheSame(oldItem: Int, newItem: Int)=oldItem==newItem
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_slider, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SliderViewHolder).onBind(position)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

data class AdapterItems(val img: Int)