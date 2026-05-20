package com.ahmed.konyaaddfamily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.ahmed.konyaaddfamily.databinding.FragmentAssistanceBinding;

public class AssistanceFragment extends Fragment {

    private FragmentAssistanceBinding binding;

    public AssistanceFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAssistanceBinding.inflate(inflater, container, false);
        binding.textAssist.setText("مساعدات");

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
