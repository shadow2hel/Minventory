package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.Errors;
import me.shadow2hel.minventory.model.InventoryTracker;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PlayerInventoryRepo implements IPlayerInventoryRepo {
    private final Database db;
    private final JavaPlugin main;
    private final String table = "minventory_inventory";

    public PlayerInventoryRepo(Database db, JavaPlugin main) {
        this.db = db;
        this.main = main;
    }

    public InventoryTracker createPlayerInventory(InventoryTracker inventoryTracker) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(
                    String.format("INSERT OR IGNORE INTO %s(player,type,location_x,location_y,location_z,world) VALUES (?,?,?,?,?,?)",
                            table));

            ps.setString(1, inventoryTracker.getUUID());
            ps.setString(2, inventoryTracker.getType());
            ps.setInt(3, inventoryTracker.getLocationX());
            ps.setInt(4, inventoryTracker.getLocationY());
            ps.setInt(5, inventoryTracker.getLocationZ());
            ps.setString(6, inventoryTracker.getWorld());
            int rows = ps.executeUpdate();
            return rows > 0 ? inventoryTracker : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }

        return null;
    }

    public List<InventoryTracker> readAllPlayerInventory() {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s ORDER BY location_x, location_z, world", table));
            rs = ps.executeQuery();
            List<InventoryTracker> inventoryTrackerList = new ArrayList<>();
            while(rs.next()) {
                inventoryTrackerList.add(new InventoryTracker(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getString(6)));
            }
            return inventoryTrackerList;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public InventoryTracker readPlayerInventory(String UUID) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE player = ? ORDER BY location_x, location_z, world", table));
            ps.setString(1, UUID);
            rs = ps.executeQuery();
            List<InventoryTracker> inventoryTrackerList = new ArrayList<>();
            while(rs.next()) {
                inventoryTrackerList.add(new InventoryTracker(
                        rs.getString(1),
                        rs.getString(2), rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getString(6)));
            }
            return inventoryTrackerList.size() > 0 ? inventoryTrackerList.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public InventoryTracker readPlayerInventory(InventoryTracker inventoryTracker) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE player = ? ORDER BY location_x, location_z, world", table));
            ps.setString(1, inventoryTracker.getUUID());
            rs = ps.executeQuery();
            List<InventoryTracker> inventoryTrackerList = new ArrayList<>();
            while(rs.next()) {
                inventoryTrackerList.add(new InventoryTracker(
                        rs.getString(1),
                        rs.getString(2), rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getString(6)));
            }
            return inventoryTrackerList.size() > 0 ? inventoryTrackerList.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps,rs);
        }

        return null;
    }

    public InventoryTracker updatePlayerInventory(InventoryTracker inventoryTracker) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("UPDATE %s SET type = ?, location_x = ?, location_y = ?, location_z = ?, world = ? WHERE player = ?", table));
            ps.setString(1, inventoryTracker.getType());
            ps.setInt(2, inventoryTracker.getLocationX());
            ps.setInt(3, inventoryTracker.getLocationY());
            ps.setInt(4, inventoryTracker.getLocationZ());
            ps.setString(5, inventoryTracker.getWorld());
            ps.setString(6, inventoryTracker.getUUID());
            int rows = ps.executeUpdate();
            return rows > 0 ? inventoryTracker : null;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public boolean deletePlayerInventory(InventoryTracker inventoryTracker) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE player = ? AND location_x = ? AND location_y = ? AND location_z = ? AND world = ?", table));
            ps.setString(1, inventoryTracker.getUUID());
            ps.setInt(2, inventoryTracker.getLocationX());
            ps.setInt(3, inventoryTracker.getLocationY());
            ps.setInt(4, inventoryTracker.getLocationZ());
            ps.setString(5, inventoryTracker.getWorld());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }
        return false;
    }
}
