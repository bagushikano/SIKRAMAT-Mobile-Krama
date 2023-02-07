package com.bagushikano.sikedatmobile.model.cacahkrama;

import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacahMipilTamiuGetResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("penduduk")
    @Expose
    private Penduduk penduduk;
    @SerializedName("cacahKramaMipil")
    @Expose
    private CacahKramaMipil cacahKramaMipil;
    @SerializedName("cacahKramaTamiu")
    @Expose
    private CacahKramaTamiu cacahKramaTamiu;
    @SerializedName("message")
    @Expose
    private String message;
}
