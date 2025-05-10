package com.example.cabinetmedical.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cabinetmedical.data.local.dao.PatientDao;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.utils.Constants;

/**
 * Room database for the medical cabinet application
 */
@Database(entities = {Patient.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase instance;
    
    /**
     * Get the PatientDao interface
     * @return PatientDao
     */
    public abstract PatientDao patientDao();
    
    /**
     * Get a singleton instance of the AppDatabase
     * @param context application context
     * @return AppDatabase instance
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    Constants.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}