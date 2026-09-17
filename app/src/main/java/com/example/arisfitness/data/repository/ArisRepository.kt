package com.example.arisfitness.data.repository

import android.content.Context
import com.example.arisfitness.data.local.ArisDatabaseHelper
import com.example.arisfitness.data.model.CharacterProfile
import com.example.arisfitness.data.model.DailyRoutineEntry
import com.example.arisfitness.data.model.RoutineLogEntry
import com.example.arisfitness.data.model.UserProfile
import com.example.arisfitness.data.model.WeightEntry
import com.example.arisfitness.engine.CalorieEngine
import com.example.arisfitness.engine.DateResolutionEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArisRepository(private val context: Context) {

    private val dbHelper = ArisDatabaseHelper(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _todayLog = MutableStateFlow<RoutineLogEntry?>(null)
    val todayLog: StateFlow<RoutineLogEntry?> = _todayLog.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        scope.launch {
            val profile = dbHelper.getUserProfile()
            _userProfile.value = profile
            loadTodayLog()
        }
    }

    private fun loadTodayLog() {
        val todayStr = DateResolutionEngine.getTodayString()
        val log = dbHelper.getRoutineLog(todayStr)
        _todayLog.value = log
    }

    fun saveUserProfile(profile: UserProfile) {
        scope.launch {
            dbHelper.saveUserProfile(profile)
            _userProfile.value = profile
        }
    }

    /**
     * Retrieves the daily routine for the specified character and dayIndex,
     * scaled directly to the user's specific TDEE.
     */
    fun getDailyRoutine(characterId: String, dayIndex: Int, userTdee: Float): DailyRoutineEntry {
        // First check cached database
        val cached = dbHelper.getCachedRoutine(characterId, dayIndex)
        val rawRoutine = cached ?: RoutineDataGenerator.generateDay(characterId, dayIndex)

        // Scale calories to user's TDEE
        val character = CharacterProfile.getById(characterId)
        val scaleFactor = CalorieEngine.calculateScaleFactor(userTdee, character.referenceTdee)
        return CalorieEngine.scaleRoutineEntry(rawRoutine, scaleFactor)
    }

    /**
     * Toggles completion status of a checklist item (wake, meal, workout, reminder).
     */
    fun toggleChecklistItem(itemId: String, characterId: String, dayIndex: Int) {
        scope.launch {
            val todayStr = DateResolutionEngine.getTodayString()
            val currentLog = dbHelper.getRoutineLog(todayStr) ?: RoutineLogEntry(
                date = todayStr,
                characterId = characterId,
                dayIndex = dayIndex
            )

            val updatedItems = currentLog.completedItemIds.toMutableList()
            if (updatedItems.contains(itemId)) {
                updatedItems.remove(itemId)
            } else {
                updatedItems.add(itemId)
            }

            val updatedLog = currentLog.copy(
                completedItemIds = updatedItems,
                isDayCompleted = updatedItems.isNotEmpty()
            )
            dbHelper.saveRoutineLog(updatedLog)
            _todayLog.value = updatedLog
        }
    }

    /**
     * Updates manual calories logged for today.
     */
    fun updateCaloriesConsumed(calories: Int, characterId: String, dayIndex: Int) {
        scope.launch {
            val todayStr = DateResolutionEngine.getTodayString()
            val currentLog = dbHelper.getRoutineLog(todayStr) ?: RoutineLogEntry(
                date = todayStr,
                characterId = characterId,
                dayIndex = dayIndex
            )
            val updatedLog = currentLog.copy(customCaloriesConsumed = calories)
            dbHelper.saveRoutineLog(updatedLog)
            _todayLog.value = updatedLog
        }
    }

    /**
     * Logs a new weight measurement for the user.
     */
    fun logWeight(weightKg: Float) {
        scope.launch {
            val current = _userProfile.value ?: return@launch
            val todayStr = DateResolutionEngine.getTodayString()
            val newHistory = current.weightHistory.filter { it.date != todayStr }.toMutableList()
            newHistory.add(WeightEntry(todayStr, weightKg))
            newHistory.sortBy { it.date }

            // Recalculate BMR and TDEE with new weight
            val newBmr = CalorieEngine.calculateBmr(weightKg, current.heightCm, current.age, current.gender)
            val newTdee = CalorieEngine.calculateTdee(newBmr, current.activityLevel)

            val updatedProfile = current.copy(
                weightKg = weightKg,
                bmr = newBmr,
                tdee = newTdee,
                weightHistory = newHistory
            )
            dbHelper.saveUserProfile(updatedProfile)
            _userProfile.value = updatedProfile
        }
    }

    /**
     * Handles Missed-Day logic according to Specification 6.4:
     * If user resumes from where left off: pause/skip missed dates and stay on saved day index.
     * If user catches up: advance day index to expected calendar day.
     */
    fun handleMissedDays(resumeFromSaved: Boolean, missedDates: List<String>, expectedCalendarDay: Int) {
        scope.launch {
            val current = _userProfile.value ?: return@launch
            val updatedPausedDays = (current.pausedDays + missedDates).distinct()

            val updatedProfile = if (resumeFromSaved) {
                current.copy(pausedDays = updatedPausedDays)
            } else {
                current.copy(currentDayIndex = expectedCalendarDay)
            }
            dbHelper.saveUserProfile(updatedProfile)
            _userProfile.value = updatedProfile
        }
    }

    /**
     * Switch character (with reset warning as required by Spec Section 8).
     */
    fun switchCharacter(newCharacterId: String, resetDayIndex: Boolean = true) {
        scope.launch {
            val current = _userProfile.value ?: return@launch
            val updated = current.copy(
                selectedCharacterId = newCharacterId,
                currentDayIndex = if (resetDayIndex) 1 else current.currentDayIndex,
                startDate = if (resetDayIndex) DateResolutionEngine.getTodayString() else current.startDate
            )
            dbHelper.saveUserProfile(updated)
            _userProfile.value = updated
        }
    }

    fun getTotalCompletedDaysCount(): Int {
        return dbHelper.getAllCompletedLogsCount()
    }
}
