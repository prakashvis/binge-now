package com.bingenow.client;

import com.bingenow.server.show.ShowHandler;
import com.bingenow.server.show.ShowUtil;
import com.bingenow.server.user.UserUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "showServlet", value = "/show")
public class ShowServlet extends HttpServlet
{

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {

        String operation = "unknown", username = null, show = null;

        try
        {
            JSONParser parser = new JSONParser();
            JSONObject requestObject = (JSONObject) parser.parse(request.getReader());

            if(requestObject.containsKey("operation"))
                operation = (String) requestObject.get("operation");

            if(requestObject.containsKey("username"))
                username = (String) requestObject.get("username");

            if(requestObject.containsKey("show"))
                show = (String) requestObject.get("show");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Long userId = UserUtil.getUserId(username);

        switch (operation)
        {
            case "fetch-show":
            {
                JSONObject showDetails = ShowHandler.getShow(userId);
                ServletUtil.setResponse(response, !showDetails.isEmpty(), "show", showDetails);
                break;
            }
            case "add-to-watchlist":
            {
                int result = ShowHandler.addShowToWatchlist(userId);
                ServletUtil.setResponse(response, result != -1, "Added to watchlist");
                break;
            }
            case "get-watchlist":
            {
                JSONObject result = ShowHandler.getWatchlist(userId);
                ServletUtil.setResponse(response, !result.isEmpty(), "watchlist", result);
                break;
            }
            case "reset":
            {
                int result = ShowHandler.resetHistory(userId);
                ServletUtil.setResponse(response, result != -1, "Reset successful");
                break;
            }
            default:
            {
                ServletUtil.setResponse(response, false, "Unknown operation");
            }
        }
    }
}
