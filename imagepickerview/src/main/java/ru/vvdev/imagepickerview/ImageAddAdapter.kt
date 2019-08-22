package ru.vvdev.imagepickerview


import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions

import java.util.ArrayList


import com.bumptech.glide.request.RequestOptions.bitmapTransform


class ImageAddAdapter(private val mOnClickListenerDetail: OnClickListenerDetail,
                      private val mOnLongClickListenerDetail: OnLongClickListenerDetail,
                      private val color: Int, private val colorBack: Int)
    : RecyclerView.Adapter<ImageAddAdapter.ViewHolderMy>() {
    var width = 0

    var child: RecyclerView? = null
    private var imageList: MutableList<Image>? = ArrayList()


    fun setData(imageList: MutableList<Image>?) {
        if (imageList == null)
            return
        this.imageList = imageList
    }

    fun reload() {
        notifyDataSetChanged()
    }

    fun reloadItem(position: Int) {
        notifyItemChanged(position)
    }


    fun getItem(i: Int): Image? {
        return if (imageList!!.size > i && i >= 0) {
            imageList!![i]
        } else {
            null
        }
    }


    override fun getItemId(i: Int): Long {
        return java.lang.Long.parseLong(getItem(i).toString())
    }

    fun changeItem(dialog: Image, position: Int) {
        if (position < imageList!!.size && position >= 0) {
            imageList!![position] = dialog
            reloadItem(position)
        }
    }

    override fun getItemCount(): Int {
        return if (imageList == null) 0 else imageList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMy {

        var view: View? = null



        view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_close, parent, false)
        return ViewHolderMy(view)


    }


    override fun onBindViewHolder(holder: ViewHolderMy, position: Int) {


        holder.itemView.tag = position
        holder.close.tag = position

        holder.bind(getItem(position), TYPE_ITEM, position)

    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
    }

    interface OnClickListenerDetail {
        fun onClickDetail(v: View, position: Int)
    }

    interface OnLongClickListenerDetail {
        fun onLongClick(v: View, position: Int)
    }

    inner class ViewHolderMy internal constructor(var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {


        internal var userAvatar: ImageView
        internal var close: ImageView
        internal var closeBack: ImageView


        init {
            userAvatar = view.findViewById<View>(R.id.image) as ImageView
            close = view.findViewById<View>(R.id.close) as ImageView
            closeBack = view.findViewById<View>(R.id.close_back) as ImageView
        }


        fun bind(dialog: Image?, type: Int, position: Int) {
            if (dialog == null)
                return
            Log.i("MyLog", "$dialog tyu")
            close.setOnClickListener(this)
            close.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            closeBack.setColorFilter(colorBack,PorterDuff.Mode.MULTIPLY)
            Glide.with(view.context)
                    .load(dialog.image)
                    .apply(bitmapTransform(CenterCrop()))
                    .into(userAvatar)


        }

        override fun onClick(v: View) {
            val position = v.tag as Int
            mOnClickListenerDetail.onClickDetail(v, position)
        }

        override fun onLongClick(v: View): Boolean {
            val position = v.tag as Int
            mOnLongClickListenerDetail.onLongClick(v, position)
            return true
        }
    }

    companion object {

        private val TYPE_HEADER = 2
        private val TYPE_ITEM = 1
    }


}
