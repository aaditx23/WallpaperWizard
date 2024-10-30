package com.aaditx23.wallpaperwizard.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.wallpaperwizard.backend.models.QuickSetModel
import com.aaditx23.wallpaperwizard.backend.models.ScheduleModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.SingleQueryChange
import io.realm.kotlin.query.find
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class ScheduleVM @Inject constructor(
    private val realm: Realm
) : ViewModel() {

    // StateFlow to hold list of ScheduleModel items
    private val _allSchedules = MutableStateFlow<List<ScheduleModel>>(emptyList())
    val allSchedules: StateFlow<List<ScheduleModel>> = _allSchedules.asStateFlow()

    // Loading state to indicate data loading status
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSchedules()
    }

    // Method to load all ScheduleModel items from Realm
    private fun loadSchedules() {
        viewModelScope.launch {
            realm.query<ScheduleModel>()
                .asFlow()
                .map { results ->
                    results.list.toList()
                }
                .collect { schedules ->
                    _allSchedules.value = schedules
                    _isLoading.value = false // Set loading to false after initial load
                }
        }
    }

    // Method to add or update a ScheduleModel in Realm
    fun addOrUpdateSchedule(schedule: ScheduleModel) {
        viewModelScope.launch {
            realm.write {
                copyToRealm(schedule)
            }
        }
    }

    // Method to delete a ScheduleModel from Realm by id
    fun deleteSchedule(id: ObjectId) {
        viewModelScope.launch {
            realm.write {
                val scheduleToDelete = query<ScheduleModel>("_id == $0", id).first().find()
                if (scheduleToDelete != null) {
                    delete(scheduleToDelete)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }
}
