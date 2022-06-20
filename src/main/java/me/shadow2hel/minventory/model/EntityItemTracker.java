package me.shadow2hel.minventory.model;

public class EntityItemTracker {
    private final String UUID;
    private final boolean hasName;
    private final String type;
    private final int location_x,location_y,location_z;
    private final String world;

    public EntityItemTracker(String UUID, boolean hasName, String type, int location_x, int location_y, int location_z, String world) {
        this.UUID = UUID;
        this.hasName = hasName;
        this.type = type;
        this.location_x = location_x;
        this.location_y = location_y;
        this.location_z = location_z;
        this.world = world;
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

    public int getLocation_x() {
        return location_x;
    }

    public int getLocation_y() {
        return location_y;
    }

    public int getLocation_z() {
        return location_z;
    }

    public String getWorld() {
        return world;
    }
}
