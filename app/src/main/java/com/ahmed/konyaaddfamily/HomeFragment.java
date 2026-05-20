package com.ahmed.konyaaddfamily;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ahmed.konyaaddfamily.database.DatabaseAccess;
import com.ahmed.konyaaddfamily.database.DatabaseSchema;
import com.ahmed.konyaaddfamily.databinding.FragmentHomeBinding;
import com.ahmed.konyaaddfamily.model.MemberModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseAccess dbAccess;

    public HomeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // افتح قاعدة البيانات
        dbAccess = DatabaseAccess.getInstance(getContext());
        dbAccess.open();
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        // عرض عدد العائلات
        Cursor familiesCursor = dbAccess.getAllFamilies();
        int familyCount = (familiesCursor != null) ? familiesCursor.getCount() : 0;
        binding.tvFamilyCount.setText(String.valueOf(familyCount));

        // عرض عدد الأفراد
        Cursor membersCursor = dbAccess.getAllMembers();  // ✅ تأكد أن هذه الدالة موجودة في DatabaseAccess
        int memberCount = (membersCursor != null) ? membersCursor.getCount() : 0;
        binding.tvMemberCount.setText(String.valueOf(memberCount));

        binding.btnAddFamily.setOnClickListener(v -> {
            // ✅ غيّر العنصر النشط في الـ bottom nav
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
            bottomNav.setSelectedItemId(R.id.nav_add); // تأكد من ID الصحيح في menu XML

            // ✅ افتح شاشة إضافة العائلة
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new AddFamilyFragment())
                    .addToBackStack(null)
                    .commit();
        });





// عند الضغط على بطاقة عدد العائلات
        binding.cardFamilies.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new FamiliesListFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // زر: عرض العائلات
        binding.btnViewFamilies.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new FamiliesListFragment())
                    .addToBackStack(null)
                    .commit();
        });
        binding.cardMembers.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new MembersListFragment())  // بدون familyId = عرض الكل
                    .addToBackStack(null)
                    .commit();
        });
        // (لاحقًا) زر: الإحصائيات
        binding.btnExport.setOnClickListener(v -> {
            exportToExcel();
        });

        return binding.getRoot();
    }



    @SuppressLint("Range")
    private void exportToExcel() {
        Workbook workbook = new XSSFWorkbook();

        // إنشاء ورقة للعائلة
        Sheet familySheet = workbook.createSheet("Family Details");

        // إضافة رأس العمود للعائلة
        Row familyHeaderRow = familySheet.createRow(0);
        familyHeaderRow.createCell(0).setCellValue("اسم العائلة");
        familyHeaderRow.createCell(1).setCellValue("اسم الأب");
        familyHeaderRow.createCell(2).setCellValue("رقم هوية الأب");
        familyHeaderRow.createCell(3).setCellValue("تاريخ ميلاد الأب");
        familyHeaderRow.createCell(4).setCellValue("رقم الجوال الأساسي");
        familyHeaderRow.createCell(5).setCellValue("رقم الجوال البديل");
        familyHeaderRow.createCell(6).setCellValue("عدد الأفراد");
        familyHeaderRow.createCell(7).setCellValue("اسم الزوجة");
        familyHeaderRow.createCell(8).setCellValue("رقم هوية الزوجة");
        familyHeaderRow.createCell(9).setCellValue("تاريخ ميلاد الزوجة");
        familyHeaderRow.createCell(10).setCellValue("نوع العائلة");
        familyHeaderRow.createCell(11).setCellValue("عدد الشهداء");
        familyHeaderRow.createCell(12).setCellValue("عدد الجرحى");
        familyHeaderRow.createCell(13).setCellValue("عدد الأسرى");
        familyHeaderRow.createCell(14).setCellValue("عدد الحوامل");
        familyHeaderRow.createCell(15).setCellValue("عدد المرضعات");
        familyHeaderRow.createCell(16).setCellValue("عدد المرضى المزمنين");
        familyHeaderRow.createCell(17).setCellValue("عدد الأطفال 0-5 الذكور");
        familyHeaderRow.createCell(18).setCellValue("عدد الأطفال 0-5 الإناث");
        familyHeaderRow.createCell(19).setCellValue("عدد الأطفال 5-17 الذكور");
        familyHeaderRow.createCell(20).setCellValue("عدد الأطفال 5-17 الإناث");
        familyHeaderRow.createCell(21).setCellValue("عدد البالغين 17- 50 الذكور");
        familyHeaderRow.createCell(22).setCellValue("عدد البالغين 17-50 الإناث");
        familyHeaderRow.createCell(23).setCellValue("عدد كبار السن 50+ الذكور");
        familyHeaderRow.createCell(24).setCellValue("عدد كبار السن 50+ الإناث");
        familyHeaderRow.createCell(25).setCellValue("الحالة الإجتماعية");

        // إضافة تفاصيل العائلة إلى ورقة "العائلة"

        Cursor familiesCursor = dbAccess.getAllFamilies(); // تعديل دالة استرجاع العائلات
        int rowNumFamily = 1;
        while (familiesCursor != null && familiesCursor.moveToNext()) {
            Row familyRow = familySheet.createRow(rowNumFamily++);
            familyRow.createCell(0).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_FATHER_NAME)));
            familyRow.createCell(1).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_FATHER_NAME)));
            familyRow.createCell(2).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_FATHER_NATIONAL_ID)));
            familyRow.createCell(3).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_FATHER_BIRTHDATE)));
            familyRow.createCell(4).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_PHONE_PRIMARY)));
            familyRow.createCell(5).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_PHONE_SECONDARY)));
            familyRow.createCell(6).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_TOTAL_MEMBERS)));
            familyRow.createCell(7).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_WIFE_NAME)));
            familyRow.createCell(8).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_WIFE_NATIONAL_ID)));
            familyRow.createCell(9).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_WIFE_BIRTHDATE)));
            familyRow.createCell(10).setCellValue(familiesCursor.getString(familiesCursor.getColumnIndex(DatabaseSchema.COL_FAMILY_TYPE)));
            familyRow.createCell(11).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_NUM_MARTYRS)));
            familyRow.createCell(12).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_NUM_INJURED)));
            familyRow.createCell(13).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_NUM_PRISONERS)));
            familyRow.createCell(14).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_NUM_PREGNANT)));
            familyRow.createCell(15).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_NUM_BREASTFEEDING)));
            familyRow.createCell(16).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_NUM_CHRONIC)));
            familyRow.createCell(17).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_CHILD_MALE_0_5)));
            familyRow.createCell(18).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_CHILD_FEMALE_0_5)));
            familyRow.createCell(19).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_CHILD_MALE_5_17)));
            familyRow.createCell(20).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_CHILD_FEMALE_5_17)));
            familyRow.createCell(21).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_ADULT_MALE_17_50)));
            familyRow.createCell(22).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_ADULT_FEMALE_17_50)));
            familyRow.createCell(23).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_SENIOR_MALE_50PLUS)));
            familyRow.createCell(24).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_SENIOR_FEMALE_50PLUS)));
            familyRow.createCell(25).setCellValue(familiesCursor.getInt(familiesCursor.getColumnIndex(DatabaseSchema.COL_MARITAL_STATUS)));
        }

        // إنشاء ورقة للأفراد
        Sheet membersSheet = workbook.createSheet("Members Details");

        // إضافة رأس العمود للأفراد
        Row membersHeaderRow = membersSheet.createRow(0);
        membersHeaderRow.createCell(0).setCellValue("اسم العضو");
        membersHeaderRow.createCell(1).setCellValue("رقم الهوية");
        membersHeaderRow.createCell(2).setCellValue("اسم الأب");
        membersHeaderRow.createCell(3).setCellValue("رقم هوية الأب");
        membersHeaderRow.createCell(4).setCellValue("اسم الأم");
        membersHeaderRow.createCell(5).setCellValue("رقم هوية الأم");
        membersHeaderRow.createCell(6).setCellValue("تاريخ الميلاد");
        membersHeaderRow.createCell(7).setCellValue("التعليم");
        membersHeaderRow.createCell(8).setCellValue("حالة المرض");
        membersHeaderRow.createCell(9).setCellValue("تفاصيل المرض");
        membersHeaderRow.createCell(10).setCellValue("شهيد");
        membersHeaderRow.createCell(11).setCellValue("أسير");
        membersHeaderRow.createCell(12).setCellValue("يتيم");
        membersHeaderRow.createCell(13).setCellValue("الجنس");
        membersHeaderRow.createCell(14).setCellValue("مصاب");
        membersHeaderRow.createCell(15).setCellValue("تفاصيل الاصابة");


        // إضافة الأفراد إلى ورقة "الأفراد"
        Cursor membersCursor = dbAccess.getAllMembers();
        int rowNumMembers = 1;
        while (membersCursor != null && membersCursor.moveToNext()) {
            Row row = membersSheet.createRow(rowNumMembers++);
            row.createCell(0).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_MEMBER_NAME)));
            row.createCell(1).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_MEMBER_NATIONAL_ID)));
            row.createCell(2).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_FATHER_NAME)));  // اسم الأب
            row.createCell(3).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_FATHER_NATIONAL_ID))); // رقم هوية الأب
            row.createCell(4).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_WIFE_NAME))); // اسم الأم
            row.createCell(5).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_WIFE_NATIONAL_ID))); // رقم هوية الأم
            row.createCell(6).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_MEMBER_BIRTHDATE)));
            row.createCell(7).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_MEMBER_EDUCATION)));
            row.createCell(8).setCellValue(membersCursor.getInt(membersCursor.getColumnIndex(DatabaseSchema.COL_IS_SICK)) == 1 ? "مريض" : "لا");
            row.createCell(9).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_DISEASE)));  // تفاصيل المرض
            row.createCell(10).setCellValue(membersCursor.getInt(membersCursor.getColumnIndex(DatabaseSchema.COL_IS_MARTYR)) == 1 ? "شهيد" : "لا");
            row.createCell(11).setCellValue(membersCursor.getInt(membersCursor.getColumnIndex(DatabaseSchema.COL_IS_PRISONER)) == 1 ? "أسير" : "لا");
            row.createCell(12).setCellValue(membersCursor.getInt(membersCursor.getColumnIndex(DatabaseSchema.COL_IS_ORPHAN)) == 1 ? "يتيم" : "لا");
            row.createCell(13).setCellValue(membersCursor.getString(membersCursor.getColumnIndexOrThrow(DatabaseSchema.COL_MEMBER_GENDER)));
            row.createCell(14).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_IS_INJURED)));
            row.createCell(15).setCellValue(membersCursor.getString(membersCursor.getColumnIndex(DatabaseSchema.COL_INJURED_DETAILS)));

        }

//        File file1 = new File( Environment.getExternalStorageDirectory()+ File.separator+folderName);
//        file1.mkdirs();


        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "تحديث العائلات/FamilyAndMembersDetails.xlsx");

        // تأكد من أن المجلد موجود، وإذا لم يكن، أنشئه
        File directory = file.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            Toast.makeText(getContext(), "تم تصدير البيانات إلى Excel بنجاح", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "حدث خطأ أثناء التصدير", Toast.LENGTH_SHORT).show();
        }
    }

}
