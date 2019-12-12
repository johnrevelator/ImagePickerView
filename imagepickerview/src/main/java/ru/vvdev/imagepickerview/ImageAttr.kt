package ru.vvdev.imagepickerview

class ImageAttr(
    val cornerRadius: Float,
    val viewHeight: Int,
    val viewWight: Int,
    var backClose: Int,
    var tintClose: Int,
    val addAttr: AddAttr,
    var maxPhotos: Int,
    val messageWhenMaxSize: String,
    var canAddPhoto: Boolean,
    val canDelete: Boolean
)