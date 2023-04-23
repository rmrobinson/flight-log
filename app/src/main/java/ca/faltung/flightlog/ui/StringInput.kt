package ca.faltung.flightlog.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StringInput(
    val value: String = "",
    val errorResId: Int? = null
) : Parcelable