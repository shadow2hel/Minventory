package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.Errors;
import me.shadow2hel.minventory.model.PlayerTracker;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class WipeRepo implements IWipeRepo {
    private Database db;
    private String table;
    private JavaPlugin main;

    public WipeRepo(Database db, JavaPlugin main) {
        this.db = db;
        this.main = main;
        table = "minventory_wipes";
    }

    @Override
    public Date createWipe(Date date) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(
                    String.format("INSERT OR IGNORE INTO %s(date) VALUES (?)",
                            table));

            ps.setLong(1, date.getTime());
            int rows = ps.executeUpdate();
            return rows > 0 ? date : null;
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

    @Override
    public List<Date> readAllWipes() {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s ORDER BY date DESC", table));
            rs = ps.executeQuery();
            List<Date> dates = new ArrayList<>();
            while(rs.next()) {
                dates.add(new Date(
                        rs.getInt(1)));
            }
            return dates.size() > 0 ? dates : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    @Override
    public Date readLatestWipe() {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s ORDER BY date DESC", table));
            rs = ps.executeQuery();
            List<Date> dates = new ArrayList<>();
            while(rs.next()) {
                dates.add(new Date(
                        rs.getInt(1)));
            }
            return dates.size() > 0 ? dates.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }
}
