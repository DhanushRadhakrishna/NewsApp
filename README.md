# NewsApp
The app supports the following functionalities:
1. Display top headlines
2. Scroll through the list to get more responses (limited by the subscription from the apiKey)
3. Search with keywords for news articles. Keywords matches with title, description and content.
4. Enable and diable dark mode. (Mode choice persist over multiple sessions)
5. Add and remove news articles to/from favorites.
6. View favorite news articles (top right icon).
7. Tracks user interactions to Firebase Analytics.
8. An hyperlink to the source redirects you to the default browser or allows a browser selection.
9. Clicking on an article will display more information.

Here are few images from the app.

<img src="assets/NewsApp1.jpeg" width="250"/>
<img src="assets/NewApp2.jpeg" width="250"/>

Adding and removing favorites:

  An outlined favorite indicates the article is not a favorite.
  
  A filled favorite indiactes the article is saved as a favorite.
  
  Tapping on the icon will add/remove from the favorite list.


Searching for a news article:

  The search request with the query triggeres only after 0.5 secs once the user stops typing.
  
  This is to reduce the number of requests to the backend.


Paging:

  Paging is done manually as there is no paging information in the response.
  
  The number of pages with the free account limited. The app displays a message at the bottom once the end is reached.

Please add you own apiKey in local.properties API_KEY=your_api_key

Or please email me at dhanush8699@gmail.com for my apiKey.




# NewsApp — High-Level Architecture                                                                                                                                                                                                
                                                                                                                                                                          

## Architecture Pattern
```
┌─────────────────────────────────────────────┐
│              Presentation Layer             │
│  Compose UI → ViewModel → UIState (Flow)    │
├─────────────────────────────────────────────┤
│               Domain Layer                  │
│   Use Cases → Repository Interfaces         │
├─────────────────────────────────────────────┤
│                Data Layer                   │
│  Repository Impls → Network API + Room DB   │
└─────────────────────────────────────────────┘
```


  Dependency flow is strictly **top-down**: Presentation depends on Domain; Domain defines interfaces; Data implements them. The Domain layer has zero Android/framework dependencies.

  ---

  ## Layers

  ### Presentation (`presentation/`)

  | Component | Responsibility |
  |---|---|
  | `MainActivity` | Single Activity, hosts the Compose NavHost |
  | `NewsScreen` | Top headlines list, search, pagination |
  | `FavoritesScreen` | Saved/favorited articles list |


  ### Domain (`domain/`)

  | Component | Responsibility |
  |---|---|
  | `NewsUseCase` | Fetch paged headlines |
  | `SearchNewsUseCase` | Search articles by query |
  | `FavoritesUseCase` | Add / remove / check / list favorites |
  | `NewsRepository` (interface) | Abstracts network access |
  | `FavoriteRepository` (interface) | Abstracts local DB access |
  | `ArticleHeadline`, `News`, `HeadlineSource` | Domain models (no Android deps) |

  ### Data (`data/`)

  **Network**
  - Retrofit 3.0.0 + OkHttp against `https://newsapi.org/v2/`
  - API key injected via an OkHttp `Interceptor` (read from `BuildConfig`)
  - `safeApiCall()` utility wraps all calls in a `Result<T>` sealed type, translating HTTP errors and network failures into typed error messages

  **Local DB**
  - Room (`NewsArticlesDatabase`) with a single `FavoriteArticle` entity
  - `FavoriteArticleDao` exposes `Flow<List<FavoriteArticle>>` for reactive updates

  **Repositories**
  - `NewsRepositoryImpl` — delegates to Retrofit, returns `Result<News>`
  - `FavoriteRepositoryImpl` — delegates to Room on `Dispatchers.IO`

  ---

  ## Key Libraries

  | Concern | Library |
  |---|---|
  | UI | Jetpack Compose + Material 3 |
  | Navigation | Compose Navigation + Kotlin Serialization (type-safe) |
  | DI | Hilt 2.57.1 |
  | Networking | Retrofit 3.0.0 + OkHttp + Gson |
  | Local Storage | Room 2.8.4 |
  | Preferences | DataStore (dark mode persistence) |
  | Async | Kotlin Coroutines + Flow |
  | Analytics | Firebase Analytics |

  ---


  ## Navigation

  Two destinations using type-safe Kotlin Serialization objects:

  \```
  NavHost
   ├── HomeDestination      → NewsScreen
   └── FavoritesDestination → FavoritesScreen
  \```


  ## Dependency Injection (Hilt Modules)

  | Module | Provides |
  |---|---|
  | `NetworkModule` | OkHttpClient, Retrofit, `NewsApi`, FirebaseAnalytics |
  | `DatabaseModule` | `NewsArticlesDatabase`, `FavoriteArticleDao` |
  | `NewsRepositoryModule` | Binds `NewsRepositoryImpl → NewsRepository` |
  | `FavoriteRepositoryModule` | Binds `FavoriteRepositoryImpl → FavoriteRepository` |
  | `PreferencesModule` | DataStore / UserPreferences |


  ---

  ## Error Handling

  All network calls go through `safeApiCall()` which maps exceptions to `Result.Error` with a user-readable message:

  - Network unavailable
  - Connection timeout
  - HTTP 400 / 401 / 403 / 404 / 429 / 500 / 503
  - Unknown exception fallback

  The ViewModel maps `Result.Error` → `TopHeadlinesUIState.Error` and the UI renders it as an inline error message.

  ---

  ## Firebase Analytics Events

  | Event | Trigger |
  |---|---|
  | `app_open` | App launched |
  | `refresh` | Pull-to-refresh |
  | `article_click` | User opens article URL |
  | `page_load` | New page of headlines fetched |
  | `pagination` | Infinite scroll triggers next page |
  | `search` | Search query submitted |
  | `favorites` | Article favorited/unfavorited |

