package com.example.mobilebd

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.util.Log
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun MovieScreen(movieViewModel: MovieViewModel = viewModel()) {
    val movies by movieViewModel.moviesWithPhotos.collectAsState(initial = emptyList())
    var showAddMovieFields by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var photoName by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Movies List", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Список фильмов
        items(movies.size) { index ->
            val movieWithPhotos = movies[index]

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val imageResId = movieWithPhotos.photos.firstOrNull()?.photoName?.let { photo ->
                    val resourceId = movieViewModel.getResourceIdByName(photo)
                    if (resourceId != 0) resourceId else null
                }

                imageResId?.let { resId ->
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = movieWithPhotos.movie.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = movieWithPhotos.movie.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Rating: ${movieWithPhotos.movie.rating}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = movieWithPhotos.movie.description,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }

        // Кнопка для добавления фильма
        item {
            Button(onClick = { showAddMovieFields = !showAddMovieFields }) {
                Text("Add Movie")
            }
        }

        // Форма для добавления нового фильма
        if (showAddMovieFields) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = rating,
                        onValueChange = { rating = it },
                        label = { Text("Rating") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = photoName,
                        onValueChange = { photoName = it },
                        label = { Text("Photo Name") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        val ratingValue = rating.toFloatOrNull() ?: 0.0f
                        if (title.isNotBlank() && description.isNotBlank()) {
                            try {
                                movieViewModel.addMovie(title, ratingValue, description, photoName)
                                title = ""
                                description = ""
                                rating = ""
                                photoName = ""
                                showAddMovieFields = false
                            } catch (e: Exception) {
                                Log.e("MovieScreen", "Ошибка добавления фильма: ${e.message}")
                            }
                        } else {
                            Log.e("MovieScreen", "Ошибка: Заголовок или описание пусты")
                        }
                    }) {
                        Text("Save Movie")
                    }
                }
            }
        }
    }
}
