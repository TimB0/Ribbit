package com.racecondition.ribbit.espresso;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.racecondition.ribbitsdmp.app.R;
import com.racecondition.ribbitsdmp.app.ui.LoginActivity;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


@LargeTest
public class LoginTests extends ActivityInstrumentationTestCase2<LoginActivity>{

    @SuppressWarnings("deprecation")
    public LoginTests() {
        super("com.racecondition.ribbitsdmp.app.ui", LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void test000CheckText() {
        onView(withId(R.id.title))
                .check(matches(withText("Ribbit")));
    }

    public void test001SignUp() {
        onView(withId(R.id.signUpText)).perform(click());
        onView(withId(R.id.usernameField)).perform(typeText("lkjsdflkjsdf"));
        onView(withId(R.id.passwordField)).perform(typeText("password"));
        onView(withId(R.id.emailField)).perform(typeText("lkjsdflkj"));
        pressBack();
    }

    public void test002Login() {

        // Enter Username
        onView(withId(R.id.usernameField)).perform(typeText("timbolandYahoo"));
        // Enter Password
        onView(withId(R.id.passwordField)).perform(typeText("1"));
        // Click Sign In
        onView(withId(R.id.loginButton)).perform(click());
        // Verify Inbox List Item(s)
        //onView(withId(R.id.senderLabel)).check(matches(withText("nightkitty")));
        onData(allOf(is(instanceOf(String.class)), is("justin")))
                .perform(click());


    }
}




