package hackaton.android.com.ireport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    protected  int _splashTime = 5000;
    private Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashThread = new Thread(){
            @Override
            public void run(){
                try {
                    synchronized (this){
                        wait(_splashTime);
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
