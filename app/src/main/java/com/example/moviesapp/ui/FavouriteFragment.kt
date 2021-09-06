package com.example.moviesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyforecastapp.Error.HandleError
import com.example.moviesapp.MainActivity
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentFavouriteBinding
import com.example.moviesapp.ui.adapter.HomeAdapter
import com.example.moviesapp.ui.models.ItemDetails
import com.example.moviesapp.ui.viewmodel.HomeViewModel

class FavouriteFragment : Fragment() {

    private lateinit var viewRef: FragmentFavouriteBinding

    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var homeAdapter: HomeAdapter

    private val onFavouriteCallback: (movie: ItemDetails, position: Int) -> Unit = { movie, pos ->
        if (movie.isFavourite) viewModel.removeFromFavourite(movie)
        else viewModel.addToFavourite(movie)
        movie.isFavourite = !movie.isFavourite
        homeAdapter.notifyItemChanged(pos)
    }

    private val onMovieCallback: (movie: ItemDetails) -> Unit = { movie ->
        viewModel.selectedMovie = movie
        findNavController().navigate(R.id.action_favouriteFragment_to_movieDetailsFragment2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewRef = FragmentFavouriteBinding.inflate(inflater, container, false)

        setBtnListeners()

        subscribeToLiveData()

        return viewRef.root
    }

    private fun subscribeToLiveData() {
        viewModel.favMoviesLiveData.observe(viewLifecycleOwner, {
            it.let {
                viewRef.txtEmptyFavourite.visibility = View.GONE
                (requireActivity() as MainActivity?)!!.hideProgressBar()
                initRecycler(it)
            }
        })
    }

    private fun setBtnListeners() {
        viewRef.searchViewTopRated.BaseSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                this@FavouriteFragment.homeAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun initRecycler(data: ArrayList<ItemDetails>?) {
        viewRef.recyclerFavourite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            homeAdapter = HomeAdapter(data!!, onFavouriteCallback, onMovieCallback)
            adapter = homeAdapter
        }
    }
}