package org.totschnig.myexpenses.test.espresso

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers
import org.junit.BeforeClass
import org.junit.Test
import org.totschnig.myexpenses.R
import org.totschnig.myexpenses.activity.TestMyExpenses
import org.totschnig.myexpenses.model.Account
import org.totschnig.myexpenses.provider.DatabaseConstants
import org.totschnig.myexpenses.testutils.BaseMyExpensesTest

class MyExpensesIntentTest : BaseMyExpensesTest() {
    @Test
    fun shouldNavigateToAccountReceivedThroughIntent1() {
       doTheTest(account1)
    }

    @Test
    fun shouldNavigateToAccountReceivedThroughIntent2() {
       doTheTest(account2)
    }

    private fun doTheTest(account: Account) {
        testScenario = ActivityScenario.launch(
            Intent(targetContext, TestMyExpenses::class.java)
                .putExtra(DatabaseConstants.KEY_ROWID, account.id)
        )
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText(account.label),
                ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    companion object {
        private lateinit var account1: Account
        private lateinit var account2: Account

        @JvmStatic
        @BeforeClass
        fun fixture() {
            account1 = Account("Test label 1", 0, "").also {
                it.save()
            }
            account2 = Account("Test label 2", 0, "").also {
                it.save()
            }
        }
    }
}