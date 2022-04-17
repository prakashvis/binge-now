package com.bingenow.client;

import com.bingenow.server.user.UserHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

/*
 *
 *  Author : Prakash U
 *
 */

@WebServlet(name = "userServlet", value = "/user")
public class UserServlet extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        boolean result = false;
        String operation = "unknown", password = null, username = null, message = null;

        try
        {
            JSONParser parser = new JSONParser();
            JSONObject requestObject = (JSONObject) parser.parse(request.getReader());

            if(requestObject.containsKey("operation"))
                operation = (String) requestObject.get("operation");

            if(requestObject.containsKey("username"))
                username = (String) requestObject.get("username");

            if(requestObject.containsKey("password"))
                password = (String) requestObject.get("password");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        switch (operation)
        {
            case "login":
            {
                result = UserHandler.authenticateUser(username, password);
                message = result ? "Login successful" : "Invalid username / password";
                break;
            }
            case "signup":
            {
                result = UserHandler.addUser(username, password);
                message = result ? "User created" : "User already exists";
                break;
            }
            default:
            {
                message = "Unknown operation";
            }
        }
        ServletUtil.setResponse(response, result, message);
    }
}
