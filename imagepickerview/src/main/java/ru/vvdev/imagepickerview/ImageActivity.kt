package ru.vvdev.imagepickerview

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        var uri = Uri.EMPTY
        intent.extras?.let {
            uri = it.get("uro_image") as Uri
        }
        imageClose.setOnClickListener { finish() }
        Glide.with(applicationContext)
                .load(uri)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imageSrc)

    }
}