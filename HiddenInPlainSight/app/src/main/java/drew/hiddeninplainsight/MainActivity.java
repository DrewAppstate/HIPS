package drew.hiddeninplainsight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;


/**
 * Created by Drew and Doug.
 * Modified by Drew.
 */
public class MainActivity extends AppCompatActivity {

    MsgBody msgBody = new MsgBody();
    private static final int SELECT_PHOTO = 100;

    ImageView iv;

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
            if (requestCode == SELECT_PHOTO)
            {
                    try {
                        if (resultCode == RESULT_OK) {
                            Uri selectedImage = imageReturnedIntent.getData();
                            InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
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
