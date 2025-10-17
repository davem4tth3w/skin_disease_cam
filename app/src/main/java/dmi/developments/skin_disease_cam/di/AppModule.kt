package dmi.developments.skin_disease_cam.di

import android.content.Context
import androidx.room.Room
import dmi.developments.skin_disease_cam.data.database.AppDatabase
import dmi.developments.skin_disease_cam.data.repository.ScanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideScanResultDao(db: AppDatabase) = db.scanResultDao()

    @Provides
    fun provideScanRepository(dao: dmi.developments.skin_disease_cam.data.dao.ScanResultDao): ScanRepository {
        return ScanRepository(dao)
    }
}
