package com.bagushikano.sikedatmobile.model.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class Pekerjaan {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("profesi")
    @Expose
    public String profesi;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public Object updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
}
