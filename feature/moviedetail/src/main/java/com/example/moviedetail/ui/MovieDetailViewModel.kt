package com.example.moviedetail.ui

import androidx.lifecycle.*
import com.example.moviedetail.domain.MovieDetailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tokopedia.app.abstraction.util.state.ResultState
import tokopedia.app.data.entity.Movie

interface MoviewDetailContract {
    fun getMovieDetail(movieId: String)
    fun setMovieId(movieId: String)
}


class MovieDetailViewModel(
    private val useCase: MovieDetailUseCase
) : ViewModel(), MoviewDetailContract {

    private val _movieId = MutableLiveData<String>()

    private val _movie = MediatorLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    init {
        _movie.addSource(_movieId) {
            getMovieDetail(it)
        }
    }

    override fun setMovieId(movieId: String) {
        _movieId.value = movieId
    }


    override fun getMovieDetail(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = useCase.get(movieId)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ResultState.Success -> {
                        _movie.value = response.data
                    }
                    is ResultState.Error -> {
                        _error.value = response.error
                    }
                }
            }
        }
    }

}