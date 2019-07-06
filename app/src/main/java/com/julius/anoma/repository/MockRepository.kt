package com.julius.anoma.repository

class MockRepository : Repository {
    override fun getFeeds(): FeedAggregator {
        return FeedAggregator(
            "Feeds", listOf(
                Feed(
                    "Beavers",
                    "Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
                    "http://1.bp.blogspot.com/_VZVOmYVm68Q/SMkzZzkGXKI/AAAAAAAAADQ/U89miaCkcyo/s400/the_golden_compass_still.jpg"
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