package el.team_application.Models.Entities;

import android.provider.ContactsContract;

import java.util.Date;

/**
 * Created by ariel-mac on 24/05/2015.
 */

//abstract class to only inherent

public class Member {

     //members - (default protected)
    String Id;
    String Name;
    Date JoinDate;
    String EmailAddress;
    Long phoneNumber;


    //getters and setters
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public Date getJoinDate() {
        return JoinDate;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public String getJobTitle() {
        return JobTitle;
    }
//setters
    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public void setJoinDate(Date joinDate) {
        JoinDate = joinDate;
    }

    public void setName(String name) {
        Name = name;
    }

    String JobTitle; //job can be Manager/Employee 1/2 using ENUM

    //C'tor
    public Member(String id, String name, String emailAddress) {
        Id = id;
        Name = name;
        EmailAddress = emailAddress;
    }


}
