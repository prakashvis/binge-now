package com.bingenow.server.user;

import com.bingenow.server.database.DataAccess;
import com.bingenow.server.database.DataBaseUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {
    public static int createUser(String username, String password)
    {
        String passwordHash = getPasswordHash(password);
        String columns = DataBaseUtil.DBConstants.USERNAME + "," + DataBaseUtil.DBConstants.PASSWORD;
        String values = "'" + username + "','" + passwordHash + "'";

        return DataAccess.insertRow(DataBaseUtil.DBConstants.USER_TABLE, columns, values);
    }

    public static JSONObject getUser(String username, List<String> columnList)
    {
        JSONObject user = new JSONObject();

        String condition = DataBaseUtil.DBConstants.USERNAME + "='" + username + "'";
        JSONArray result = DataAccess.executeQuery(DataBaseUtil.DBConstants.USER_TABLE, columnList,condition);

        if(!result.isEmpty()) {
            user = (JSONObject) result.get(0);
        }
        return user;
    }

    public static Long getUserId(String username)
    {
        Long userId = -1L;
        List<String> columnList= new ArrayList<>();
        columnList.add(DataBaseUtil.DBConstants.USER_ID);

        JSONObject user = getUser(username, columnList);
        if(!user.isEmpty()) {
            userId = Long.parseLong(user.get(DataBaseUtil.DBConstants.USER_ID).toString());
        }

        return userId;
    }

    public static boolean isUserExists(String username)
    {
        Long userId = getUserId(username);
        return userId != -1L;
    }

    public static String getPasswordHash(String password)
    {
        int passwordHash = (password + "salt").hashCode();
        return Integer.toString(passwordHash);
    }
}
