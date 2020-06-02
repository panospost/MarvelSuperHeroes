package com.example.superherosquadmaker.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.superherosquadmaker.R
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.data.localdb.HerosLocalDb
import com.example.superherosquadmaker.databinding.FragmentHeroListBinding
import com.example.superherosquadmaker.ui.shared.HeroListViewModel
import com.example.superherosquadmaker.ui.shared.HeroListViewModelFactory
import com.example.superherosquadmaker.ui.shared.LocalDbRepository
import com.example.superherosquadmaker.ui.shared.MarverRepository
import com.example.superherosquadmaker.utils.Status
import com.example.superherosquadmaker.utils.setDivider
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HeroList : Fragment() {

    private lateinit var heroListViewModel: HeroListViewModel
    private lateinit var binding: FragmentHeroListBinding
    private lateinit var adapter: LinearHeroAdapter
    private lateinit var horizontalAdapter: HorizontalHeroListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_hero_list,
            container, false
        )

        setUpViewModel()
        setUpUI()
        renderHeroes()

        return binding.root
    }

    private fun handleItemClick(hero: Hero?) {
        heroListViewModel.getComics(
            hero!!.id
        )

        val action = HeroListDirections.actionHeroListToHeroDetails(hero)
        findNavController().navigate(action)
    }

    private fun setUpViewModel() {
        heroListViewModel = ViewModelProvider(
            requireActivity(),
            HeroListViewModelFactory(
                MarverRepository(
                    requireContext()
                ),
                LocalDbRepository(HerosLocalDb.getInstance(requireContext()))
            )
        ).get(HeroListViewModel::class.java)
    }

    @ExperimentalCoroutinesApi
    private fun setUpUI() {
        binding.viewModel = heroListViewModel
        adapter = LinearHeroAdapter(ItemClickedListener { item -> handleItemClick(item) })
        binding.listView.adapter = adapter
        binding.run {
            listView.setDivider(R.drawable.recycler_view_divider)
            listView.addOnScrollListener(object : RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        heroListViewModel.getMoreHeroes()
                    }
                }

            })
        }

        horizontalAdapter = HorizontalHeroListAdapter(ItemClickedListener { item -> handleItemClick(item) })
        binding.mySquad.adapter = horizontalAdapter
    }

    private fun renderHeroes() {
        heroListViewModel.getHeroes()

        heroListViewModel.heroes.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.loading.visibility = View.GONE
                    adapter.submitList(it.data?.toMutableList())
                    Log.d("newdata", it.data?.size.toString())
                }
                Status.ERROR -> {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                }
            }

        })

        heroListViewModel.mySquad.observe(viewLifecycleOwner, Observer{
            when(it.status) {
                Status.SUCCESS -> {
                    if(it.data?.size!! > 0) {
                        binding.mySquad.visibility = View.VISIBLE
                        binding.mySquadTitle.visibility = View.VISIBLE
                       horizontalAdapter.submitList(it.data)
                    }else {
                        binding.mySquad.visibility = View.GONE
                        binding.mySquadTitle.visibility = View.GONE
                    }

                }
                Status.ERROR -> {
                }
                Status.LOADING -> {}
            }
        })

        heroListViewModel.loadingMore.observe(viewLifecycleOwner, Observer{
            when(it.status) {
                Status.LOADING -> {
                     if(it.data!!) {
                         binding.loadingMore.visibility = View.VISIBLE
                         val layoutParams = (binding.listView.layoutParams as? ViewGroup.MarginLayoutParams)
                         layoutParams?.setMargins(resources.getDimensionPixelSize(R.dimen.recyclerView_left)
                             , 0, 0,
                             resources.getDimensionPixelSize(R.dimen.recyclerView_bottomMargin)
                         )
                     }else {
                         binding.loadingMore.visibility = View.GONE
                         val layoutParams = (binding.listView.layoutParams as? ViewGroup.MarginLayoutParams)
                         layoutParams?.setMargins(resources.getDimensionPixelSize(R.dimen.recyclerView_left)
                             , 0, 0,
                             0
                         )
                     }

                }
                Status.ERROR -> {
                    binding.loadingMore.visibility = View.GONE
                    val layoutParams = (binding.listView.layoutParams as? ViewGroup.MarginLayoutParams)
                    layoutParams?.setMargins(resources.getDimensionPixelSize(R.dimen.recyclerView_left)
                        , 0, 0,
                        0
                    )
                    Log.i("error", it.message!!)

                    Toast.makeText(requireContext(), "There is a problem with the internet connection. Try again", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
