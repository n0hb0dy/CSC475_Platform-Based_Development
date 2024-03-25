package com.example.spontaneouschef

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date

const val DATABASE_NAME = "RecipeDB"
const val PREF_TABLE = "preferences"
const val DAYSTREAK_TABLE = "daystreak"
const val DISHESCOMPLETE_TABLE = "dishescompleted"
const val COL_INTOLERANCES = "Intolerences"
const val COL_CALORIES = "MaxCalories"
const val COL_CARBS = "MaxCarbs"
const val COL_SUGAR = "MaxSugar"
const val COL_SODIUM = "MaxSodium"
const val COL_DAYS = "Days"
const val COL_ACTIVESTATUS = "ActiveStatus"
const val COL_LASTLOCKINDATE = "LastLockinDate"
const val COL_DISHID = "DishID"
const val COL_DISHNAME = "DishName"
const val COL_INGREDIENTS = "Ingredients"
const val COL_INSTRUCTIONS = "Instructions"

class DataBaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 5){

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
                                            COL_DISHNAME + " TEXT, " +
                                            COL_INGREDIENTS + " TEXT, " +
                                            COL_INSTRUCTIONS + " TEXT);"

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

    fun setDishesCompleted(dishID: String, dishName: String, ingredients: String, instructions: String){
        val db: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()

        values.put(COL_DISHID, dishID)
        values.put(COL_DISHNAME, dishName)
        values.put(COL_INGREDIENTS, ingredients)
        values.put(COL_INSTRUCTIONS, instructions)

        db.insert(DISHESCOMPLETE_TABLE, null, values)
    }

    @SuppressLint("Range")
    fun getPreferences() : Preferences? {
        val db: SQLiteDatabase = this.readableDatabase
        setPreferences("", 9999, 9999, 9999, 9999)
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
        setDayStreak("", "true", SimpleDateFormat("dd/M/yyyy").format(Date()))
        val cursor =db.rawQuery("SELECT * FROM $DAYSTREAK_TABLE LIMIT 1", null)
        var currentDayStreak: DayStreak? = null

        if (cursor.moveToNext()){
            val days = cursor.getInt(cursor.getColumnIndexOrThrow(COL_DAYS))
            val activeStatus = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ACTIVESTATUS))
            val lastLockinDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_LASTLOCKINDATE))

            currentDayStreak = DayStreak(days, activeStatus, lastLockinDate)

        }

        cursor.close()
        db.close()

        return currentDayStreak
    }

    fun getDishesCompleted() : ArrayList<DishesCompleted> {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $DISHESCOMPLETE_TABLE", null)
        val currentDishesCompleted: ArrayList<DishesCompleted> = ArrayList()

        while (cursor.moveToNext()){
            val dishID = cursor.getInt(cursor.getColumnIndexOrThrow(COL_DISHID))
            val dishName = cursor.getString(cursor.getColumnIndexOrThrow(COL_DISHNAME))
            val ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENTS))
            val instructions = cursor.getString(cursor.getColumnIndexOrThrow(COL_INSTRUCTIONS))

            currentDishesCompleted.add(DishesCompleted(dishID, dishName, ingredients, instructions))
        }

        cursor.close()
        db.close()

        return currentDishesCompleted
    }
}