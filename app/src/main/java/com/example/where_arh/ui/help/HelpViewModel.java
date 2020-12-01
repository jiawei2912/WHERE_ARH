package com.example.where_arh.ui.help;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelpViewModel extends ViewModel{
    private MutableLiveData<String> mText;

    public HelpViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This app can help user find a centred meeting spot in Singapore :)");
    }

    public LiveData<String> getText(){return mText;}
}
