package com.ahmed.konyaaddfamily.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseSchema.DB_NAME, null, DatabaseSchema.DB_VERSION);
    }

    // داخل DatabaseHelper.java

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAMILIES = "CREATE TABLE " + DatabaseSchema.TABLE_FAMILIES + " (" +
                DatabaseSchema.COL_FAMILY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseSchema.COL_FATHER_NAME + " TEXT NOT NULL, " +
                DatabaseSchema.COL_FATHER_NATIONAL_ID + " TEXT NOT NULL, " +
                DatabaseSchema.COL_FATHER_BIRTHDATE + " TEXT, " +
                DatabaseSchema.COL_PHONE_PRIMARY + " TEXT, " +
                DatabaseSchema.COL_PHONE_SECONDARY + " TEXT, " +
                DatabaseSchema.COL_MARITAL_STATUS + " TEXT, " +
                DatabaseSchema.COL_TOTAL_MEMBERS + " INTEGER, " +
                DatabaseSchema.COL_WIFE_NAME + " TEXT, " +
                DatabaseSchema.COL_WIFE_NATIONAL_ID + " TEXT, " +
                DatabaseSchema.COL_WIFE_BIRTHDATE + " TEXT, " +
                DatabaseSchema.COL_CHILD_MALE_0_5 + " INTEGER, " +
                DatabaseSchema.COL_CHILD_FEMALE_0_5 + " INTEGER, " +
                DatabaseSchema.COL_CHILD_MALE_5_17 + " INTEGER, " +
                DatabaseSchema.COL_CHILD_FEMALE_5_17 + " INTEGER, " +
                DatabaseSchema.COL_ADULT_MALE_17_50 + " INTEGER, " +
                DatabaseSchema.COL_ADULT_FEMALE_17_50 + " INTEGER, " +
                DatabaseSchema.COL_SENIOR_MALE_50PLUS + " INTEGER, " +
                DatabaseSchema.COL_SENIOR_FEMALE_50PLUS + " INTEGER, " +
                DatabaseSchema.COL_NUM_PREGNANT + " INTEGER, " +
                DatabaseSchema.COL_NUM_BREASTFEEDING + " INTEGER, " +
                DatabaseSchema.COL_NUM_CHRONIC + " INTEGER, " +
                DatabaseSchema.COL_NUM_INJURED + " INTEGER, " +
                DatabaseSchema.COL_NUM_MARTYRS + " INTEGER, " +
                DatabaseSchema.COL_NUM_ORPHANS + " INTEGER, " +
                DatabaseSchema.COL_NUM_PRISONERS + " INTEGER, " +
                DatabaseSchema.COL_FAMILY_TYPE + " TEXT NOT NULL, " +
                DatabaseSchema.COL_FAMILY_NOTES + " TEXT, " +
                DatabaseSchema.COL_FAMILY_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP );";

        db.execSQL(CREATE_FAMILIES);

        String CREATE_MEMBERS = "CREATE TABLE " + DatabaseSchema.TABLE_MEMBERS + " (" +
                DatabaseSchema.COL_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseSchema.COL_MEMBER_FAMILY_ID + " INTEGER, " +
                DatabaseSchema.COL_MEMBER_NAME + " TEXT NOT NULL, " +
                DatabaseSchema.COL_MEMBER_NATIONAL_ID + " TEXT, " +
                DatabaseSchema.COL_MEMBER_BIRTHDATE + " TEXT, " +
                DatabaseSchema.COL_MEMBER_EDUCATION + " TEXT, " +
                DatabaseSchema.COL_MEMBER_PHONE + " TEXT, " +
                DatabaseSchema.COL_MEMBER_GENDER + " TEXT, " +  // العمود الجديد
                DatabaseSchema.COL_IS_SICK + " INTEGER DEFAULT 0, " +
                DatabaseSchema.COL_FATHER_NAME + " TEXT NOT NULL, " +
                DatabaseSchema.COL_FATHER_NATIONAL_ID + " TEXT NOT NULL, " +
                DatabaseSchema.COL_WIFE_NAME + " TEXT, " +
                DatabaseSchema.COL_WIFE_NATIONAL_ID + " TEXT, " +
                DatabaseSchema.COL_DISEASE + " TEXT, " +
                DatabaseSchema.COL_IS_INJURED + " INTEGER DEFAULT 0, " +
                DatabaseSchema.COL_INJURED_DETAILS + " TEXT, " +
                DatabaseSchema.COL_IS_MARTYR + " INTEGER DEFAULT 0, " +
                DatabaseSchema.COL_IS_PRISONER + " INTEGER DEFAULT 0, " +
                DatabaseSchema.COL_IS_ORPHAN + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY (" + DatabaseSchema.COL_MEMBER_FAMILY_ID + ") REFERENCES " +
                DatabaseSchema.TABLE_FAMILIES + "(" + DatabaseSchema.COL_FAMILY_ID + ") ON DELETE CASCADE );";

        db.execSQL(CREATE_MEMBERS);

        String CREATE_ASSISTANCES = "CREATE TABLE " + DatabaseSchema.TABLE_ASSISTANCES + " (" +
                DatabaseSchema.COL_ASSISTANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseSchema.COL_ASSISTANCE_TYPE + " TEXT NOT NULL, " +
                DatabaseSchema.COL_ASSISTANCE_DESC + " TEXT, " +
                DatabaseSchema.COL_ASSISTANCE_AMOUNT + " REAL, " +
                DatabaseSchema.COL_ASSISTANCE_QTY + " INTEGER, " +
                DatabaseSchema.COL_ASSISTANCE_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                DatabaseSchema.COL_ASSISTANCE_PROVIDER + " TEXT );";

        db.execSQL(CREATE_ASSISTANCES);

        String CREATE_AID_FAMILY = "CREATE TABLE " + DatabaseSchema.TABLE_AID_FAMILY + " (" +
                DatabaseSchema.COL_AID_FAMILY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseSchema.COL_AID_ID + " INTEGER, " +
                DatabaseSchema.COL_AID_FAMILY_FAMILY_ID + " INTEGER, " +
                "FOREIGN KEY (" + DatabaseSchema.COL_AID_ID + ") REFERENCES " +
                DatabaseSchema.TABLE_ASSISTANCES + "(" + DatabaseSchema.COL_ASSISTANCE_ID + "), " +
                "FOREIGN KEY (" + DatabaseSchema.COL_AID_FAMILY_FAMILY_ID + ") REFERENCES " +
                DatabaseSchema.TABLE_FAMILIES + "(" + DatabaseSchema.COL_FAMILY_ID + ") );";

        db.execSQL(CREATE_AID_FAMILY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL("ALTER TABLE " + DatabaseSchema.TABLE_MEMBERS + " ADD COLUMN " + DatabaseSchema.COL_MEMBER_GENDER + " TEXT");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // أضف ترقيات لاحقة هنا حسب الحاجة
    }

}
