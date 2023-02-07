package com.bagushikano.sikedatmobile.model.notifikasi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmMessage {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("data_id")
    @Expose
    public Integer dataId;
    @SerializedName("user_type")
    @Expose
    public Integer userType;
}
