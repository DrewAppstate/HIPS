package drew.hiddeninplainsight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Drew and Doug.
 * Modified by Drew.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void newMessage(View view)
    {
        Intent intent = new Intent(MainActivity.this, Send.class);
        startActivity(intent);
    }
}
