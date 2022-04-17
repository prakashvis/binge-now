package com.bingenow.server.user;

import com.bingenow.server.database.DataBaseUtil;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 *
 *  Author : Prakash U
 *
 */

public class UserHandler {

    public static boolean addUser(String username, String password)
    {
        boolean result = false;
        boolean isUserNameAvailable = !UserUtil.isUserExists(username);

        if(isUserNameAvailable)
        {
            int status = UserUtil.createUser(username, password);
            result = status > 0;
        }

        return result;
    }

    public static boolean authenticateUser(String username, String password)
    {
        boolean result = false;
        List<String> columnList = new ArrayList<>();
        columnList.add(DataBaseUtil.DBConstants.USERNAME);
        columnList.add(DataBaseUtil.DBConstants.PASSWORD);

        JSONObject user = UserUtil.getUser(username, columnList);
        if(!user.isEmpty())
        {
            String passwordHash = (String)user.get(DataBaseUtil.DBConstants.PASSWORD);
            result = passwordHash.equals(UserUtil.getPasswordHash(password));
        }

        return result;
    }
}