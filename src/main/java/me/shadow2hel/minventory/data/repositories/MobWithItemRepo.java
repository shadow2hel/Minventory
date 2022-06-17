package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.Errors;
import me.shadow2hel.minventory.model.MobWithItem;
import me.shadow2hel.minventory.model.TouchedInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MobWithItemRepo implements IMobWithItemRepo {
    private final Database db;
    private final JavaPlugin main;
    private final String table = "minventory_mobs";

    public MobWithItemRepo(Database db, JavaPlugin main) {
        this.db = db;
        this.main = main;
    }

    public MobWithItem createMobWithItem(MobWithItem mobWithItem) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(
                    String.format("INSERT OR IGNORE INTO %s(mob,nametag,type) VALUES (?,?,?)",
                            table));

            ps.setString(1, mobWithItem.getUUID());
            ps.setString(2, "" + mobWithItem.hasName());
            ps.setString(3, "" + mobWithItem.getType());
            int rows = ps.executeUpdate();
            return rows > 0 ? mobWithItem : null;
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

    public MobWithItem readMobWithItem(String UUID) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MobWithItem> mobWithItems = new ArrayList<>();
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE mob = ?", table));
            ps.setString(1, UUID);
            rs = ps.executeQuery();
            List<TouchedInventory> touchedInventoryList = new ArrayList<>();
            while(rs.next()) {
                mobWithItems.add(new MobWithItem(
                        rs.getString(1),
                        rs.getString(2).equalsIgnoreCase("true"),
                        rs.getString(3)));
            }
            return mobWithItems.size() > 0 ? mobWithItems.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    public MobWithItem readMobWithItem(MobWithItem mobWithItem) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE mob = ?", table));
            ps.setString(1, mobWithItem.getUUID());
            rs = ps.executeQuery();
            List<MobWithItem> mobWithItems = new ArrayList<>();
            while(rs.next()) {
                mobWithItems.add(new MobWithItem(
                        rs.getString(1),
                        rs.getString(2).equalsIgnoreCase("true"),
                        rs.getString(3)));
            }
            return mobWithItems.size() > 0 ? mobWithItems.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps,rs);
        }

        return null;
    }

    public MobWithItem updateMobWithItem(MobWithItem mobWithItem) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("UPDATE %s SET type = ?, location_x = ?, location_y = ?, location_z = ? WHERE player = ?", table));
            ps.setString(1, mobWithItem.getUUID());
            ps.setString(2, "" + mobWithItem.hasName());
            ps.setString(3, mobWithItem.getType());
            int rows = ps.executeUpdate();
            return rows > 0 ? mobWithItem: null;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, null);
        }

        return null;
    }

    public boolean deleteMobWithItem(MobWithItem mobWithItem) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE mob = ?", table));
            ps.setString(1, mobWithItem.getUUID());
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