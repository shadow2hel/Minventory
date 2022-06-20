package me.shadow2hel.minventory.model;

public class PlayerTracker {
    private final String UUID;
    private boolean isEnderChestWiped;
    private int prestige;
    private boolean markedForPrestige;

    public PlayerTracker(String UUID, boolean isEnderChestWiped, int prestige, boolean markedForPrestige) {
        this.UUID = UUID;
        this.isEnderChestWiped = isEnderChestWiped;
        this.prestige = prestige;
        this.markedForPrestige = markedForPrestige;
    }

    public String getUUID() {
        return UUID;
    }

    public boolean isEnderChestWiped() {
        return isEnderChestWiped;
    }

    public void setEnderChestWiped(boolean enderChestWiped) {
        isEnderChestWiped = enderChestWiped;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public boolean isMarkedForPrestige() {
        return markedForPrestige;
    }

    public void setMarkedForPrestige(boolean markedForPrestige) {
        this.markedForPrestige = markedForPrestige;
    }
}
