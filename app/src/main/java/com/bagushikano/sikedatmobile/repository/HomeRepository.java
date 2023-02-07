package com.bagushikano.sikedatmobile.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahMipilTamiuGetResponse;
import com.bagushikano.sikedatmobile.model.krama.KramaMipilGetResponse;
import com.bagushikano.sikedatmobile.model.krama.KramaTamiuGetResponse;
import com.bagushikano.sikedatmobile.model.notifikasi.NotifikasiGetResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {
    private static HomeRepository homeRepository;

    public static HomeRepository getInstance() {
        if (homeRepository == null) {
            homeRepository = new HomeRepository();
        }
        return homeRepository;
    }

    private ApiRoute getData;

    public HomeRepository(){
        getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
    }

    public MutableLiveData<CacahMipilTamiuGetResponse> getCacahMipilTamiu(String token){
        MutableLiveData<CacahMipilTamiuGetResponse> cacahMipilTamiuGetResponseMutableLiveData = new MutableLiveData<>();
        Call<CacahMipilTamiuGetResponse> cacahMipilTamiuGetResponseCall = getData.getCacahMipilTamiu("Bearer " + token);
        cacahMipilTamiuGetResponseCall.enqueue(new Callback<CacahMipilTamiuGetResponse>() {
            @Override
            public void onResponse(Call<CacahMipilTamiuGetResponse> call, Response<CacahMipilTamiuGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data penduduk sukses")) {
                    cacahMipilTamiuGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    cacahMipilTamiuGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CacahMipilTamiuGetResponse> call, Throwable t) {
                cacahMipilTamiuGetResponseMutableLiveData.setValue(null);
            }
        });
        return cacahMipilTamiuGetResponseMutableLiveData;
    }

    public MutableLiveData<KramaMipilGetResponse> getKramaMipil(String token){
        MutableLiveData<KramaMipilGetResponse> kramaMipilGetResponseMutableLiveData = new MutableLiveData<>();
        Call<KramaMipilGetResponse> kramaMipilGetResponseCall = getData.getKramaMipil("Bearer " + token);
        kramaMipilGetResponseCall.enqueue(new Callback<KramaMipilGetResponse>() {
            @Override
            public void onResponse(Call<KramaMipilGetResponse> call, Response<KramaMipilGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data krama mipil sukses")) {
                    kramaMipilGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    kramaMipilGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<KramaMipilGetResponse> call, Throwable t) {
                kramaMipilGetResponseMutableLiveData.setValue(null);
            }
        });
        return kramaMipilGetResponseMutableLiveData;
    }

    public MutableLiveData<KramaTamiuGetResponse> getKramaTamiu(String token){
        MutableLiveData<KramaTamiuGetResponse> kramaTamiuGetResponseMutableLiveData = new MutableLiveData<>();
        Call<KramaTamiuGetResponse> kramaTamiuGetResponseCall = getData.getKramaTamiu("Bearer " + token);
        kramaTamiuGetResponseCall.enqueue(new Callback<KramaTamiuGetResponse>() {
            @Override
            public void onResponse(Call<KramaTamiuGetResponse> call, Response<KramaTamiuGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data krama tamiu sukses")) {
                    kramaTamiuGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    kramaTamiuGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<KramaTamiuGetResponse> call, Throwable t) {
                kramaTamiuGetResponseMutableLiveData.setValue(null);
            }
        });
        return kramaTamiuGetResponseMutableLiveData;
    }

    public MutableLiveData<NotifikasiGetResponse> getNotifikasi(String token){
        MutableLiveData<NotifikasiGetResponse> notifikasiGetResponseMutableLiveData = new MutableLiveData<>();
        Call<NotifikasiGetResponse> notifikasiGetResponseCall = getData.getNotifikasi("Bearer " + token);
        notifikasiGetResponseCall.enqueue(new Callback<NotifikasiGetResponse>() {
            @Override
            public void onResponse(Call<NotifikasiGetResponse> call, Response<NotifikasiGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data notifikasi sukses")) {
                    notifikasiGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    notifikasiGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NotifikasiGetResponse> call, Throwable t) {
                notifikasiGetResponseMutableLiveData.setValue(null);
            }
        });
        return notifikasiGetResponseMutableLiveData;
    }
}
