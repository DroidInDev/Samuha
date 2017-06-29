package com.stgobain.samuha.Model;

/**
 * Created by vignesh on 26-06-2017.
 */

public class MemeoriesData {
    String id;
    String file_type;
    String file_name;
    String event_type;
    String event_name;
    String post_date_staring;
    String post_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getPost_date_staring() {
        return post_date_staring;
    }

    public void setPost_date_staring(String post_date_staring) {
        this.post_date_staring = post_date_staring;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getTotal_vote() {
        return total_vote;
    }

    public void setTotal_vote(String total_vote) {
        this.total_vote = total_vote;
    }

    public String getUser_vote_status() {
        return user_vote_status;
    }

    public void setUser_vote_status(String user_vote_status) {
        this.user_vote_status = user_vote_status;
    }

    String total_vote;
    String user_vote_status;
}
