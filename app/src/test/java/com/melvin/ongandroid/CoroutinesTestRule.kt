package com.melvin.ongandroid

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Coroutines test rule
 * in order to overwrite starting and finished functions
 * created on 26 April 2022 by Leonel Gomez
 *
 * @constructor Create empty Coroutines test rule
 */
@ExperimentalCoroutinesApi
class CoroutinesTestRule : TestWatcher() {

    val testDispatcher = TestCoroutineDispatcher()

    override fun starting(description: Description?) {
        super.starting(description)
        //pass dispatcher created
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        //To reset original dispatcher
        Dispatchers.resetMain()
        //Cancel testdispatcher in case he got hooked
        testDispatcher.cleanupTestCoroutines()
    }
}