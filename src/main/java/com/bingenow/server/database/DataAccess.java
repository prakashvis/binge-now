package com.bingenow.server.database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataAccess {

    public static DBConnection dbConnection = new DBConnection();

    public static int deleteRows(String tableName, String condition)
    {
        String deleteQuery = "delete from " + tableName + " where " + condition + ";";
        return executeUpdate(deleteQuery);
    }
    public static int insertRow(String tableName, String columns, String values)
    {
        String insertQuery = "insert into " + tableName + "(" + columns + ") values (" + values + ");";
        return executeUpdate(insertQuery);
    }

    public static int updateRow(String tableName, String values, String condition)
    {
        String updateQuery = "update " + tableName + " set " + values + " where " + condition + ";";
        return executeUpdate(updateQuery);
    }

    public static int executeUpdate(String query)
    {
        int result = 0;
        Connection connection = dbConnection.getConnection();
        try
        {
            Statement statement = connection.createStatement();
            result = statement.executeUpdate(query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = -2;
        }
        return result;
    }

    public static JSONArray executeQuery(String tableName, String column, String condition)
    {
        List<String> columnList= new ArrayList<>();
        columnList.add(column);
        return executeQuery(tableName, columnList, condition);
    }

    public static JSONArray executeQuery(String tableName, List<String> columns, String condition)
    {
        return executeQuery(null, tableName, columns, condition);
    }


    public static JSONArray executeQuery(String select, String tableName, List<String> columns, String condition)
    {
        Statement statement;
        ResultSet resultSet = null;
        Connection connection = dbConnection.getConnection();
        JSONArray result = new JSONArray();
        String query;
        try
        {
            statement = connection.createStatement();
            query = select != null ? select : "select * from ";
            query = query + tableName;
            if(condition != null) query = query + " where " + condition;
            query = query + ";";
            resultSet = statement.executeQuery(query);
            result = getJSONArray(resultSet, columns);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONArray getJSONArray(ResultSet resultSet, List<String> columns)
    {
        JSONArray result = new JSONArray();
        try
        {
            while(resultSet.next())
            {
                JSONObject row = new JSONObject();
                for(String column : columns)
                {
                    if(column.contains(".")) {
                        column = column.substring(column.indexOf(".") + 1);
                    }
                    String value = resultSet.getString(column);
                    if(value == null) value = "null";
                    row.put(column, value);
                }
                result.add(row);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
