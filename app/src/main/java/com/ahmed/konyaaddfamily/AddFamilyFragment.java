package com.ahmed.konyaaddfamily;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ahmed.konyaaddfamily.database.DatabaseAccess;
import com.ahmed.konyaaddfamily.database.DatabaseSchema;
import com.ahmed.konyaaddfamily.databinding.FragmentAddFamilyBinding;

import android.content.ContentValues;

import java.util.Calendar;
import java.util.Locale;

public class AddFamilyFragment extends Fragment {

    private FragmentAddFamilyBinding binding;
    private DatabaseAccess dbAccess;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddFamilyBinding.inflate(inflater, container, false);

        // تهيئة قاعدة البيانات
        dbAccess = DatabaseAccess.getInstance(getContext());
        dbAccess.open();

        // إعداد سبينر نوع العائلة
        String[] familyTypes = {"أصلية", "مؤقتة"};
        binding.spinnerFamilyType.setAdapter(new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, familyTypes));

        // إعداد DatePickers
        setupDatePicker(binding.etBirthdate);
        setupDatePicker(binding.etWifeBirthdate);

        // زر الحفظ
        binding.btnSaveFamily.setOnClickListener(v -> saveFamily());

        return binding.getRoot();
    }

    private void setupDatePicker(final android.widget.EditText editText) {
        editText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(getContext(), (DatePicker view, int year, int month, int dayOfMonth) -> {
                String date = year + "-" + String.format(Locale.ENGLISH,"%02d", month + 1) + "-" + String.format(Locale.ENGLISH,"%02d", dayOfMonth);
                editText.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }
    private void clearFields() {
        binding.etFatherName.setText("");
        binding.etNationalId.setText("");
        binding.etBirthdate.setText("");
        binding.etPhonePrimary.setText("");
        binding.etPhoneSecondary.setText("");
        binding.etWifeName.setText("");
        binding.etWifeNationalId.setText("");
        binding.etWifeBirthdate.setText("");
        binding.spinnerFamilyType.setSelection(0);

        binding.etTotalMembers.setText("");
        binding.etOrphans.setText("");
        binding.etChronicDiseases.setText("");
        binding.etPregnant.setText("");
        binding.etBreastfeeding.setText("");
        binding.etInjured.setText("");
        binding.etMartyrs.setText("");
        binding.etPrisoners.setText("");

        binding.etChildMale05.setText("");
        binding.etChildFemale05.setText("");
        binding.etChildMale517.setText("");
        binding.etChildFemale517.setText("");
        binding.etAdultMale1750.setText("");
        binding.etAdultFemale1750.setText("");
        binding.etSeniorMale50plus.setText("");
        binding.etSeniorFemale50plus.setText("");
    }

    private void saveFamily() {
        dbAccess.open();

        boolean hasError = false;

        String fatherName = binding.etFatherName.getText().toString().trim();
        String nationalId = binding.etNationalId.getText().toString().trim();
        String birthdateStr = binding.etBirthdate.getText().toString().trim();
        String phonePrimary = binding.etPhonePrimary.getText().toString().trim();
        String maritalStatus = binding.spinnerMaritalStatus.getSelectedItem().toString();

        // 🔹 التحقق من اسم الأب
        if (fatherName.isEmpty()) {
            binding.etFatherName.setError("الرجاء إدخال اسم الأب");
            binding.etFatherName.requestFocus();
            hasError = true;
        }

        // 🔹 التحقق من رقم الهوية (9 أرقام)
        if (!nationalId.matches("\\d{9}")) {
            binding.etNationalId.setError("رقم الهوية يجب أن يكون 9 أرقام");
            if (!hasError) binding.etNationalId.requestFocus();
            hasError = true;
        }

        // 🔹 التحقق من تاريخ الميلاد
        if (birthdateStr.isEmpty()) {
            binding.etBirthdate.setError("الرجاء إدخال تاريخ الميلاد");
            if (!hasError) binding.etBirthdate.requestFocus();
            hasError = true;
        } else {
            try {
                String[] parts = birthdateStr.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);

                Calendar birthCal = Calendar.getInstance();
                birthCal.set(year, month - 1, day);
                Calendar today = Calendar.getInstance();

                if (birthCal.after(today)) {
                    binding.etBirthdate.setError("تاريخ الميلاد لا يمكن أن يكون في المستقبل");
                    if (!hasError) binding.etBirthdate.requestFocus();
                    hasError = true;
                }
            } catch (Exception e) {
                binding.etBirthdate.setError("صيغة تاريخ غير صحيحة");
                if (!hasError) binding.etBirthdate.requestFocus();
                hasError = true;
            }
        }

        // 🔹 التحقق من رقم الجوال الأساسي (يبدأ بـ 059 أو 056)
        if (!phonePrimary.matches("^(059|056)\\d{7}$")) {
            binding.etPhonePrimary.setError("رقم الجوال يجب أن يبدأ بـ 059 أو 056 ويتكون من 10 أرقام");
            if (!hasError) binding.etPhonePrimary.requestFocus();
            hasError = true;
        }

        // 🔹 عدد الأفراد يجب ألا يكون صفرًا
        int totalMembers = getInt(binding.etTotalMembers);
        if (totalMembers <= 0) {
            binding.etTotalMembers.setError("عدد الأفراد يجب أن يكون أكبر من صفر");
            if (!hasError) binding.etTotalMembers.requestFocus();
            hasError = true;
        }

        if (hasError) return;  // 🛑 لا تكمل إذا كان هناك خطأ

        // ✅ إذا لم يوجد خطأ، استمر بالحفظ
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.COL_FATHER_NAME, fatherName);
        values.put(DatabaseSchema.COL_FATHER_NATIONAL_ID, nationalId);
        values.put(DatabaseSchema.COL_FATHER_BIRTHDATE, birthdateStr);
        values.put(DatabaseSchema.COL_PHONE_PRIMARY, phonePrimary);
        values.put(DatabaseSchema.COL_PHONE_SECONDARY, binding.etPhoneSecondary.getText().toString());
        values.put(DatabaseSchema.COL_FAMILY_TYPE, binding.spinnerFamilyType.getSelectedItem().toString());
        values.put(DatabaseSchema.COL_MARITAL_STATUS, maritalStatus);

        values.put(DatabaseSchema.COL_WIFE_NAME, binding.etWifeName.getText().toString());
        values.put(DatabaseSchema.COL_WIFE_NATIONAL_ID, binding.etWifeNationalId.getText().toString());
        values.put(DatabaseSchema.COL_WIFE_BIRTHDATE, binding.etWifeBirthdate.getText().toString());

        values.put(DatabaseSchema.COL_TOTAL_MEMBERS, totalMembers);
        values.put(DatabaseSchema.COL_NUM_ORPHANS, getInt(binding.etOrphans));
        values.put(DatabaseSchema.COL_NUM_CHRONIC, getInt(binding.etChronicDiseases));
        values.put(DatabaseSchema.COL_NUM_PREGNANT, getInt(binding.etPregnant));
        values.put(DatabaseSchema.COL_NUM_BREASTFEEDING, getInt(binding.etBreastfeeding));
        values.put(DatabaseSchema.COL_NUM_INJURED, getInt(binding.etInjured));
        values.put(DatabaseSchema.COL_NUM_MARTYRS, getInt(binding.etMartyrs));
        values.put(DatabaseSchema.COL_NUM_PRISONERS, getInt(binding.etPrisoners));

        values.put(DatabaseSchema.COL_CHILD_MALE_0_5, getInt(binding.etChildMale05));
        values.put(DatabaseSchema.COL_CHILD_FEMALE_0_5, getInt(binding.etChildFemale05));
        values.put(DatabaseSchema.COL_CHILD_MALE_5_17, getInt(binding.etChildMale517));
        values.put(DatabaseSchema.COL_CHILD_FEMALE_5_17, getInt(binding.etChildFemale517));
        values.put(DatabaseSchema.COL_ADULT_MALE_17_50, getInt(binding.etAdultMale1750));
        values.put(DatabaseSchema.COL_ADULT_FEMALE_17_50, getInt(binding.etAdultFemale1750));
        values.put(DatabaseSchema.COL_SENIOR_MALE_50PLUS, getInt(binding.etSeniorMale50plus));
        values.put(DatabaseSchema.COL_SENIOR_FEMALE_50PLUS, getInt(binding.etSeniorFemale50plus));

        long inserted = dbAccess.addFamily(values);

        if (inserted != -1) {
            Toast.makeText(getContext(), "تم حفظ العائلة بنجاح ✅", Toast.LENGTH_SHORT).show();
            binding.familyFormLayout.clearFocus();
            clearFields();
            Bundle bundle = new Bundle();
            bundle.putLong("family_id", inserted);
            AddMemberFragment addMemberFragment = AddMemberFragment.newInstance((int) inserted);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, addMemberFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "حدث خطأ أثناء الحفظ ❌", Toast.LENGTH_SHORT).show();
        }
    }

    private int getInt(android.widget.EditText editText) {
        String value = editText.getText().toString().trim();
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }


}
