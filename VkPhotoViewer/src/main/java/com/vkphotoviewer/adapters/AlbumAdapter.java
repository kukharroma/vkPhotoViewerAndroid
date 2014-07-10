package com.vkphotoviewer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKList;
import com.vkphotoviewer.R;
import com.vkphotoviewer.util.AlbumConst;


public class AlbumAdapter extends BaseAdapter {

    private Context context;
    private VKList<VKApiPhotoAlbum> photoAlbumList;
    private ImageLoader imageLoader;

    public AlbumAdapter(Context context, VKList<VKApiPhotoAlbum> photoAlbumList) {
        this.photoAlbumList = photoAlbumList;
        this.context = context;
        initImageLoader();
    }

    private void initImageLoader() {
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
            albumName.setText(cutTittle(photoAlbumList.get(position).title));
            String imageURl = photoAlbumList.get(position).thumb_src;
            if (imageURl == null) {
                setCustomAlbumImage(photoAlbumList.get(position), albumImage);
            } else {
                imageLoader.loadImage(imageURl, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        albumImage.setImageResource(R.drawable.failed);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        albumImage.setImageBitmap(loadedImage);

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }

    private void setCustomAlbumImage(VKApiPhotoAlbum album, ImageView imageView) {
        if (album.title.equals(AlbumConst.WITH_ME)) {
            imageView.setImageResource(R.drawable.me_icon);
        }else if (album.title.equals(AlbumConst.WALL)) {
            imageView.setImageResource(R.drawable.wall_icon);
        }else if (album.title.equals(AlbumConst.PROFILE)) {
            imageView.setImageResource(R.drawable.profile_icon);
        }else if (album.title.equals(AlbumConst.SAVED)) {
            imageView.setImageResource(R.drawable.saved_icon);
        }else if (album.title.equals(AlbumConst.PREVIEW)) {
            imageView.setImageResource(R.drawable.preview_icon);
        }
    }

    private String cutTittle(String str) {
        String result = "";
        if (str.length() > 9) {
            result = str.substring(0, 6);
            result = result + "...";
        } else {
            return str;
        }
        return result;
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
