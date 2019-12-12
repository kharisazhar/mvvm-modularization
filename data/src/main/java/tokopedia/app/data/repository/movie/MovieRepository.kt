package tokopedia.app.data.repository.movie

import tokopedia.app.data.entity.Movies
import retrofit2.Response
import tokopedia.app.data.entity.Movie

interface MovieRepository {
    suspend fun getPopularMovie(): Response<Movies>
}