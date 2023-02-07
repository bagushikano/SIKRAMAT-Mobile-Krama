package com.bagushikano.sikedatmobile.api;

import com.bagushikano.sikedatmobile.model.AuthResponse;
import com.bagushikano.sikedatmobile.model.PendudukGetResponse;
import com.bagushikano.sikedatmobile.model.RegisterCheckNikResponse;
import com.bagushikano.sikedatmobile.model.RegisterResponse;
import com.bagushikano.sikedatmobile.model.ResponseGeneral;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipilDetailResponse;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaTamiuDetailResponse;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahMipilTamiuGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuan;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuanDetailResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranDetailResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranPengajuanResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianAjuanDetailResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianDetailResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianGetCacahMipilResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianGetResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianPengajuanResponse;
import com.bagushikano.sikedatmobile.model.krama.AnggotaKramaTamiuGetResponse;
import com.bagushikano.sikedatmobile.model.krama.KramaMipilGetResponse;
import com.bagushikano.sikedatmobile.model.krama.KramaTamiuDetailResponse;
import com.bagushikano.sikedatmobile.model.krama.KramaTamiuGetResponse;
import com.bagushikano.sikedatmobile.model.notifikasi.NotifikasiGetResponse;
import com.bagushikano.sikedatmobile.model.profile.ProfileEditFotoResponse;
import com.bagushikano.sikedatmobile.model.profile.ProfileEditResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiRoute {

    /**
     *  Auth related thing start here
     */

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("login")
    Call<AuthResponse> loginUser(
            @Field("email") String username,
            @Field("password") String password
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("user/change-password")
    Call<ResponseGeneral> changePassword(
            @Header("Authorization") String authHeader,
            @Field("old_password") String oldPass,
            @Field("password") String password
    );



    /**
     * Register related thing start here
     */

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("regis-check-nik")
    Call<RegisterCheckNikResponse> registerCheckNik(
            @Field("nik") String nik
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("regis")
    Call<RegisterResponse> registerUser(
            @Field("penduduk_id") Integer pendudukId,
            @Field("email") String email,
            @Field("password") String password
    );


    /**
     * Penduduk related thing start here
     */

    @Headers({"Accept: application/json"})
    @GET("user/penduduk/get")
    Call<PendudukGetResponse> getPenduduk(
            @Header("Authorization") String authHeader
    );

    /**
     * Krama Mipil related thing start here
     */

    @Headers({"Accept: application/json"})
    @GET("user/krama/get-mipil")
    Call<KramaMipilGetResponse> getKramaMipil(
            @Header("Authorization") String authHeader
    );


    /**
     * Krama tamiu related here
     */


    @Headers({"Accept: application/json"})
    @GET("user/krama/get-tamiu")
    Call<KramaTamiuGetResponse> getKramaTamiu(
            @Header("Authorization") String authHeader
    );


    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-krama-tamiu/{id}")
    Call<KramaTamiuDetailResponse> getDetailKramaTamiu(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-krama-tamiu-detail/{id}")
    Call<AnggotaKramaTamiuGetResponse> getKramaTamiuDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );




    /**
     * Cacah krama related thing start here
     */

    @Headers({"Accept: application/json"})
    @GET("user/cacah-krama/get-cacah-krama")
    Call<CacahMipilTamiuGetResponse> getCacahMipilTamiu(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("user/cacah-krama/get-cacah-mipil")
    Call<CacahKramaMipilDetailResponse> getCacahMipilDetail(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/cacah-krama/get-mipil-detail/{id}")
    Call<CacahKramaMipilDetailResponse> getCacahKramaMipilDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/cacah-krama/get-cacah-krama-tamiu-detail/{id}")
    Call<CacahKramaTamiuDetailResponse> getCacahKramaTamiulDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );



    /**
     * Kelahiran related thing start here
     */

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("user/kelahiran/create-pengajuan")
    Call<KelahiranPengajuanResponse> kelahiranPengajuan(
            @Header("Authorization") String authHeader,
            @Part("nik") RequestBody nik,
            @Part("nama") RequestBody nama,
            @Part("tempat_lahir") RequestBody tempatLahir,
            @Part("tanggal_lahir") RequestBody tanggalLahir,
            @Part("jenis_kelamin") RequestBody jenisKelamin,
            @Part("golongan_darah") RequestBody golonganDarah,
            @Part("alamat") RequestBody alamat,
            @Part("ayah_kandung") RequestBody ayahKandung,
            @Part("ibu_kandung") RequestBody ibuKandung,
            @Part("nomor_akta_kelahiran") RequestBody noAktaKelahiran,
            @Part("keterangan") RequestBody keterangan,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part foto
    );

    @Headers({"Accept: application/json"})
    @GET("user/kelahiran/get")
    Call<KelahiranGetResponse> getKelahiran(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("user/kelahiran/detail/{id}")
    Call<KelahiranDetailResponse> getKelahiranDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("user/kelahiran/get-ajuan")
    Call<KelahiranAjuanGetResponse> getKelahiranAjuan(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("user/kelahiran/detail-ajuan/{id}")
    Call<KelahiranAjuanDetailResponse> getKelahiranAjuanDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );




    /**
     * Kematian related thing start here
     */
    @Headers({"Accept: application/json"})
    @Multipart
    @POST("user/kematian/create-pengajuan")
    Call<KematianPengajuanResponse> kematianPengajuan(
            @Header("Authorization") String authHeader,
            @Part("cacah_krama_mipil") RequestBody cacahKramaMipil,
            @Part("tanggal_kematian") RequestBody tanggalKematian,
            @Part("penyebab_kematian") RequestBody penyebabKematian,
            @Part("nomor_akta_kematian") RequestBody nomorAktaKematian,
            @Part("nomor_suket_kematian") RequestBody nomorSuketKematian,
            @Part("keterangan") RequestBody keterangan,
            @Part MultipartBody.Part file_suket_kematian,
            @Part MultipartBody.Part file_akta_kematian
    );

    @Headers({"Accept: application/json"})
    @GET("user/kematian/get-list-cacah-mipil")
    Call<KematianGetCacahMipilResponse> kematianGetCacahMipil(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("user/kematian/get")
    Call<KematianGetResponse> getKematian(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("user/kematian/detail/{id}")
    Call<KematianDetailResponse> getKematianDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );


    @Headers({"Accept: application/json"})
    @GET("user/kematian/get-ajuan")
    Call<KematianAjuanGetResponse> getKematianAjuan(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("user/kematian/detail-ajuan/{id}")
    Call<KematianAjuanDetailResponse> getKematianAjuanDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    /**
     * Profile related things start here
     */
    @Headers({"Accept: application/json"})
    @Multipart
    @POST("user/profile/edit-foto")
    Call<ProfileEditFotoResponse> profileEditFoto(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part foto
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("user/profile/edit-profile")
    Call<ProfileEditResponse> profileEdit(
            @Header("Authorization") String authHeader,
            @Field("nama") String username,
            @Field("gelar_depan") String gelarDepan,
            @Field("gelar_belakang") String gelarBelakang,
            @Field("nama_alias") String namaAlias,
            @Field("telepon") String telepon,
            @Field("alamat") String alamat,
            @Field("koordinat_alamat") String koordinatAlamat
    );


    /**
     * Notifikasi things
     */

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("notifikasi/store-firebase-token")
    Call<ResponseGeneral> sendFcmToken(
            @Header("Authorization") String authHeader,
            @Field("firebase_token") String fcmToken
    );

    @Headers({"Accept: application/json"})
    @GET("notifikasi/get-notifikasi/krama")
    Call<NotifikasiGetResponse> getNotifikasi(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("notifikasi/read-all-notifikasi/krama")
    Call<NotifikasiGetResponse> readAllNotifikasi(
            @Header("Authorization") String authHeader
    );
}
