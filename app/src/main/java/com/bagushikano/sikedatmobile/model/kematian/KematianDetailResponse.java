package com.bagushikano.sikedatmobile.model.kematian;

import com.bagushikano.sikedatmobile.model.kelahiran.Kelahiran;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KematianDetailResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Kematian kematian;
    @SerializedName("message")
    @Expose
    private String message;
}
