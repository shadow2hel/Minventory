package me.shadow2hel.minventory.model;

public class InventoryTracker {
    private final String UUID;
    private final String type;
    private final Integer locationX;
    private final Integer locationY;
    private final Integer locationZ;
    private final String world;

    public InventoryTracker(String UUID, String type, Integer locationX, Integer locationY, Integer locationZ, String world) {
        this.UUID = UUID;
        this.type = type;
        this.locationX = locationX;
        this.locationY = locationY;
        this.locationZ = locationZ;
        this.world = world;
    }

    public String getUUID() {
        return UUID;
    }

    public String getType() {
        return type;
    }

    public Integer getLocationX() {
        return locationX;
    }

    public Integer getLocationY() {
        return locationY;
    }

    public Integer getLocationZ() {
        return locationZ;
    }

    public String getWorld() {
        return world;
    }
}
