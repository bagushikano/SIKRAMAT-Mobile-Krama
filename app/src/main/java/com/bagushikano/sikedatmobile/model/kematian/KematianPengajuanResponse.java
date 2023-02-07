package com.bagushikano.sikedatmobile.model.kematian;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KematianPengajuanResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private KematianAjuan kematian;
    @SerializedName("message")
    @Expose
    private String message;
}
