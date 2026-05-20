package com.ahmed.konyaaddfamily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.ahmed.konyaaddfamily.databinding.FragmentReportBinding;

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;

    public ReportFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        binding.textReport.setText("تقارير التطبيق");

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
