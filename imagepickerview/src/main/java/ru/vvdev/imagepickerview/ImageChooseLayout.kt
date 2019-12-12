package ru.vvdev.imagepickerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import android.support.v7.app.AlertDialog


/**
 * Created by alexanderklimov on 6/2/18.
 */

class ImageChooseLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs),
    ImageAddAdapter.OnClickChooseImage, ImageAddAdapter.OpenClick {

    internal var imageList: MutableList<Image> = mutableListOf()
    internal lateinit var openClick: ImageAddAdapter.OpenClick
    private lateinit var imageRv: RecyclerView
    internal lateinit var imageAddAdapter: ImageAddAdapter
    private lateinit var imageAttr: ImageAttr
    lateinit var lisenter: ILoadPahts


    val items: List<Image>
        get() = imageList
    private val TAG = "ImageChooseLayout"

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
        imageRv = v.findViewById(R.id.imageRv)
        imageRv.layoutManager = mLayoutManager
        imageRv.itemAnimator = DefaultItemAnimator()
        imageRv.isNestedScrollingEnabled = false

        imageRv.setHasFixedSize(true)
        setOpenClickListener(this)
        val arr = context.obtainStyledAttributes(attrs, R.styleable.imgPickr)
        imageAttr = ImageAttr(
            cornerRadius = arr.getDimension(
                R.styleable.imgPickr_cornerRadius,
                resources.getDimension(R.dimen.defCornerRadius)
            ),
            viewHeight = arr.getDimension(
                R.styleable.imgPickr_viewHeight,
                resources.getDimension(R.dimen.defHeight)
            ).toInt(),
            viewWight = arr.getDimension(
                R.styleable.imgPickr_viewWidth,
                resources.getDimension(R.dimen.defWidth)
            ).toInt(),
            backClose = arr.getColor(
                R.styleable.imgPickr_backgroundView,
                Color.parseColor("#ffffff")
            ),
            tintClose = arr.getColor(
                R.styleable.imgPickr_close_btn_color,
                resources.getColor(R.color.colorPrimary)
            ),
            addAttr = AddAttr(
                drawable = arr.getResourceId(R.styleable.imgPickr_imageAdd, R.drawable.ic_camera),
                text = arr.getString(R.styleable.imgPickr_text) ?: "",
                textSize = arr.getDimension(
                    R.styleable.imgPickr_textSize,
                    resources.getDimension(R.dimen.defTextSize)
                ).spToPx(),
                textStyle = arr.getInt(R.styleable.imgPickr_textStyle, 0),
                imageBack = arr.getColor(
                    R.styleable.imgPickr_backAdd,
                    resources.getColor(R.color.dark_grey)
                ),
                textColor = arr.getColor(
                    R.styleable.imgPickr_textColor,
                    resources.getColor(R.color.grey)
                ),
                addHeight = arr.getDimension(
                    R.styleable.imgPickr_addHeight,
                    resources.getDimension(R.dimen.defAddHeight)
                ).toInt(),
                addWidth = arr.getDimension(
                    R.styleable.imgPickr_addHeight,
                    resources.getDimension(R.dimen.defAddWight)
                ).toInt()
            ),
            maxPhotos = arr.getInteger(
                R.styleable.imgPickr_maxPhoto, 0
            ),
            messageWhenMaxSize = arr.getString(R.styleable.imgPickr_messageWhenMaxSize)
                ?: context.getString(R.string.more_when_limit),
            canAddPhoto = arr.getBoolean(R.styleable.imgPickr_canAddPhoto, true),
            canDelete = arr.getBoolean(R.styleable.imgPickr_canDelete, false)
        )
        Log.d(
            "TAGLIFE",
            "textSize ${arr.getDimension(
                R.styleable.imgPickr_textSize,
                resources.getDimension(R.dimen.defTextSize)
            )}" +
                    "viewHeight ${arr.getDimension(
                        R.styleable.imgPickr_viewHeight,
                        resources.getDimension(R.dimen.defHeight)
                    )}"
        )


        imageRv.setBackgroundColor(imageAttr.backClose)

        imageAddAdapter = ImageAddAdapter(this, imageAttr, resources, this)
        imageRv.adapter = imageAddAdapter
        if (imageAttr.canAddPhoto) {
            imageList.add(Image(Uri.EMPTY))
            //  imageAttr.maxPhotos += 1
        }

        imageAddAdapter.setData(imageList)
        imageAddAdapter.reload()

    }


    @SuppressLint("CheckResult")
    fun addImage(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("")
            .setMessage("Нужно выбрать откуда загрузить фотографию")
            .setCancelable(true)
            .setPositiveButton("Камера") { dialog, which ->
                RxImagePicker.with(context).requestImage(Sources.CAMERA).subscribe { uri ->
                    if (::lisenter.isInitialized) {
                        lisenter.loadPhoto(uri)
                    } else {
                        imageList.add(Image(uri))
                        imageAddAdapter.setData(imageList)
                        imageAddAdapter.reload()
                    }

                }
            }
            .setNegativeButton(
                "Галлерея"
            ) { dialog, id ->
                RxImagePicker.with(context).requestImage(Sources.GALLERY).subscribe { uri ->
                    if (::lisenter.isInitialized) {
                        lisenter.loadPhoto(uri)
                    } else {
                        imageList.add(Image(uri))
                        imageAddAdapter.setData(imageList)
                        imageAddAdapter.reload()
                    }

                }
            }
        val alert = builder.create()
        alert.show()

    }


    fun setCanAddPhoto(b: Boolean) {
        if (b != imageAttr.canAddPhoto) {
            imageAttr.canAddPhoto = b

            if (b && imageList.isNotEmpty() && imageList[0].image != Uri.EMPTY) {
                //imageAttr.maxPhotos += 1
                imageList.add(0, Image(Uri.EMPTY))
            } else if (imageList.isNotEmpty() && imageList[0].image == Uri.EMPTY) {
                //  imageAttr.maxPhotos -= 1
                imageList.removeAt(0)
            }
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

    override fun openClick(uri: Uri, position: Int) {
        val i = Intent(context, ImageActivity::class.java).putExtra("uro_image", uri)
        context.startActivity(i)
    }

    /**
     * this set listener for send item(imageUri and itemPosition)
     */
    private fun setOpenClickListener(openClick: ImageAddAdapter.OpenClick) {
        this.openClick = openClick
    }


    /**
     * set animator to recycler view
     * */
    fun setAnimator(animator: RecyclerView.ItemAnimator) {
        imageRv.itemAnimator = animator
    }

    fun uploadList(uploadList: List<Image>) {
        val totalListSize = uploadList.size + imageList.size
        if (imageAttr.maxPhotos != 0 && totalListSize <= imageAttr.maxPhotos) {
            imageList.addAll(uploadList)
            imageAddAdapter.setData(imageList)
            imageAddAdapter.reload()

        } else {
            if (totalListSize - imageAttr.maxPhotos <= 0) {
                Toast.makeText(context, imageAttr.messageWhenMaxSize, Toast.LENGTH_LONG).show()
            } else {
                imageList.addAll(uploadList.take(totalListSize - imageAttr.maxPhotos))
                Log.i(
                    TAG,
                    "images loaded, but not all,  because you try upload $totalListSize, when limit upload photos = ${imageAttr.maxPhotos}"
                )
                imageAddAdapter.setData(imageList)
                imageAddAdapter.reload()
            }
        }
    }


    fun uploadImage(image: Image) {
        val totalListSize = imageList.size + 1
        if (imageAttr.maxPhotos == 0 || totalListSize <= imageAttr.maxPhotos) {
            imageList.add(image)
            imageAddAdapter.setData(imageList)
            imageAddAdapter.reload()
        } else {
            Toast.makeText(context, imageAttr.messageWhenMaxSize, Toast.LENGTH_LONG).show()
        }
    }

    interface ILoadPahts {
        fun loadPhoto(uri: Uri)

    }


}

