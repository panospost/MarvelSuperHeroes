package com.example.superherosquadmaker.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superherosquadmaker.*
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.databinding.HeroListViewBinding
import com.example.superherosquadmaker.utils.RoundCornersTransform
import com.squareup.picasso.Picasso

class LinearHeroAdapter(private val onClickListener: ItemClickedListener): ListAdapter<Hero, LinearHeroAdapter.ViewHolder>(DiffUtilCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
             val bindingLinear: HeroListViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),R.layout.hero_list_view , parent, false
            )
            return ViewHolder(bindingLinear)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), onClickListener)


    inner class ViewHolder(val binding: HeroListViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: Hero, listener: ItemClickedListener){
            binding.user = user
            binding.clickListener = listener
            Picasso.get().load(user.thumbnail).transform(RoundCornersTransform()).fit().centerCrop().into(binding.heroImg)
            binding.executePendingBindings()
        }
    }
}

class DiffUtilCallback: DiffUtil.ItemCallback<Hero>(){

    override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean {
        if (oldItem == newItem) {
            return true
        }
        return false
    }
}

class ItemClickedListener(val clickListener: (item: Hero?) -> Unit) {
    fun onClick(item: Hero) = clickListener(item)
}