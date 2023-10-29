package com.assocation.justice.util.enumeration;

public enum SituationFamiliale {
    أعزب("أعزب"),
    متزوج("متزوج"),
    أرمل("أرمل"),
    مطلق("مطلق");

    private final String displayName;

    SituationFamiliale(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}