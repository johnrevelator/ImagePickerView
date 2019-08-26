package ru.vvdev.imagepickerview

import android.graphics.PorterDuff
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import java.util.ArrayList

class ImageItem
(
        private val uri: Uri,
        private val mOnClickListenerDetail: ImageAddAdapter.OnClickListenerDetail,
        private val mOnLongClickListenerDetail: ImageAddAdapter.OnLongClickListenerDetail,
        private val color: Int, private val colorBack: Int) : AbstractFlexibleItem<ImageItem.ViewHolderMy>() {
    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, holder: ViewHolderMy, position: Int, payloads: MutableList<Any?>?) {
        holder.itemView.tag = position
        holder.close.tag = position

        //holder.bind(getItem(position), TYPE_ITEM, position)
    }

    override fun equals(other: Any?): Boolean {
        val itemOther = other as ImageItem
        return false
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<out IFlexible<*>>?): ViewHolderMy {
        return ViewHolderMy(view!!, adapter!!)
    }

    override fun getLayoutRes(): Int = R.layout.item_photo_close

    /*fun setData(imageList: MutableList<Image>?) {
        if (imageList == null)
            return
        this.imageList = imageList
    }

    fun reload() {
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        notifyItemRemoved(position); }

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

    fun changeItem(dialog: Image, position: Int) {
        if (position < imageList!!.size && position >= 0) {
            imageList!![position] = dialog
            reloadItem(position)
        }
    }*/

    interface OnClickListenerDetail {
        fun onClickDetail(v: View, position: Int)
    }

    interface OnLongClickListenerDetail {
        fun onLongClick(v: View, position: Int)
    }
    /*


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
    }*/

    inner class ViewHolderMy(val view: View,
                             adapter: FlexibleAdapter<out IFlexible<*>>
    ) : FlexibleViewHolder(view, adapter),
            View.OnClickListener,
            View.OnLongClickListener {


        private var userAvatar: ImageView
        internal var close: ImageView
        private var closeBack: ImageView


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
            closeBack.setColorFilter(colorBack, PorterDuff.Mode.MULTIPLY)
            Glide.with(view.context)
                    .load(dialog.image)
                    .apply(RequestOptions.bitmapTransform(CenterCrop()))
                    .into(userAvatar)


        }

        override fun onClick(v: View) {
            val position = v.tag as Int
            mOnClickListenerDetail.onClickDetail(v, position)
            super.onClick(v)

        }

        override fun onLongClick(v: View): Boolean {
            val position = v.tag as Int
            mOnLongClickListenerDetail.onLongClick(v, position)
            super.onLongClick(v)
            return true
        }
    }

    companion object {

        private val TYPE_HEADER = 2
        private val TYPE_ITEM = 1
    }
}