package com.bagushikano.sikedatmobile.model.krama;

import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaTamiu;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnggotaKramaTamiu {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("krama_tamiu_id")
    @Expose
    private Integer kramaTamiuId;
    @SerializedName("cacah_krama_tamiu_id")
    @Expose
    private Integer cacahKramaTamiuId;
    @SerializedName("status_hubungan")
    @Expose
    private String statusHubungan;
    @SerializedName("tanggal_registrasi")
    @Expose
    private String tanggalRegistrasi;
    @SerializedName("tanggal_nonaktif")
    @Expose
    private Object tanggalNonaktif;
    @SerializedName("alasan_keluar")
    @Expose
    private Object alasanKeluar;
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
    @SerializedName("cacah_krama_tamiu")
    @Expose
    private CacahKramaTamiu cacahKramaTamiu;
}
