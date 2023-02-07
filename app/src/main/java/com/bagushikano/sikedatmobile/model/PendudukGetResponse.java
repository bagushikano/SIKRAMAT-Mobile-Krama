package com.bagushikano.sikedatmobile.model;

import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendudukGetResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Penduduk penduduk;
    @SerializedName("message")
    @Expose
    private String message;
}
