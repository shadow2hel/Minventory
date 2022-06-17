package me.shadow2hel.minventory.data;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Database {
    JavaPlugin plugin;
    Connection connection;

    String dbname = "minventory";

    public Database(JavaPlugin instance){
        plugin = instance;
        initialize();
    }

    public abstract Connection getSQLConnection();

    public abstract void initialize();

    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
}
