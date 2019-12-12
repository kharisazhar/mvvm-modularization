package com.example.moviedetail.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.moviedetail.R
import com.example.moviedetail.domain.MovieDetailUseCase
import com.example.moviedetail.factory.MovieDetailFactory
import kotlinx.android.synthetic.main.activity_movie_detail.*
import tokopedia.app.abstraction.base.BaseActivity
import tokopedia.app.abstraction.util.ext.load
import tokopedia.app.data.entity.Movie
import tokopedia.app.data.repository.movie.MovieRepository
import tokopedia.app.data.repository.movie.MovieRepositoryImpl
import tokopedia.app.data.repository.moviedetail.MovieDetailRepository
import tokopedia.app.data.repository.moviedetail.MovieDetailRepositoryImpl
import tokopedia.app.data.routes.NetworkServices
import tokopedia.app.network.Network

class MovieDetailActivity : BaseActivity() {

    override fun contentView(): Int = R.layout.activity_movie_detail

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var useCase: MovieDetailUseCase
    private lateinit var repository: MovieDetailRepository

    override fun initObservable() {
        val networkBuilder = Network.builder().create(NetworkServices::class.java)

        //init repository
        repository = MovieDetailRepositoryImpl(networkBuilder)

        //init usecase
        useCase = MovieDetailUseCase(repository)

        //init viewModel
        viewModel = ViewModelProviders
            .of(this, MovieDetailFactory(useCase))
            .get(MovieDetailViewModel::class.java)
    }

    override fun initView() {
        // get movie ID
        intent?.data?.lastPathSegment?.let {
            viewModel.setMovieId(it)
        }


        viewModel.error.observe(this, Observer {
            onShowError()
        })

        viewModel.movie.observe(this, Observer {
            showMoviewDetail(it)
        })
    }

    private fun showMoviewDetail(movie: Movie) {
        imgBanner.load(movie.bannerUrl())
        imgPoster.load(movie.posterUrl())
        txtMovieName.text = movie.title
        txtYear.text = movie.releaseDate
        txtRating.text = movie.voteAverage.toString()
        txtVote.text = movie.voteCount.toString()
        txtContent.text = movie.overview
    }

    private fun onShowError(): Observer<String> {
        return Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

}
