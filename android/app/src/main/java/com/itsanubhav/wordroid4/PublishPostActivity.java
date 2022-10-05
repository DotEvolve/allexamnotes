package com.itsanubhav.wordroid4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.itsanubhav.libdroid.model.media.Media;
import com.itsanubhav.libdroid.network.ApiClient;
import com.itsanubhav.libdroid.network.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishPostActivity extends AppCompatActivity {


    private Editor editor;
    private Toolbar toolbar;
    private String tempUuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_post);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        editor = findViewById(R.id.editor);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Publish a post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_chevron_left));
        toolbar.setNavigationOnClickListener(view -> {
            super.onBackPressed();
        });


        findViewById(R.id.action_h1).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.H1));

        findViewById(R.id.action_h2).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.H2));

        findViewById(R.id.action_h3).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.H3));

        findViewById(R.id.action_bold).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.BOLD));

        findViewById(R.id.action_Italic).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.ITALIC));

        findViewById(R.id.action_indent).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.INDENT));

        findViewById(R.id.action_outdent).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.OUTDENT));

        findViewById(R.id.action_bulleted).setOnClickListener(v -> editor.insertList(false));

        findViewById(R.id.action_color).setOnClickListener(v ->{
            Toast.makeText(getApplicationContext(),"Color Updated",Toast.LENGTH_SHORT).show();
            editor.updateTextColor("#FF3333");
        } );

        findViewById(R.id.action_unordered_numbered).setOnClickListener(v -> editor.insertList(true));

        findViewById(R.id.action_hr).setOnClickListener(v -> editor.insertDivider());

        findViewById(R.id.action_insert_image).setOnClickListener(v -> editor.openImagePicker());

        findViewById(R.id.action_insert_link).setOnClickListener(v -> editor.insertLink());

        findViewById(R.id.action_erase).setOnClickListener(v -> editor.clearAllContents());

        findViewById(R.id.action_blockquote).setOnClickListener(v -> editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE));

        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onUpload(Bitmap image, String uuid) {
                //do your upload image operations here, once done, call onImageUploadComplete and pass the url and uuid as reference.
                tempUuid = uuid;
                new UploadMediaFile().execute(image);
                //editor.onImageUploadComplete("http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg",uuid);
                // editor.onImageUploadFailed(uuid);
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }
        });
        editor.render();
    }

    private void uploadMedia(Bitmap bitmap){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        menu.findItem(R.id.option_publish).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id==R.id.option_publish){
            startActivity(new Intent(getApplicationContext(),PostParametersActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                editor.insertImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // editor.RestoreState();
        }
    }

    class UploadMediaFile extends AsyncTask<Bitmap,Void,MultipartBody.Part>{

        @Override
        protected MultipartBody.Part doInBackground(Bitmap... bitmaps) {
            //create a file to write bitmap data
            File f = new File(getApplicationContext().getCacheDir(), "file");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Convert bitmap to byte array
            Bitmap bitmap = bitmaps[0];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
            String fileName =  generateRandomString() + ".png";
            Log.e("Making Request", "File: " + fileName);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileName, reqFile);
            SharedPreferences sharedPreferences = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);
            String token = sharedPreferences.getString("token",null);
            Log.e("Making Request", "token: " + token);
            if (token!=null) {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


/*                Call<String> media = apiInterface.uploadMedia("Bearer " + token,part);

                media.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("Making Request", "Msg: " + response);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Making Request", t.getLocalizedMessage());
                    }
                });*/


                Call<Media> mediaCall = apiInterface.uploadMedia("Bearer " + token,part);
                mediaCall.enqueue(new Callback<Media>() {
                    @Override
                    public void onResponse(Call<Media> call, Response<Media> response) {
                        if (response.isSuccessful()&&response.body()!=null) {
                            if (response.body().getSourceUrl()!=null) {
                                Toast.makeText(getApplicationContext(),"Image uploaded successfully",Toast.LENGTH_SHORT).show();
                                editor.onImageUploadComplete(response.body().getSourceUrl(), tempUuid);
                            }else {
                                Toast.makeText(getApplicationContext(), R.string.upload_failed,Toast.LENGTH_SHORT).show();
                                editor.onImageUploadFailed(tempUuid);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Media> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.upload_failed,Toast.LENGTH_SHORT).show();
                        editor.onImageUploadFailed(tempUuid);
                    }
                });
            }
            return part;
        }

        @Override
        protected void onPostExecute(MultipartBody.Part file) {
            super.onPostExecute(file);
            /*SharedPreferences sharedPreferences = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);
            String token = sharedPreferences.getString("token",null);
            Log.e("Making Request", "token: " + token);
            if (token!=null) {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                Call<String> media = apiInterface.uploadMedia("Bearer " + token,file);

                media.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("Making Request", "Msg: " + response);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Making Request", t.getLocalizedMessage());
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(),"Login required",Toast.LENGTH_SHORT).show();
            }*/

            /*Call<Media> mediaCall = apiInterface.uploadMedia(file);
            mediaCall.enqueue(new Callback<Media>() {
                @Override
                public void onResponse(Call<Media> call, Response<Media> response) {
                    if (response.isSuccessful()&&response.body()!=null) {
                        if (response.body().getSourceUrl()!=null) {
                            Toast.makeText(getApplicationContext(),"Image uploaded successfully",Toast.LENGTH_SHORT).show();
                            editor.onImageUploadComplete(response.body().getSourceUrl(), tempUuid);
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.upload_failed,Toast.LENGTH_SHORT).show();
                            editor.onImageUploadFailed(tempUuid);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Media> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.upload_failed,Toast.LENGTH_SHORT).show();
                    editor.onImageUploadFailed(tempUuid);
                }
            });*/
        }
    }

    public String generateRandomString() {
        Random r = new Random( System.currentTimeMillis() );
        return String.valueOf(10000 + r.nextInt(20000));
    }
}
