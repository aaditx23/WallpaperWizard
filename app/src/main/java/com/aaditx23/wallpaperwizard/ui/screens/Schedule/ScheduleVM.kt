package com.aaditx23.wallpaperwizard.ui.screens.Schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.wallpaperwizard.models.ScheduleModel
import com.aaditx23.wallpaperwizard.ui.components.getCroppedStoragePath
import com.aaditx23.wallpaperwizard.ui.components.getPrevStoragePath
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
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

    var croppedDir: String = ""
        private set
    var prevDir: String = ""
        private set

    fun initDir(context: Context) {
        val _croppedDir = getCroppedStoragePath(context)
        println("path is: $_croppedDir")
        croppedDir = _croppedDir
        val _prevDir = getPrevStoragePath(context)
        println("path is: $_prevDir")
        prevDir = _prevDir
    }
    fun initPrevDir(context: Context) {

    }
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
    fun create() {
        viewModelScope.launch {
            realm.write {
                copyToRealm(ScheduleModel(), updatePolicy = UpdatePolicy.ALL)
            }
        }
    }
    fun setStartTime(id: ObjectId, startTime: String){
        viewModelScope.launch {
            realm.write {
                val schedule = query<ScheduleModel>("_id == $0", id).first().find()
                if (schedule != null){
                    schedule.startTime = startTime
                }
            }
        }
    }
    fun setEndTime(id: ObjectId, endTime: String){
        viewModelScope.launch {
            realm.write {
                val schedule = query<ScheduleModel>("_id == $0", id).first().find()
                if (schedule != null){
                    schedule.endTime = endTime
                }
            }
        }
    }
    fun setRunning(id: ObjectId, running: String){
        viewModelScope.launch {
            realm.write {
                val schedule = query<ScheduleModel>("_id == $0", id).first().find()
                if (schedule != null){
                    schedule.running = running
                }
            }
        }
    }

    fun setPrevHome(id: ObjectId, name: String){
        viewModelScope.launch {
            realm.write {
                val schedule = query<ScheduleModel>("_id = $0", id).first().find()
                if(schedule != null){
                    schedule.prevHome = name
                }
            }
        }
    }
    fun setPrevLock(id: ObjectId, name: String){
        viewModelScope.launch {
            realm.write {
                val schedule = query<ScheduleModel>("_id = $0", id).first().find()
                if(schedule != null){
                    schedule.prevLock = name
                }
            }
        }
    }
    fun setScheduledHome(id: ObjectId, name: String){
        viewModelScope.launch {
            realm.write {
                val schedule = query<ScheduleModel>("_id = $0", id).first().find()
                if(schedule != null){
                    schedule.scheduledHome = name
                }
            }
        }
    }
    fun setScheduledLock(id: ObjectId, name: String){
        viewModelScope.launch {
            realm.write {
                val schedule = query<ScheduleModel>("_id = $0", id).first().find()
                if(schedule != null){
                    schedule.scheduledLock = name
                }
            }
        }
    }
    fun setRepeat(id: ObjectId, repeat: String){
        viewModelScope.launch {
            realm.write {
                val schedule = query<ScheduleModel>("_id = $0", id).first().find()
                if(schedule != null){
                    schedule.repeat = repeat
                }
            }
        }
    }
    fun clearSchedule(id: ObjectId){
        viewModelScope.launch{
            realm.write {
                val schedule = query<ScheduleModel>("_id = $0", id).first().find()
                if(schedule != null){
                    schedule.prevHome= ""
                    schedule.prevLock= ""
                    schedule.scheduledHome= ""
                    schedule.scheduledLock= ""
                    schedule.startTime= "00:00"
                    schedule.endTime= "00:00"
                    schedule.repeat= "0000000"
                    schedule.running= ""
                }
            }
        }
    }
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