package com.example.arisfitness.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.arisfitness.data.model.DailyRoutineEntry
import com.example.arisfitness.data.model.RoutineLogEntry
import com.example.arisfitness.data.model.UserProfile
import com.example.arisfitness.data.model.WeightEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ArisDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        const val DATABASE_NAME = "aris_fitness.db"
        const val DATABASE_VERSION = 1

        // User Profile Table
        const val TABLE_USER = "user_profile"
        const val COL_USER_ID = "user_id"
        const val COL_HEIGHT = "height_cm"
        const val COL_WEIGHT = "weight_kg"
        const val COL_AGE = "age"
        const val COL_GENDER = "gender"
        const val COL_ACTIVITY = "activity_level"
        const val COL_BMR = "bmr"
        const val COL_TDEE = "tdee"
        const val COL_CHARACTER_ID = "selected_character_id"
        const val COL_START_DATE = "start_date"
        const val COL_DAY_INDEX = "current_day_index"
        const val COL_PAUSED_DAYS = "paused_days"
        const val COL_WEIGHT_HISTORY = "weight_history"

        // Routine Logs Table
        const val TABLE_LOGS = "routine_logs"
        const val COL_LOG_DATE = "log_date"
        const val COL_LOG_CHAR_ID = "character_id"
        const val COL_LOG_DAY_INDEX = "day_index"
        const val COL_COMPLETED_ITEMS = "completed_items"
        const val COL_CALORIES_CONSUMED = "calories_consumed"
        const val COL_IS_COMPLETED = "is_completed"
        const val COL_NOTES = "notes"

        // Cached Routines Table
        const val TABLE_CACHED_ROUTINES = "cached_routines"
        const val COL_ROUTINE_CHAR_ID = "character_id"
        const val COL_ROUTINE_DAY_INDEX = "day_index"
        const val COL_ROUTINE_DATA = "routine_json"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create User Profile Table
        db.execSQL(
            """
            CREATE TABLE $TABLE_USER (
                $COL_USER_ID TEXT PRIMARY KEY,
                $COL_HEIGHT REAL,
                $COL_WEIGHT REAL,
                $COL_AGE INTEGER,
                $COL_GENDER TEXT,
                $COL_ACTIVITY TEXT,
                $COL_BMR REAL,
                $COL_TDEE REAL,
                $COL_CHARACTER_ID TEXT,
                $COL_START_DATE TEXT,
                $COL_DAY_INDEX INTEGER,
                $COL_PAUSED_DAYS TEXT,
                $COL_WEIGHT_HISTORY TEXT
            )
            """.trimIndent()
        )

        // Create Routine Logs Table
        db.execSQL(
            """
            CREATE TABLE $TABLE_LOGS (
                $COL_LOG_DATE TEXT PRIMARY KEY,
                $COL_LOG_CHAR_ID TEXT,
                $COL_LOG_DAY_INDEX INTEGER,
                $COL_COMPLETED_ITEMS TEXT,
                $COL_CALORIES_CONSUMED INTEGER,
                $COL_IS_COMPLETED INTEGER,
                $COL_NOTES TEXT
            )
            """.trimIndent()
        )

        // Create Cached Routines Table
        db.execSQL(
            """
            CREATE TABLE $TABLE_CACHED_ROUTINES (
                $COL_ROUTINE_CHAR_ID TEXT,
                $COL_ROUTINE_DAY_INDEX INTEGER,
                $COL_ROUTINE_DATA TEXT,
                PRIMARY KEY ($COL_ROUTINE_CHAR_ID, $COL_ROUTINE_DAY_INDEX)
            )
            """.trimIndent()
        )

        // Index on (character_id, day_index) for fast O(1) daily lookups
        db.execSQL(
            "CREATE INDEX idx_routine_char_day ON $TABLE_CACHED_ROUTINES ($COL_ROUTINE_CHAR_ID, $COL_ROUTINE_DAY_INDEX)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CACHED_ROUTINES")
        onCreate(db)
    }

    // -------------------------------------------------------------
    // User Profile CRUD
    // -------------------------------------------------------------
    fun getUserProfile(): UserProfile? {
        val db = readableDatabase
        val cursor = db.query(TABLE_USER, null, null, null, null, null, null, "1")
        return cursor.use {
            if (it.moveToFirst()) {
                val pausedDaysJson = it.getString(it.getColumnIndexOrThrow(COL_PAUSED_DAYS)) ?: "[]"
                val weightHistoryJson = it.getString(it.getColumnIndexOrThrow(COL_WEIGHT_HISTORY)) ?: "[]"

                val pausedDays: List<String> = try { json.decodeFromString(pausedDaysJson) } catch (_: Exception) { emptyList() }
                val weightHistory: List<WeightEntry> = try { json.decodeFromString(weightHistoryJson) } catch (_: Exception) { emptyList() }

                UserProfile(
                    userId = it.getString(it.getColumnIndexOrThrow(COL_USER_ID)),
                    heightCm = it.getFloat(it.getColumnIndexOrThrow(COL_HEIGHT)),
                    weightKg = it.getFloat(it.getColumnIndexOrThrow(COL_WEIGHT)),
                    age = it.getInt(it.getColumnIndexOrThrow(COL_AGE)),
                    gender = it.getString(it.getColumnIndexOrThrow(COL_GENDER)),
                    activityLevel = it.getString(it.getColumnIndexOrThrow(COL_ACTIVITY)),
                    bmr = it.getFloat(it.getColumnIndexOrThrow(COL_BMR)),
                    tdee = it.getFloat(it.getColumnIndexOrThrow(COL_TDEE)),
                    selectedCharacterId = it.getString(it.getColumnIndexOrThrow(COL_CHARACTER_ID)),
                    startDate = it.getString(it.getColumnIndexOrThrow(COL_START_DATE)),
                    currentDayIndex = it.getInt(it.getColumnIndexOrThrow(COL_DAY_INDEX)),
                    pausedDays = pausedDays,
                    weightHistory = weightHistory
                )
            } else null
        }
    }

    fun saveUserProfile(profile: UserProfile) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_USER_ID, profile.userId)
            put(COL_HEIGHT, profile.heightCm)
            put(COL_WEIGHT, profile.weightKg)
            put(COL_AGE, profile.age)
            put(COL_GENDER, profile.gender)
            put(COL_ACTIVITY, profile.activityLevel)
            put(COL_BMR, profile.bmr)
            put(COL_TDEE, profile.tdee)
            put(COL_CHARACTER_ID, profile.selectedCharacterId)
            put(COL_START_DATE, profile.startDate)
            put(COL_DAY_INDEX, profile.currentDayIndex)
            put(COL_PAUSED_DAYS, json.encodeToString(profile.pausedDays))
            put(COL_WEIGHT_HISTORY, json.encodeToString(profile.weightHistory))
        }
        db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    // -------------------------------------------------------------
    // Routine Log CRUD
    // -------------------------------------------------------------
    fun getRoutineLog(date: String): RoutineLogEntry? {
        val db = readableDatabase
        val cursor = db.query(TABLE_LOGS, null, "$COL_LOG_DATE = ?", arrayOf(date), null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                val completedJson = it.getString(it.getColumnIndexOrThrow(COL_COMPLETED_ITEMS)) ?: "[]"
                val completedItems: List<String> = try { json.decodeFromString(completedJson) } catch (_: Exception) { emptyList() }

                RoutineLogEntry(
                    date = it.getString(it.getColumnIndexOrThrow(COL_LOG_DATE)),
                    characterId = it.getString(it.getColumnIndexOrThrow(COL_LOG_CHAR_ID)),
                    dayIndex = it.getInt(it.getColumnIndexOrThrow(COL_LOG_DAY_INDEX)),
                    completedItemIds = completedItems,
                    customCaloriesConsumed = it.getInt(it.getColumnIndexOrThrow(COL_CALORIES_CONSUMED)),
                    isDayCompleted = it.getInt(it.getColumnIndexOrThrow(COL_IS_COMPLETED)) == 1,
                    notes = it.getString(it.getColumnIndexOrThrow(COL_NOTES)) ?: ""
                )
            } else null
        }
    }

    fun getAllCompletedLogsCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_LOGS WHERE $COL_IS_COMPLETED = 1", null)
        return cursor.use {
            if (it.moveToFirst()) it.getInt(0) else 0
        }
    }

    fun saveRoutineLog(entry: RoutineLogEntry) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_LOG_DATE, entry.date)
            put(COL_LOG_CHAR_ID, entry.characterId)
            put(COL_LOG_DAY_INDEX, entry.dayIndex)
            put(COL_COMPLETED_ITEMS, json.encodeToString(entry.completedItemIds))
            put(COL_CALORIES_CONSUMED, entry.customCaloriesConsumed)
            put(COL_IS_COMPLETED, if (entry.isDayCompleted) 1 else 0)
            put(COL_NOTES, entry.notes)
        }
        db.insertWithOnConflict(TABLE_LOGS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    // -------------------------------------------------------------
    // Cached Custom Routine CRUD
    // -------------------------------------------------------------
    fun getCachedRoutine(characterId: String, dayIndex: Int): DailyRoutineEntry? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CACHED_ROUTINES,
            arrayOf(COL_ROUTINE_DATA),
            "$COL_ROUTINE_CHAR_ID = ? AND $COL_ROUTINE_DAY_INDEX = ?",
            arrayOf(characterId, dayIndex.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                val jsonStr = it.getString(0)
                try { json.decodeFromString<DailyRoutineEntry>(jsonStr) } catch (_: Exception) { null }
            } else null
        }
    }

    fun cacheRoutine(entry: DailyRoutineEntry) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ROUTINE_CHAR_ID, entry.characterId)
            put(COL_ROUTINE_DAY_INDEX, entry.dayIndex)
            put(COL_ROUTINE_DATA, json.encodeToString(entry))
        }
        db.insertWithOnConflict(TABLE_CACHED_ROUTINES, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }
}
