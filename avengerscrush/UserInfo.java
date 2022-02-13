package com.example.avengerscrush;

import java.io.Serializable;

public class UserInfo implements Serializable,Comparable<UserInfo> {
    private String mName;
    private int mScore;
    private boolean mIsAsset;

    public UserInfo(String name, int score,  boolean isAsset) {
        this.mName = name;
        this.mScore = score;
        this.mIsAsset = isAsset;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getScore() {
        return mScore;
    }
    @Override
    public int compareTo(UserInfo userInfo) {
        if(this.mScore == userInfo.mScore){
            return Integer.compare(this.getScore(),userInfo.getScore());
        } else {
            return Integer.compare(userInfo.mScore, this.mScore);
        }
    }
}
