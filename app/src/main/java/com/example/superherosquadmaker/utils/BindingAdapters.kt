package com.example.superherosquadmaker.utils

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.superherosquadmaker.R
import com.squareup.picasso.Picasso

@BindingAdapter("buttonSwap")
fun Button.swapButton(isInSquad: Int) {
    when(isInSquad) {
        1->{
            text = context.getString(R.string.fire_from_squad)
            setBackgroundColor(context.getColor(R.color.danger))
        }
        0->{
            text = context.getString(R.string.add_him_in_your_squad)
            setBackgroundColor(context.getColor(R.color.colorPrimary))

        }
    }
}

@BindingAdapter("loadFromPicasso")
fun ImageView.loadFromPicasso(url: String?) {
    Picasso.get().load(url)
        .fit()
        .centerCrop().into(this)
}

@BindingAdapter("roundFromPicasso")
fun ImageView.roundFromPicasso(url: String?) {
    Picasso.get().load(url).transform(RoundCornersTransform()).fit().centerCrop().into(this)
}

@BindingAdapter("nullOrEmptyDescription")
fun TextView.nullOrEmptyDescription(description: String) {
    if(description == "") {
        text = context.getString(R.string.no_description)
    }else
        text = description
}