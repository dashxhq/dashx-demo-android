package com.dashxdemo.app.utils.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoPlayerData(
    val videoUrl: String,
) : Parcelable
