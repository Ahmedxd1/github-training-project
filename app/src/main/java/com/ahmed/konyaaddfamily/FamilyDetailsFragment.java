package com.ahmed.konyaaddfamily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ahmed.konyaaddfamily.database.DatabaseAccess;
import com.ahmed.konyaaddfamily.database.DatabaseSchema;

import android.database.Cursor;

public class FamilyDetailsFragment extends Fragment {

    private int familyId;

    private TextView tvFatherName, tvNationalId, tvTotalMembers, tvFamilyType;
    private Button btnShowMembers;

    public FamilyDetailsFragment() {}

    public static FamilyDetailsFragment newInstance(int familyId) {
        FamilyDetailsFragment fragment = new FamilyDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("family_id", familyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family_details, container, false);

        tvFatherName = view.findViewById(R.id.tvFatherName);
        tvNationalId = view.findViewById(R.id.tvNationalId);
        tvTotalMembers = view.findViewById(R.id.tvTotalMembers);
        tvFamilyType = view.findViewById(R.id.tvFamilyType);
        btnShowMembers = view.findViewById(R.id.btnShowMembers);

        if (getArguments() != null) {
            familyId = getArguments().getInt("family_id", -1);
        }

        loadFamilyData(familyId);

        btnShowMembers.setOnClickListener(v -> {
            MembersListFragment membersListFragment = MembersListFragment.newInstance(familyId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, membersListFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadFamilyData(int familyId) {
        DatabaseAccess db = DatabaseAccess.getInstance(requireContext());
        db.open();

        Cursor cursor = db.getAllFamilies(); // يمكنك تعديل هذا لتصفية بـ familyId إذا كان هناك دالة خاصة لذلك

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_FAMILY_ID));
            if (id == familyId) {
                tvFatherName.setText("رب الأسرة: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_FATHER_NAME)));
                tvNationalId.setText("الهوية: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_FATHER_NATIONAL_ID)));
                tvTotalMembers.setText("عدد الأفراد: " + cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_TOTAL_MEMBERS)));
                tvFamilyType.setText("النوع: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.COL_FAMILY_TYPE)));
                break;
            }
        }

        cursor.close();
        db.close();
    }
}
