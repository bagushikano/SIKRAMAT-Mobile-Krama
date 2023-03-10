package com.bagushikano.sikedatmobile.model.krama;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KramaTamiuGetResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private KramaTamiu kramaTamiu;
    @SerializedName("message")
    @Expose
    private String message;

}
