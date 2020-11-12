package dev.lallemand.lcore.profile.prefix;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {

    COUNTRY("Country"),
    SYMBOL("Symbol"),
    TEXT("Text"),
    PARTNER("Partner");

    private String name;

    public static Category getByName(String name) {
        for (Category category : values()) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}
