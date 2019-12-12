package com.example.movie.ui

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import kotlinx.android.synthetic.main.item_movie.view.*
import tokopedia.app.abstraction.util.ext.load
import tokopedia.app.data.entity.Movie

class PopularMovieAdapter(
    private val movies: List<Movie>
) : RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PopularMovieViewHolder.create(parent)

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size
    class PopularMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtTitle = view.txtMovieTitle
        private val txtYear = view.txtYear
        private val imgPoster = view.imgPoster
        fun bind(movie: Movie) {
            imgPoster.load(movie.posterUrl())
            txtTitle.text = movie.title
            txtYear.text = movie.releaseDate

            itemView.setOnClickListener {
                onMovieItemClick(movie)
            }
        }

        private fun onMovieItemClick(movie: Movie) {
            val url = "movie://detail/${movie.id}".toUri()
            itemView.context.startActivity(Intent(ACTION_VIEW, url))
        }

        companion object {
            fun create(viewGroup: ViewGroup): PopularMovieViewHolder {
                val view = LayoutInflater
                    .from(viewGroup.context)
                    .inflate(R.layout.item_movie, viewGroup, false)
                return PopularMovieViewHolder(view)
            }
        }
    }
}