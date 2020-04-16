package com.example.uibeautifulcollection2.check;

public class Upload {
    private String mNamel;
    private String ImageUrl;

    public Upload() {

    }

    public Upload(String mNamel, String imageUrl) {
        if(mNamel.trim().equals("")){
            mNamel = "No name";
        }
        this.mNamel = mNamel;
        ImageUrl = imageUrl;
    }

    public String getmNamel() {
        return mNamel;
    }

    public void setmNamel(String mNamel) {
        this.mNamel = mNamel;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
