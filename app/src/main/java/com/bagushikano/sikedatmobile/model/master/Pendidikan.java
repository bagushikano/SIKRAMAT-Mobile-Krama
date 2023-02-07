package com.bagushikano.sikedatmobile.model.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class Pendidikan {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("jenjang_pendidikan")
    @Expose
    public String jenjangPendidikan;
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
