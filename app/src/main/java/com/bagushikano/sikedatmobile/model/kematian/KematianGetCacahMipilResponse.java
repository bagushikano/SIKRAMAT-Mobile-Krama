package com.bagushikano.sikedatmobile.model.kematian;

import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KematianGetCacahMipilResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<CacahKramaMipil> cacahKramaMipilList = null;
    @SerializedName("message")
    @Expose
    private String message;
}
