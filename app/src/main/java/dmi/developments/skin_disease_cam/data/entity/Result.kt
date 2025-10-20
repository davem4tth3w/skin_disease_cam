package dmi.developments.skin_disease_cam.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val skindisease: String? = null,
    val remedies: String? = null,
    val timestamp: String
)
