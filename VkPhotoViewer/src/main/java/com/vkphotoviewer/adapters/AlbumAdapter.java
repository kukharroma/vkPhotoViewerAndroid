package com.vkphotoviewer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKList;
import com.vkphotoviewer.R;


public class AlbumAdapter extends BaseAdapter {

    private Context context;
    private VKList<VKApiPhotoAlbum> photoAlbumList;
    private ImageLoader imageLoader;

    public AlbumAdapter(Context context, VKList<VKApiPhotoAlbum> photoAlbumList) {
        this.photoAlbumList = photoAlbumList;
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.album_item_layout, null);
            final ImageView albumImage = (ImageView) gridView.findViewById(R.id.itemImageAlbum);
            TextView albumName = (TextView) gridView.findViewById(R.id.itemName);
            albumName.setText(photoAlbumList.get(position).title);
            String imageURl = photoAlbumList.get(position).thumb_src;

            imageLoader.loadImage(imageURl, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    albumImage.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }


    @Override
    public int getCount() {
        return photoAlbumList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoAlbumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
