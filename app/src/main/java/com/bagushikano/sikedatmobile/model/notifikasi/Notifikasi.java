package com.bagushikano.sikedatmobile.model.notifikasi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notifikasi {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("banjar_adat_id")
    @Expose
    private Integer banjarAdatId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("data_id")
    @Expose
    private Integer dataId;
    @SerializedName("konten")
    @Expose
    private String konten;
    @SerializedName("jenis")
    @Expose
    private Integer jenis;
    @SerializedName("sub_jenis")
    @Expose
    private String subJenis;
    @SerializedName("is_read")
    @Expose
    private Integer isRead;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("converted_created_at")
    @Expose
    private String convertedCreatedAt;
}
