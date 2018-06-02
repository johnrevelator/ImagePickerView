package ru.vvdev.imagepickerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alexanderklimov on 6/2/18.
 */

public class ImageChooseLayout extends LinearLayout implements ImageAddAdapter.OnClickListenerDetail, ImageAddAdapter.OnLongClickListenerDetail, View.OnClickListener {
    public ImageChooseLayout(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        init(attrs);
    }

    List<Image> imageList=new ArrayList<>();

    ImageAddAdapter imageAddAdapter;

    RelativeLayout add;

    HorizontalScrollView llRoot;

    int background,close;


    private void init(AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_image_choose_layout, this, true);
        add=v.findViewById(R.id.add);
        llRoot=v.findViewById(R.id.ll_root);
        add.setOnClickListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        RecyclerView imageRv=v.findViewById(R.id.imageRv);
        imageRv.setLayoutManager(mLayoutManager);
        imageRv.setItemAnimator(null);
        imageRv.setNestedScrollingEnabled(false);

        imageRv.setHasFixedSize(true);

        TypedArray arr = getContext().obtainStyledAttributes(attrs,R.styleable.imgPickr);
        close=arr.getColor(R.styleable.imgPickr_close_btn_color,getResources().getColor(R.color.colorPrimary));
        background=arr.getColor(R.styleable.imgPickr_backgroundView,Color.parseColor("#ffffff"));
        llRoot.setBackgroundColor(background);

        imageAddAdapter = new ImageAddAdapter(this,this,close,background);
        imageRv.setAdapter(imageAddAdapter);
        imageAddAdapter.setData(imageList);
        imageAddAdapter.reload();

    }


    public void addImage(Context context){
        RxImagePicker.with(context).requestImage(Sources.GALLERY).subscribe(uri -> {
            imageList.add(new Image(uri));
            imageAddAdapter.setData(imageList);
            imageAddAdapter.reload();
        });

    }


    public List<Image> getItems(){
        return imageList;
    }

    @Override
    public void onClickDetail(View v, int position) {
        if(v.getId()==R.id.close){
            imageList.remove(position);
            imageAddAdapter.notifyItemRemoved(position);
            imageAddAdapter.reload();
        }

    }

    @Override
    public void onLongClick(View v, int position) {

    }

    @Override
    public void onClick(View view) {
        addImage(getContext());
    }
}
