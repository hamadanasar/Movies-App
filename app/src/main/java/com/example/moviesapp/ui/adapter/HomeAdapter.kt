package com.example.moviesapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.R
import com.example.moviesapp.ui.models.ItemDetails
import kotlinx.android.synthetic.main.item_movie.view.*
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter(
    private val nowPlayingList: ArrayList<ItemDetails>,
    private val onFavouriteCallback: (movie: ItemDetails, position: Int) -> Unit,
    private val onMovieCallback: (movie: ItemDetails) -> Unit
) : RecyclerView.Adapter<HomeAdapter.NowPlayingVieHolder>(), Filterable {

    private var itemNowPlayingList: ArrayList<ItemDetails> = nowPlayingList

    class NowPlayingVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingVieHolder {
        return NowPlayingVieHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NowPlayingVieHolder, position: Int) {
        val item = itemNowPlayingList[position]

        holder.itemView.txtTitle.text = item.title
        holder.itemView.txtVoteAverage.text = item.voteAverage.toString()

        val moviePosterUrl = BuildConfig.basePosterUrl + item.posterPath

        Glide.with(holder.itemView.context)
            .load(moviePosterUrl)
            .into(holder.itemView.imgMovie)

        if (!item.isFavourite){
            holder.itemView.btnFavorite.setImageResource(R.drawable.ic_favorite_false)
        }else{
            holder.itemView.btnFavorite.setImageResource(R.drawable.ic_favorite)

        }

        holder.itemView.cardMovie.setOnClickListener {
            onMovieCallback.invoke(item)
        }

        holder.itemView.btnFavorite.setOnClickListener {
            onFavouriteCallback.invoke(item, position)
        }
    }

    override fun getItemCount(): Int {
        return itemNowPlayingList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemNowPlayingList = nowPlayingList
                } else {
                    val resultList = ArrayList<ItemDetails>()
                    for (row in nowPlayingList) {
                        if (row.title!!.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    itemNowPlayingList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = itemNowPlayingList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemNowPlayingList = results?.values as ArrayList<ItemDetails>
                notifyDataSetChanged()
            }
        }
    }

}