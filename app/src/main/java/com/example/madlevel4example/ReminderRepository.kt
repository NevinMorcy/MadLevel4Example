package com.example.madlevel4example

import android.content.Context


//create a repository class which is responsible for using the DAO to make operations on the database.
// prevents us from having to create and initialize the dao objects in the activity classes
// using the getDatabase method all the time.
    public class ReminderRepository(context: Context) {

        //Create and initialize a ReminderDao variable.
        private var reminderDao: ReminderDao

        init {
            //The class constructor takes a Context object because we need this to access the database.
            val reminderRoomDatabase = ReminderRoomDatabase.getDatabase(context)
            //The reminderDao is constructed using the abstract method we added in the ReminderRoomDatabase class.
            reminderDao = reminderRoomDatabase!!.reminderDao()
        }

    //The methods will use the reminderDao methods to make the actual operations.

        suspend fun getAllReminders(): List<Reminder> {
            return reminderDao.getAllReminders()
        }

    //For example insertReminder will insert a reminder in the database using the reminderDao insertReminder method.
        suspend fun insertReminder(reminder: Reminder) {
            reminderDao.insertReminder(reminder)
        }

        suspend fun deleteReminder(reminder: Reminder) {
            reminderDao.deleteReminder(reminder)
        }


        suspend fun updateReminder(reminder: Reminder) {
            reminderDao.updateReminder(reminder)
        }
    }

