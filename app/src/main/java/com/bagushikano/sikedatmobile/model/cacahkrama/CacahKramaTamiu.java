package com.bagushikano.sikedatmobile.model.cacahkrama;

import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.bagushikano.sikedatmobile.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobile.model.master.banjar.BanjarDinas;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacahKramaTamiu {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("banjar_adat_id")
    @Expose
    private Integer banjarAdatId;
    @SerializedName("penduduk_id")
    @Expose
    private Integer pendudukId;
    @SerializedName("banjar_dinas_id")
    @Expose
    private Integer banjarDinasId;
    @SerializedName("nomor_cacah_krama_tamiu")
    @Expose
    private String nomorCacahKramaTamiu;
    @SerializedName("tanggal_masuk")
    @Expose
    private String tanggalMasuk;
    @SerializedName("tanggal_keluar")
    @Expose
    private Object tanggalKeluar;
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
    @SerializedName("asal")
    @Expose
    private String asal;
    @SerializedName("alamat_asal")
    @Expose
    private String alamatAsal;
    @SerializedName("banjar_adat_asal_id")
    @Expose
    private Integer banjarAdatAsalId;
    @SerializedName("desa_asal_id")
    @Expose
    private Object desaAsalId;
    @SerializedName("penduduk")
    @Expose
    private Penduduk penduduk;
    @SerializedName("banjar_adat")
    @Expose
    private BanjarAdat banjarAdat;
    @SerializedName("banjar_dinas")
    @Expose
    private BanjarDinas banjarDinas;
}
