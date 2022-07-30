package fr.delcey.blockingthreadlivedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;

public class TimerRepository {

    private final MutableLiveData<Integer> secondsMutableLiveData = new MutableLiveData<>();

    public TimerRepository(@NonNull MainThreadExecutor mainThreadExecutor, @NonNull Executor ioExecutor) {
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // We are in a background thread ! we can do long or CPU-intensive work ! Yay !

                // In this case, we are completely blocking a thread forever with "while (true)"
                // and waking up every second with "Thread.sleep(1_000)"
                while (true) {
                    // We block the thread for 1 second
                    try {
                        // That's a bad idea to completely lock a Thread for a timer but it's easier to read / understand my point,
                        // you can ignore this warning. But don't use this kind of code !
                        // Use ScheduledExecutorService.scheduleAtFixedRate instead :)
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }

                    final Integer seconds;

                    // True the first time
                    if (secondsMutableLiveData.getValue() == null) {
                        seconds = 0;
                    } else {
                        seconds = secondsMutableLiveData.getValue();
                    }

                    // LiveData can't be used from a Background thread!
                    //
                    // One solution could be using "postValue()" instead of "setValue()", but this is a bad practice,
                    // lots of bad stuff can happen, like one value not being emitted (because another one is posted at the same time)
                    //
                    // The better solution is to "schedule" a Runnable to work the next time the Main Thread is available,
                    // so we can emit the value in the LiveData in the Main Thread !
                    mainThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            secondsMutableLiveData.setValue(seconds + 1);
                        }
                    });
                }
            }
        });
    }

    public LiveData<Integer> getSecondsLiveData() {
        return secondsMutableLiveData;
    }
}
