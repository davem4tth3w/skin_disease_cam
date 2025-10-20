package dmi.developments.skin_disease_cam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmi.developments.skin_disease_cam.data.entity.Result
import dmi.developments.skin_disease_cam.data.repository.ResultRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: ResultRepository
) : ViewModel() {

    val results = repository.getAllResults()

    fun addResult(result: Result) {
        viewModelScope.launch {
            repository.insertResult(result)
        }
    }
}
