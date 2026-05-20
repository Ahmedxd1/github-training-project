package com.ahmed.konyaaddfamily.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import com.ahmed.konyaaddfamily.R;
import com.ahmed.konyaaddfamily.model.FamilyModel;

import java.util.List;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {

    public interface OnFamilyClickListener {
        void onViewMembersClick(int familyId);
    }

    private Context context;
    private List<FamilyModel> families;
    private OnFamilyClickListener listener;

    public FamilyAdapter(Context context, List<FamilyModel> families, OnFamilyClickListener listener) {
        this.context = context;
        this.families = families;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family, parent, false);
        return new FamilyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyViewHolder holder, int position) {
        FamilyModel family = families.get(position);
        holder.name.setText(family.getFatherName());
        holder.type.setText(family.getFamilyType());

        holder.btnViewMembers.setOnClickListener(v -> {
            if (listener != null) listener.onViewMembersClick(family.getId());
        });
    }

    @Override
    public int getItemCount() {
        return families.size();
    }

    static class FamilyViewHolder extends RecyclerView.ViewHolder {
        TextView name, type;
        Button btnViewMembers;

        public FamilyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvFatherName);
            type = itemView.findViewById(R.id.tvFamilyType);
            btnViewMembers = itemView.findViewById(R.id.btnViewMembers);
        }
    }
}
