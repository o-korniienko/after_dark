package com.work.olexii.after_dark.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Guild {

    private String name;
    private String realm;
    private List<Character> members;


    public Guild() {
        members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public List<Character> getMembers() {
        return members;
    }

    public void setMembers(List<Character> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Guild{" +
                "name='" + name + '\'' +
                ", realm='" + realm + '\'' +
                ", members=" + members +
                '}';
    }
}
