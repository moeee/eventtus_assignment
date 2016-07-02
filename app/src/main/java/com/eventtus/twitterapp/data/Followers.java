package com.eventtus.twitterapp.data;


public class Followers {
    private String name;
    private String handle;
    private String description;
    private String profImage;
    private String bgImage;

    public void setName(String name){
        this.name=name;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setHandle(String handle){
        this.handle=handle;
    }
    public void setProfImage(String profImage){
        this.profImage = profImage;
    }
    public void setBgImage(String bgImage){
        this.bgImage = bgImage;
    }

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getHandle(){
        return handle;
    }
    public String getProfImage(){
        return profImage;
    }
    public String getBgImage(){
        return bgImage;
    }
}
