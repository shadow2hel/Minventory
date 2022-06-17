package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.Errors;
import me.shadow2hel.minventory.model.TouchedInventory;
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

    public TouchedInventory createPlayerInventory(TouchedInventory touchedInventory) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(
                    String.format("INSERT OR IGNORE INTO %s(player,type,location_x,location_y,location_z) VALUES (?,?,?,?,?)",
                            table));

            ps.setString(1, touchedInventory.getUUID());
            ps.setString(2, touchedInventory.getType());
            ps.setInt(3, touchedInventory.getLocationX());
            ps.setInt(4, touchedInventory.getLocationY());
            ps.setInt(5, touchedInventory.getLocationZ());
            int rows = ps.executeUpdate();
            return rows > 0 ? touchedInventory : null;
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


    public TouchedInventory readPlayerInventory(String UUID) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE player = ? ORDER BY location_x, location_z", table));
            ps.setString(1, UUID);
            rs = ps.executeQuery();
            List<TouchedInventory> touchedInventoryList = new ArrayList<>();
            while(rs.next()) {
                touchedInventoryList.add(new TouchedInventory(
                        rs.getString(1),
                        rs.getString(2), rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5)));
            }
            return touchedInventoryList.size() > 0 ? touchedInventoryList.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public TouchedInventory readPlayerInventory(TouchedInventory touchedInventory) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE player = ? ORDER BY location_x, location_z", table));
            ps.setString(1, touchedInventory.getUUID());
            rs = ps.executeQuery();
            List<TouchedInventory> touchedInventoryList = new ArrayList<>();
            while(rs.next()) {
                touchedInventoryList.add(new TouchedInventory(
                        rs.getString(1),
                        rs.getString(2), rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5)));
            }
            return touchedInventoryList.size() > 0 ? touchedInventoryList.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps,rs);
        }

        return null;
    }

    public TouchedInventory updatePlayerInventory(TouchedInventory touchedInventory) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("UPDATE %s SET type = ?, location_x = ?, location_y = ?, location_z = ? WHERE player = ?", table));
            ps.setString(1, touchedInventory.getType());
            ps.setInt(2, touchedInventory.getLocationX());
            ps.setInt(3, touchedInventory.getLocationY());
            ps.setInt(4, touchedInventory.getLocationZ());
            ps.setString(5, touchedInventory.getUUID());
            int rows = ps.executeUpdate();
            return rows > 0 ? touchedInventory: null;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public boolean deletePlayerInventory(TouchedInventory touchedInventory) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE player = ?", table));
            ps.setString(1, touchedInventory.getUUID());
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
