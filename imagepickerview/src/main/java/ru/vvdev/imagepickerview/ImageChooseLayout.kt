package ru.vvdev.imagepickerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources

import java.util.ArrayList
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.COMPLEX_UNIT_SP


/**
 * Created by alexanderklimov on 6/2/18.
 */

class ImageChooseLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs), ImageAddAdapter.OnClickChooseImage {

    internal var imageList: MutableList<Image> = ArrayList()

    internal lateinit var imageAddAdapter: ImageAddAdapter
    private lateinit var imageAttr: ImageAttr

    val items: List<Image>
        get() = imageList

    init {
        init(attrs)
    }


    @SuppressLint("Recycle", "CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.view_image_choose_layout, this, true)
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = HORIZONTAL
        val imageRv = v.findViewById<RecyclerView>(R.id.imageRv)
        imageRv.layoutManager = mLayoutManager
        imageRv.itemAnimator = DefaultItemAnimator()
        imageRv.isNestedScrollingEnabled = false

        imageRv.setHasFixedSize(true)

        val arr = context.obtainStyledAttributes(attrs, R.styleable.imgPickr)
        imageAttr = ImageAttr(
                cornerRadius = arr.getDimension(R.styleable.imgPickr_cornerRadius, resources.getDimension(R.dimen.defCornerRadius)),
                viewHeight = arr.getDimension(R.styleable.imgPickr_viewHeight, resources.getDimension(R.dimen.defHeight)).toInt(),
                viewWight = arr.getDimension(R.styleable.imgPickr_viewWidth, resources.getDimension(R.dimen.defWidth)).toInt(),
                backClose = arr.getColor(R.styleable.imgPickr_backgroundView, Color.parseColor("#ffffff")),
                tintClose = arr.getColor(R.styleable.imgPickr_close_btn_color, resources.getColor(R.color.colorPrimary)),
                addAttr = AddAttr(
                        drawable = arr.getResourceId(R.styleable.imgPickr_imageAdd, R.drawable.ic_camera),
                        text = arr.getString(R.styleable.imgPickr_text) ?: "",
                        textSize = arr.getDimension(R.styleable.imgPickr_textSize, resources.getDimension(R.dimen.defTextSize)).spToPx(),
                        textStyle = arr.getInt(R.styleable.imgPickr_textStyle, 0),
                        imageBack = arr.getColor(R.styleable.imgPickr_backAdd, resources.getColor(R.color.dark_grey)),
                        textColor = arr.getColor(R.styleable.imgPickr_textColor, resources.getColor(R.color.grey)),
                        addHeight = arr.getDimension(R.styleable.imgPickr_addHeight, resources.getDimension(R.dimen.defAddHeight)).toInt(),
                        addWidth = arr.getDimension(R.styleable.imgPickr_addHeight, resources.getDimension(R.dimen.defAddWight)).toInt()
                )
        )
        Log.d("TAGLIFE", "textSize ${arr.getDimension(R.styleable.imgPickr_textSize, resources.getDimension(R.dimen.defTextSize))}" +
                "viewHeight ${arr.getDimension(R.styleable.imgPickr_viewHeight, resources.getDimension(R.dimen.defHeight))}")


        imageRv.setBackgroundColor(imageAttr.backClose)

        imageAddAdapter = ImageAddAdapter(this, imageAttr, resources)
        imageRv.adapter = imageAddAdapter
        imageList.add(Image(Uri.EMPTY))
        imageAddAdapter.setData(imageList)
        imageAddAdapter.reload()

    }


    @SuppressLint("CheckResult")
    fun addImage(context: Context) {
        RxImagePicker.with(context).requestImage(Sources.GALLERY).subscribe { uri ->
            imageList.add(Image(uri))
            imageAddAdapter.setData(imageList)
            imageAddAdapter.reload()
        }

    }

    override fun onClickAdd(v: View, position: Int) {
        addImage(context)

    }

    override fun onClickOpenImage(v: View, position: Int) {
    }

    override fun onClickDeleteImage(v: View, position: Int) {
        if (v.id == R.id.close) {
            imageAddAdapter.deleteItem(position)
        }
    }

    override fun onLongClickImage(v: View, position: Int) {
    }


    private fun Float.spToPx(): Float {
        return this / Resources.getSystem().displayMetrics.density
    }


}
