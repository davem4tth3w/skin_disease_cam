package dmi.developments.skin_disease_cam.data.repository

import dmi.developments.skin_disease_cam.data.dao.ScanResultDao
import dmi.developments.skin_disease_cam.data.entity.ScanResult

class ScanRepository(private val dao: ScanResultDao) {
    fun getAllScans() = dao.getAll()
    suspend fun insertScan(scan: ScanResult) = dao.insert(scan)
}
