package dmi.developments.skin_disease_cam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmi.developments.skin_disease_cam.data.entity.ScanResult
import dmi.developments.skin_disease_cam.data.repository.ScanRepository
import kotlinx.coroutines.launch

class ScanViewModel(private val repository: ScanRepository) : ViewModel() {
    val scans = repository.getAllScans()

    fun addScan(scan: ScanResult) {
        viewModelScope.launch {
            repository.insertScan(scan)
        }
    }
}
