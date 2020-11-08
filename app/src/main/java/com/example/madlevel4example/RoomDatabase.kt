package com.example.madlevel4example

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Creating the database

//Annotate the class to be a Room database, declare the entities that belong in the database and set the version number
@Database(entities = [Reminder::class], version = 1, exportSchema = false)
abstract class ReminderRoomDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object {
        private const val DATABASE_NAME = "REMINDER_DATABASE"

        @Volatile
        private var reminderRoomDatabaseInstance: ReminderRoomDatabase? = null


        //Because we want the database to be static we encapsulate the getDatabase function within a companion object
        fun getDatabase(context: Context): ReminderRoomDatabase? {
            if (reminderRoomDatabaseInstance == null) {
                synchronized(ReminderRoomDatabase::class.java) {
                    if (reminderRoomDatabaseInstance == null) {
                        reminderRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            //Inside getDatabase the ReminderRoomDatabase is
                            // created using Room.databaseBuilder. This is where we define the database name.
                            ReminderRoomDatabase::class.java, DATABASE_NAME
                        )
                                //By allowing the queries to be performed on the main thread
                            // we will face serious performance issues once the database queries increase in length.
                            //.allowMainThreadQueries()
                                //we need to let it run in the background thread.
                            .build()
                    }
                }
            }
            return reminderRoomDatabaseInstance
        }
    }

}
