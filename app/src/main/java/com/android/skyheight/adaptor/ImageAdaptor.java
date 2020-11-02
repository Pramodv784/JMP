package com.android.skyheight.adaptor;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.android.skyheight.R;
import com.android.skyheight.model.ImageModel;

import java.util.ArrayList;
import java.util.Objects;

public class ImageAdaptor extends PagerAdapter {
  Context context;
    ArrayList<ImageModel> images = new ArrayList<ImageModel>();

    LayoutInflater layoutInflater;

    public ImageAdaptor(Context context,ArrayList<ImageModel> images)
    {
        this.context=context;
        this.images=images;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==((LinearLayout) object);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View viewitem= layoutInflater.inflate(R.layout.image_layout,container,false);
        ImageView imageView=viewitem.findViewById(R.id.image_view);
        imageView.setImageResource(Integer.parseInt(images.get(position).getImage()));
        Objects.requireNonNull(container).addView(viewitem);

        return viewitem;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
