package ru.vvdev.imagepickerview

import android.net.Uri

class Image(uri: Uri) {
    var image: Uri
        internal set

    init {
        this.image = uri
    }
}
