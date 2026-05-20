package com.ahmed.konyaaddfamily;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmed.konyaaddfamily.adapters.FamilyAdapter;
import com.ahmed.konyaaddfamily.database.DatabaseAccess;
import com.ahmed.konyaaddfamily.databinding.FragmentFamiliesListBinding;
import com.ahmed.konyaaddfamily.model.FamilyModel;

import java.util.ArrayList;
import java.util.List;

public class FamiliesListFragment extends Fragment {

    private FragmentFamiliesListBinding binding;
    private DatabaseAccess dbAccess;

    public FamiliesListFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFamiliesListBinding.inflate(inflater, container, false);
        dbAccess = DatabaseAccess.getInstance(requireContext());
        dbAccess.open();

        List<FamilyModel> families = new ArrayList<>();
        Cursor cursor = dbAccess.getAllFamilies();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("father_name"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("family_type"));
                families.add(new FamilyModel(id, name, type));
            } while (cursor.moveToNext());
        }

        FamilyAdapter adapter = new FamilyAdapter(requireContext(), families, familyId -> {
            // عند الضغط على "عرض الأفراد"
            MembersListFragment fragment = MembersListFragment.newInstance(familyId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.recyclerFamilies.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerFamilies.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbAccess.close();
        binding = null;
    }
}
