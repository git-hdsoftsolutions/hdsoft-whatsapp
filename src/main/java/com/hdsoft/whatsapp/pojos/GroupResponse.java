package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

public class GroupResponse {

    private String id;
    private String name;
    private String linkedParentJID;

    public GroupResponse() {
    }

    public GroupResponse(String id, String name, String linkedParentJID) {
        this.id = id;
        this.name = name;
        this.linkedParentJID = linkedParentJID;
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

    public String getLinkedParentJID() {
        return linkedParentJID;
    }

    public void setLinkedParentJID(String linkedParentJID) {
        this.linkedParentJID = linkedParentJID;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
