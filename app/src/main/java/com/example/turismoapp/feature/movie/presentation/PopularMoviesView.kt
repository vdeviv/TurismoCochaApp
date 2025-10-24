package com.example.turismoapp.feature.movie.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.turismoapp.feature.movie.domain.model.MovieModel

@Composable
fun PopularMoviesView( movies: List<MovieModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(movies.size) {
            CardMovie(movie = movies[it])
        }
    }
}

@Composable
fun CardMovie(movie: MovieModel) {
    OutlinedCard(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = movie.pathUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            )

            Text(
                text = movie.title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                maxLines = 2
            )
        }
    }
}