package module.kalj123.skisbot.database;

import sx.blah.discord.handle.obj.IGuild;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Project: skisbot
 * Created by Kale on 18/08/2017 at 6:35 PM.
 */
public class SQLite {


    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:skisbot";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established!");
            createServerTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void createServerTable() {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:skisbot.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Guilds " +
                    "(ID VARCHAR PRIMARY KEY     NOT NULL," +
                    " Botchannel     VARCHAR);";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void updateGuilds(List<IGuild> guilds) {
        Connection c = null;
        Statement stmt = null;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:skisbot.db");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Guilds;" );

            LinkedList<String> serverIDs = new LinkedList();
            while (rs.next()) {
                serverIDs.add(rs.getString("ID"));
            }
            LinkedList<IGuild> removeList = new LinkedList();
            for(IGuild guild: guilds) {
                for(String ID: serverIDs) {
                    if (Objects.equals(guild.getLongID() + "", ID)) {
                        removeList.add(guild);
                    }
                }
            }
            guilds.removeAll(removeList);
            if(!guilds.isEmpty()) {
                System.out.println("New Guilds:");
                for (IGuild guild: guilds) {
                    System.out.println(guild.getName());
                }
            }

            rs.close();
            stmt.close();
            c.close();

            for(IGuild guild : guilds) {
                addGuild(guild);
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void addGuild(IGuild guild) {
        Connection c = null;
        Statement stmt = null;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:skisbot.db");

            stmt = c.createStatement();
            stmt.executeUpdate( "INSERT INTO Guilds (ID) VALUES (" + guild.getLongID() + ");" );

            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void updateBotChannel(IGuild guild, String botChannelID) {
        Connection c = null;
        Statement stmt = null;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:skisbot.db");

            stmt = c.createStatement();
            stmt.executeUpdate( "UPDATE Guilds set Botchannel = " + botChannelID + " where ID=" + guild.getLongID() +";" );

            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static String getBotChannelID(IGuild guild) {
        Connection c = null;
        Statement stmt = null;
        String returnString = null;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:skisbot.db");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT Botchannel FROM Guilds WHERE ID=" + guild.getLongID() + ";" );
            while(rs.next()) {
                returnString = rs.getString("Botchannel");
            }
            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return returnString;
    }
}
