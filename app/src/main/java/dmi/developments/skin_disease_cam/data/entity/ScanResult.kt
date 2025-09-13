package dmi.developments.skin_disease_cam.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val skindisease: String? = null,
    val remedies: String? = null,
    val timestamp: String
)
