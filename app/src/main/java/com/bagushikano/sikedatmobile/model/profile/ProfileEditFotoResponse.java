package com.bagushikano.sikedatmobile.model.profile;

import com.bagushikano.sikedatmobile.model.krama.AnggotaKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileEditFotoResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public String foto;
    @SerializedName("message")
    @Expose
    public String message;
}
