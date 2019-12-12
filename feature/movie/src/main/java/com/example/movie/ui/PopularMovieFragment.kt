package com.example.movie.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movie.R
import com.example.movie.domain.PopularMovieUseCase
import com.example.movie.factory.PopularMovieFactory
import kotlinx.android.synthetic.main.fragment_popular_movie.*
import tokopedia.app.abstraction.base.BaseFragment
import tokopedia.app.data.entity.Movie
import tokopedia.app.data.repository.movie.MovieRepository
import tokopedia.app.data.repository.movie.MovieRepositoryImpl
import tokopedia.app.data.routes.NetworkServices
import tokopedia.app.network.Network

class PopularMovieFragment : BaseFragment() {

    override fun contentView(): Int = R.layout.fragment_popular_movie

    private lateinit var viewModel: PopularMovieViewModel
    private lateinit var useCase: PopularMovieUseCase
    private lateinit var repository: MovieRepository

    private val movies = mutableListOf<Movie>()
    //adapter
    private val adapter by lazy {
        PopularMovieAdapter(movies)
    }

    override fun initObservable() {
        val networkBuilder = Network.builder().create(NetworkServices::class.java)

        //init repository
        repository = MovieRepositoryImpl(networkBuilder)
        useCase = PopularMovieUseCase(repository)


        viewModel = ViewModelProviders
            .of(this, PopularMovieFactory(useCase))
            .get(PopularMovieViewModel::class.java)
    }

    override fun initView() {
        rvPopularMovie.adapter = adapter
        rvPopularMovie.layoutManager = GridLayoutManager(context, 2)
        viewModel.movies.observe(viewLifecycleOwner, Observer { data ->
            //get list of movie
            movies.addAll(data.resultsIntent)
            adapter.notifyDataSetChanged()
            Log.d("tagmoviee", data.resultsIntent.toString())
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            showError()
        })
    }

    private fun showError(): Observer<String> {
        return Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}