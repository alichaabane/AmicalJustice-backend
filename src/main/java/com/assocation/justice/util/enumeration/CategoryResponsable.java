package com.assocation.justice.util.enumeration;

public enum CategoryResponsable {
    رئيس_فرع("رئيس فرع"),
    كاتب_عام("كاتب عام"),
    أمين_مال("أمين مال"),
    عضو("عضو");

    private final String name;

    CategoryResponsable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
