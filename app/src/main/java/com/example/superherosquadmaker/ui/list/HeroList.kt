package com.example.superherosquadmaker.ui.list

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.superherosquadmaker.MainActivity
import com.example.superherosquadmaker.R
import com.example.superherosquadmaker.data.api.APIClient
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
 *A list of heroes that are being fetched from the marvel api.
 * plus the my squad section with the heroes the user clicked.
 * Also the ability to search a hero by name directly to the marvel api
 * if the hero is not cached
 */
class HeroList : Fragment() {

    private lateinit var heroListViewModel: HeroListViewModel
    private lateinit var binding: FragmentHeroListBinding
    private lateinit var adapter: LinearHeroAdapter
    private lateinit var horizontalAdapter: HorizontalHeroListAdapter

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_hero_list,
            container, false
        )
        setHasOptionsMenu(true)

        setUpViewModel()
        setUpUI()
        renderHeroes()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
        val searchView = SearchView((context as MainActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.action_search).apply {
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                heroListViewModel.getHeroByName(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText) || newText.isEmpty()) {
                    heroListViewModel.getHeroes()
                }else if(newText.length > 3) {
                    heroListViewModel.getHeroByName(newText)
                }
                return false
            }
        })
    }

    @ExperimentalCoroutinesApi
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
                    APIClient.getRetrofit(requireContext())
                ),
                LocalDbRepository(HerosLocalDb.getInstance(requireContext()).heroesDao)
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
                    binding.mySquad.visibility = View.GONE
                    binding.mySquadTitle.visibility = View.GONE

                    Toast.makeText(requireContext(), getString(R.string.squad_didnt_load), Toast.LENGTH_SHORT).show()

                }
                Status.LOADING -> {}
            }
        })

        heroListViewModel.loadingMore.observe(viewLifecycleOwner, Observer{
            when(it.status) {
                Status.LOADING -> {
                     if(it.data!!) {
                         binding.loadingMore.visibility = View.VISIBLE
                         changeRecyclerViewParams(resources.getDimensionPixelSize(R.dimen.recyclerView_bottomMargin))
                     }else {
                         binding.loadingMore.visibility = View.GONE
                         changeRecyclerViewParams(0)
                     }

                }
                Status.ERROR -> {
                    binding.loadingMore.visibility = View.GONE
                    changeRecyclerViewParams(0)

                    Toast.makeText(requireContext(), getString(R.string.connection_problem), Toast.LENGTH_SHORT).show()
                }
                else ->{

                }
            }
        })
    }

    private fun changeRecyclerViewParams(bottom: Int) {
        val layoutParams = (binding.listView.layoutParams as? ViewGroup.MarginLayoutParams)
        layoutParams?.setMargins(resources.getDimensionPixelSize(R.dimen.recyclerView_left)
            , 0, 0,
            bottom
        )
    }

}
