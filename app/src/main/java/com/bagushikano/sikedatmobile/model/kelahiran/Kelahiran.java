package com.bagushikano.sikedatmobile.model.kelahiran;


import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kelahiran {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nomor_akta_kelahiran")
    @Expose
    private String nomorAktaKelahiran;
    @SerializedName("cacah_krama_mipil_id")
    @Expose
    private Integer cacahKramaMipilId;
    @SerializedName("banjar_adat_id")
    @Expose
    private Integer banjarAdatId;
    @SerializedName("krama_mipil_id")
    @Expose
    private Integer kramaMipilId;
    @SerializedName("file_akta_kelahiran")
    @Expose
    private String fileAktaKelahiran;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("tanggal_lahir")
    @Expose
    private Object tanggalLahir;
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
