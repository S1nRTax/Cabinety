package com.example.cabinetmedical.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cabinetmedical.data.local.dao.AppointmentDao;
import com.example.cabinetmedical.data.local.dao.PatientDao;
import com.example.cabinetmedical.data.local.entity.Appointment;
import com.example.cabinetmedical.data.local.entity.Patient;
import com.example.cabinetmedical.utils.Constants;

/**
 * Room database for the medical cabinet application
 */
@Database(entities = {Patient.class, Appointment.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create appointments table
            database.execSQL("CREATE TABLE IF NOT EXISTS `appointments` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`patientId` INTEGER NOT NULL, " +
                    "`appointmentTime` INTEGER, " +
                    "`purpose` TEXT, " +
                    "`status` TEXT, " +
                    "`notes` TEXT, " +
                    "FOREIGN KEY(`patientId`) REFERENCES `patients`(`id`) ON DELETE CASCADE)");
        }
    };
    private static AppDatabase instance;

    public abstract PatientDao patientDao();

    public abstract AppointmentDao appointmentDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            Constants.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }


}