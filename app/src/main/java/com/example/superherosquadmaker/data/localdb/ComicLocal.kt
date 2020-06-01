package com.example.superherosquadmaker.data.localdb

import androidx.room.*

@Entity(tableName = "comicsDb", foreignKeys = [ForeignKey(entity=Hero::class, parentColumns = arrayOf("id"), childColumns = arrayOf("characterId"),
    onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE, deferred = true)], indices= arrayOf(
    Index(value = arrayOf("characterId"), unique = true)
) )
data class ComicLocal(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "characterId")
    val characterId: Int = 0
)