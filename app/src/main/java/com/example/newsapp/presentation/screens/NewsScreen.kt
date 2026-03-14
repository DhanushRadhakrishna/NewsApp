package com.example.newsapp.presentation.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.presentation.TopBar
import com.example.newsapp.presentation.uistate.TopHeadlinesUIState
import com.example.newsapp.presentation.viewmodel.MainViewModel
import com.example.newsapp.ui.theme.NewsAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeadlines(
    viewModel : MainViewModel = hiltViewModel(),
    onFavoritesIconClick: () -> Unit,
    modifier: Modifier = Modifier)
{
    var isRefreshing by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        TopBar(onFavoritesIconClick = onFavoritesIconClick)
         TopHeadlinesScreen(
             uiState = uiState,
             isRefreshing = isRefreshing,
             updateIsRefreshing = {newRefreshingValue -> isRefreshing = newRefreshingValue}
         ){
             viewModel.getTopHeadlines()
         }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeadlinesScreen(
    uiState : TopHeadlinesUIState,
    isRefreshing : Boolean,
    updateIsRefreshing : (Boolean) -> Unit,
    refreshTopHeadlines : () -> Unit,
    )
{
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
                updateIsRefreshing(true)
                refreshTopHeadlines()
        },
        modifier = Modifier.fillMaxSize()
    ){
        when(uiState){
            is TopHeadlinesUIState.Loading->{
                updateIsRefreshing(false)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
            is TopHeadlinesUIState.Success ->{
                updateIsRefreshing(false)
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.articlesHeadline) { article ->
                        HeadlineItem(article = article)
                        HorizontalDivider()
                    }
                }
            }
            is TopHeadlinesUIState.Error -> {
                updateIsRefreshing(false)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.message)
                }
            }
        }
    }

}

@Composable
fun HeadlineItem(article: ArticleHeadline, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = article.source.name,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Expand",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewsScreenPreview() {
    NewsAppTheme {
        TopHeadlines(viewModel(),{}, Modifier)
    }
}
