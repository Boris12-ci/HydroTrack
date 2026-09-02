package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.DrinkLog
import com.example.model.HydrationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HydrationViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("hydration_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(HydrationState())
    val uiState: StateFlow<HydrationState> = _uiState.asStateFlow()

    init {
        loadState()
    }

    private fun getTodayDateKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun loadState() {
        val savedDate = prefs.getString("saved_date", "") ?: ""
        val today = getTodayDateKey()
        val goal = prefs.getInt("goal_ml", 2000)

        if (savedDate == today) {
            val intake = prefs.getInt("current_intake_ml", 0)
            val logsJson = prefs.getString("logs_json", "[]") ?: "[]"
            val logsList = parseLogs(logsJson)
            val streak = prefs.getInt("streak_days", 1)

            _uiState.value = HydrationState(
                goalMl = goal,
                currentIntakeMl = intake,
                logs = logsList,
                streakDays = streak
            )
        } else {
            // New day: reset daily intake but maintain/update streak
            val previousIntake = prefs.getInt("current_intake_ml", 0)
            var currentStreak = prefs.getInt("streak_days", 1)
            if (previousIntake >= goal && savedDate.isNotEmpty()) {
                currentStreak += 1
            }
            
            _uiState.value = HydrationState(
                goalMl = goal,
                currentIntakeMl = 0,
                logs = emptyList(),
                streakDays = currentStreak
            )
            saveState()
        }
    }

    private fun saveState() {
        viewModelScope.launch {
            val state = _uiState.value
            val editor = prefs.edit()
            editor.putString("saved_date", getTodayDateKey())
            editor.putInt("goal_ml", state.goalMl)
            editor.putInt("current_intake_ml", state.currentIntakeMl)
            editor.putInt("streak_days", state.streakDays)
            editor.putString("logs_json", serializeLogs(state.logs))
            editor.apply()
        }
    }

    private fun serializeLogs(logs: List<DrinkLog>): String {
        val jsonArray = JSONArray()
        for (log in logs) {
            val obj = JSONObject()
            obj.put("id", log.id)
            obj.put("amountMl", log.amountMl)
            obj.put("timestamp", log.timestamp)
            obj.put("label", log.label)
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }

    private fun parseLogs(json: String): List<DrinkLog> {
        val list = mutableListOf<DrinkLog>()
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    DrinkLog(
                        id = obj.getString("id"),
                        amountMl = obj.getInt("amountMl"),
                        timestamp = obj.getLong("timestamp"),
                        label = obj.optString("label", "Verre d'eau")
                    )
                )
            }
        } catch (e: Exception) {
            // fallback gracefully
        }
        return list
    }

    fun addWater(amountMl: Int = 250, label: String = "Verre d'eau") {
        if (amountMl <= 0) return
        val newLog = DrinkLog(
            amountMl = amountMl,
            label = label,
            timestamp = System.currentTimeMillis()
        )
        _uiState.update { current ->
            current.copy(
                currentIntakeMl = current.currentIntakeMl + amountMl,
                logs = listOf(newLog) + current.logs
            )
        }
        saveState()
    }

    fun resetIntake() {
        _uiState.update { current ->
            current.copy(
                currentIntakeMl = 0,
                logs = emptyList()
            )
        }
        saveState()
    }

    fun removeLog(logId: String) {
        _uiState.update { current ->
            val logToRemove = current.logs.find { it.id == logId }
            if (logToRemove != null) {
                current.copy(
                    currentIntakeMl = (current.currentIntakeMl - logToRemove.amountMl).coerceAtLeast(0),
                    logs = current.logs.filter { it.id != logId }
                )
            } else {
                current
            }
        }
        saveState()
    }

    fun undoLast() {
        val lastLog = _uiState.value.logs.firstOrNull() ?: return
        removeLog(lastLog.id)
    }

    fun setGoal(newGoalMl: Int) {
        if (newGoalMl < 500) return
        _uiState.update { current ->
            current.copy(goalMl = newGoalMl)
        }
        saveState()
    }
}
