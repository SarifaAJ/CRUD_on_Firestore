package com.example.crudonfirestore.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Students(
    var NIS: String? = null,
    var name: String? = null,
    var gender: String? = null,
    var religion: String? = null,
    var birthDate: String? = null
) : Parcelable
