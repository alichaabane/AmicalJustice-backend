package com.assocation.justice.util.enumeration;

public enum Region {
    أريانة("أريانة"),
    باجة("باجة"),
    بن_عروس("بن عروس"),
    بنزرت("بنزرت"),
    قابس("قابس"),
    قفصة("قفصة"),
    جندوبة("جندوبة"),
    القيروان("القيروان"),
    القصرين("القصرين"),
    قبلي("قبلي"),
    الكاف("الكاف"),
    المهدية("المهدية"),
    منوبة("منوبة"),
    مدنين("مدنين"),
    المنستير("المنستير"),
    نابل("نابل"),
    صفاقس("صفاقس"),
    سيدي_بوزيد("سيدي بوزيد"),
    سليانة("سليانة"),
    سوسة("سوسة"),
    تطاوين("تطاوين"),
    توزر("توزر"),
    تونس("تونس"),
    زغوان("زغوان");

    private final String displayName;

    Region(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
