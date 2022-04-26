package com.melvin.ongandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.melvin.ongandroid.CoroutinesTestRule
import com.melvin.ongandroid.repository.OngRepository
import org.junit.Assert.*
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Contact view model test
 * checks form fields from Contact UI
 * created on 26 April 2022 by Leonel Gomez
 *
 * @constructor Create empty Contact view model test
 */
@ExperimentalCoroutinesApi
class ContactViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repo: OngRepository

    private lateinit var vm: ContactViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        vm = ContactViewModel(repo)
        setSuccessfulFields()
    }

    @After
    fun tearDown() {
    }

    private fun setSuccessfulFields(){
        vm.firstName.value = "Leo"
        vm.lastName.value = "Gom"
        vm.email.value = "admin@alkemy.com"
        vm.contactMessage.value = "Debe tener igual o mas de 30 ."
    }

    @Test
    fun `check all fields are ok`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isFirstNameValid)
        assertEquals(true, vm.isLastNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isContactMessageValid)
        assertEquals(true, vm.isButtonEneabled.value)
    }

    @Test
    fun `check empty firstname`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.firstName.value = "" //empty
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isLastNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isContactMessageValid)

        assertEquals(false, vm.isFirstNameValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

    @Test
    fun `check short length of firstname`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.firstName.value = "Wi"   //two
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isLastNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isContactMessageValid)

        assertEquals(false, vm.isFirstNameValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

    @Test
    fun `check empty lastname`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.lastName.value = ""  //empty
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isFirstNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isContactMessageValid)

        assertEquals(false, vm.isLastNameValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

    @Test
    fun `check short length of lastname`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.lastName.value = "Wi"    //two
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isFirstNameValid)
        assertEquals(true, vm.isEmailValid)
        assertEquals(true, vm.isContactMessageValid)

        assertEquals(false, vm.isLastNameValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

    @Test
    fun `check empty message`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.contactMessage.value = ""    //empty
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isFirstNameValid)
        assertEquals(true, vm.isLastNameValid)
        assertEquals(true, vm.isEmailValid)

        assertEquals(false, vm.isContactMessageValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

    @Test
    fun `check short message`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.contactMessage.value = "Debe tener igual o mas de 30 "   //29
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isFirstNameValid)
        assertEquals(true, vm.isLastNameValid)
        assertEquals(true, vm.isEmailValid)

        assertEquals(false, vm.isContactMessageValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

    @Test
    fun `check empty email`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.email.value = ""    //empty
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isFirstNameValid)
        assertEquals(true, vm.isLastNameValid)
        assertEquals(true, vm.isContactMessageValid)

        assertEquals(false, vm.isEmailValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

    @Test
    fun `check wrong email`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.email.value = "admin.com"    //wrong email, does not have @
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isFirstNameValid)
        assertEquals(true, vm.isLastNameValid)
        assertEquals(true, vm.isContactMessageValid)

        assertEquals(false, vm.isEmailValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

    @Test
    fun `check wrong email 2`() = coroutinesTestRule.testDispatcher.runBlockingTest  {
        vm.email.value = "admin@alkemy"    //wrong email, does not have .com for example
        vm.checkContactFormViewModel()
        assertEquals(true, vm.isFirstNameValid)
        assertEquals(true, vm.isLastNameValid)
        assertEquals(true, vm.isContactMessageValid)

        assertEquals(false, vm.isEmailValid)
        assertEquals(false, vm.isButtonEneabled.value)
    }

}