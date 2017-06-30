package com.stgobain.samuha.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stgobain.samuha.CustomUserInterface.CustomEditTextView;
import com.stgobain.samuha.Model.ContextData;
import com.stgobain.samuha.R;
import com.stgobain.samuha.utility.AppUtils;
import com.stgobain.samuha.utility.SharedPrefsUtils;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import life.knowledge4.videotrimmer.utils.FileUtils;

import static com.stgobain.samuha.utility.AppUtils.GET_CONTEXT_TO_UPLOAD_URL;
import static com.stgobain.samuha.utility.AppUtils.GET_EVENTS_TO_UPLOAD_URL;
import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_MESSAGE;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.POST_SAB;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_GET_CONTEXT_TO_UPLOAD;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_GET_EVENTS_TO_UPLOAD;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_POST_SAB;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 28-06-2017.
 */

public class SabAuditionActivity extends AppCompatActivity implements NetworkServiceResultReceiver.Receiver {
    private static final String UPLOAD = "MUPLOAD";
    private static final String RESULT_CONTEXTODE = "contestlistitem";
    private static final int CTXT_REQ_CODE = 9002;
    private static final int VIDEO_TRIM_CODE = 9003;
    private static final int EVENT_REQ_CODE = 9004;
    private NetworkServiceResultReceiver mReceiver;
    private static final int STORAGE_PERMISSION_CODE_UPLOAD_IMAGE = 9001;
    private static final int STORAGE_PERMISSION_CODE_UPLOAD_VIDEO = 9000;
    Button btnUpload, btnMulUpload, btnPickImage, btnPickVideo;
    private ArrayList<ContextData> contextList = new ArrayList<>();
    private ArrayList<String> eventsList = new ArrayList<>();
    String mediaPath, mediaPath1;
    ImageView imgView;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    ProgressDialog progressDialog;
    TextView str1, str2;
    private String encImage, eventContestTag;
    LinearLayout layoutUpload;
    private ArrayAdapter<String> listAdapter;
    static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    private static final String VIDEO_TRIM_PATH = "video_path";
    String videoPath;

    boolean isFileSIzeToolarge;
    String auditiionType ="";
    String relationShip="";
    String fileType ="";
    CustomEditTextView descTxt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_auditions);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Bundle extras = getIntent().getExtras();
            String teamTittle = "Uploads";
            getSupportActionBar().setTitle(teamTittle);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            isFileSIzeToolarge = false;
            btnUpload = (Button) findViewById(R.id.upload);
            btnPickImage = (Button) findViewById(R.id.pick_img);
            btnPickVideo = (Button) findViewById(R.id.pick_vdo);
            imgView = (ImageView) findViewById(R.id.preview);
            str1 = (TextView) findViewById(R.id.filename1);
            layoutUpload = (LinearLayout) findViewById(R.id.uploadLayout);
            encImage = "";
            eventContestTag = "";
            descTxt = (CustomEditTextView)findViewById(R.id.audDescr);
            //  layoutUpload.setVisibility(View.INVISIBLE);

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadFile();
                }
            });
            btnPickImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isReadStorageAllowed()) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 0);
                    } else {
                        requestStoragePermission(STORAGE_PERMISSION_CODE_UPLOAD_IMAGE);
                    }
                }
            });
           RadioGroup rg = (RadioGroup) findViewById(R.id.radioTypeOptionsTag);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioSinging:
                        auditiionType ="Singing";
                        break;
                    case R.id.radioMusic:
                        auditiionType ="Music";
                        break;
                    case R.id.radioDance:
                        auditiionType ="Dance";
                        break;
                    case R.id.radioOther:
                        auditiionType ="Other";
                        break;
                }
            }
        });
        RadioGroup rgEvent =(RadioGroup) findViewById(R.id.radioFamilyOptionsTag);
        rgEvent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioFather:
                        relationShip ="Father";
                        break;
                    case R.id.radioMother:
                        relationShip ="Mother";
                        break;
                    case R.id.radioSon:
                        relationShip ="Son";
                        break;
                    case R.id.radioDaughter:
                        relationShip ="Daughter";
                        break;
                }
            }
        });



            // Video must be low in Memory or need to be compressed before uploading...
            btnPickVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isReadStorageAllowed()) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        requestStoragePermission(STORAGE_PERMISSION_CODE_UPLOAD_VIDEO);
                    }
                }
            });
            //  getContests();
        }

        @Override
        protected void onResume () {
            super.onResume();

        }

    private void uploadFile() {
        uploadImage();
    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(int permissionCode) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionCode);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE_UPLOAD_IMAGE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE_UPLOAD_VIDEO) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                fileType ="image";
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath1 = cursor.getString(columnIndex);
                // str1.setText(mediaPath1);
                FileInputStream fis = null;
                File file = new File(mediaPath1);
                long length = file.length() / 1024;
                long fileSizeInMB = length / 1024;
                byte[] byteArray = new byte[0];
                try {
                    Bitmap compressedBitmap = null;
                    if (fileSizeInMB > 1) {
                        compressedBitmap = decodeFile(file);
                    } else {
                        compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    long compressedSize = (byteArray.length / 1024) / 1024;
                    if (compressedSize <= 1) {
                        isFileSIzeToolarge = false;
                    } else {
                        isFileSIzeToolarge = true;
                    }
                    encImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

              /*  try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bm = BitmapFactory.decodeStream(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                encImage = Base64.encodeToString(baos.toByteArray(), 0);*/
                // Set the Image in ImageView for Previewing the Media
                imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath1));
                cursor.close();

            } // When an Video is picked
            else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                // Get the Video from data
                Uri selectedVideo = data.getData();

                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startTrimActivity(selectedUri);
                }
              /*  String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                mediaPath1 = cursor.getString(columnIndex);
                str2.setText(mediaPath1);
                // Set the Video Thumb in ImageView Previewing the Media
                imgView.setImageBitmap(getThumbnailPathForLocalFile(SabAuditionActivity.this, selectedVideo));
                cursor.close();*/

            } else if (requestCode == CTXT_REQ_CODE && resultCode == Activity.RESULT_OK) {
                eventContestTag = "#" + data.getStringExtra(RESULT_CONTEXTODE);
                str1.setText(eventContestTag);
                //  Toast.makeText(this, "You selected countrycode: " + eventContestTag, Toast.LENGTH_LONG).show();
            } else if (requestCode == EVENT_REQ_CODE && resultCode == Activity.RESULT_OK) {
                eventContestTag = "#" + data.getStringExtra(RESULT_CONTEXTODE);
                str1.setText(eventContestTag);
                //  Toast.makeText(this, "You selected countrycode: " + eventContestTag, Toast.LENGTH_LONG).show();
            } else if (requestCode == VIDEO_TRIM_CODE && resultCode == Activity.RESULT_OK) {
                fileType ="video";
                videoPath = data.getStringExtra(VIDEO_TRIM_PATH);
                //  String[] filePathColumn = {MediaStore.Video.Media.DATA};
                final Uri videoUri; //= Uri.fromFile(new File(videoPath));
                MediaScannerConnection.scanFile(
                        getApplicationContext(),
                        new String[]{videoPath},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                assert cursor != null;
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                mediaPath1 = cursor.getString(columnIndex);

                                // Set the Video Thumb in ImageView Previewing the Media
                                imgView.setImageBitmap(getThumbnailPathForLocalFile(SabAuditionActivity.this, uri));
                                cursor.close();
                            }
                        });


                String strFile = null;
                File file = new File(videoPath);
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    FileInputStream fis = new FileInputStream(new File(videoPath));

                    byte[] buf = new byte[1024];
                    int n;
                    while (-1 != (n = fis.read(buf)))
                        baos.write(buf, 0, n);

                    byte[] videoBytes = baos.toByteArray();
                    long compressedSize = (videoBytes.length / 1024) / 1024;
                    if (compressedSize <= 1) {
                        isFileSIzeToolarge = false;
                    } else {
                        isFileSIzeToolarge = true;
                    }
                    encImage = Base64.encodeToString(videoBytes, Base64.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(this, "You selected countrycode: " + eventContestTag, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("UPLOAD", e.getMessage());
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(this, TrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri));
        startActivityForResult(intent, VIDEO_TRIM_CODE);
    }

    private Bitmap decodeFile(File f) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 95;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    // Providing Thumbnail For Selected Image
    public Bitmap getThumbnailPathForLocalFile(Activity context, Uri fileUri) {
        long fileId = getFileId(context, fileUri);
        return MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
    }

    // Getting Selected File ID
    public long getFileId(Activity context, Uri fileUri) {
        Cursor cursor = context.getContentResolver().query(fileUri, mediaColumns, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            return cursor.getInt(columnIndex);
        }
        return 0;
    }

    private void getContests() {
        if (progressDialog != null)
            progressDialog.show();
        String userId = SharedPrefsUtils.getStringPreference(SabAuditionActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("type", "contest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_GET_CONTEXT_TO_UPLOAD, GET_CONTEXT_TO_UPLOAD_URL);
    }

    private void requestEventList() {
        String userId = SharedPrefsUtils.getStringPreference(SabAuditionActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            //   jsonObject.put("event_date","2017-06-22");
            jsonObject.put("id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_GET_EVENTS_TO_UPLOAD, GET_EVENTS_TO_UPLOAD_URL);
    }

    private void uploadImage() {
        if (!TextUtils.isEmpty(encImage)) {
            if (progressDialog != null)
                progressDialog.show();
            if (!isFileSIzeToolarge) {
                String descTxtStr = descTxt.getText().toString().trim();
                String userId = SharedPrefsUtils.getStringPreference(SabAuditionActivity.this, SKEY_ID);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", userId);
                    jsonObject.put("audition_type", auditiionType);
                    jsonObject.put("dependent_type", relationShip);
                    jsonObject.put("file_type", fileType);
                    jsonObject.put("files", encImage);
                    jsonObject.put("short_description",descTxtStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestWebservice(jsonObject.toString(), SERVICE_REQUEST_POST_SAB, POST_SAB);
            } else {
                AppUtils.showAlertDialog(SabAuditionActivity.this, "File size too large too upload");
            }
        } else if (TextUtils.isEmpty(encImage)) {
            AppUtils.showAlertDialog(SabAuditionActivity.this, "Choose Image or Video to upload!");
        } else {
            AppUtils.showAlertDialog(SabAuditionActivity.this, "Choose Relationship!");
        }
    }

    private void requestWebservice(String request, int reqID, String url) {
        try {
            this.mReceiver = new NetworkServiceResultReceiver(new Handler());
            this.mReceiver.setReceiver(this);
            Intent intent = new Intent("android.intent.action.SYNC", null, SabAuditionActivity.this, NetworkService.class);
            intent.putExtra("url", url);
            intent.putExtra(KEY_RECIVER, this.mReceiver);
            intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
            intent.putExtra("request", request);
            startService(intent);
        } catch (Exception e) {
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            AppUtils.showAlertDialog(SabAuditionActivity.this, "File Size is too large to upload!");
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case STATUS_RUNNING:
                Log.d(UPLOAD, "STATUS_RUNNING");
                break;
            case STATUS_FINISHED:

                String result = resultData.getString(KEY_RESULT);
                Log.d("UPLOAD", "FINISHED" + result);
                int requestId = Integer.valueOf(resultData.getString(KEY_REQUEST_ID));
                String status = "0";
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("UPLOAD", "FINISHED status " + status + " ");
                switch (requestId) {
                    case SERVICE_REQUEST_POST_SAB:
                        if (status.equals("false")) {

                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                            }
                            String message = "";
                            try {
                                message = new JSONObject(result).getString(KEY_MESSAGE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AppUtils.showAlertDialog(SabAuditionActivity.this, message);
                        }
                        Log.d("UPLOAD", "FINISHED status " + status + " ");
                        break;
                }
                break;

            case STATUS_ERROR:
                AppUtils.showAlertDialog(SabAuditionActivity.this, "Login Failed. Try Again!");
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

  /*  public void alertDialogView(ArrayList<String> arrayList) {
        View dialogView = View.inflate(SabAuditionActivity.this,R.layout.contest_dialog, null);
        ListView lv= (ListView) dialogView.findViewById(R.id.listview);
        final String[] contestArray = new String[arrayList.size()];
        arrayList.toArray(contestArray);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contestArray);
        lv.setAdapter(listAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(SabAuditionActivity.this)
                .setTitle("Choose contest")
                .setView(dialogView)
                .setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String selectedText = contestArray[i].toString();
                        str1.setText(selectedText);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //   str1.setText(contestArray[which]);
                        // TODO do something
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO do something
                    }
                });
        builder.create().show();

    }
    private void showdialog()
    {
        final String[] contestArray = new String[contextList.size()];
        contextList.toArray(contestArray);
        Dialog listDialog = new Dialog(this);
        listDialog.setTitle("Select Item");
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.contest_dialog, null, false);
        listDialog.setContentView(v);
        listDialog.setCancelable(true);
        //there are a lot of settings, for dialog, check them all out!

        ListView list1 = (ListView) listDialog.findViewById(R.id.listview);
        list1.setOnItemClickListener(this);
        list1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, contestArray));
        //now that the dialog is set up, it's time to show it
        listDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final String[] contestArray = new String[contextList.size()];
        contextList.toArray(contestArray);
        String selectedText = contestArray[i].toString();
        str1.setText(selectedText);
    }*/
}

