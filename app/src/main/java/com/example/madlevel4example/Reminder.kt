package com.example.madlevel4example

//Room creates an SQLite database using all objects annotated with @Entity.
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//WITH ROOM YOU Put the reminders in a database


//this is an entity that needs to be stored in the database.
// Using tableName we provide Room with a tableName
// we want to store this entity in. By default it will use the name of the object (Reminder)
@Entity(tableName = "reminderTable")
data class Reminder(


    //Using the @ColumnInfo annotation we can define aspects of the column. We only give it a name. By default
    // it would have used the name of the variable so in this case
    // we could’ve just used @ColumnInfo instead of @ColumnInfo(name = “reminder”)
    @ColumnInfo(name = "reminder")
    var reminderText: String,

    //databases need a primary key. We want room to auto generate the id.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    // It’s also marked as nullable with the question mark so that it’s optional in the constructor.
    var id: Long? = null
)