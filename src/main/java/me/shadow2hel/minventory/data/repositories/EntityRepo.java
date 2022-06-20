package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.Errors;
import me.shadow2hel.minventory.model.EntityItemTracker;
import me.shadow2hel.minventory.model.InventoryTracker;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EntityRepo implements IEntityRepo {
    private final Database db;
    private final JavaPlugin main;
    private final String table = "minventory_mobs";

    public EntityRepo(Database db, JavaPlugin main) {
        this.db = db;
        this.main = main;
    }

    public EntityItemTracker createMobWithItem(EntityItemTracker entityItemTracker) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(
                    String.format("INSERT OR IGNORE INTO %s(mob,nametag,type,location_x,location_y,location_z,world) VALUES (?,?,?,?,?,?,?)",
                            table));

            ps.setString(1, entityItemTracker.getUUID());
            ps.setString(2, "" + entityItemTracker.hasName());
            ps.setString(3, "" + entityItemTracker.getType());
            ps.setInt(4, entityItemTracker.getLocation_x());
            ps.setInt(5, entityItemTracker.getLocation_y());
            ps.setInt(6, entityItemTracker.getLocation_z());
            ps.setString(7, entityItemTracker.getWorld());
            int rows = ps.executeUpdate();
            return rows > 0 ? entityItemTracker : null;
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


    public List<EntityItemTracker> readAllMobWithItem() {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<EntityItemTracker> entityItemTrackers = new ArrayList<>();
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s", table));
            rs = ps.executeQuery();
            while (rs.next()) {
                entityItemTrackers.add(new EntityItemTracker(
                        rs.getString(1),
                        rs.getString(2).equalsIgnoreCase("true"),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7)));
            }
            return entityItemTrackers;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public EntityItemTracker readMobWithItem(String UUID) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<EntityItemTracker> entityItemTrackers = new ArrayList<>();
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE mob = ?", table));
            ps.setString(1, UUID);
            rs = ps.executeQuery();
            List<InventoryTracker> inventoryTrackerList = new ArrayList<>();
            while (rs.next()) {
                entityItemTrackers.add(new EntityItemTracker(
                        rs.getString(1),
                        rs.getString(2).equalsIgnoreCase("true"),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7)));
            }
            return entityItemTrackers.size() > 0 ? entityItemTrackers.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public EntityItemTracker readMobWithItem(EntityItemTracker entityItemTracker) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE mob = ?", table));
            ps.setString(1, entityItemTracker.getUUID());
            rs = ps.executeQuery();
            List<EntityItemTracker> entityItemTrackers = new ArrayList<>();
            while (rs.next()) {
                entityItemTrackers.add(new EntityItemTracker(
                        rs.getString(1),
                        rs.getString(2).equalsIgnoreCase("true"),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7)));
            }
            return entityItemTrackers.size() > 0 ? entityItemTrackers.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public EntityItemTracker updateMobWithItem(EntityItemTracker entityItemTracker) {
        Connection conn;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("UPDATE %s SET nametag = ?, type = ?, location_x = ?, location_y = ?, location_z = ?, world = ? WHERE mob = ?", table));
            ps.setString(1, "" + entityItemTracker.hasName());
            ps.setString(2, entityItemTracker.getType());
            ps.setInt(3, entityItemTracker.getLocation_x());
            ps.setInt(4, entityItemTracker.getLocation_y());
            ps.setInt(5, entityItemTracker.getLocation_z());
            ps.setString(6, entityItemTracker.getWorld());
            ps.setString(7, entityItemTracker.getUUID());
            int rows = ps.executeUpdate();
            return rows > 0 ? entityItemTracker : null;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, null);
        }

        return null;
    }

    public boolean deleteMobWithItem(EntityItemTracker entityItemTracker) {
        Connection conn;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE mob = ?", table));
            ps.setString(1, entityItemTracker.getUUID());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, null);
        }

        return false;
    }
}
