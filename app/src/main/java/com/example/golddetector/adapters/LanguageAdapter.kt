package com.example.golddetector.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.golddetector.R
import com.example.golddetector.interfaces.OnitemClickListener
import com.example.golddetector.model.Languages

class LanguageAdapter(
    private val items: List<Languages>,
    private val itemClickListener: OnitemClickListener,
    private var selectedItemPosition: Int
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_list_view, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemImage.setImageResource(item.imageResource)
        holder.itemText.text = item.text
        holder.itemRadioButton.isChecked = position == selectedItemPosition

        // Set the visibility of the divider based on the selected item


        // Handle item clicks
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }

        // Set an OnClickListener for the RadioButton to handle item selection
        holder.itemRadioButton.setOnClickListener {
            itemClickListener.onItemClick(position)

        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.img_country)
        val itemText: TextView = itemView.findViewById(R.id.txt_country_name)
        val itemRadioButton: RadioButton = itemView.findViewById(R.id.radioSelected)
    }
}