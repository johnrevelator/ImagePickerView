package ru.vvdev.imagepickerview


import android.content.res.Resources
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
                      private val attr: ImageAttr,
                      private val resources: Resources)
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
                (holder as AddHolder).bind(position, attr)
            }
            else -> {
                (holder as ViewHolderMy).bind(getItem(position), TYPE_ITEM, position, attr)
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


        fun bind(dialog: Image?, type: Int, position: Int, attr: ImageAttr) {
            if (dialog == null)
                return
            Log.i("MyLog", "$dialog tyu")
            itemView.tag = position
            close.tag = position
            userAvatar.layoutParams.height = attr.viewHeight.toInt()
            userAvatar.layoutParams.width = attr.viewWight.toInt()
            imageCardView.radius = attr.cornerRadius

            close.setOnClickListener(this)
            close.setColorFilter(attr.tintClose, PorterDuff.Mode.SRC_IN)
            closeBack.setColorFilter(attr.backClose, PorterDuff.Mode.MULTIPLY)
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

    inner class AddHolder internal constructor(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
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

        fun bind(position: Int, attr: ImageAttr) {
            imageCard.tag = position
            background.layoutParams.width = attr.viewWight.toInt()
            background.layoutParams.height = attr.viewHeight.toInt()
            imageCard.radius = attr.cornerRadius
            srcAdd.setImageDrawable(resources.getDrawable(attr.addAttr.drawable))

            // srcAdd.setImageDrawable()
            imageCard.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mClickListener.onClickAdd(v, v.tag as Int)
        }


    }
}
