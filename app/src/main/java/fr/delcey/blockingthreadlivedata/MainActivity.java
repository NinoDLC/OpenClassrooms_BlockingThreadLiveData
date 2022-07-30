package fr.delcey.blockingthreadlivedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.main_textview);

        MainViewModel mainViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);

        mainViewModel.getSentenceLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String sentence) {
                textView.setText(sentence);
            }
        });
    }
}