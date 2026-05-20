package com.ahmed.konyaaddfamily;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmed.konyaaddfamily.adapters.MemberAdapter;
import com.ahmed.konyaaddfamily.database.DatabaseAccess;
import com.ahmed.konyaaddfamily.database.DatabaseSchema;
import com.ahmed.konyaaddfamily.databinding.DialogFilterBinding;
import com.ahmed.konyaaddfamily.databinding.FragmentMembersListBinding;
import com.ahmed.konyaaddfamily.model.MemberModel;

import java.util.ArrayList;
import java.util.List;

public class MembersListFragment extends Fragment {

    private static final String ARG_FAMILY_ID = "family_id";
    private int familyId = -1; // افتراضي = كل الأفراد
    private FragmentMembersListBinding binding;
    private DatabaseAccess dbAccess;
    private MemberAdapter adapter;
    private List<MemberModel> membersList;

    public MembersListFragment() {}

    // لإنشاء Fragment مع ID للعائلة
    public static MembersListFragment newInstance(int familyId) {
        MembersListFragment fragment = new MembersListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FAMILY_ID, familyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMembersListBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            familyId = getArguments().getInt(ARG_FAMILY_ID, -1);
        }

        dbAccess = DatabaseAccess.getInstance(requireContext());
        dbAccess.open();

        membersList = new ArrayList<>();
        Cursor cursor;

        if (familyId != -1) {
            cursor = dbAccess.getMembersByFamilyId(familyId);
            binding.tvMembersTitle.setText("👥 الأفراد المرتبطون بالعائلة");
            binding.toolbar.setVisibility(View.GONE);
        } else {
            cursor = dbAccess.getAllMembers(); // تأكد من هذه الدالة
            binding.tvMembersTitle.setText("👥 جميع الأفراد");
            binding.toolbar.setVisibility(View.VISIBLE);
        }

        // إضافة الأفراد إلى قائمة membersList
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_MEMBER_NAME));
                String nationalId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_MEMBER_NATIONAL_ID));
                String birthdate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_MEMBER_BIRTHDATE));
                String education = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_MEMBER_EDUCATION));
                boolean isSick = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_IS_SICK)) == 1;
                boolean isMartyr = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_IS_MARTYR)) == 1;
                boolean isPrisoner = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_IS_PRISONER)) == 1;
                boolean isOrphan = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_IS_ORPHAN)) == 1;

                membersList.add(new MemberModel(name, nationalId, birthdate, education, isSick, isMartyr, isPrisoner, isOrphan));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // إعداد RecyclerView
        adapter = new MemberAdapter(requireContext(), membersList);
        binding.recyclerMembers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerMembers.setAdapter(adapter);

        // إعداد البحث في EditText
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // تطبيق الفلترة عبر جميع الحقول
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }
        });

        // إعداد الفلتر المتقدم
        binding.btnAdvancedFilter.setOnClickListener(v -> showAdvancedFilterDialog());

        return binding.getRoot();
    }

    // عرض نافذة الفلتر المتقدم
    private void showAdvancedFilterDialog() {
        DialogFilterBinding dialogBinding = DialogFilterBinding.inflate(LayoutInflater.from(getContext()));

        EditText etNameFilter = dialogBinding.etNameFilter;
        EditText etIdFilter = dialogBinding.etIdFilter;
        EditText etEducationFilter = dialogBinding.etEducationFilter;
        EditText etAgeFilter = dialogBinding.etAgeFilter;
        EditText etDiseaseFilter = dialogBinding.etDiseaseFilter;

        CheckBox cbSickFilter = dialogBinding.cbSickFilter;
        CheckBox cbMartyrFilter = dialogBinding.cbMartyrFilter;
        CheckBox cbPrisonerFilter = dialogBinding.cbPrisonerFilter;
        CheckBox cbOrphanFilter = dialogBinding.cbOrphanFilter;

        // الاستماع لتغيير حالة "مريض" لإظهار/إخفاء حقل "نوع المرض"
        cbSickFilter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etDiseaseFilter.setVisibility(View.VISIBLE); // إظهار حقل "نوع المرض"
            } else {
                etDiseaseFilter.setVisibility(View.GONE); // إخفاء حقل "نوع المرض"
            }
        });

        new AlertDialog.Builder(getContext())
                .setTitle("فلتر متقدم")
                .setView(dialogBinding.getRoot())  // عرض الـ dialog باستخدام binding
                .setPositiveButton("بحث", (dialog, which) -> {
                    String nameFilter = etNameFilter.getText().toString();
                    String idFilter = etIdFilter.getText().toString();
                    String educationFilter = etEducationFilter.getText().toString();
                    String ageFilter = etAgeFilter.getText().toString();
                    String diseaseFilter = etDiseaseFilter.getText().toString();

                    applyAdvancedFilter(nameFilter, idFilter, educationFilter, ageFilter, diseaseFilter);
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    // تطبيق الفلتر المتقدم
    private void applyAdvancedFilter(String nameFilter, String idFilter,
                                     String educationFilter, String ageFilter, String diseaseFilter) {
        List<MemberModel> filteredMembers = new ArrayList<>();
        DialogFilterBinding dialogBinding = DialogFilterBinding.inflate(LayoutInflater.from(getContext()));


        CheckBox cbSickFilter = dialogBinding.cbSickFilter;
        CheckBox cbMartyrFilter = dialogBinding.cbMartyrFilter;
        CheckBox cbPrisonerFilter = dialogBinding.cbPrisonerFilter;
        CheckBox cbOrphanFilter = dialogBinding.cbOrphanFilter;

        for (MemberModel member : membersList) {
            boolean matches = true;

            // التحقق من الاسم
            if (!TextUtils.isEmpty(nameFilter) && !member.getName().toLowerCase().contains(nameFilter.toLowerCase())) {
                matches = false;
            }

            // التحقق من رقم الهوية
            if (!TextUtils.isEmpty(idFilter) && !member.getNationalId().contains(idFilter)) {
                matches = false;
            }

            // التحقق من التعليم
            if (!TextUtils.isEmpty(educationFilter) && !member.getEducation().toLowerCase().contains(educationFilter.toLowerCase())) {
                matches = false;
            }

            // التحقق من العمر
            if (!TextUtils.isEmpty(ageFilter) && !String.valueOf(member.getAge()).contains(ageFilter)) {
                matches = false;
            }

            // التحقق من كون الشخص مريض
            if (!TextUtils.isEmpty(diseaseFilter) && !member.isSick() && !diseaseFilter.isEmpty()) {
                matches = false;
            }

            // التحقق من الحالات الخاصة (مريض، شهيد، أسير، يتيم)
            if (!cbSickFilter.isChecked() && member.isSick()) {
                matches = false;
            }
            if (!cbMartyrFilter.isChecked() && member.isMartyr()) {
                matches = false;
            }
            if (!cbPrisonerFilter.isChecked() && member.isPrisoner()) {
                matches = false;
            }
            if (!cbOrphanFilter.isChecked() && member.isOrphan()) {
                matches = false;
            }

            // إضافة العنصر إذا تطابق مع الفلاتر
            if (matches) {
                filteredMembers.add(member);
            }
        }

        // تحديث البيانات في الـ adapter
        adapter = new MemberAdapter(requireContext(), filteredMembers);
        binding.recyclerMembers.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbAccess != null) dbAccess.close();
        binding = null;
    }
}
