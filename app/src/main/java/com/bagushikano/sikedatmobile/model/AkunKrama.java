package com.bagushikano.sikedatmobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AkunKrama {
    @SerializedName("penduduk_id")
    @Expose
    public String pendudukId;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("id")
    @Expose
    public Integer id;
}
