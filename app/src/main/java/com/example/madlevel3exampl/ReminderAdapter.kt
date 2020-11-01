package com.example.madlevel3exampl
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_reminder.view.*

//Create a ReminderAdapter class that will be used by the RecyclerView.
class ReminderAdapter(private val reminders: List<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>(){

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return reminders.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(reminders[position])
    }

    // everytime you have a reclyverview, you need to build a custom view holder class that
    //describes what the views are going to be looking like in your recyclerview
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun databind(reminder: Reminder) {
            itemView.tvReminder.text = reminder.reminderText

        }
    }


}