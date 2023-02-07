package com.bagushikano.sikedatmobile.model.krama;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KramaMipilDetailResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public KramaMipil kramaMipil;
    @SerializedName("message")
    @Expose
    public String message;

}
