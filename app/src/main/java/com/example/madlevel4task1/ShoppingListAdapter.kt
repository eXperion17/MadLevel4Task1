package com.example.madlevel4task1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/*class ShoppingListAdapter(private val products:List<Product>) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val binding = ItemReminderBinding.bind(itemView);

        fun databind(product: Product) {

            //itemView.tvReminder.text = reminder.reminderText;
        }
    }

    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            //LayoutInflater.from(parent.context).inflate((R.layout.item_reminder), parent, false)
        );
    }*/


    override fun getItemCount(): Int {
        return products.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(products[position]);
    }

}*/