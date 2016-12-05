package drew.hiddeninplainsight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;


/**
 * Created by Drew and Doug.
 * Modified by Drew.
 *
 * This is the Home page of the application. It provides a
 * button to choose a picture to decode and a button to
 * create a new message. If the decode button is pressed
 * after a picture is chosen the decoded text will apear
 * in a text box at the bottom of the screen.
 */
public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;

    Steg steg = new Steg();
    ImageView iv;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void decodePic(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        iv = (ImageView)findViewById(R.id.Receive);
        tv = (TextView)findViewById(R.id.decodedMsg);
        tv.setMovementMethod(new ScrollingMovementMethod());
        String str = "";
            if (requestCode == SELECT_PHOTO)
            {
                    try {
                        if (resultCode == RESULT_OK) {
                            Uri selectedImage = imageReturnedIntent.getData();
                            InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                            iv.setVisibility(View.VISIBLE);
                            iv.setImageBitmap(yourSelectedImage);
                            str = steg.decodePicture(yourSelectedImage);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText(str);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

            }
    }

    public void newMessage(View view)
    {
        Intent intent = new Intent(MainActivity.this, Send.class);
        startActivity(intent);
    }

}
