package drew.hiddeninplainsight;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Drew and Doug.
 * Modified by Drew.
 */
public class Send extends AppCompatActivity {

    Encrypt encrypt = new Encrypt();
    Steg steg = new Steg();
    ImageView iv;
    Button sendBtn;
    Bitmap bp;
    File photoPath;
    FileOutputStream out = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendMessage();
            }
        });
        iv = (ImageView)findViewById(R.id.imageView);
    }

    public void takePicture(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        bp = (Bitmap) data.getExtras().get("data");
        iv.setImageBitmap(bp);

    }

    public void saveImage(Bitmap bitmap)
    {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "H.I.P.S.");


        String uniquePhotoId = UUID.randomUUID().toString();


        // folder doesn't exist create it
        if (!folder.exists())
        {
            folder.mkdirs();
        }

        photoPath = new File(folder, uniquePhotoId + ".png");

        try
        {
            out = new FileOutputStream(photoPath);
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        iv.setImageBitmap(bp);
        bp.compress(Bitmap.CompressFormat.PNG,100,out);

        try
        {
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        EditText phoneNum = (EditText) findViewById(R.id.phoneNum);
        TextView sending_message = (TextView) findViewById(R.id.sending_message);

        Log.i("Send SMS", "");
        String phoneNo = phoneNum.getText().toString();
        String message = sending_message.getText().toString();

        String encryptedMessage = encrypt.convert(message);
        steg.encodePicture(bp, encryptedMessage);
        iv.setImageBitmap(bp);
        iv.buildDrawingCache();
        Bitmap bm = iv.getDrawingCache();
        saveImage(bm);

        try {
            sendIntent.putExtra("address", phoneNo);
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoPath));
            sendIntent.setType("image/png"); //this sends everything you just have to choose messaging

            startActivity(Intent.createChooser(sendIntent,"Send"));

            finish();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }
}
