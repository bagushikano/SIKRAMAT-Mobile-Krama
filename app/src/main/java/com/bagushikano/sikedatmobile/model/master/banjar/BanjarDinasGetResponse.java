package com.bagushikano.sikedatmobile.model.master.banjar;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanjarDinasGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public BanjarDinasPaginate banjarDinasPaginate;
    @SerializedName("message")
    @Expose
    public String message;
}
