package fr.delcey.blockingthreadlivedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static volatile ViewModelFactory factory;

    // Singleton pattern : ViewModelFactory is unique !
    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    // An executor bound to the main thread : perfect to speak to LiveData (that only works on the main thread)
    private final MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();

    // An executor bound to 4 background threads : perfect for long or CPU-intensive work
    private final Executor ioExecutor = Executors.newFixedThreadPool(4);

    private final TimerRepository timerRepository = new TimerRepository(mainThreadExecutor, ioExecutor);

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(timerRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass.getSimpleName());
    }
}
