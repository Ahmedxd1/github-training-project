package com.ahmed.konyaaddfamily.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MemberModel {

    private String name;
    private String birthdate; // تاريخ الميلاد
    private String nationalId; // تاريخ الميلاد
    private String education; // التعليم
    private boolean isSick;
    private boolean isMartyr;
    private boolean isPrisoner;
    private boolean isOrphan;

    // الكونستركتور


    public MemberModel(String name, String birthdate, String nationalId, String education, boolean isSick, boolean isMartyr, boolean isPrisoner, boolean isOrphan) {
        this.name = name;
        this.birthdate = birthdate;
        this.nationalId = nationalId;
        this.education = education;
        this.isSick = isSick;
        this.isMartyr = isMartyr;
        this.isPrisoner = isPrisoner;
        this.isOrphan = isOrphan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setSick(boolean sick) {
        isSick = sick;
    }

    public void setMartyr(boolean martyr) {
        isMartyr = martyr;
    }

    public void setPrisoner(boolean prisoner) {
        isPrisoner = prisoner;
    }

    public void setOrphan(boolean orphan) {
        isOrphan = orphan;
    }

    // دالة لحساب العمر من تاريخ الميلاد
    public String getAge() {
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

            return String.valueOf(age);

        } catch (Exception e) {
            e.printStackTrace();
            return "0"; // إذا حدث خطأ في التحويل
        }
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getEducation() {
        return education;
    }

    public boolean isSick() {
        return isSick;
    }

    public boolean isMartyr() {
        return isMartyr;
    }

    public boolean isPrisoner() {
        return isPrisoner;
    }

    public boolean isOrphan() {
        return isOrphan;
    }

    // ToString يمكن إضافته إذا كنت بحاجة لعرض المعلومات
    @Override
    public String toString() {
        return "MemberModel{" +
                "name='" + name + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", education='" + education + '\'' +
                ", isSick=" + isSick +
                ", isMartyr=" + isMartyr +
                ", isPrisoner=" + isPrisoner +
                ", isOrphan=" + isOrphan +
                '}';
    }
}
