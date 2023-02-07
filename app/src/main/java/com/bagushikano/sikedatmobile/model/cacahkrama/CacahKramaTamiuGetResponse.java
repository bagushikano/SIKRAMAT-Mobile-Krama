package com.bagushikano.sikedatmobile.model.cacahkrama;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacahKramaTamiuGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public CacahKramaTamiuPaginate cacahKramaTamiuPaginate;
    @SerializedName("message")
    @Expose
    public String message;
}
