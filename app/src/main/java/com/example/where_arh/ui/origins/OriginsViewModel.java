package com.example.where_arh.ui.origins;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OriginsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OriginsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is origins.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}