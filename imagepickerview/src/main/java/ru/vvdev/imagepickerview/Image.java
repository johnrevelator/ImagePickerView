package ru.vvdev.imagepickerview;

import android.net.Uri;

public class Image {
   Uri image;

    public Uri getImage() {
        return image;
    }

    public Image(Uri uri){
        this.image=uri;
    }
}
