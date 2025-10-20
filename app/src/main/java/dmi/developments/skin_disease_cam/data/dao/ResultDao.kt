package dmi.developments.skin_disease_cam.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dmi.developments.skin_disease_cam.data.entity.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {
    @Insert
    suspend fun insert(result: Result)

    @Query("SELECT * FROM results ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Result>>
}
