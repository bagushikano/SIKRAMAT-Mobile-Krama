package com.bagushikano.sikedatmobile.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahMipilTamiuGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranGetResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelahiranRepository {
    private static KelahiranRepository kelahiranRepository;

    public static KelahiranRepository getInstance() {
        if (kelahiranRepository == null) {
            kelahiranRepository = new KelahiranRepository();
        }
        return kelahiranRepository;
    }

    private ApiRoute getData;

    public KelahiranRepository(){
        getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
    }

    public MutableLiveData<KelahiranGetResponse> getKelahiranData(String token){
        MutableLiveData<KelahiranGetResponse> kelahiranGetResponseMutableLiveData = new MutableLiveData<>();
        Call<KelahiranGetResponse> cacahMipilTamiuGetResponseCall = getData.getKelahiran("Bearer " + token);
        cacahMipilTamiuGetResponseCall.enqueue(new Callback<KelahiranGetResponse>() {
            @Override
            public void onResponse(Call<KelahiranGetResponse> call, Response<KelahiranGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    kelahiranGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    kelahiranGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<KelahiranGetResponse> call, Throwable t) {
                kelahiranGetResponseMutableLiveData.setValue(null);
            }
        });
        return kelahiranGetResponseMutableLiveData;
    }

    public MutableLiveData<KelahiranAjuanGetResponse> getKelahiranAjuanData(String token){
        MutableLiveData<KelahiranAjuanGetResponse> kelahiranGetResponseMutableLiveData = new MutableLiveData<>();
        Call<KelahiranAjuanGetResponse> cacahMipilTamiuGetResponseCall = getData.getKelahiranAjuan("Bearer " + token);
        cacahMipilTamiuGetResponseCall.enqueue(new Callback<KelahiranAjuanGetResponse>() {
            @Override
            public void onResponse(Call<KelahiranAjuanGetResponse> call, Response<KelahiranAjuanGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    kelahiranGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    kelahiranGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<KelahiranAjuanGetResponse> call, Throwable t) {
                kelahiranGetResponseMutableLiveData.setValue(null);
            }
        });
        return kelahiranGetResponseMutableLiveData;
    }
}
