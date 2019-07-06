package com.julius.anoma.repository

class MockRepository : Repository {
    override fun getFeeds(): FeedAggregator {
        return FeedAggregator(
            "Feeds", listOf(
                Feed(
                    "Beavers",
                    "Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
                    "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
                ),
                Feed(
                    "Geography",
                    "It's really big.",
                    "null"
                )
            )
        )
    }
}