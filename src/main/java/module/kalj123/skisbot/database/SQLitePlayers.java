package module.kalj123.skisbot.database;

import jdk.nashorn.internal.ir.annotations.Ignore;
import module.kalj123.skisbot.SkisBot;
import module.kalj123.skisbot.league.Player;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static module.kalj123.skisbot.SkisBot.discordClient;

/**
 * Project: skisbot
 * Created by Kale on 19/08/2017 at 2:17 AM.
 */
public class SQLitePlayers {

    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:skisbot";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

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

    public static void updateGuilds(List<IGuild> guilds) {
        createPlayerTable(guilds);
    }

    private static void createPlayerTable(List<IGuild> guilds) {
        for (IGuild guild : guilds) {
            createPlayerTable(guild);
        }
    }

    private static void createPlayerTable(IGuild guild) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:skisbot";
            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + guild.getName() + guild.getLongID() + " (ID VARCHAR PRIMARY KEY NOT NULL, Username VARCHAR NOT NULL, SummonnerID VARCHAR);";
            stmt.executeUpdate(sql);
            sql = "SELECT COUNT(*) FROM " + guild.getName() + guild.getLongID() + ";";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            if (rs.getInt("COUNT(*)") == 0) {
                rs.close();
                stmt.close();
                conn.close();
                System.out.println("empty table: "+  guild.getName() + guild.getLongID());
                populatePlayerTable(guild);
            } else {
                stmt.close();
                conn.close();
            }
            //printTable(guild);
        } catch (SQLException e) {
            e.printStackTrace();
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

    private static void printTable(IGuild guild) { //Debug function
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:skisbot";
            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Username FROM " + guild.getName() + guild.getLongID() + ";");
            while(rs.next()) {
                System.out.println("Username: " + rs.getString("Username"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
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

    private static void populatePlayerTable(IGuild guild) {
            Connection conn = null;
            try {
                String url = "jdbc:sqlite:skisbot";
                conn = DriverManager.getConnection(url);

                Statement stmt = conn.createStatement();
                for (IUser user :guild.getUsers()) {

                    String sql = "INSERT INTO " + guild.getName() + guild.getLongID() + " (ID, Username) VALUES (" + user.getLongID() + ", '" + user.getName() + "');";
                    stmt.executeUpdate(sql);
                }
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
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

    public static void addSummonerIDToPlayer(IUser user, IGuild guild, String SummonerID) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:skisbot";
            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            String sql = "UPDATE " + guild.getName() + guild.getLongID() + " set SummonerID = " + SummonerID + " WHERE ID=" + user.getLongID() + ";";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public static void addSummonerIDToPlayerWithoutGuild(IUser user, String SummonerID) {
        LinkedList<IGuild> guilds = (LinkedList<IGuild>) SkisBot.discordClient.getGuilds();
        for (IGuild guild: guilds) {
            for(IUser loopUser: guild.getUsers()) {
                if (loopUser == user) {
                    addSummonerIDToPlayer(user, guild, SummonerID);
                }
            }
        }
    }

    public static void deleteSummonerIDFromPlayer(IUser user, IGuild guild) {
        Connection conn = null;

        try {
            String url = "jdbc:sqlite:skisbot";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String sql = "UPDATE " + guild.getName() + guild.getLongID() + " set SummonerID= null WHERE ID=" + user.getLongID() + ";";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
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


    public static List<Player> getPlayerList(IGuild guild) {
        Connection conn = null;
        LinkedList<Player> players = new LinkedList();
        try {
            String url = "jdbc:sqlite:skisbot";
            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, Username FROM " + guild.getName() + guild.getLongID() + ";");
            while(rs.next()) {
                players.add(new Player(rs.getString("ID"), rs.getString("Username")));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return players;
    }
}
