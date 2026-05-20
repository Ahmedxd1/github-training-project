package com.ahmed.konyaaddfamily.database;

public class DatabaseSchema {

    public static final String DB_NAME = "camp_aid.db";
    public static final int DB_VERSION =2;

    // ========== Table: Families ==========
    public static final String TABLE_FAMILIES = "families";

    public static final String COL_FAMILY_ID = "id";
    public static final String COL_FATHER_NAME = "father_name";
    public static final String COL_FATHER_NATIONAL_ID = "father_national_id";
    public static final String COL_FATHER_BIRTHDATE = "father_birthdate";
    public static final String COL_PHONE_PRIMARY = "phone_primary";
    public static final String COL_PHONE_SECONDARY = "phone_secondary";
    public static final String COL_TOTAL_MEMBERS = "total_members";
    public static final String COL_MARITAL_STATUS = "marital_status";

    public static final String COL_CHILD_MALE_0_5 = "child_male_0_5";
    public static final String COL_CHILD_FEMALE_0_5 = "child_female_0_5";
    public static final String COL_CHILD_MALE_5_17 = "child_male_5_17";
    public static final String COL_CHILD_FEMALE_5_17 = "child_female_5_17";
    public static final String COL_ADULT_MALE_17_50 = "adult_male_17_50";
    public static final String COL_ADULT_FEMALE_17_50 = "adult_female_17_50";
    public static final String COL_SENIOR_MALE_50PLUS = "senior_male_50plus";
    public static final String COL_SENIOR_FEMALE_50PLUS = "senior_female_50plus";

    public static final String COL_NUM_PREGNANT = "num_pregnant";
    public static final String COL_NUM_BREASTFEEDING = "num_breastfeeding";
    public static final String COL_NUM_CHRONIC = "num_chronic_disease";
    public static final String COL_NUM_INJURED = "num_injured";
    public static final String COL_NUM_MARTYRS = "num_martyrs";
    public static final String COL_NUM_ORPHANS = "num_orphans";
    public static final String COL_NUM_PRISONERS = "num_prisoners";

    public static final String COL_FAMILY_TYPE = "family_type";
    public static final String COL_FAMILY_NOTES = "notes";
    public static final String COL_FAMILY_CREATED_AT = "created_at";

    // ========== Table: Members ==========
    public static final String TABLE_MEMBERS = "members";

    public static final String COL_MEMBER_ID = "id";
    public static final String COL_MEMBER_FAMILY_ID = "family_id";
    public static final String COL_MEMBER_NAME = "name";
    public static final String COL_MEMBER_NATIONAL_ID = "national_id";
    public static final String COL_MEMBER_BIRTHDATE = "birthdate";
    public static final String COL_MEMBER_EDUCATION = "education_level";
    public static final String COL_MEMBER_PHONE = "phone";
    public static final String COL_IS_SICK = "is_sick";
    public static final String COL_DISEASE = "disease";
    public static final String COL_IS_INJURED = "is_injured";
    public static final String COL_INJURED_DETAILS = "injured_details";
    public static final String COL_IS_MARTYR = "is_martyr";
    public static final String COL_IS_PRISONER = "is_prisoner";
    public static final String COL_IS_ORPHAN = "is_orphan";
    public static final String COL_MEMBER_GENDER = "gender";

    // ========== Table: Assistances ==========
    public static final String TABLE_ASSISTANCES = "assistances";

    public static final String COL_ASSISTANCE_ID = "id";
    public static final String COL_ASSISTANCE_TYPE = "type";
    public static final String COL_ASSISTANCE_DESC = "description";
    public static final String COL_ASSISTANCE_AMOUNT = "amount";
    public static final String COL_ASSISTANCE_QTY = "quantity";
    public static final String COL_ASSISTANCE_DATE = "date";
    public static final String COL_ASSISTANCE_PROVIDER = "provider";

    // ========== Table: Aid-Family Relation ==========
    public static final String TABLE_AID_FAMILY = "aid_family";

    public static final String COL_AID_FAMILY_ID = "id";
    public static final String COL_AID_ID = "aid_id";
    public static final String COL_AID_FAMILY_FAMILY_ID = "family_id";
    public static final String COL_WIFE_NAME = "wife_name";
    public static final String COL_WIFE_NATIONAL_ID = "wife_national_id";
    public static final String COL_WIFE_BIRTHDATE = "wife_birthdate";
}
