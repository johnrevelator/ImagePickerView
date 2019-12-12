package ru.vvdev.imagepicker

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ru.vvdev.imagepickerview.Image

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        firstImPi?.setCanAddPhoto(false)

        val uploadList: List<Image> = listOf(
            Image(Uri.parse("https://d3nevzfk7ii3be.cloudfront.net/igi/hAgr2LwD6AECIwsh.large")),
            Image(Uri.parse("https://cdn0.tnwcdn.com/wp-content/blogs.dir/1/files/2019/07/Saddo-Android-hed-796x419.jpg")),
            Image(Uri.parse("https://cdn.tproger.ru/wp-content/uploads/2015/03/android-development-770x270.jpg"))
        )
        firstImPi.uploadList(uploadList)
        btnUploadPhoto.setOnClickListener {
            secondImPi.uploadImage(uploadList[1])
        }
        // https://d3nevzfk7ii3be.cloudfront.net/igi/hAgr2LwD6AECIwsh.large
        // https://cdn0.tnwcdn.com/wp-content/blogs.dir/1/files/2019/07/Saddo-Android-hed-796x419.jpg
        // https://cdn.tproger.ru/wp-content/uploads/2015/03/android-development-770x270.jpg

    }

}
