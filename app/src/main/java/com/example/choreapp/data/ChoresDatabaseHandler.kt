package com.example.choreapp.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.choreapp.model.*
import java.text.DateFormat
import java.util.*

class ChoresDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_CHORE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+
                                 KEY_CHORE_NAME + " TEXT," +
                                KEY_CHORE_ASSIGNED_BY + " TEXT," +
                                KEY_CHORE_ASSIGNED_TO + " TEXT," +
                                KEY_CHORE_ASSIGNED_TIME + " LONG" +")"

        db?.execSQL(CREATE_CHORE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)

        onCreate(db)
    }

    fun createChore(chore: Chore) {
        val db: SQLiteDatabase? = writableDatabase

        val values: ContentValues = ContentValues()

        //Получаем извне Chore и складываем его в базу данных
        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO, chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())

        db?.insert(TABLE_NAME, null, values)
        Log.d("DATA INSERTED", "SUCCESS")
        db?.close()
    }

    fun readChore(id: Int) : Chore {
        val db: SQLiteDatabase? = writableDatabase
        val cursor: Cursor? = db?.query(TABLE_NAME, arrayOf(KEY_ID, KEY_CHORE_NAME, KEY_CHORE_ASSIGNED_BY, KEY_CHORE_ASSIGNED_TO, KEY_CHORE_ASSIGNED_TIME), KEY_ID + "=?", arrayOf(id.toString()), null, null, null, null)

        cursor?.moveToFirst()

        val chore = Chore()
        chore.choreName = cursor!!.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
        chore.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO))
        chore.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY))
        chore.timeAssigned = cursor.getLong(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME))

        val dataFormat: java.text.DateFormat = DateFormat.getDateInstance()
        var formattedDate = dataFormat.format(Date( cursor.getLong(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME))).time)

        cursor.close()
        return chore
    }

    fun readChores() : ArrayList<Chore> {
        val db: SQLiteDatabase? = writableDatabase
        val list: ArrayList<Chore> = ArrayList()

        //Select all chores from table
        val selectAll = "SELECT * FROM " + TABLE_NAME
        val cursor: Cursor = db!!.rawQuery(selectAll, null)

        //loop through chores
        if (cursor.moveToFirst()) {
            do {
                val chore = Chore()

                chore.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                chore.choreName = cursor.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
                chore.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO))
                chore.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY))

                list.add(chore)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun updateChore(chore: Chore) : Int {
        val db: SQLiteDatabase? = writableDatabase
        var values: ContentValues = ContentValues()

        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO, chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())

        //Update row
        return db!!.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(chore.id.toString()))
    }

    fun deleteChore(id: Int) {
        val db: SQLiteDatabase? = writableDatabase
        db?.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(id.toString()))
        db?.close()
    }

    fun getChoresCount() : Int {
        val db: SQLiteDatabase? = readableDatabase
        val countQuery = "SELECT * FROM " + TABLE_NAME
        val cursor: Cursor = db!!.rawQuery(countQuery, null)
        var count = cursor.count
        cursor.close()
        return count
    }

 }