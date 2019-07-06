package com.julius.anoma

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.julius.anoma.repository.Feed
import com.julius.anoma.repository.FeedAggregator
import com.julius.anoma.repository.Repository
import com.julius.anoma.ui.MainActivity
import com.julius.anoma.ui.MainActivityViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest

class FeedInstrumentationTest : KoinTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun startKoin() {
        loadKoinModules(module(override = true) {
            single {
                object : Repository {
                    override fun getFeeds(): FeedAggregator {
                        return FeedAggregator(
                            "Feeds", listOf(
                                Feed("A", "A1", "A2"),
                                Feed("B", "B1", "B2")
                            )
                        )
                    }
                } as Repository
            }
            viewModel { MainActivityViewModel(get()) }
        })
    }

    @Test
    fun testIfViewsAreDisplayed() {
        activityTestRule.launchActivity(Intent())
        verifyIfTitleTextIsDisplayed("Feeds")
        isItemDisplayed(0, "A", R.id.heading)
        isItemDisplayed(0, "A1", R.id.content)
        isItemDisplayed(1, "B", R.id.heading)
        isItemDisplayed(1, "B1", R.id.content)
    }

    private fun verifyIfTitleTextIsDisplayed(text: String) {
        Espresso.onView(withText(text)).check(matches(isDisplayed()))
    }

    private fun isItemDisplayed(position: Int, text: String, viewId: Int) {
        onView(withId(R.id.feedList)).check(
            matches(
                recyclerViewAtPositionOnView(
                    position,
                    withText(text),
                    viewId
                )
            )
        )
    }
}