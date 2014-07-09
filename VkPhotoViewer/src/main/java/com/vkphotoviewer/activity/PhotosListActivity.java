package com.vkphotoviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vkphotoviewer.R;
import com.vkphotoviewer.adapters.PhotoAdapter;
import com.vkphotoviewer.services.PhotoService;


public class PhotosListActivity extends Activity {

    private GridView photosGridView;
    private VKApiPhotoAlbum photoAlbum;
    private ImageButton backBtnPhoto, forwardBtnPhoto;
    private TextView albumNameTxt;
    private Intent forwardIntent;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_list_layout);
        initComponents();
        initImageLoader();
        getUser();
    }

    private void initComponents() {
        photosGridView = (GridView) findViewById(R.id.photosGridView);
        photoAlbum = getIntent().getParcelableExtra("photoAlbum");
        backBtnPhoto = (ImageButton) findViewById(R.id.backBtnPhoto);
        forwardBtnPhoto = (ImageButton) findViewById(R.id.forwardBtnPhoto);
        albumNameTxt = (TextView) findViewById(R.id.albumName);
        String albName = photoAlbum.title;
        albumNameTxt.setText(albumNameTxt.getText() + " " + albName);
        forwardBtnPhoto.setVisibility(View.GONE);
    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
    }

    private void getUser() {
        VKRequest userRequest = VKApi.users().get();
        userRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiUserFull user = (VKApiUserFull) ((VKList) response.parsedModel).get(0);
                getPhotos(user);
            }
        });
    }

    private void getPhotos(VKApiUserFull user) {
        VKRequest photoRequest = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID, user.getId(),
                VKApiConst.ALBUM_ID, photoAlbum.getId()));
        photoRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiPhoto> photoList = new VKList<VKApiPhoto>(response.json, VKApiPhoto.class);
                setPhotosGridView(photoList);
            }
        });
    }

    private void setPhotosGridView(final VKList<VKApiPhoto> photoList) {
        photosGridView.setAdapter(new PhotoAdapter(this, photoList));

        photosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewPhotoActivity.class);
                intent.putExtra("photo", photoList.get(position));
                startActivity(intent);
                forwardIntent = intent;
            }
        });
    }

    public void onBackBtnPhotoClick(View view) {
        super.onBackPressed();
    }

    public void onForwardBtnPhotoClick(View view) {
        startActivity(forwardIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        forwardBtnPhoto.setVisibility(View.VISIBLE);
    }

}
