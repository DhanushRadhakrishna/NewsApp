package com.example.newsapp.presentation.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.newsapp.ui.theme.NewsAppTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsapp.data.local.entity.FavoriteArticle
import com.example.newsapp.presentation.viewmodel.FavoritesViewModel
import com.example.newsapp.presentation.viewmodel.MainViewModel
import com.example.newsapp.ui.theme.HyperlinkBlue

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel : FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.allFavorites.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        FavoritesTopBar()
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorites yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = favorites, key = { it.url }) { article ->
                    FavoriteItem(article = article){viewModel.removeFromFavorites(article)}
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(article: FavoriteArticle,
                 modifier: Modifier = Modifier,
                 removeFavorite:() -> Unit
                 ) {
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 12.dp)
//    ) {
//        Text(
//            text = article.title,
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.SemiBold
//        )
//        Text(
//            text = article.sourceName,
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier.padding(top = 4.dp)
//        )
//        Text(
//            text = article.publishedAt.take(10),
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier.padding(top = 2.dp)
//        )
//    }
    var expanded by remember { mutableStateOf(false) }
    val publishedDate = article.publishedAt.take(10)
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = article.sourceName,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = HyperlinkBlue,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                            context.startActivity(intent)
                        }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { removeFavorite() }) {
                    Icon(
                        imageVector =  Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(width = 22.dp, height = 22.dp)
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier
                )
            }

        }
        if (expanded) {
            Text(
                text = publishedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
            article.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun FavoritesTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 0.dp)
        )
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    val fakeFavorites = (1..4).map {
        FavoriteArticle(
            url = "https://example.com/article/$it",
            title = "Sample favorite article headline number $it",
            description = "A short description for article $it.",
            publishedAt = "2024-03-14T08:00:00Z",
            sourceName = "BBC News"
        )
    }
    NewsAppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            FavoritesTopBar()
            LazyColumn {
                items(items = fakeFavorites, key = { it.url }) { article ->
                    FavoriteItem(article = article, modifier = Modifier,{})
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Favorites Empty")
@Composable
fun FavoritesScreenEmptyPreview() {
    NewsAppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            FavoritesTopBar()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No favorites yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}