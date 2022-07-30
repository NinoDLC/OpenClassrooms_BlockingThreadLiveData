package fr.delcey.blockingthreadlivedata;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final LiveData<String> sentenceLiveData;

    public MainViewModel(TimerRepository timerRepository) {
        sentenceLiveData = Transformations.map(timerRepository.getSecondsLiveData(), new Function<Integer, String>() {
            @Override
            public String apply(Integer seconds) {
                return "Time elapsed : " + seconds + " seconds";
            }
        });
    }

    public LiveData<String> getSentenceLiveData() {
        return sentenceLiveData;
    }
}
