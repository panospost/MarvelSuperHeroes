package com.example.superherosquadmaker.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superherosquadmaker.*
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.databinding.HeroListViewHorizontalBinding
import com.example.superherosquadmaker.utils.RoundCornersTransform
import com.squareup.picasso.Picasso

class HorizontalHeroListAdapter(private val onClickListener: ItemClickedListener): ListAdapter<Hero, HorizontalHeroListAdapter.ViewHolder>(DiffUtilCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bindingLinear: HeroListViewHorizontalBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.hero_list_view_horizontal , parent, false
        )
        return ViewHolder(bindingLinear)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), onClickListener)


    inner class ViewHolder(val binding: HeroListViewHorizontalBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: Hero, listener: ItemClickedListener){
            binding.user = user
            binding.clickListener = listener
        }
    }
}

