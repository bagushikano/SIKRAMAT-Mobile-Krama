package com.bagushikano.sikedatmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaTamiuGetResponse;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahMipilTamiuGetResponse;
import com.bagushikano.sikedatmobile.repository.HomeRepository;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<CacahMipilTamiuGetResponse> cacahMipilTamiuGetResponseMutableLiveData;
    private HomeRepository homeRepository;

    public void init(String token){
        if (cacahMipilTamiuGetResponseMutableLiveData != null){
            return;
        }
        homeRepository = homeRepository.getInstance();
        cacahMipilTamiuGetResponseMutableLiveData = homeRepository.getCacahMipilTamiu(token);
    }
    public void getData(String token) {
        cacahMipilTamiuGetResponseMutableLiveData = homeRepository.getCacahMipilTamiu(token);
    }

    public LiveData<CacahMipilTamiuGetResponse> getUserProfie() {
        return cacahMipilTamiuGetResponseMutableLiveData;
    }
}
