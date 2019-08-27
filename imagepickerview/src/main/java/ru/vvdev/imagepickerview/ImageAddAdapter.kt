package ru.vvdev.imagepickerview


import android.graphics.PorterDuff
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions

import java.util.ArrayList


class ImageAddAdapter(private val mClickListener: OnClickChooseImage,
                      private val color: Int, private val colorBack: Int)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var imageList: MutableList<Image>? = ArrayList()


    fun setData(imageList: MutableList<Image>?) {
        if (imageList == null)
            return
        this.imageList = imageList
    }

    fun reload() {
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        imageList?.removeAt(position);
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            AddHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false))
        } else {
            ViewHolderMy(LayoutInflater.from(parent.context).inflate(R.layout.item_photo_close, parent, false))
        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            0 -> {
                (holder as AddHolder).bind(position)
            }
            else -> {
                (holder as ViewHolderMy).bind(getItem(position), TYPE_ITEM, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    interface OnClickChooseImage {
        fun onClickAdd(v: View, position: Int)
        fun onClickOpenImage(v: View, position: Int)
        fun onClickDeleteImage(v: View, position: Int)
        fun onLongClickImage(v: View, position: Int)
    }

    inner class ViewHolderMy internal constructor(var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {


        internal var userAvatar: ImageView
        internal var close: ImageView
        internal var closeBack: ImageView
        internal var rootLay: ConstraintLayout
        internal var imageCardView: CardView


        init {
            userAvatar = view.findViewById<View>(R.id.image) as ImageView
            close = view.findViewById<View>(R.id.close) as ImageView
            closeBack = view.findViewById<View>(R.id.close_back) as ImageView
            rootLay = view.findViewById<View>(R.id.rootLayout) as ConstraintLayout
            imageCardView = view.findViewById<View>(R.id.imageCard) as CardView

        }


        fun bind(dialog: Image?, type: Int, position: Int) {
            if (dialog == null)
                return
            Log.i("MyLog", "$dialog tyu")
            itemView.tag = position
            close.tag = position
            userAvatar.layoutParams.height = 400
            userAvatar.layoutParams.width = 400

            close.setOnClickListener(this)
            close.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            closeBack.setColorFilter(colorBack, PorterDuff.Mode.MULTIPLY)
            Glide.with(view.context)
                    .load(dialog.image)
                    .apply(RequestOptions().transform(CenterCrop()))
                    .into(userAvatar)
            imageCardView.setOnClickListener {
                mClickListener.onClickOpenImage(it, position)
            }


        }

        override fun onClick(v: View) {
            val position = v.tag as Int
            mClickListener.onClickDeleteImage(v, position)
        }

        override fun onLongClick(v: View): Boolean {
            val position = v.tag as Int
            mClickListener.onLongClickImage(v, position)
            return true
        }
    }

    companion object {

        private val TYPE_HEADER = 2
        private val TYPE_ITEM = 1
    }

    inner class AddHolder internal constructor(var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        internal var srcAdd: ImageView
        internal var background: RelativeLayout
        internal var text: TextView
        internal var imageCard: CardView

        init {
            srcAdd = view.findViewById<View>(R.id.camera) as ImageView
            background = view.findViewById<View>(R.id.add) as RelativeLayout
            text = view.findViewById<View>(R.id.tv) as TextView
            imageCard = view.findViewById<View>(R.id.imageCard) as CardView
        }

        fun bind(position: Int) {
            imageCard.tag = position
            background.layoutParams.width = 400
            background.layoutParams.height = 400
            // srcAdd.setImageDrawable()
            imageCard.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mClickListener.onClickAdd(v, v.tag as Int)
        }


    }
}
