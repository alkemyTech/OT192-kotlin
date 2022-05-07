package com.melvin.ongandroid.viewmodel.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.melvin.ongandroid.CoroutinesTestRule
import com.melvin.ongandroid.R
import com.melvin.ongandroid.core.ResourcesProvider
import com.melvin.ongandroid.repository.OngRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SignUpViewModelTest {

    companion object {
        const val nameOk = "Leo"
        const val emailOk = "admin@alkemy.com"
        const val passwordOk = "asd123A#"
        const val passwordConfirmOk = "asd123A#"
    }

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var ruleInstantExecutor: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var resourcesProvider: ResourcesProvider

    //@RelaxedMockK
    //private lateinit var repo: OngRepository

    private lateinit var vm: SignUpViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        vm = SignUpViewModel(resourcesProvider)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `check all fields are ok`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = passwordOk
        val passwordConfirmTest = passwordConfirmOk

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(true, vm.isRegisterBtnEnabled.value)
    }


}