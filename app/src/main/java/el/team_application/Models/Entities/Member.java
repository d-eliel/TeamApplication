package el.team_application.Models.Entities;

import android.provider.ContactsContract;

import java.util.Date;

/**
 * Created by ariel-mac on 24/05/2015.
 */

//abstract class to only inherent

public class Member {

     //members - (default protected)
    String id;
    String userId;
    String joinDate;
    String jobTitle;

    // constructor

    public Member(String id, String userId, String jobTitle, String joinDate) {
        this.id = id;
        this.userId = userId;
        this.jobTitle = jobTitle;
        this.joinDate = joinDate;
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }


}
