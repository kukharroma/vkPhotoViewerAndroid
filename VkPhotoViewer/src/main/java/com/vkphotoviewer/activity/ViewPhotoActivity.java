package com.vkphotoviewer.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vkphotoviewer.R;
import com.vkphotoviewer.services.PhotoService;


public class ViewPhotoActivity extends Activity {

    private ImageView imageView;
    private VKApiPhoto photo;
    private ImageButton backBtnBigPhoto;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_photo_layout);
        initComponents();
        initImageLoader();
        setPhoto();
    }

    private void initComponents() {
        imageView = (ImageView) findViewById(R.id.bigImage);
        backBtnBigPhoto = (ImageButton) findViewById(R.id.backBtnBigPhoto);
        photo = getIntent().getParcelableExtra("photo");
    }

    private void setPhoto() {
        PhotoService ps = new PhotoService();
        String imageURl = ps.getMaxPhotoSize(photo);
        imageLoader.loadImage(imageURl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
    }

    public void onBackBtnBigPhotoClick(View view) {
        imageLoader.stop();
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        imageLoader.stop();
        super.onBackPressed();
    }
}
