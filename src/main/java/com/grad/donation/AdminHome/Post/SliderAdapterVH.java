package com.grad.donation.AdminHome.Post;

import android.view.View;
import android.widget.ImageView;

import com.grad.donation.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

    View itemView;
    ImageView imageViewBackground;

    public SliderAdapterVH(View itemView) {
        super(itemView);
        imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        this.itemView = itemView;
    }
}