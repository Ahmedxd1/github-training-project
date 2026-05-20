package com.ahmed.konyaaddfamily.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccess {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context.getApplicationContext());
        } else if (instance.db == null || !instance.db.isOpen()) {
            instance.open();  // ← أعد الفتح إذا كانت مغلقة
        }
        return instance;
    }
    public Cursor getFamilyDataByFamilyId(int familyId) {
        // استعلام لاسترجاع بيانات الأب والأم بناءً على معرف العائلة
        String query = "SELECT * FROM " + DatabaseSchema.TABLE_FAMILIES + " f " +
                "WHERE " + DatabaseSchema.COL_FAMILY_ID + " = ?";  // استخدم علامة الاستفهام بدلاً من إضافة القيمة مباشرة

        // تنفيذ الاستعلام مع المعامل (familyId) وإرجاع الكيرسور
        return db.rawQuery(query, new String[]{String.valueOf(familyId)});
    }



    public void open() {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
    }


    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
    public Cursor getAllMembers() {
        return db.query(DatabaseSchema.TABLE_MEMBERS, null, null, null, null, null, null);
    }
    // ✅ إضافة عائلة
    public long addFamily(ContentValues values) {
        return db.insert(DatabaseSchema.TABLE_FAMILIES, null, values);
    }

    // ✅ إضافة فرد
    public long addMember(ContentValues values) {
        return db.insert(DatabaseSchema.TABLE_MEMBERS, null, values);
    }

    // ✅ جلب جميع العائلات
    public Cursor getAllFamilies() {
        return db.query(DatabaseSchema.TABLE_FAMILIES, null, null, null, null, null, DatabaseSchema.COL_FAMILY_ID + " DESC");
    }

    // ✅ جلب أفراد عائلة معينة
    public Cursor getMembersByFamilyId(int familyId) {
        return db.query(
                DatabaseSchema.TABLE_MEMBERS,
                null,
                DatabaseSchema.COL_MEMBER_FAMILY_ID + "=?",
                new String[]{String.valueOf(familyId)},
                null, null, null
        );
    }
}
