package com.bagushikano.sikedatmobile.model.krama;

import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnggotaKramaMipil {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("krama_mipil_id")
    @Expose
    private Integer kramaMipilId;
    @SerializedName("cacah_krama_mipil_id")
    @Expose
    private Integer cacahKramaMipilId;
    @SerializedName("status_hubungan")
    @Expose
    private String statusHubungan;
    @SerializedName("tanggal_registrasi")
    @Expose
    private String tanggalRegistrasi;
    @SerializedName("tanggal_nonaktif")
    @Expose
    private Object tanggalNonaktif;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("cacah_krama_mipil")
    @Expose
    private CacahKramaMipil cacahKramaMipil;
}
