package me.shadow2hel.minventory.model;

public class ModelPlayer {
    private final String UUID;
    private boolean isEnderChestWiped;

    public ModelPlayer(String UUID, boolean isEnderChestWiped) {
        this.UUID = UUID;
        this.isEnderChestWiped = isEnderChestWiped;
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
}
