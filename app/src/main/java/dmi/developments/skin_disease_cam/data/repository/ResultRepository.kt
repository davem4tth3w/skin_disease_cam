package dmi.developments.skin_disease_cam.data.repository

import dmi.developments.skin_disease_cam.data.dao.ResultDao
import dmi.developments.skin_disease_cam.data.entity.Result

class ResultRepository(private val dao: ResultDao) {
    fun getAllResults() = dao.getAll()
    suspend fun insertResult(result: Result) = dao.insert(result)
}
