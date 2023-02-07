package com.bagushikano.sikedatmobile.model.krama;

import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaTamiu;
import com.bagushikano.sikedatmobile.model.master.banjar.BanjarAdat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KramaTamiu {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nomor_krama_tamiu")
    @Expose
    private String nomorKramaTamiu;
    @SerializedName("banjar_adat_id")
    @Expose
    private Integer banjarAdatId;
    @SerializedName("cacah_krama_tamiu_id")
    @Expose
    private Integer cacahKramaTamiuId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("alasan_perubahan")
    @Expose
    private String alasanPerubahan;
    @SerializedName("alasan_keluar")
    @Expose
    private Object alasanKeluar;
    @SerializedName("tanggal_registrasi")
    @Expose
    private String tanggalRegistrasi;
    @SerializedName("tanggal_nonaktif")
    @Expose
    private Object tanggalNonaktif;
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
    @SerializedName("anggota")
    @Expose
    private List<AnggotaKramaTamiu> anggotaKramaTamiuList = null;
    @SerializedName("banjar_adat")
    @Expose
    private BanjarAdat banjarAdat;
}
