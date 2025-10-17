package dmi.developments.skin_disease_cam.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dmi.developments.skin_disease_cam.data.entity.ScanResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanResultDao {
    @Insert
    suspend fun insert(scanResult: ScanResult)

    @Query("SELECT * FROM scan_results ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ScanResult>>
}
