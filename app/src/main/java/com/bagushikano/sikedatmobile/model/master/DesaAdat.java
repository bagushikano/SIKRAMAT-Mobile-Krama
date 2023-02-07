package com.bagushikano.sikedatmobile.model.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesaAdat {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("desadat_jenis_id")
    @Expose
    public Object desadatJenisId;
    @SerializedName("desadat_nama")
    @Expose
    public String desadatNama;
    @SerializedName("desadat_kode")
    @Expose
    public String desadatKode;
    @SerializedName("desadat_kantor_long")
    @Expose
    public Object desadatKantorLong;
    @SerializedName("desadat_kantor_lat")
    @Expose
    public Object desadatKantorLat;
    @SerializedName("desadat_bendesa_nama")
    @Expose
    public String desadatBendesaNama;
    @SerializedName("desadat_penyarikan_nama")
    @Expose
    public String desadatPenyarikanNama;
    @SerializedName("desadat_petengen")
    @Expose
    public Object desadatPetengen;
    @SerializedName("desadat_nomor_register")
    @Expose
    public Object desadatNomorRegister;
    @SerializedName("desadat_alamat_kantor")
    @Expose
    public Object desadatAlamatKantor;
    @SerializedName("desadat_telpon_kantor")
    @Expose
    public Object desadatTelponKantor;
    @SerializedName("desadat_fax_kantor")
    @Expose
    public Object desadatFaxKantor;
    @SerializedName("desadat_email")
    @Expose
    public Object desadatEmail;
    @SerializedName("desadat_web")
    @Expose
    public Object desadatWeb;
    @SerializedName("desadat_luas")
    @Expose
    public Object desadatLuas;
    @SerializedName("desadat_sejarah")
    @Expose
    public Object desadatSejarah;
    @SerializedName("desadat_file_struktur_pem")
    @Expose
    public Object desadatFileStrukturPem;
    @SerializedName("desadat_hp_kontak1")
    @Expose
    public String desadatHpKontak1;
    @SerializedName("desadat_hp_kontak2")
    @Expose
    public Object desadatHpKontak2;
    @SerializedName("desadat_wa_kontak_1")
    @Expose
    public Object desadatWaKontak1;
    @SerializedName("desadat_wa_kontak_2")
    @Expose
    public Object desadatWaKontak2;
    @SerializedName("kecamatan_id")
    @Expose
    public Integer kecamatanId;
    @SerializedName("kabkot_id")
    @Expose
    public Integer kabkotId;
    @SerializedName("desadat_ttd_lokasi")
    @Expose
    public String desadatTtdLokasi;
    @SerializedName("desadat_punya_lpd")
    @Expose
    public Integer desadatPunyaLpd;
    @SerializedName("desadat_jum_staf")
    @Expose
    public Integer desadatJumStaf;
    @SerializedName("desadat_jum_banjar")
    @Expose
    public Integer desadatJumBanjar;
    @SerializedName("desadat_jum_kk_mipil")
    @Expose
    public Integer desadatJumKkMipil;
    @SerializedName("desadat_jum_krama_mipil")
    @Expose
    public Integer desadatJumKramaMipil;
    @SerializedName("desadat_jum_kk_krama_tamiu")
    @Expose
    public Integer desadatJumKkKramaTamiu;
    @SerializedName("desadat_jum_krama_tamiu")
    @Expose
    public Integer desadatJumKramaTamiu;
    @SerializedName("desadat_jum_kk_tamiu")
    @Expose
    public Integer desadatJumKkTamiu;
    @SerializedName("desadat_jum_tamiu")
    @Expose
    public Integer desadatJumTamiu;
    @SerializedName("desadat_sebutan_pemimpin")
    @Expose
    public String desadatSebutanPemimpin;
    @SerializedName("desadat_status_aktif")
    @Expose
    public Integer desadatStatusAktif;
    @SerializedName("password_temp")
    @Expose
    public Object passwordTemp;
    @SerializedName("created_at")
    @Expose
    public Object createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
}
