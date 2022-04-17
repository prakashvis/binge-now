package com.bingenow.server.database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 *
 *  Author : Prakash U
 *
 */

public class DBConnection {

    private static String USERNAME = "vbmowrevuxkuih";
    private static String PASSWORD = "fcf951a0e073bf5d8aa155389b8deba0c155eb06af77907736246e7e53a37095";
    private static String URL = "jdbc:postgresql://ec2-63-35-156-160.eu-west-1.compute.amazonaws.com:5432/d41nmsm7otvk02";

    private static Connection connection = null;

    DBConnection()
    {
        initializeDatabase();
    }

    public Connection getConnection()
    {
        if (connection == null)
        {
            connection = newConnection();
        }
        return connection;
    }

    private static Connection newConnection()
    {
        Connection connection = null;
        try
        {
            String databaseConf = "database.conf";
            String path = ".." + File.separator + "resources" + File.separator + databaseConf;
            Properties dbProps = fileToProperties(path);

            if(!dbProps.isEmpty()) {
                USERNAME = dbProps.getProperty("postgres.database.username");
                PASSWORD = dbProps.getProperty("postgres.database.password");
                URL = dbProps.getProperty("postgres.database.url");
            }

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if(connection != null)
            {
                System.out.println("Connection initialized");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection;
    }

    private void initializeDatabase()
    {
        Statement statement;

        List<String> queries = new ArrayList<>();
        queries.add("create table if not exists aaauser (user_id SERIAL, username varchar(100), password text, primary key (user_id));");
        queries.add("create table if not exists aaashow (show_id SERIAL, show_name varchar(200), remote_show_id text, primary key (show_id));");
        queries.add("create table if not exists aaarecommendation (recommendation_id SERIAL, user_id integer, show_id integer, watchlist integer, primary key (recommendation_id), constraint fk_aaauser foreign key (user_id) references aaauser(user_id), constraint fk_aaashow foreign key (show_id) references aaashow(show_id));");

        try
        {
            Connection connection = getConnection();
            statement = connection.createStatement();
            for(String query : queries)
            {
                statement.executeUpdate(query);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Properties fileToProperties(String filePath)
    {
        File file;
        FileInputStream fis = null;
        Properties properties = new Properties();
        try
        {
            file = new File(filePath);
            fis = new FileInputStream(file);
            properties.load(fis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
}
