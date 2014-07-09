package com.vkphotoviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vkphotoviewer.R;
import com.vkphotoviewer.adapters.AlbumAdapter;


public class AlbumListActivity extends Activity {

    private GridView albumGridView;
    private ImageButton backBtnAlbum, forwardBtnAlbum;
    private Intent forwardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_layout);
        initComponents();
        getUser();
    }

    private void initComponents(){
        albumGridView = (GridView) findViewById(R.id.albumGridView);
        backBtnAlbum = (ImageButton) findViewById(R.id.backBtnAlbum);
        forwardBtnAlbum = (ImageButton) findViewById(R.id.forwardBtnAlbum);
        forwardBtnAlbum.setVisibility(View.GONE);
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
        VKRequest albumsRequest = new VKRequest("photos.getAlbums", VKParameters.from(VKApiConst.OWNER_ID, user.getId(), "need_covers", 1));

        albumsRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiPhotoAlbum> photoAlbumList = new VKList<VKApiPhotoAlbum>(response.json, VKApiPhotoAlbum.class);
                if (photoAlbumList.size() > 0) {
                    setAlbumGridView(photoAlbumList);
                } else {
                    Toast.makeText(getApplicationContext(), "У Вас немає фотоальбомів", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAlbumGridView(final VKList<VKApiPhotoAlbum> photoAlbumList) {
        albumGridView.setAdapter(new AlbumAdapter(this, photoAlbumList));
        albumGridView.refreshDrawableState();
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

    public void onBackBtnClick(View view) {
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
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

}
