package dmi.developments.skin_disease_cam.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dmi.developments.skin_disease_cam.data.dao.ScanResultDao
import dmi.developments.skin_disease_cam.data.entity.ScanResult

@Database(entities = [ScanResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanResultDao(): ScanResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}
