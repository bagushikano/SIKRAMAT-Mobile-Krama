package com.bagushikano.sikedatmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobile.model.cacahkrama.CacahMipilTamiuGetResponse;
import com.bagushikano.sikedatmobile.model.krama.KramaMipil;
import com.bagushikano.sikedatmobile.model.krama.KramaMipilGetResponse;
import com.bagushikano.sikedatmobile.model.krama.KramaTamiuGetResponse;
import com.bagushikano.sikedatmobile.repository.HomeRepository;

public class KramaViewModel extends ViewModel {
    private MutableLiveData<KramaMipilGetResponse> kramaMipilGetResponseMutableLiveData;
    private MutableLiveData<KramaTamiuGetResponse> kramaTamiuGetResponseMutableLiveData;
    private HomeRepository homeRepository;

    public void init(String token){
        if (kramaMipilGetResponseMutableLiveData != null){
            return;
        }
        homeRepository = homeRepository.getInstance();
        kramaMipilGetResponseMutableLiveData = homeRepository.getKramaMipil(token);
        kramaTamiuGetResponseMutableLiveData = homeRepository.getKramaTamiu(token);
    }
    public void getData(String token) {
        kramaMipilGetResponseMutableLiveData = homeRepository.getKramaMipil(token);
        kramaTamiuGetResponseMutableLiveData = homeRepository.getKramaTamiu(token);
    }


    public LiveData<KramaMipilGetResponse> getUserProfie() {
        return kramaMipilGetResponseMutableLiveData;
    }

    public LiveData<KramaTamiuGetResponse> getKramaTamiu() {
        return kramaTamiuGetResponseMutableLiveData;
    }
}
