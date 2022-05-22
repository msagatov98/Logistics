package kz.logistics.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Deliver(
    val price: Int = 0,
    val name: String = "",
    val imageResId: Int = 0
) : Parcelable