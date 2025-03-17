package com.aaditx23.wallpaperwizard.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.wallpaperwizard.backend.models.QuickSetModel
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
class QuickSetVM @Inject constructor(
    private val realm: Realm
) : ViewModel() {

    private val _allQuickSets = MutableStateFlow<List<QuickSetModel>>(emptyList())
    val allQuickSets: StateFlow<List<QuickSetModel>> = _allQuickSets.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadQuickSets()
    }

    private fun loadQuickSets() {
        viewModelScope.launch {
            realm.query<QuickSetModel>()
                .asFlow()
                .map { results ->
                    results.list.toList()
                }
                .collect { quickSets ->
                    _allQuickSets.value = quickSets
                    _isLoading.value = false // Data has loaded, stop showing loading
                }
        }
    }


    // Add a new QuickSetModel item with auto-incremented id
    fun create() {
        viewModelScope.launch {
            realm.write {
                copyToRealm(QuickSetModel(), updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    // Delete a QuickSetModel item by id
    fun deleteQuickSet(id: ObjectId) {
        viewModelScope.launch {
            realm.write {
                val quickSet = query<QuickSetModel>("_id == $0", id).first().find()
                if (quickSet != null) {
                    delete(quickSet)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }
}