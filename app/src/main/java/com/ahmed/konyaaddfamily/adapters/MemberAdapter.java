package com.ahmed.konyaaddfamily.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.konyaaddfamily.R;
import com.ahmed.konyaaddfamily.model.MemberModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private Context context;
    private List<MemberModel> members;
    private List<MemberModel> membersFiltered;

    public MemberAdapter(Context context, List<MemberModel> members) {
        this.context = context;
        this.members = members;
        this.membersFiltered = new ArrayList<>(members); // يتم تخزين نسخة من البيانات الأصلية للفلترة
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        MemberModel member = membersFiltered.get(position);

        holder.tvName.setText("👤 " + member.getName());
        holder.tvBirthdate.setText("🎂 " + member.getBirthdate());
        holder.tvEducation.setText("🎓 " + member.getEducation());

        // حساب العمر بناءً على تاريخ الميلاد
        String birthdate = member.getBirthdate();
        if (!TextUtils.isEmpty(birthdate)) {
            int age = calculateAge(birthdate);
            holder.tvAge.setText("🧑‍🦳 العمر: " + age + " سنة");
        }

        // حالات خاصة
        StringBuilder status = new StringBuilder();
        if (member.isSick()) status.append("🩺 مريض | ");
        if (member.isMartyr()) status.append("💔 شهيد | ");
        if (member.isPrisoner()) status.append("🎗️ أسير | ");
        if (member.isOrphan()) status.append("👶 يتيم | ");

        if (status.length() > 0) {
            status.setLength(status.length() - 2); // حذف "| " الزائد
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(status.toString());
        } else {
            holder.tvStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return membersFiltered.size();
    }

    // حساب العمر بناءً على تاريخ الميلاد
    private int calculateAge(String birthdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(sdf.parse(birthdate));
            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

            // تأكد من أن عيد الميلاد قد مر هذا العام أم لا
            if (today.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                    (today.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))) {
                age--;
            }

            return age;

        } catch (Exception e) {
            e.printStackTrace();
            return 0; // إذا حدث خطأ في التحويل
        }
    }

    // دالة فلترة
    public void filter(String query) {
        membersFiltered.clear();
        if (query.isEmpty()) {
            membersFiltered.addAll(members);
        } else {
            for (MemberModel member : members) {
                if (member.getName().toLowerCase().contains(query.toLowerCase()) ||
                        member.getEducation().toLowerCase().contains(query.toLowerCase()) ||
                        member.getAge().contains(query)) {  // يمكنك إضافة المزيد من الفلاتر هنا
                    membersFiltered.add(member);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBirthdate, tvEducation, tvStatus, tvAge;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMemberName);
            tvBirthdate = itemView.findViewById(R.id.tvMemberBirthdate);
            tvEducation = itemView.findViewById(R.id.tvMemberEducation);
            tvStatus = itemView.findViewById(R.id.tvMemberStatus);
            tvAge = itemView.findViewById(R.id.tvMemberAge); // أضف هذا العنصر إلى item_member.xml
        }
    }
}
