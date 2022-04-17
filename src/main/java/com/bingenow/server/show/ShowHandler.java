package com.bingenow.server.show;

import com.bingenow.server.database.DataAccess;
import com.bingenow.server.database.DataBaseUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowHandler {

    public static JSONObject getShow(Long userId)
    {
        Long showId = ShowUtil.getLastRecommendedShow(userId);
        JSONObject show = ShowUtil.getShow(++showId);
        if(!show.isEmpty())
        {
            ShowUtil.addShow(show);
            ShowUtil.updateRecommendedShow(userId, showId);
        }

        JSONObject showDetails = new JSONObject();

        showDetails.put("name", show.get("name"));
        showDetails.put("details", ShowUtil.getFormattedDetails(show));
        showDetails.put("castdetails", ShowUtil.getCast(show));
        showDetails.put("showurl", show.get("officialSite"));
        showDetails.put("description", show.get("summary"));
        showDetails.put("image", ((JSONObject)show.get("image")).get("original"));

        return showDetails;
    }

    public static int addShowToWatchlist(Long userId)
    {
        Long showId = ShowUtil.getLastRecommendedShow(userId);
        String values = DataBaseUtil.DBConstants.WATCHLIST + "=" + "1";
        String condition = DataBaseUtil.DBConstants.USER_ID + "=" + userId
                + " and " + DataBaseUtil.DBConstants.SHOW_ID + "=" + showId;

        return DataAccess.updateRow(DataBaseUtil.DBConstants.RECOMMENDATION_TABLE, values, condition);
    }

    public static JSONObject getWatchlist(Long userId)
    {
        JSONObject watchlist = new JSONObject();
        List<String> columnList = new ArrayList<>();
        String showId = DataBaseUtil.DBConstants.RECOMMENDATION_TABLE + "." + DataBaseUtil.DBConstants.SHOW_ID;
        String showName = DataBaseUtil.DBConstants.SHOW_TABLE + "." + DataBaseUtil.DBConstants.SHOW_NAME;
        columnList.add(showId);
        columnList.add(showName);
        String condition = DataBaseUtil.DBConstants.RECOMMENDATION_TABLE + "." + DataBaseUtil.DBConstants.USER_ID + "=" + userId + " and " + DataBaseUtil.DBConstants.RECOMMENDATION_TABLE + "." + DataBaseUtil.DBConstants.WATCHLIST + "=1 and " + DataBaseUtil.DBConstants.SHOW_TABLE + "." + DataBaseUtil.DBConstants.SHOW_ID + "=" + DataBaseUtil.DBConstants.RECOMMENDATION_TABLE + "." + DataBaseUtil.DBConstants.SHOW_ID;
        JSONArray result = DataAccess.executeQuery(DataBaseUtil.DBConstants.RECOMMENDATION_TABLE + "," + DataBaseUtil.DBConstants.SHOW_TABLE, columnList, condition);

        for(Object object : result)
        {
            JSONObject tempObj = (JSONObject) object;
            watchlist.put(tempObj.get(DataBaseUtil.DBConstants.SHOW_ID), tempObj.get(DataBaseUtil.DBConstants.SHOW_NAME));
        }

        return watchlist;
    }

    public static int resetHistory(Long userId)
    {
        String condition = DataBaseUtil.DBConstants.USER_ID + "=" + userId;
        return DataAccess.deleteRows(DataBaseUtil.DBConstants.RECOMMENDATION_TABLE, condition);
    }
}
