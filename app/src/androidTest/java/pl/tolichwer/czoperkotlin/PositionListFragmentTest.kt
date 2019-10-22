package pl.tolichwer.czoperkotlin

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import pl.tolichwer.czoperkotlin.ui.NavigationActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class PositionListFragmentTest


    @Rule
    val navigationActivityRule: ActivityTestRule<NavigationActivity> = ActivityTestRule(NavigationActivity::class.java)


    @Before
    fun setUp(){
        navigationActivityRule.activity.supportFragmentManager.beginTransaction()
    }


