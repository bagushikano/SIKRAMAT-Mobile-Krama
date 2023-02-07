package com.bagushikano.sikedatmobile.model.krama;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnggotaKramaMipilGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public List<AnggotaKramaMipil> anggotaKramaMipilList = null;
    @SerializedName("message")
    @Expose
    public String message;
}
