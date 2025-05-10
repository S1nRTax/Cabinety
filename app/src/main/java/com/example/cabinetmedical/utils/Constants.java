package com.example.cabinetmedical.utils;

/**
 * Constants used throughout the application
 */
public class Constants {
    
    // Database constants
    public static final String DATABASE_NAME = "medical_cabinet_db";
    
    // Shared Preferences
    public static final String PREFS_NAME = "medical_cabinet_prefs";
    
    // Intent keys
    public static final String KEY_PATIENT_ID = "patient_id";
    
    // Bundle keys
    public static final String BUNDLE_PATIENT = "bundle_patient";
    
    // Request codes
    public static final int REQUEST_CODE_ADD_PATIENT = 100;
    public static final int REQUEST_CODE_EDIT_PATIENT = 101;
    
    // Navigation
    public static final String DEEP_LINK_PATIENT_DETAILS = "cabinetmedical://patient/";
    
    // App constants
    public static final int MIN_SEARCH_QUERY_LENGTH = 2;
}