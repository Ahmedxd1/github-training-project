package com.ahmed.konyaaddfamily;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ahmed.konyaaddfamily.database.DatabaseAccess;
import com.ahmed.konyaaddfamily.database.DatabaseSchema;
import com.ahmed.konyaaddfamily.databinding.FragmentAddMemberBinding;

import android.content.ContentValues;

import java.util.Calendar;
import java.util.Locale;

public class AddMemberFragment extends Fragment {

    private FragmentAddMemberBinding binding;
    private DatabaseAccess dbAccess;
    private int familyId; // سيتم تمريره من AddFamilyFragment



    public AddMemberFragment() {
    }

    public static AddMemberFragment newInstance(int familyId) {
        AddMemberFragment fragment = new AddMemberFragment();
        Bundle args = new Bundle();
        args.putInt("family_id", familyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddMemberBinding.inflate(inflater, container, false);

        // اقرأ family_id من الـ arguments بشكل آمن
        if (getArguments() != null) {
            familyId = getArguments().getInt("family_id", -1);
        }

        dbAccess = DatabaseAccess.getInstance(getContext());
        dbAccess.open();  // افتح الاتصال هنا مرة واحدة

        setupDatePicker(binding.etMemberBirthdate);

        binding.btnSaveMember.setOnClickListener(v -> saveMember());

        return binding.getRoot();
    }

    private void setupDatePicker(final android.widget.EditText editText) {
        editText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(requireContext(), (DatePicker view, int year, int month, int dayOfMonth) -> {
                String date = year + "-" + String.format(Locale.ENGLISH,"%02d", month + 1) + "-" + String.format(Locale.ENGLISH,"%02d", dayOfMonth);
                editText.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }

    private void saveMember() {
        dbAccess.open();

        // 🔹 قراءة الحقول
        String name = binding.etMemberName.getText().toString().trim();
        String birthdateStr = binding.etMemberBirthdate.getText().toString().trim();
        String nationalId = binding.etMemberNationalId.getText().toString().trim();
        String phone = binding.etMemberPhone.getText().toString().trim();
        String education = binding.etMemberEducation.getText().toString().trim();
        String gender = binding.radioMale.isChecked() ? "ذكر" : "أنثى";

        boolean hasError = false;

        // 🔸 الاسم
        if (name.isEmpty()) {
            binding.etMemberName.setError("الرجاء إدخال اسم الفرد");
            binding.etMemberName.requestFocus();
            hasError = true;
        }

        // 🔸 التعليم
        if (education.isEmpty()) {
            binding.etMemberEducation.setError("الرجاء إدخال المستوى التعليمي");
            if (!hasError) binding.etMemberEducation.requestFocus();
            hasError = true;
        }

        // 🔸 الهوية
        if (!nationalId.matches("\\d{9}")) {
            binding.etMemberNationalId.setError("رقم الهوية يجب أن يكون 9 أرقام");
            if (!hasError) binding.etMemberNationalId.requestFocus();
            hasError = true;
        }

        // 🔸 رقم الهاتف
        if (!phone.matches("^(059|056)\\d{7}$")) {
            binding.etMemberPhone.setError("رقم الجوال يجب أن يبدأ بـ 059 أو 056 ويحتوي على 10 أرقام");
            if (!hasError) binding.etMemberPhone.requestFocus();
            hasError = true;
        }

        // 🔸 تاريخ الميلاد
        if (birthdateStr.isEmpty()) {
            binding.etMemberBirthdate.setError("الرجاء إدخال تاريخ الميلاد");
            if (!hasError) binding.etMemberBirthdate.requestFocus();
            hasError = true;
        } else {
            try {
                String[] parts = birthdateStr.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);

                Calendar birthCalendar = Calendar.getInstance();
                birthCalendar.set(year, month - 1, day);
                Calendar today = Calendar.getInstance();

                if (birthCalendar.after(today)) {
                    binding.etMemberBirthdate.setError("تاريخ الميلاد لا يمكن أن يكون في المستقبل");
                    if (!hasError) binding.etMemberBirthdate.requestFocus();
                    hasError = true;
                }
            } catch (Exception e) {
                binding.etMemberBirthdate.setError("صيغة التاريخ غير صحيحة");
                if (!hasError) binding.etMemberBirthdate.requestFocus();
                hasError = true;
            }
        }

        // إذا كان هناك خطأ، لا تحفظ
        if (hasError) return;
//        Cursor familiesCursor = dbAccess.getFamilyDataByFamilyId(familyId); // تعديل دالة استرجاع العائلات

        // ✅ لا توجد أخطاء، احفظ
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.COL_MEMBER_FAMILY_ID, familyId);
        values.put(DatabaseSchema.COL_MEMBER_NAME, name);
        values.put(DatabaseSchema.COL_MEMBER_NATIONAL_ID, nationalId);
        values.put(DatabaseSchema.COL_MEMBER_BIRTHDATE, birthdateStr);
        values.put(DatabaseSchema.COL_MEMBER_EDUCATION, education);
        values.put(DatabaseSchema.COL_MEMBER_PHONE, phone);
        Cursor familiesCursor = dbAccess.getFamilyDataByFamilyId(familyId);

        if (familiesCursor != null && familiesCursor.moveToFirst()) {
            values.put(DatabaseSchema.COL_FATHER_NAME,
                    familiesCursor.getString(familiesCursor.getColumnIndexOrThrow(DatabaseSchema.COL_FATHER_NAME)));

            values.put(DatabaseSchema.COL_FATHER_NATIONAL_ID,
                    familiesCursor.getString(familiesCursor.getColumnIndexOrThrow(DatabaseSchema.COL_FATHER_NATIONAL_ID)));

            values.put(DatabaseSchema.COL_WIFE_NAME,
                    familiesCursor.getString(familiesCursor.getColumnIndexOrThrow(DatabaseSchema.COL_WIFE_NAME)));

            values.put(DatabaseSchema.COL_WIFE_NATIONAL_ID,
                    familiesCursor.getString(familiesCursor.getColumnIndexOrThrow(DatabaseSchema.COL_WIFE_NATIONAL_ID)));
        }
        values.put(DatabaseSchema.COL_MEMBER_GENDER, gender);

        values.put(DatabaseSchema.COL_IS_SICK, binding.checkIsSick.isChecked() ? 1 : 0);
        values.put(DatabaseSchema.COL_DISEASE, binding.etDisease.getText().toString().trim());
        values.put(DatabaseSchema.COL_IS_INJURED, binding.checkIsInjured.isChecked() ? 1 : 0);
        values.put(DatabaseSchema.COL_INJURED_DETAILS, binding.etInjured.getText().toString().trim());
        values.put(DatabaseSchema.COL_IS_MARTYR, binding.checkIsMartyr.isChecked() ? 1 : 0);
        values.put(DatabaseSchema.COL_IS_PRISONER, binding.checkIsPrisoner.isChecked() ? 1 : 0);
        values.put(DatabaseSchema.COL_IS_ORPHAN, binding.checkIsOrphan.isChecked() ? 1 : 0);

        long result = dbAccess.addMember(values);

        if (result != -1) {
            Toast.makeText(getContext(), "تم حفظ بيانات الفرد ✅", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(getContext(), "فشل الحفظ ❌", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        binding.etMemberName.setText("");
        binding.etMemberNationalId.setText("");
        binding.etMemberBirthdate.setText("");
        binding.etMemberEducation.setText("");
        binding.etMemberPhone.setText("");
        binding.checkIsSick.setChecked(false);
        binding.etDisease.setText("");
        binding.checkIsInjured.setChecked(false);
        binding.checkIsMartyr.setChecked(false);
        binding.checkIsPrisoner.setChecked(false);
        binding.checkIsOrphan.setChecked(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbAccess.close();
        binding = null;
    }
}
