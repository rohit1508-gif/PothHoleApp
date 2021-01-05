package com.example.pothhole;

public class locate {
    String image;
    String location;
    String sender;
    String _id;
    public locate(){}
    public locate(String image,String location,String sender,String _id){
        this.image = image;
        this.location=location;
        this.sender = sender;
        this._id = _id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
