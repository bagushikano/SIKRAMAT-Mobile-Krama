package com.bagushikano.sikedatmobile.model.kematian;

import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KematianAjuanDetailResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private KematianAjuan kematianAjuan;
    @SerializedName("message")
    @Expose
    private String message;
}
