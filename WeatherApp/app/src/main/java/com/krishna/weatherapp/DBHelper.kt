package com.krishna.weatherapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    val allData: ArrayList<String>
        get() {
            val arrayList = ArrayList<String>()

            val db = this.readableDatabase
            val res = db.rawQuery("select * from jsondata", null)
            try {
                res.moveToFirst()

                while (!res.isAfterLast) {
                    arrayList.add(res.getString(res.getColumnIndex(JSON_COLUMN_DATA)))
                    res.moveToNext()
                }
            } finally {
                res.close()
            }
            return arrayList
        }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
                "create table jsondata " + "(id integer primary key, data text)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS jsondata")
        onCreate(db)
    }

    fun insertData(data: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("data", data)
        db.insert("jsondata", null, contentValues)
        return true
    }

    fun getData(id: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("select * from jsondata where id=$id", null)
    }

    fun numberOfRows(): Int {
        val db = this.readableDatabase
        return DatabaseUtils.queryNumEntries(db, JSON_TABLE_NAME).toInt()
    }

    fun updateData(id: Int?, data: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("data", data)
        db.update("jsondata", contentValues, "id = ? ", arrayOf(Integer.toString(id!!)))
        return true
    }

    fun deleteData(id: Int?): Int? {
        val db = this.writableDatabase
        return db.delete("jsondata",
                "id = ? ",
                arrayOf((id!!).toString()))
    }

    companion object {

        private val DATABASE_NAME = "WeatherDB.db"
        private val JSON_TABLE_NAME = "jasondata"
        private val JSON_COLUMN_ID = "id"
        private val JSON_COLUMN_DATA = "data"
    }
}
