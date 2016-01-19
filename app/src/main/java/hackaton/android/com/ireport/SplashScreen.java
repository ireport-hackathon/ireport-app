package hackaton.android.com.ireport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity{
    protected  int _splashTime = 100;
    private Thread splashThread;
    private ProgressBar mProg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProg = (ProgressBar)findViewById(R.id.progressBar);

        splashThread = new Thread(){
            @Override
            public void run(){
                try {
                    synchronized (this){
                        for(int i=0; i<=100 ; i+=5){
                            mProg.setProgress(i);
                            wait(_splashTime);
                        }
                    }
                }catch (InterruptedException e){
                    Toast.makeText(SplashScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                }
                finally {
                    Intent dashboard = new Intent(SplashScreen.this, Dashboard.class);
                    startActivity(dashboard);
                    finish();
                }
            }
        };
        splashThread.start();
    }

}
