package me.shadow2hel.minventory.model;

public class MobWithItem {
    private final String UUID;
    private final boolean hasName;

    private final String type;

    public MobWithItem(String UUID, boolean hasName, String type) {
        this.UUID = UUID;
        this.hasName = hasName;
        this.type = type;
    }

    public String getUUID() {
        return UUID;
    }

    public boolean hasName() {
        return hasName;
    }

    public String getType() {
        return type;
    }
}
