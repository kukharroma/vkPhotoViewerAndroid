package com.vkphotoviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vkphotoviewer.R;
import com.vkphotoviewer.adapters.PhotoAdapter;
import com.vkphotoviewer.util.AlbumConst;


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
        String albumID;
        if (photoAlbum.getId() == 0) {
            albumID = getIdByTitle(photoAlbum.title);
        } else {
            albumID = String.valueOf(photoAlbum.getId());
        }

        if (albumID.equals(AlbumConst.WITH_ME)) {
            VKRequest requestPhotoWitMy = new VKRequest("photos.getUserPhotos", VKParameters.from(VKApiConst.OWNER_ID, user.getId()));
            requestPhotoWitMy.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    VKList<VKApiPhoto> photoList = new VKList<VKApiPhoto>(response.json, VKApiPhoto.class);
                    setPhotosGridView(photoList);
                }
            });
        } else {
            VKRequest photoRequest = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID, user.getId(), VKApiConst.ALBUM_ID, albumID));
            photoRequest.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    VKList<VKApiPhoto> photoList = new VKList<VKApiPhoto>(response.json, VKApiPhoto.class);
                    setPhotosGridView(photoList);
                }
            });
        }
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
        imageLoader.stop();
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        imageLoader.stop();
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

    private String getIdByTitle(String tittle) {
        String result = "";
        if (tittle.equals(AlbumConst.WALL)) {
            result = "wall";

        } else if (tittle.equals(AlbumConst.PROFILE)) {
            result = "profile";

        } else if (tittle.equals(AlbumConst.SAVED)) {
            result = "saved";

        } else if (tittle.equals(AlbumConst.PREVIEW)) {
            result = "preview";

        } else if (tittle.equals(AlbumConst.WITH_ME)) {
            result = AlbumConst.WITH_ME;
        }
        return result;
    }

}
