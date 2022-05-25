package com.melvin.ongandroid.viewmodel.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.melvin.ongandroid.CoroutinesTestRule
import com.melvin.ongandroid.core.ResourcesProvider
import com.melvin.ongandroid.repository.OngRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @RelaxedMockK
    private lateinit var repo: OngRepository

    private lateinit var vm: SignUpViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        vm = SignUpViewModel(resourcesProvider, repo)
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

    @Test
    fun `check empty name`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = ""
        val emailTest = emailOk
        val passwordTest = passwordOk
        val passwordConfirmTest = passwordConfirmOk

        assertNotEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(false, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check empty email`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = ""
        val passwordTest = passwordOk
        val passwordConfirmTest = passwordConfirmOk

        assertEquals(null, vm.checkName(nameTest))
        assertNotEquals(null, vm.checkEmail(emailTest))
        assertEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(false, vm.isEmailValid)
        assertEquals(true, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check empty password`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = ""
        val passwordConfirmTest = passwordConfirmOk

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertNotEquals(null, vm.checkPassword(passwordTest))
        assertNotEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(false, vm.isPasswordValid)
        assertEquals(false, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check empty password confirm`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = passwordOk
        val passwordConfirmTest = ""

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertEquals(null, vm.checkPassword(passwordTest))
        assertNotEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isPasswordValid)
        assertEquals(false, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check short length of name`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = "Le"     //Less than three letters
        val emailTest = emailOk
        val passwordTest = passwordOk
        val passwordConfirmTest = passwordConfirmOk

        assertNotEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(false, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check wrong email`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = "admin.com" //wrong email, does not have @
        val passwordTest = passwordOk
        val passwordConfirmTest = passwordConfirmOk

        assertEquals(null, vm.checkName(nameTest))
        assertNotEquals(null, vm.checkEmail(emailTest))
        assertEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(false, vm.isEmailValid)
        assertEquals(true, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check wrong email 2`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = "admin@alkemy"    //wrong email, does not have .com for example
        val passwordTest = passwordOk
        val passwordConfirmTest = passwordConfirmOk

        assertEquals(null, vm.checkName(nameTest))
        assertNotEquals(null, vm.checkEmail(emailTest))
        assertEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(false, vm.isEmailValid)
        assertEquals(true, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check short password`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = "asd12A#"    //Less than 8
        val passwordConfirmTest = passwordTest

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertNotEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(false, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check weak password without special character`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = "asd123AA"    //Does not have special character
        val passwordConfirmTest = passwordTest

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertNotEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(false, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check weak password without special upper case`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = "asd123a#"    //Does not have upper character
        val passwordConfirmTest = passwordTest

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertNotEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(false, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check weak password without lower character`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = "ASD123A#"    //Does not have lower character
        val passwordConfirmTest = passwordTest

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertNotEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(false, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check weak password without number`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = "asd###A#"    //Does not have a number
        val passwordConfirmTest = passwordTest

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertNotEquals(null, vm.checkPassword(passwordTest))
        assertEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(false, vm.isPasswordValid)
        assertEquals(true, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }

    @Test
    fun `check passwords do not match`() = runTest(coroutinesTestRule.testDispatcher) {
        val nameTest = nameOk
        val emailTest = emailOk
        val passwordTest = passwordOk
        val passwordConfirmTest = "$passwordOk#" //Does not match password

        assertEquals(null, vm.checkName(nameTest))
        assertEquals(null, vm.checkEmail(emailTest))
        assertEquals(null, vm.checkPassword(passwordTest))
        assertNotEquals(null, vm.checkPasswordConfirm(passwordConfirmTest))

        assertEquals(true, vm.isNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isPasswordValid)
        assertEquals(false, vm.isPasswordConfirmValid)
        assertEquals(false, vm.isRegisterBtnEnabled.value)
    }
}