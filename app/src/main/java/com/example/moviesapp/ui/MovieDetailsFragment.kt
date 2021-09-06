package com.example.moviesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentMovieDetailsBinding
import com.example.moviesapp.ui.models.ItemDetails
import com.example.moviesapp.ui.viewmodel.HomeViewModel

class MovieDetailsFragment : Fragment() {

    lateinit var viewRef: FragmentMovieDetailsBinding

    private val viewModel: HomeViewModel by activityViewModels()

    lateinit var item: ItemDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = viewModel.selectedMovie
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewRef = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        initViews()

        return viewRef.root
    }

    private fun initViews() {
        viewRef.txtTitle.text = item.title.toString()

        viewRef.txtVoteAverage.text = item.voteAverage.toString()
        viewRef.txtCountAverage.text = item.voteCount.toString()

        viewRef.txtDescription.text = item.overview.toString()

        val moviePosterUrl = BuildConfig.basePosterUrl + item.posterPath

        Glide.with(requireActivity())
            .load(moviePosterUrl)
            .into(viewRef.imgMovie)

        viewRef.txtGenreIds.text =
            getString(R.string.genre_id) + "\t" + "\t" + item.genreIds.toString()
    }

}