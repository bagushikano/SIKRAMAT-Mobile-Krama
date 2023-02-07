package com.bagushikano.sikedatmobile.model.master.banjar;

import com.bagushikano.sikedatmobile.model.master.DesaAdat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanjarAdat {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("desa_adat_id")
    @Expose
    private Integer desaAdatId;
    @SerializedName("kode_banjar_adat")
    @Expose
    private String kodeBanjarAdat;
    @SerializedName("nama_banjar_adat")
    @Expose
    private String namaBanjarAdat;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("desa_adat")
    @Expose
    private DesaAdat desaAdat;
}
