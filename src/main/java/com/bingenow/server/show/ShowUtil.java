package com.bingenow.server.show;

import com.bingenow.server.database.DataAccess;
import com.bingenow.server.database.DataBaseUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class ShowUtil {

    public static void updateRecommendedShow(Long userId, Long showId) {

        String columns = DataBaseUtil.DBConstants.USER_ID + "," + DataBaseUtil.DBConstants.SHOW_ID + "," + DataBaseUtil.DBConstants.WATCHLIST;
        String values = userId + "," + showId + ",0";
        DataAccess.insertRow(DataBaseUtil.DBConstants.RECOMMENDATION_TABLE, columns, values);
    }

    public static JSONObject getShow(Long showId) {

        String showApi = "https://api.tvmaze.com/shows/%d?embed=cast";

        JSONObject showDetails = new JSONObject();

        try{
            String api = String.format(showApi, showId);
            URL url = new URL(api);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == 200)
            {
                StringBuilder responseBuilder = new StringBuilder();

                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext())
                {
                    responseBuilder.append(scanner.nextLine());
                }
                scanner.close();

                JSONParser parser = new JSONParser();
                showDetails = (JSONObject) parser.parse(responseBuilder.toString());
            }
            else
            {
                showDetails.put("error", "Something went wrong, please try again later");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return showDetails;
    }

    public static Long getLastRecommendedShow(Long userId) {

        Long lastShow = 0L;

        List<String> columnList = new ArrayList<>();

        columnList.add(DataBaseUtil.DBConstants.MAX);

        String condition = DataBaseUtil.DBConstants.USER_ID + "=" + userId;
        String select = "select MAX(" + DataBaseUtil.DBConstants.SHOW_ID + ") from ";

        JSONArray res = DataAccess.executeQuery(select, DataBaseUtil.DBConstants.RECOMMENDATION_TABLE, columnList, condition);

        if (!res.isEmpty())
        {
            JSONObject object = (JSONObject) res.get(0);
            if (object.containsKey(DataBaseUtil.DBConstants.MAX))
            {
                String max = (String) object.get(DataBaseUtil.DBConstants.MAX);
                if (!"null".equalsIgnoreCase(max))
                {
                    lastShow = Long.parseLong(max);
                }
            }
        }
        return lastShow;
    }

    public static void addShow(JSONObject showDetails)
    {
        String showName = (String) showDetails.get("name");
        if(isShowPresent(showName))
        {
            return;
        }
        String columns = DataBaseUtil.DBConstants.SHOW_NAME + "," + DataBaseUtil.DBConstants.REMOTE_SHOW_ID;
        String values = "'" + showName + "'," + showDetails.get("id");
        DataAccess.insertRow(DataBaseUtil.DBConstants.SHOW_TABLE, columns, values);
    }

    private static boolean isShowPresent(String showName)
    {
        String condition = DataBaseUtil.DBConstants.SHOW_NAME + "='" + showName + "'";
        JSONArray result = DataAccess.executeQuery(DataBaseUtil.DBConstants.SHOW_TABLE, DataBaseUtil.DBConstants.SHOW_ID, condition);
        return !result.isEmpty();
    }

    public static String getFormattedDetails(JSONObject show) {

        StringBuilder details = new StringBuilder();
        details.append(show.get("language")).append(" | ").append("Rating : ").append(((JSONObject)show.get("rating")).get("average")).append(" | ");

        JSONArray genres = (JSONArray) show.get("genres");
        for (Object o : genres) {
            String genre = (String) o;
            details.append(genre);
            details.append(", ");
        }
        details.deleteCharAt(details.lastIndexOf(","));

        return details.toString();
    }

    public static String getCast(JSONObject show) {

        StringBuilder cast = new StringBuilder();
        HashSet<String> set = new HashSet<>();

        JSONArray castArray = (JSONArray) ((JSONObject) show.get("_embedded")).get("cast");
        for (Object o : castArray)
        {
            JSONObject person = (JSONObject) ((JSONObject) o).get("person");
            String name = (String) person.get("name");
            if(!set.contains(person))
            {
                set.add(name);
            }
        }

        for(String s : set) {
            cast.append(s).append(", ");
        }
        cast.deleteCharAt(cast.lastIndexOf(", "));

        return cast.toString();
    }
}