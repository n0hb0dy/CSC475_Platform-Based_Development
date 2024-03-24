package com.example.spontaneouschef

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date

val DATABASE_NAME = "RecipeDB"
val PREF_TABLE = "preferences"
val DAYSTREAK_TABLE = "daystreak"
val DISHESCOMPLETE_TABLE = "dishescompleted"
val COL_INTOLERANCES = "Intolerences"
val COL_CALORIES = "MaxCalories"
val COL_CARBS = "MaxCarbs"
val COL_SUGAR = "MaxSugar"
val COL_SODIUM = "MaxSodium"
val COL_DAYS = "Days"
val COL_ACTIVESTATUS = "ActiveStatus"
val COL_LASTLOCKINDATE = "LastLockinDate"
val COL_DISHID = "DishID"
val COL_DISHNAME = "DishName"

class DataBaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 2){

    override fun onCreate(db: SQLiteDatabase?) {
        val createPrefTable = "CREATE TABLE " + PREF_TABLE +
                                " (" + COL_INTOLERANCES + " TEXT, " +
                                COL_CALORIES + " INTEGER, " +
                                COL_CARBS + " INTEGER, " +
                                COL_SUGAR + " INTEGER, " +
                                COL_SODIUM + " INTEGER);"

        val createDaystreakTable = "CREATE TABLE " + DAYSTREAK_TABLE +
                                    " (" + COL_DAYS + " INTEGER, " +
                                    COL_ACTIVESTATUS + " INTEGER, " +
                                    COL_LASTLOCKINDATE + " TEXT);"

        val createDishesCompletedTable = "CREATE TABLE " + DISHESCOMPLETE_TABLE +
                                            " (" + COL_DISHID + " INTEGER PRIMARY KEY, " +
                                            COL_DISHNAME + " TEXT);"

        db?.execSQL(createPrefTable)
        db?.execSQL(createDaystreakTable)
        db?.execSQL(createDishesCompletedTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $PREF_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $DAYSTREAK_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $DISHESCOMPLETE_TABLE")
        onCreate(db)
    }

    fun setPreferences(intolerances: String, maxCalories: Int, maxCarbs:Int,
                          maxSugar: Int, maxSodium: Int){
        val db: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()

        values.put(COL_INTOLERANCES, intolerances)
        values.put(COL_CALORIES, maxCalories)
        values.put(COL_CARBS, maxCarbs)
        values.put(COL_SUGAR, maxSugar)
        values.put(COL_SODIUM, maxSodium)

        db.execSQL("DELETE FROM $PREF_TABLE")
        db.insert(PREF_TABLE, null, values)
    }

    fun setDayStreak(days: String, activeStatus: String, lastLockinDate:String){
        val db: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()

        values.put(COL_DAYS, days)
        values.put(COL_ACTIVESTATUS, activeStatus)
        values.put(COL_LASTLOCKINDATE, lastLockinDate)

        db.execSQL("DELETE FROM $DAYSTREAK_TABLE")
        db.insert(DAYSTREAK_TABLE, null, values)
    }

    fun setDishesCompleted(dishID: String, dishName: String){
        val db: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()

        values.put(COL_DISHID, dishID)
        values.put(COL_DISHNAME, dishName)

        db.insert(DISHESCOMPLETE_TABLE, null, values)
    }

    @SuppressLint("Range")
    fun getPreferences() : Preferences? {
        val db: SQLiteDatabase = this.readableDatabase
        //setPreferences("", 9999, 9999, 9999, 9999)
        val cursor = db.rawQuery("SELECT * FROM $PREF_TABLE LIMIT 1", null)
        var currentPreferences: Preferences? = null
        cursor.moveToFirst()



        while (!cursor.isAfterLast){
            val intolerances = cursor.getString(cursor.getColumnIndex(COL_INTOLERANCES))
            val calories = cursor.getInt(cursor.getColumnIndex(COL_CALORIES))
            val carbs = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CARBS))
            val sugar = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SUGAR))
            val sodium = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SODIUM))

            currentPreferences = Preferences(intolerances, calories, carbs, sugar, sodium)
            cursor.moveToNext()
        }

        cursor.close()
        db.close()

        return currentPreferences
    }

    @SuppressLint("SimpleDateFormat")
    fun getDayStreak() : DayStreak? {
        val db: SQLiteDatabase = this.readableDatabase
        //setDayStreak("", "true", SimpleDateFormat("dd/M/yyyy").format(Date()))
        val cursor =db.rawQuery("SELECT * FROM $DAYSTREAK_TABLE LIMIT 1", null)
        var currentPreferences: DayStreak? = null

        if (cursor.moveToFirst()){
            val days = cursor.getInt(cursor.getColumnIndexOrThrow(COL_DAYS))
            val activeStatus = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ACTIVESTATUS))
            val lastLockinDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_LASTLOCKINDATE))


            currentPreferences = DayStreak(days, activeStatus, lastLockinDate)

        }

        cursor.close()
        db.close()

        return currentPreferences
    }

    fun getDishesCompleted() : ArrayList<DishesCompleted> {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor =db.rawQuery("SELECT * FROM $DISHESCOMPLETE_TABLE", null)
        val currentPreferences: ArrayList<DishesCompleted> = ArrayList()

        while (cursor.moveToNext()){
            val dishID = cursor.getInt(cursor.getColumnIndexOrThrow(COL_DISHID))
            val dishName = cursor.getString(cursor.getColumnIndexOrThrow(COL_DISHNAME))

            currentPreferences.add(DishesCompleted(dishID, dishName))
        }

        cursor.close()
        db.close()

        return currentPreferences
    }


}