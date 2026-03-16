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
