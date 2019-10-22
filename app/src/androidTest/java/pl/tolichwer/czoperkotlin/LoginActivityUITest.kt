package pl.tolichwer.czoperkotlin

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.tolichwer.czoperkotlin.ui.loginView.LoginActivity

const val USERNAME = "Tomek"
const val PASSWORD = "tomek"


@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityUITest {


    @Rule
    var loginActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun testLoginAction() {

        onView(withId(R.id.username)).perform(ViewActions.clearText(), typeText(USERNAME))
        onView(withId(R.id.password)).perform(ViewActions.clearText(), typeText(PASSWORD))
        onView(withId(R.id.btn_login))
            .perform(click())

        // intended(hasC)



    }
}