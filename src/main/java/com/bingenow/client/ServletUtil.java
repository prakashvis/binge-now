package com.bingenow.client;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ServletUtil
{

    public static void setResponse(HttpServletResponse response, boolean status, String message)
    {
        try
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            JSONObject result = new JSONObject();
            if(!status)
            {
                result.put("error", message);
            }
            else
            {
                result.put("success", message);
            }
            out.println(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void setResponse(HttpServletResponse response, boolean status, String key, JSONObject value)
    {
        try
        {
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            JSONObject result = new JSONObject();
            if(!status)
            {
                result.put("error", "Failed");
            }
            else
            {
                result.put(key, value);
            }
            out.println(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
