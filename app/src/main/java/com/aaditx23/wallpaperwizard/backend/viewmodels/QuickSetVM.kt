package com.aaditx23.wallpaperwizard.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.wallpaperwizard.backend.models.QuickSetModel
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
    fun addQuickSet(homeScreenPath: String?, lockScreenPath: String?) {
        viewModelScope.launch {
            realm.write {
                val maxId = query<QuickSetModel>().max("id", Int::class).find() ?: 0
                copyToRealm(
                    QuickSetModel().apply {
                        id = maxId + 1
                        homeScreen = homeScreenPath
                        lockScreen = lockScreenPath
                    }
                )
            }
        }
    }

    // Update an existing QuickSetModel item by id
    fun updateQuickSet(id: Int, homeScreenPath: String?, lockScreenPath: String?) {
        viewModelScope.launch {
            realm.write {
                val quickSet = query<QuickSetModel>("id == $0", id).first().find()
                quickSet?.apply {
                    this.homeScreen = homeScreenPath
                    this.lockScreen = lockScreenPath
                }
            }
        }
    }

    // Delete a QuickSetModel item by id
    fun deleteQuickSet(id: Int) {
        viewModelScope.launch {
            realm.write {
                val quickSet = query<QuickSetModel>("id == $0", id).first().find()
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