package ru.vvdev.imagepickerview;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;



import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class ImageAddAdapter extends RecyclerView.Adapter<ImageAddAdapter.ViewHolderMy> {
    public int width = 0;

    public RecyclerView child;

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;


    private OnClickListenerDetail mOnClickListenerDetail;
    private OnLongClickListenerDetail mOnLongClickListenerDetail;
    private List<Image> imageList=new ArrayList<>();
    private int color;
    private int colorBack;




    public ImageAddAdapter(OnClickListenerDetail onClickListenerDetail, OnLongClickListenerDetail onLongClickListenerDetail, int color,int colorBack) {
        mOnClickListenerDetail = onClickListenerDetail;
        mOnLongClickListenerDetail=onLongClickListenerDetail;
        this.color=color;
        this.colorBack=colorBack;
    }



    public void setData(List<Image> imageList) {
        if (imageList == null)
            return;
        this.imageList=imageList;
    }

    public void reload() {
        notifyDataSetChanged();
    }

    public void reloadItem(int position) {
        notifyItemChanged(position);
    }





    public Image getItem(int i) {
        if (imageList.size() > i && i >= 0) {
            return imageList.get(i);
        } else {
            return null;
        }
    }


    @Override
    public long getItemId(int i) {
        return Long.parseLong(String.valueOf(getItem(i)));
    }

    public void changeItem(Image dialog, int position) {
        if (position < imageList.size() && position >= 0) {
            imageList.set(position, dialog);
            reloadItem(position);
        }
    }

    @Override
    public int getItemCount() {
        if (imageList == null)
            return 0;
        return imageList.size();
    }

    @Override
    public ViewHolderMy onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;



        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_close, parent, false);
        return new ViewHolderMy(view);


    }


    @Override
    public void onBindViewHolder(final ViewHolderMy holder, int position) {



            ViewHolderMy holders = (ViewHolderMy) holder;
            holders.itemView.setTag(position);
            holders.close.setTag(position);

            holders.bind(getItem(position), TYPE_ITEM, position);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public interface OnClickListenerDetail {
        void onClickDetail(View v, int position);
    }

    public interface OnLongClickListenerDetail {
        void onLongClick(View v, int position);
    }

    public class ViewHolderMy extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {


        ImageView userAvatar;
        ImageView close;
        ImageView closeBack;



        public View view;



        ViewHolderMy(View view) {
            super(view);
            this.view = view;
            userAvatar=(ImageView)view.findViewById(R.id.image);
            close=(ImageView)view.findViewById(R.id.close);
            closeBack=(ImageView)view.findViewById(R.id.close_back);
        }


        public void bind(Image dialog, int type,int position) {
            if (dialog == null)
                return;
            Log.i("MyLog",dialog+" tyu");
            close.setOnClickListener(this);
            close.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
            closeBack.setColorFilter(colorBack, android.graphics.PorterDuff.Mode.MULTIPLY);
            Glide.with(view.getContext())
                    .load(dialog.getImage())
                    .apply(RequestOptions.bitmapTransform(new CenterCrop()))
                    .into(userAvatar);



        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            mOnClickListenerDetail.onClickDetail(v, position);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = (Integer) v.getTag();
            mOnLongClickListenerDetail.onLongClick(v, position);
            return true;
        }
    }


}
