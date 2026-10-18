package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

import java.util.ArrayList;

class Participant{
    private String id;
    private String number;
    private String name;
    private boolean isAdmin;
    private boolean isSuperAdmin;

    public Participant() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }
}

public class GroupInfo{
    private String id;
    private String name;
    private Object linkedParentJID;
    private String description;
    private String owner;
    private int createdAt;
    private ArrayList<Participant> participants;
    private boolean isReadOnly;
    private boolean isAnnounce;
    private boolean announce;
    private boolean locked;
    private int ephemeralSeconds;
    private String memberAddMode;

    public GroupInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLinkedParentJID() {
        return linkedParentJID;
    }

    public void setLinkedParentJID(Object linkedParentJID) {
        this.linkedParentJID = linkedParentJID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public boolean isAnnounce() {
        return isAnnounce;
    }

    public void setAnnounce(boolean announce) {
        isAnnounce = announce;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getEphemeralSeconds() {
        return ephemeralSeconds;
    }

    public void setEphemeralSeconds(int ephemeralSeconds) {
        this.ephemeralSeconds = ephemeralSeconds;
    }

    public String getMemberAddMode() {
        return memberAddMode;
    }

    public void setMemberAddMode(String memberAddMode) {
        this.memberAddMode = memberAddMode;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
