package me.shadow2hel.minventory.data;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class SQLite extends Database {


    public SQLite(JavaPlugin instance) {
        super(instance);
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                plugin.getDataFolder().mkdir();
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void initialize() {
        String SQLiteCreateInventoryTable = "CREATE TABLE IF NOT EXISTS minventory_inventory (" +
                "`player` varchar(32) NOT NULL," +
                "`type` varchar(32) NOT NULL," +
                "`location_x` int(32) NOT NULL," +
                "`location_y` int(4) NOT NULL," +
                "`location_z` int(32) NOT NULL," +
                "`world` varchar(32) NOT NULL," +
                "PRIMARY KEY (`player`,`location_x`,`location_y`,`location_z`,`world`)" +
                ");";

        String SQLiteCreateMobTable = "CREATE TABLE IF NOT EXISTS minventory_mobs (" +
                "`mob` varchar(32) NOT NULL," +
                "`nametag` varchar(5) NOT NULL," +
                "`type` varchar(16) NOT NULL," +
                "`location_x` int(32) NOT NULL," +
                "`location_y` int(32) NOT NULL," +
                "`location_z` int(32) NOT NULL," +
                "`world` varchar(32) NOT NULL," +
                "PRIMARY KEY (`mob`)" +
                ");";

        String SQLiteCreatePlayerTable = "CREATE TABLE IF NOT EXISTS minventory_players (" +
                "`player` varchar(32) NOT NULL," +
                "`enderchestwiped` varchar(5) NOT NULL," +
                "PRIMARY KEY (`player`)" +
                ");";

        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateInventoryTable);
            s = connection.createStatement();
            s.executeUpdate(SQLiteCreateMobTable);
            s = connection.createStatement();
            s.executeUpdate(SQLiteCreatePlayerTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
