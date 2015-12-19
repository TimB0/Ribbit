package com.racecondition.ribbitsdmp.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.racecondition.ribbitsdmp.app.R;
import com.racecondition.ribbitsdmp.app.adapters.ViewPagerAdapter;
import com.racecondition.ribbitsdmp.app.utils.ParseConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    public static final int FILE_SIZE_LIMIT = 1024 * 1024 * 10;  // 10 MB

    protected Uri mMediaUri;

    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: // Take Photo
                            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            takePhoto(takePhotoIntent);
                            break;
                        case 1: // Take Video
                            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            takeVideo(videoIntent);
                            break;
                        case 2: // Choose Picture
                            Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePhotoIntent.setType("image/*");
                            startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                            break;
                        case 3: // Choose Video
                            Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseVideoIntent.setType("video/*");
                            Toast.makeText(MainActivity.this, getString(R.string.video_file_size_warning), Toast.LENGTH_LONG).show();
                            startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
                            break;
                    }
                }


                private void takePhoto(Intent takePhotoIntent) {
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if (mMediaUri == null) {
                        // display an error
                        Toast.makeText(MainActivity.this, R.string.error_external_storage,
                                Toast.LENGTH_LONG).show();
                    } else {
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    }
                }

                private void takeVideo(Intent videoIntent) {
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    if (mMediaUri == null) {
                        // display an error
                        Toast.makeText(MainActivity.this, R.string.error_external_storage,
                                Toast.LENGTH_LONG).show();
                    } else {
                        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 0 = lowest res
                        startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
                    }
                }

                private Uri getOutputMediaFileUri(int mediaType) {
                    // To be safe, you should check that the SDCard or External Storage is mounted
                    // using Environment.getExternalStorageState() before doing this.
                    if (isExternalStorageAvailable()) {
                        // get the URI

                        // 1.  Get the external storage directory
                        // File Objects can be a File or a Directory
                        String appName = MainActivity.this.getString(R.string.app_name);
                        File mediaStorageDir = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                appName);

                        // 2.  Create our subdirectory
                        if (!mediaStorageDir.exists()) {
                            if (!mediaStorageDir.mkdirs()) {
                                Log.e(TAG, "Failed to create directory.");
                                return null;
                            }
                        }

                        // 3.  Create a file name
                        // 4.  Create the file
                        File mediaFile;
                        Date now = new Date();
                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

                        String path = mediaStorageDir.getPath() + File.separator;
                        if (mediaType == MEDIA_TYPE_IMAGE) {
                            mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
                        } else if (mediaType == MEDIA_TYPE_VIDEO) {
                            mediaFile = new File(path + "VID_" + timestamp + ".mp4");
                        } else {
                            return null;
                        }

                        Log.d(TAG, "File: " + Uri.fromFile(mediaFile));

                        // 5.  Return the files URI
                        return Uri.fromFile(mediaFile);
                    } else {
                        return null;
                    }


                }

                private boolean isExternalStorageAvailable() {
                    String state = Environment.getExternalStorageState();

                    return state.equals(Environment.MEDIA_MOUNTED);
                }

            };


    ViewPagerAdapter mViewPagerAdapter;
    ViewPager mViewPager;
    Toolbar toolbar;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            navigateToLogin();
        } else {
            Log.i(TAG, currentUser.getUsername());
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mViewPagerAdapter = new ViewPagerAdapter(this,
                getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // When the given tab is selected, switch to the corresponding page in
                // the ViewPager.
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            tabLayout.addTab(tabLayout.newTab().setIcon(mViewPagerAdapter.getIcon(i)));
                    //.setTabListener(this));   // used by action bar
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Check if Photo or Video Data
            if (requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST) {
                if (data == null) {
                    Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                } else {
                    mMediaUri = data.getData();
                }
                Log.i(TAG, "Media URI: " + mMediaUri);

                // if Video, make sure the file is less than 10 MB
                if (requestCode == PICK_VIDEO_REQUEST) {

                    int fileSize = 0;
                    InputStream inputStream = null;

                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, getString(R.string.error_opening_file), Toast.LENGTH_LONG).show();
                        return;
                    } catch (IOException e) {
                        Toast.makeText(this, getString(R.string.error_opening_file), Toast.LENGTH_LONG).show();
                        return;
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {/* Intentionally Blank */
                                             /* Just closing the InputStream */
                        }
                    }

                    if (fileSize >= FILE_SIZE_LIMIT) {
                        Toast.makeText(this, getString(R.string.error_file_size_too_large), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            } else {
                // Else, add Photo or Video to the Gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }

            // Send Photo or Video to a Recipient
            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);

            String fileType;
            if (requestCode == PICK_PHOTO_REQUEST || requestCode == TAKE_PHOTO_REQUEST) {
                fileType = ParseConstants.TYPE_IMAGE;
            } else {
                fileType = ParseConstants.TYPE_VIDEO;
            }
            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                break;
            case R.id.action_edit_friends:
                Intent intent = new Intent(this, EditFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}



