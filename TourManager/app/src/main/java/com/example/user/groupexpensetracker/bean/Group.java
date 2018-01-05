package com.example.user.groupexpensetracker.bean;

/**
 * Created by Tanuja on 28-09-2016.
 */
public class Group {
        private String groupid,groupname,groupparticipant;

    public Group()
    {}

        public Group(String groupid, String groupname, String groupparticipant) {
            this.groupid = groupid;
            this.groupname = groupname;
            this.groupparticipant = groupparticipant;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getGroupparticipant() {
            return groupparticipant;
        }

        public void setGroupparticipant(String groupparticipant) {
            this.groupparticipant = groupparticipant;
        }
    }




