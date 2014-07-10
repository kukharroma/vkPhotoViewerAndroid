package com.vkphotoviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vkphotoviewer.R;
import com.vkphotoviewer.adapters.AlbumAdapter;
import com.vkphotoviewer.util.AlbumConst;


public class AlbumListActivity extends Activity {

    private GridView albumGridView;
    private ImageButton backBtnAlbum, forwardBtnAlbum;
    private Intent forwardIntent;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_layout);
        initComponents();
        initImageLoader();
        getUser();
    }

    private void initComponents() {
        albumGridView = (GridView) findViewById(R.id.albumGridView);
        backBtnAlbum = (ImageButton) findViewById(R.id.backBtnAlbum);
        forwardBtnAlbum = (ImageButton) findViewById(R.id.forwardBtnAlbum);
        forwardBtnAlbum.setVisibility(View.GONE);
    }

    private void initImageLoader(){
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
                getAlbums(user);
            }
        });
    }

    private void getAlbums(VKApiUserFull user) {

        VKRequest requestCreatedAlbums = new VKRequest("photos.getAlbums", VKParameters.from(VKApiConst.OWNER_ID, user.getId(), "need_covers", 1));
        requestCreatedAlbums.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiPhotoAlbum> allAlbums = getCustomAlbums();
                VKList<VKApiPhotoAlbum> createdAlbums = new VKList<VKApiPhotoAlbum>(response.json, VKApiPhotoAlbum.class);
                allAlbums.addAll(createdAlbums);
                setAlbumGridView(allAlbums);
            }
        });
    }

    private void setAlbumGridView(final VKList<VKApiPhotoAlbum> photoAlbumList) {

        albumGridView.setAdapter(new AlbumAdapter(this, photoAlbumList));
        albumGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PhotosListActivity.class);
                intent.putExtra("photoAlbum", photoAlbumList.get(position));
                startActivity(intent);
                forwardIntent = intent;
            }
        });

    }

    private VKList<VKApiPhotoAlbum> getCustomAlbums() {
        VKList<VKApiPhotoAlbum> photoAlbumList = new VKList<VKApiPhotoAlbum>();
        for (int j = 0; j < 5; j++) {
            VKApiPhotoAlbum album = new VKApiPhotoAlbum();
            album.title = getTittle(j);
            photoAlbumList.add(album);
        }
        return photoAlbumList;
    }

    private String getTittle(int id) {
        String result = "";
        switch (id) {
            case 0:
                result = AlbumConst.WITH_ME;
                break;
            case 1:
                result = AlbumConst.PROFILE;
                break;
            case 2:
                result = AlbumConst.SAVED;
                break;
            case 3:
                result = AlbumConst.WALL;
                break;
            case 4:
                result = AlbumConst.PREVIEW;
                break;
        }
        return result;
    }

    public void onBackBtnClick(View view) {
        imageLoader.stop();
        super.onBackPressed();
    }

    public void onForwardBtnClick(View view) {
        startActivity(forwardIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        forwardBtnAlbum.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        imageLoader.stop();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }


}
