package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.Errors;
import me.shadow2hel.minventory.model.ModelPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PlayerRepo implements IPlayerRepo {
    private final Database db;
    private final JavaPlugin main;
    private final String table = "minventory_players";

    public PlayerRepo(Database db, JavaPlugin main) {
        this.db = db;
        this.main = main;
    }

    @Override
    public ModelPlayer createPlayer(ModelPlayer player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(
                    String.format("INSERT OR IGNORE INTO %s(player,enderchestwiped,prestige,markedforprestige) VALUES (?,?,?,?)",
                            table));

            ps.setString(1, player.getUUID());
            ps.setString(2, "" + player.isEnderChestWiped());
            ps.setInt(3, 0);
            ps.setString(4, "" + false);
            int rows = ps.executeUpdate();
            return rows > 0 ? player : null;
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
    public List<ModelPlayer> readAllPlayers() {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s", table));
            rs = ps.executeQuery();
            List<ModelPlayer> playerList = new ArrayList<>();
            while(rs.next()) {
                playerList.add(new ModelPlayer(
                        rs.getString(1),
                        rs.getString(2).equals("true"),
                        rs.getInt(3),
                        rs.getString(4).equals("true")));
            }
            return playerList;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    @Override
    public ModelPlayer readPlayer(String UUID) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE player = ?", table));
            ps.setString(1, UUID);
            rs = ps.executeQuery();
            List<ModelPlayer> playerList = new ArrayList<>();
            while(rs.next()) {
                playerList.add(new ModelPlayer(
                        rs.getString(1),
                        rs.getString(2).equals("true"),
                        rs.getInt(3),
                        rs.getString(4).equals("true")));
            }
            return playerList.size() > 0 ? playerList.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    @Override
    public ModelPlayer readPlayer(ModelPlayer player) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE player = ?", table));
            ps.setString(1, player.getUUID());
            rs = ps.executeQuery();
            List<ModelPlayer> playerList = new ArrayList<>();
            while(rs.next()) {
                playerList.add(new ModelPlayer(
                        rs.getString(1),
                        rs.getString(2).equals("true"),
                        rs.getInt(3),
                        rs.getString(4).equals("true")));
            }
            return playerList.size() > 0 ? playerList.get(0) : null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    @Override
    public ModelPlayer updatePlayer(ModelPlayer player) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("UPDATE %s SET enderchestwiped = ?, prestige = ?, markedforprestige = ? WHERE player = ?", table));
            ps.setString(1, "" + player.isEnderChestWiped());
            ps.setInt(2, player.getPrestige());
            ps.setString(3, "" + player.isMarkedForPrestige());
            ps.setString(4, "" + player.getUUID());
            int rows = ps.executeUpdate();
            return rows > 0 ? player: null;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            db.close(ps, rs);
        }

        return null;
    }

    @Override
    public boolean deletePlayer(ModelPlayer player) {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = db.getSQLConnection();
            ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE player = ?", table));
            ps.setString(1, player.getUUID());
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
