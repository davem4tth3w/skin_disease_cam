package dmi.developments.skin_disease_cam.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dmi.developments.skin_disease_cam.data.dao.ResultDao
import dmi.developments.skin_disease_cam.data.entity.Result

@Database(entities = [Result::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ResultDao(): ResultDao

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
