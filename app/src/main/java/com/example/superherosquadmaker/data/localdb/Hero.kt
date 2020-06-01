package com.example.superherosquadmaker.data.localdb

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "superheroes")
data class Hero(
                @PrimaryKey
                val id: Int,
                @ColumnInfo(name = "name")
                val name: String = "",
                @ColumnInfo(name = "description")
                val description: String = "",
                @ColumnInfo(name = "thumbnail")
                val thumbnail: String = "",
                @ColumnInfo(name = "totalComics")
                val totalComics: Int? = 0,
                @ColumnInfo(name = "isInMySquad")
                val isInMySquad: Int = 0) :Parcelable

