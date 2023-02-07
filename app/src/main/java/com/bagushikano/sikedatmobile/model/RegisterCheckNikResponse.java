package com.bagushikano.sikedatmobile.model;

import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCheckNikResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public Penduduk penduduk;
    @SerializedName("message")
    @Expose
    public String message;
}
