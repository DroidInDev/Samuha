package com.stgobain.samuha.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.stgobain.samuha.Utility.AppUtils.SKEY_DESCRIPTION;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_IMAGE_URL;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_LOCATION;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_NAME;

/**
 * Created by vignesh on 24-06-2017.
 */

public class Parser {
    public static ArrayList<SamuhaEvent> getEventArrayList(String result) {
        ArrayList<SamuhaEvent> samuhaEventArrayList = new ArrayList<>();
        try {
            JSONArray eventsArray = new JSONObject(result).getJSONArray("response");
          //  Log.d("EVENTS",eventsArray.toString());
            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject eventObject = eventsArray.getJSONObject(i);
                String id;
                String eventDate;
                String eventName;
                String eventLocation;
                String imageUrl;
                String shortDescribtion;
                id = eventObject.getString(SKEY_ID);
                eventDate = eventObject.getString("eventdate");
                eventName = eventObject.getString(SKEY_NAME);
                eventLocation = eventObject.getString(SKEY_LOCATION);
                imageUrl = eventObject.getString(SKEY_IMAGE_URL);
                shortDescribtion = eventObject.getString(SKEY_DESCRIPTION);
                SamuhaEvent samuhaEvent = new SamuhaEvent();
                samuhaEvent.setId(id);
                samuhaEvent.setEventDate(eventDate);
                samuhaEvent.setEventName(eventName);
                samuhaEvent.setEventLocation(eventLocation);
                samuhaEvent.setImageUrl(imageUrl);
                samuhaEvent.setShortDescribtion(shortDescribtion);
                samuhaEventArrayList.add(samuhaEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return samuhaEventArrayList;
    }
    public static Team getTeam(String result,int position){
        Team team = new Team();
        try {
            JSONArray teamJsonArray = new JSONObject(result).getJSONArray("response");
            if(position>=teamJsonArray.length())
                return null;
            for(int j=0;j<teamJsonArray.length();j++) {
                if (j == position) {
                    JSONObject jo_inside = teamJsonArray.getJSONObject(j);
                    team.setTeamName(jo_inside.getString("name"));
                    team.setCaptainName(jo_inside.getString("captain_name"));
                    team.setViceCaptainName(jo_inside.getString("vice_captain_name"));
                    team.setShortDescription(jo_inside.getString("short_description"));
                    team.setImageUrl(jo_inside.getString("logo"));
                    team.setScore(jo_inside.getString("score"));
                    JSONArray departmentArray = jo_inside.getJSONArray("departments");
                    ArrayList<String> deparmentArrayList = new ArrayList<>();
                    for (int k = 0; k < departmentArray.length(); k++) {
                        deparmentArrayList.add(departmentArray.getString(k));
                    }
                    team.setDepartment(deparmentArrayList);//Add your values in your `ArrayList` as below:
                }

        }
            Log.d("EVENTS",teamJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return team;
    }

    public static ArrayList<Team> getTeams(String result){
       ArrayList<Team> scoreList = new ArrayList<>();
        try {
            JSONArray teamJsonArray = new JSONObject(result).getJSONArray("response");
            for(int j=0;j<teamJsonArray.length();j++) {{
                    JSONObject jo_inside = teamJsonArray.getJSONObject(j);
                    Team team = new Team();
                    team.setTeamName(jo_inside.getString("name"));
                    team.setCaptainName(jo_inside.getString("captain_name"));
                    team.setViceCaptainName(jo_inside.getString("vice_captain_name"));
                    team.setShortDescription(jo_inside.getString("short_description"));
                    team.setImageUrl(jo_inside.getString("logo"));
                    team.setScore(jo_inside.getString("score"));
                    JSONArray departmentArray = jo_inside.getJSONArray("departments");
                    ArrayList<String> deparmentArrayList = new ArrayList<>();
                    for (int k = 0; k < departmentArray.length(); k++) {
                        deparmentArrayList.add(departmentArray.getString(k));
                    }
                    team.setDepartment(deparmentArrayList);//Add your values in your `ArrayList` as below:
                scoreList.add(team);
                }

            }
            Log.d("EVENTS",teamJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return scoreList;
    }

    public static ArrayList<Announcement> getAnnouncement(String result){
        ArrayList<Announcement> announcements = new ArrayList<>();
        try {
            JSONArray announceJsonArray = new JSONObject(result).getJSONArray("response");
            for(int j=0;j<announceJsonArray.length();j++) {
                JSONObject jo_inside = announceJsonArray.getJSONObject(j);
                Announcement announcement = new Announcement();
                announcement.setId(jo_inside.getString("id"));
                announcement.setTitle(jo_inside.getString("title"));
                announcement.setType(jo_inside.getString("type"));
                announcement.setUpdates(jo_inside.getString("updates"));
                announcements.add(announcement);
            }
            Log.d("EVENTS",announceJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return announcements;
    }
//only name t=retreived for context upload
    public static ArrayList<ContextData> getContestToUpload(String result) {
        ArrayList<ContextData> contestsList = new ArrayList<>();
        try {
            JSONArray contestJsonArray = new JSONObject(result).getJSONArray("response");
            for(int j=0;j<contestJsonArray.length();j++) {
                JSONObject jo_inside = contestJsonArray.getJSONObject(j);
                ContextData contextData = new ContextData();
                contextData.setName(jo_inside.getString("name"));
                contestsList.add(contextData);
            }
            Log.d("CONTESTS",contestJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return contestsList;
    }

    public static ArrayList<ContextData> getEventsToUpload(String result) {
        ArrayList<ContextData> contestsList = new ArrayList<>();
        try {
            JSONArray contestJsonArray = new JSONObject(result).getJSONArray("response");
            for(int j=0;j<contestJsonArray.length();j++) {
                JSONObject jo_inside = contestJsonArray.getJSONObject(j);
                ContextData contextData = new ContextData();
                contextData.setName(jo_inside.getString("name"));
                contestsList.add(contextData);
            }
            Log.d("CONTESTS",contestJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return contestsList;
    }

    public static ArrayList<MemeoriesData> getmemoryFeedArrayList(String result) {
        ArrayList<MemeoriesData> memoryList = new ArrayList<>();
        try{
         //   JSONArray memArray = new JSONObject(result).getJSONObject("response").getJSONArray("outpus");
           JSONObject resJsonObject = new JSONObject(result);
            JSONObject responseObject = resJsonObject.getJSONObject("response");
            String imgPrefix ="http://www.thanjavurkingslionsclub.com/sandbox/"+responseObject.getString("image_path");
            JSONArray memArray =  responseObject.getJSONArray("outputs");
            for(int i=0;i<memArray.length();i++){
                JSONObject memObject = memArray.getJSONObject(i);
                MemeoriesData memeoriesData = new MemeoriesData();
                memeoriesData.setId(memObject.getString("id"));
                memeoriesData.setFile_type(memObject.getString("file_type"));
                String fileName =imgPrefix+memObject.getString("file_name");
                memeoriesData.setFile_name(fileName);
                memeoriesData.setEvent_type(memObject.getString("event_type"));
                memeoriesData.setEvent_name(memObject.getString("event_name"));
                memeoriesData.setPost_date_staring(memObject.getString("post_date_staring"));
                Date date = null;
                String cDate = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat convertFormat = new SimpleDateFormat("MMM dd yyyy");

                try {
                    date = dateFormat.parse(memObject.getString("post_date"));
                    cDate = convertFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                memeoriesData.setPost_date(cDate+"");
                memeoriesData.setTotal_vote(memObject.getString("total_vote"));
                memeoriesData.setUser_vote_status(memObject.getString("user_vote_status"));
                memoryList.add(memeoriesData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return memoryList;
    }


    public static ArrayList<SabData> getsabFeedArraylist(String result) {
        ArrayList<SabData> sabDataArrayList = new ArrayList<>();
        try{
            //   JSONArray memArray = new JSONObject(result).getJSONObject("response").getJSONArray("outpus");
            JSONObject resJsonObject = new JSONObject(result);
            JSONObject responseObject = resJsonObject.getJSONObject("response");
            String imgPrefix ="http://www.thanjavurkingslionsclub.com/sandbox/"+responseObject.getString("image_path");
            JSONArray memArray =  responseObject.getJSONArray("outputs");
            for(int i=0;i<memArray.length();i++){
                JSONObject memObject = memArray.getJSONObject(i);
                SabData sabData = new SabData();
                sabData.setId(memObject.getString("id"));
                sabData.setFileType(memObject.getString("file_type"));
                String fileName =imgPrefix+memObject.getString("file_name");
                sabData.setFileName(fileName);
                sabData.setAuditions(memObject.getString("auditions"));
                sabData.setDependent(memObject.getString("dependent"));
                sabData.setShortDescription(memObject.getString("short_description"));
                Date date = null;
                String cDate = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat convertFormat = new SimpleDateFormat("MMM dd yyyy");

                try {
                    date = dateFormat.parse(memObject.getString("post_date"));
                    cDate = convertFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sabData.setPostDateStaring(memObject.getString("post_date_staring"));
                sabData.setPostDate(cDate+"");
                sabData.setTotalVote(memObject.getString("total_vote"));
                sabData.setUserVoteStatus(memObject.getString("user_vote_status"));
                sabDataArrayList.add(sabData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sabDataArrayList;
    }

    public static ArrayList<HubContestsData> getHubContests(String result) {
        ArrayList<HubContestsData> hubs = new ArrayList<>();
        try {
            JSONArray hubJsonArray = new JSONObject(result).getJSONArray("response");
            for(int j=0;j<hubJsonArray.length();j++) {
                JSONObject jo_inside = hubJsonArray.getJSONObject(j);
                HubContestsData hubContestsData = new HubContestsData();
                hubContestsData.setId(jo_inside.getString("id"));
                hubContestsData.setName(jo_inside.getString("name"));
                hubContestsData.setContestDate(jo_inside.getString("contest_date"));
                hubContestsData.setContestLocation(jo_inside.getString("contest_location"));
                hubContestsData.setContestShortDesc(jo_inside.getString("contest_short_desc"));
                hubContestsData.setContestType(jo_inside.getString("contest_type"));
                hubContestsData.setDateString(jo_inside.getString("date_string"));
                hubContestsData.setTimeString(jo_inside.getString("time_string"));
                hubs.add(hubContestsData);
            }
            Log.d("EVENTS",hubJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return hubs;
    }

    public static ArrayList<HubUpdatesData> getHubUpdates(String result) {
        ArrayList<HubUpdatesData> hubs = new ArrayList<>();
        try {
            JSONArray hubJsonArray = new JSONObject(result).getJSONArray("response");
            for(int j=0;j<hubJsonArray.length();j++) {
                JSONObject jo_inside = hubJsonArray.getJSONObject(j);
                HubUpdatesData hubContestsData = new HubUpdatesData();
                hubContestsData.setId(jo_inside.getString("id"));
                hubContestsData.setTitle(jo_inside.getString("title"));
                hubContestsData.setUpdates(jo_inside.getString("updates"));
                hubs.add(hubContestsData);
            }
            Log.d("EVENTS",hubJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return hubs;
    }
 /*   public static ArrayList<SamuhaEvent> getEventArrayList(JSONObject resultString) {
        Log.d("EVENTS",resultString.toString());
        *//*JSONArray respArray = new JSONArray();
        respArray.getJ*//*
        ArrayList<SamuhaEvent> samuhaEventArrayList = new ArrayList<>();

        try {

            JSONArray eventsArray = resultString.getJSONArray("Events");
            //  Log.d("EVENTS",eventsArray.toString());
            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject eventObject = eventsArray.getJSONObject(i);
                String id;
                String eventDate;
                String eventName;
                String eventLocation;
                String imageUrl;
                String shortDescribtion;
                id = eventObject.getString(SKEY_ID);
                eventDate = eventObject.getString("eventdate");
                eventName = eventObject.getString(SKEY_NAME);
                eventLocation = eventObject.getString(SKEY_LOCATION);
                imageUrl = eventObject.getString(SKEY_IMAGE_URL);
                shortDescribtion = eventObject.getString(SKEY_DESCRIPTION);
                SamuhaEvent samuhaEvent = new SamuhaEvent();
                samuhaEvent.setId(id);
                samuhaEvent.setEventDate(eventDate);
                samuhaEvent.setEventName(eventName);
                samuhaEvent.setEventLocation(eventLocation);
                samuhaEvent.setImageUrl(imageUrl);
                samuhaEvent.setShortDescribtion(shortDescribtion);
                samuhaEventArrayList.add(samuhaEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return samuhaEventArrayList;
    }*/
}
