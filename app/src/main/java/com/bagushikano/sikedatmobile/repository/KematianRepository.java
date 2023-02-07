package com.bagushikano.sikedatmobile.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranGetResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianGetResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KematianRepository {
    private static KematianRepository kematianRepository;

    public static KematianRepository getInstance() {
        if (kematianRepository == null) {
            kematianRepository = new KematianRepository();
        }
        return kematianRepository;
    }

    private ApiRoute getData;

    public KematianRepository(){
        getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
    }

    public MutableLiveData<KematianGetResponse> getKematian(String token){
        MutableLiveData<KematianGetResponse> kematianGetResponseMutableLiveData = new MutableLiveData<>();
        Call<KematianGetResponse> cacahMipilTamiuGetResponseCall = getData.getKematian("Bearer " + token);
        cacahMipilTamiuGetResponseCall.enqueue(new Callback<KematianGetResponse>() {
            @Override
            public void onResponse(Call<KematianGetResponse> call, Response<KematianGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    kematianGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    kematianGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<KematianGetResponse> call, Throwable t) {
                kematianGetResponseMutableLiveData.setValue(null);
            }
        });
        return kematianGetResponseMutableLiveData;
    }

    public MutableLiveData<KematianAjuanGetResponse> getKematianAjuanData(String token){
        MutableLiveData<KematianAjuanGetResponse> kematianAjuanGetResponseMutableLiveData = new MutableLiveData<>();
        Call<KematianAjuanGetResponse> kematianAjuanGetResponseCall = getData.getKematianAjuan("Bearer " + token);
        kematianAjuanGetResponseCall.enqueue(new Callback<KematianAjuanGetResponse>() {
            @Override
            public void onResponse(Call<KematianAjuanGetResponse> call, Response<KematianAjuanGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    kematianAjuanGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    kematianAjuanGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<KematianAjuanGetResponse> call, Throwable t) {
                kematianAjuanGetResponseMutableLiveData.setValue(null);
            }
        });
        return kematianAjuanGetResponseMutableLiveData;
    }
}
