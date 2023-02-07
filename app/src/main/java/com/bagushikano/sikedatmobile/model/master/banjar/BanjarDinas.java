package com.bagushikano.sikedatmobile.model.master.banjar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanjarDinas {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("desa_adat_id")
    @Expose
    public Integer desaAdatId;
    @SerializedName("desa_dinas_id")
    @Expose
    public String desaDinasId;
    @SerializedName("kode_banjar_dinas")
    @Expose
    public String kodeBanjarDinas;
    @SerializedName("nama_banjar_dinas")
    @Expose
    public String namaBanjarDinas;
    @SerializedName("jenis_banjar_dinas")
    @Expose
    public String jenisBanjarDinas;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
}
