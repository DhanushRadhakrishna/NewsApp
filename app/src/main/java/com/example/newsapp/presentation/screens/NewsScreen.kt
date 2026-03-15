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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.model.HeadlineSource
import com.example.newsapp.presentation.uistate.ContentDisplayState
import com.example.newsapp.presentation.uistate.DisplayState
import com.example.newsapp.presentation.uistate.TopHeadlinesScreenState
import com.example.newsapp.presentation.viewmodel.MainViewModel
import com.example.newsapp.ui.theme.HyperlinkBlue
import com.example.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeadlines(
    modifier: Modifier = Modifier,
    viewModel : MainViewModel = hiltViewModel(),
    onFavoritesIconClick: () -> Unit
)
{
    var isRefreshing by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            query = searchQuery,
            onQueryChange ={newQuery -> viewModel.onSearchQueryChange(newQuery)},
            onFavoritesIconClick = onFavoritesIconClick)
        TopHeadlinesScreen(
            uiState = uiState,
            isRefreshing = isRefreshing,
            refreshTopHeadlines = { viewModel.getTopHeadlines()},
            onArticleClick = {url -> viewModel.onArticleClick(url)},
            updateIsRefreshing = {newRefreshingValue -> isRefreshing = newRefreshingValue}
        ){
            viewModel.paginate()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeadlinesScreen(
    uiState : TopHeadlinesScreenState,
    isRefreshing : Boolean,
    onArticleClick: (String) -> Unit,
    updateIsRefreshing : (Boolean) -> Unit,
    refreshTopHeadlines : () -> Unit,
    getNextPage : () -> Unit,
    )
{
    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index >= totalItems - 1
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                getNextPage()
            }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
                updateIsRefreshing(true)
                refreshTopHeadlines()
        },
        modifier = Modifier.fillMaxSize()
    ){
        when(val displayState = uiState.displayState){
            is DisplayState.Loading ->{
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
            is DisplayState.Error -> {
                updateIsRefreshing(false)
                Box(
                    modifier = Modifier
                        .padding(top = 30.dp, start = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(text = displayState.message)
                }
//                LazyColumn(state = listState,modifier = Modifier.fillMaxSize()){
//                    items(
//                        items = dummyArticles,
//                        key = {article -> article.url}
//                    ){article ->
//                        HeadlineItem(article = article)
//                        HorizontalDivider()
//                    }
//                }
            }
            is DisplayState.Content ->{
                updateIsRefreshing(false)
                LazyColumn(state = listState,modifier = Modifier.fillMaxSize()) {
                    items(
                        items = displayState.articleHeadlines,
//                        key = {article -> article.url}
                    ) { article ->
                        HeadlineItem(
                            article = article,
                            onArticleClick = {articleUrl -> onArticleClick(articleUrl)}
                        )
                        HorizontalDivider()
                    }
                    item{
                        if(displayState.contentDisplayState is ContentDisplayState.Loading)
                        {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                    item{
                        if(displayState.contentDisplayState is ContentDisplayState.PaginationError)
                        {
                            Text(
                                text = displayState.contentDisplayState.message,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
            }

            null -> {
                updateIsRefreshing(false)
                Box(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(text = "No news today.")
                }
            }
        }
    }

}

@Composable
fun HeadlineItem(article: ArticleHeadline,
                 onArticleClick : (String) -> Unit,
                 modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val publishedDate = article.publishedAt.take(10)
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
                onArticleClick(article.url)
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
                    text = article.source.name,
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
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewsScreenPreview() {
    val fakeArticles = (1..5).map {
        ArticleHeadline(
            source = HeadlineSource(id = "$it", name = "BBC News"),
            title = "Breaking news headline number $it that can be quite long and wrap",
            description = "This is a short description for article $it.",
            url = "",
            publishedAt = "2024-03-14T08:00:00Z"
        )
    }
    NewsAppTheme {
        TopHeadlinesScreen(
            uiState = TopHeadlinesScreenState(displayState = DisplayState.Content(fakeArticles)),
            isRefreshing = false,
            onArticleClick = {},
            refreshTopHeadlines = {},
            updateIsRefreshing = {},
            getNextPage = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeadlineItemPreview() {
    NewsAppTheme {
        HeadlineItem(
            article = ArticleHeadline(
                source = HeadlineSource(id = "1", name = "Reuters"),
                title = "Sample news headline that might wrap to a second line",
                description = "A brief description of the article content goes here.",
                url = "",
                publishedAt = "2024-03-14T08:00:00Z"
            ),
            {}
        )
    }
}
