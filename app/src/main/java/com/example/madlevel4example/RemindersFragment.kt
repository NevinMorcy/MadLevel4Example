package com.example.madlevel4example

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_reminders.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RemindersFragment : Fragment() {
    private val reminders = arrayListOf<Reminder>()
    private val reminderAdapter = ReminderAdapter(reminders)
    private lateinit var reminderRepository: ReminderRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvReminders.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvReminders.adapter = reminderAdapter
        rvReminders.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        observeAddReminderResult()
        createItemTouchHelper().attachToRecyclerView(rvReminders)

        reminderRepository = ReminderRepository(requireContext())
        //calling the method
        // When the user starts the app we need to get the reminders from the database
        // populate the recyclerview with those reminders. This is done after initializing the recyclerview.
        getRemindersFromDatabase()

    }

    //This method is made which will get all the reminders from the database using the repository,
// clear the current reminders list, add the reminders from the database and notifies the adapter that the data set was changed.
    private fun getRemindersFromDatabase() {
        //the reminders have been received from the database we can resume on the main thread
        // to populate the reminders list and notify the adapter about the data set changes.
        //he reminders are being queried using the IO dispatcher
        CoroutineScope(Dispatchers.Main).launch {
            val reminders = withContext(Dispatchers.IO) {
                reminderRepository.getAllReminders()
            }
            this@RemindersFragment.reminders.clear()
            this@RemindersFragment.reminders.addAll(reminders)
            reminderAdapter.notifyDataSetChanged()

        }
    }



        //now we need to receive these reminders that are added and send as a bundle in the remindersfragment
      private fun observeAddReminderResult() {
            setFragmentResultListener(REQ_REMINDER_KEY) { key, bundle ->
                bundle.getString(BUNDLE_REMINDER_KEY)?.let {
                    val reminder = Reminder(it)

//                reminders.add(reminder)
//                reminderAdapter.notifyDataSetChanged()
                    //we are not storing it anymore directly
                    //we are storing it in the database
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            reminderRepository.insertReminder(reminder)
                        }
                        getRemindersFromDatabase()
                    }
                } ?: Log.e("ReminderFragment", "Request triggered, but empty reminder text!")

            }
        }


        /**
         * Create a touch helper to recognize when a user swipes an item from a recycler view.
         * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         * and uses callbacks to signal when a user is performing these actions.
         */
     private fun createItemTouchHelper(): ItemTouchHelper {

            // Callback which is used to create the ItemTouch helper. Only enables left swipe.
            // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
            val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                // Enables or Disables the ability to move items up and down.
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                // Callback triggered when a user swiped an item.
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
//                reminders.removeAt(position)
//                reminderAdapter.notifyDataSetChanged()
                    //we are not doing it directly anymore, we are using the database

                    // the reminder is deleted from the database
                    // after which the reminders are loaded from the database again to update the user interface.
                    val reminderToDelete = reminders[position]
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            reminderRepository.deleteReminder(reminderToDelete)
                        }
                        getRemindersFromDatabase()
                    }

                }
            }
            return ItemTouchHelper(callback)
        }


    }
