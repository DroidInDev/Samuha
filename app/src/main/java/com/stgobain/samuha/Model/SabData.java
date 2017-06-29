package com.stgobain.samuha.Model;

/**
 * Created by vignesh on 28-06-2017.
 */

public class SabData {
    private String id;
    private String fileType;
    private String fileName;
    private String shortDescription;
    private String auditions;
    private String dependent;
    private String postDateStaring;
    private String postDate;
    private String totalVote;
    private String userVoteStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getAuditions() {
        return auditions;
    }

    public void setAuditions(String auditions) {
        this.auditions = auditions;
    }

    public String getDependent() {
        return dependent;
    }

    public void setDependent(String dependent) {
        this.dependent = dependent;
    }

    public String getPostDateStaring() {
        return postDateStaring;
    }

    public void setPostDateStaring(String postDateStaring) {
        this.postDateStaring = postDateStaring;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getTotalVote() {
        return totalVote;
    }

    public void setTotalVote(String totalVote) {
        this.totalVote = totalVote;
    }

    public String getUserVoteStatus() {
        return userVoteStatus;
    }

    public void setUserVoteStatus(String userVoteStatus) {
        this.userVoteStatus = userVoteStatus;
    }

}
