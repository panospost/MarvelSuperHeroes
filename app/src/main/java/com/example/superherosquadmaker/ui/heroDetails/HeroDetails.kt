package com.example.superherosquadmaker.ui.heroDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.superherosquadmaker.*
import com.example.superherosquadmaker.data.api.APIClient
import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.data.localdb.HerosLocalDb
import com.example.superherosquadmaker.databinding.FragmentHeroDetailsBinding
import com.example.superherosquadmaker.ui.shared.HeroListViewModel
import com.example.superherosquadmaker.ui.shared.HeroListViewModelFactory
import com.example.superherosquadmaker.ui.shared.LocalDbRepository
import com.example.superherosquadmaker.ui.shared.MarverRepository
import com.example.superherosquadmaker.utils.*
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HeroDetails : Fragment() {

    private lateinit var heroListViewModel: HeroListViewModel
    private lateinit var binding: FragmentHeroDetailsBinding
    private lateinit var hero: Hero

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_hero_details,
            container,
            false
        )
        setUpUi()
        setUpViewModel()
        setUpComicsObs()
        return binding.root
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    @ExperimentalCoroutinesApi
    private fun setUpUi() {
        hero = arguments?.get("Hero") as Hero
        binding.hero = hero
        binding.goBackToList.setOnClickListener {
            findNavController().navigate(R.id.action_HeroDetails_to_HeroList)
        }
        binding.addRemoveButton.setOnClickListener {
            updateCharacter()
        }
    }

    private fun setUpComicsObs() {
        heroListViewModel.comics.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data is List && it.data.isNotEmpty()) {
                        binding.relativeLayout.visibility = View.VISIBLE
                        binding.andMore.visibility = View.VISIBLE
                        binding.loading.visibility = View.GONE
                        Log.i("we", it.data.toString())
                        setUpComicsSection(it.data)
                    } else {
                        binding.loading.visibility = View.GONE
                        binding.andMore.visibility = View.VISIBLE
                        binding.andMore.text = getString(R.string.no_comics)
                    }
                }
                Status.ERROR -> {
                    binding.relativeLayout.visibility = View.GONE
                    binding.loading.visibility = View.GONE
                    binding.andMore.visibility = View.VISIBLE
                    binding.andMore.text = getString(R.string.no_comics)
                }
                Status.LOADING -> {
                    binding.relativeLayout.visibility = View.GONE
                    binding.loading.visibility = View.VISIBLE
                    binding.andMore.visibility = View.GONE
                }
            }
        })
    }

    private fun setUpComicsSection(comics: List<ComicLocal>?) {
        val totalComics = binding.hero!!.totalComics?.minus(2)
        if (totalComics!! > 2) {
            binding.andMore.text = "and ${binding.hero!!.totalComics?.minus(2)} other comic"
        }

        if (comics != null && comics.isNotEmpty()) {
            populateComics(comics)
        }

    }

    private fun populateComics(comics: List<ComicLocal>) {
        binding.localComic1 = comics[0]
        if(comics.size > 1) {
            binding.localComic2 = comics[1]
        }
    }

    @ExperimentalCoroutinesApi
    fun updateCharacter() {
        if (binding.hero?.isInMySquad == 0) {
            val newHero =
                hero.copyWithNewSquadStatus(1)
            heroListViewModel.updateCharacter(newHero)
            binding.hero = newHero
        }else {
            val dialog = AlertDialog
                .Builder(requireActivity())
                .setTitle(getString(R.string.alert_title))
                .setMessage(getString(R.string.alert_message))
                .setPositiveButton("Yes") { _, _ ->
                        val newHero =
                        hero.copyWithNewSquadStatus(0)
                    heroListViewModel.updateCharacter(newHero)
                    binding.hero = newHero
                }.setNegativeButton("No") { _, _ ->
                }.create()
            dialog.show()
        }

    }


}
