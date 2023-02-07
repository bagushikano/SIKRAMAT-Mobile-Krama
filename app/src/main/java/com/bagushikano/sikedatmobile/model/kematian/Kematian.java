package com.bagushikano.sikedatmobile.model.kematian;

import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kematian {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cacah_krama_mipil_id")
    @Expose
    private Integer cacahKramaMipilId;
    @SerializedName("banjar_adat_id")
    @Expose
    private Integer banjarAdatId;
    @SerializedName("nomor_akta_kematian")
    @Expose
    private String nomorAktaKematian;
    @SerializedName("file_akta_kematian")
    @Expose
    private String fileAktaKematian;
    @SerializedName("nomor_suket_kematian")
    @Expose
    private String nomorSuketKematian;
    @SerializedName("file_suket_kematian")
    @Expose
    private String fileSuketKematian;
    @SerializedName("tanggal_kematian")
    @Expose
    private String tanggalKematian;
    @SerializedName("penyebab_kematian")
    @Expose
    private String penyebabKematian;
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
    @SerializedName("alasan_tolak_ajuan")
    @Expose
    private String alasanTolakAjuan;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
}
