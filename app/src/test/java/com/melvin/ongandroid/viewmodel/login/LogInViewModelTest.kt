package com.melvin.ongandroid.viewmodel.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.melvin.ongandroid.MainDispatcherRule
import com.melvin.ongandroid.R
import com.melvin.ongandroid.core.ResourcesProvider
import com.melvin.ongandroid.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LogInViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var ruleInstantExecutor: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var resourcesProvider: ResourcesProvider

    private val viewModel: LogInViewModel  by lazy { LogInViewModel(resourcesProvider) }


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

    }

    @After
    fun tearDown() {
    }


    @Test
    fun `should pass with all fields correct`(){
        val email = "juan@gmail.com"
        val password = "Hola!123"

        viewModel.checkEmail(email)
        viewModel.checkPassword(password)

        assertEquals(null,viewModel.checkEmail(email) )
        assertEquals(null, viewModel.checkPassword(password))
    }

    @Test
    fun `should return Debe ccompletar el campo when all fields are empty`(){

        val email = ""
        val password = ""

        viewModel.checkEmail(email)
        viewModel.checkPassword(password)

        assertEquals(resourcesProvider.getString(R.string.validation_empty),viewModel.checkEmail(email) )
        assertEquals(resourcesProvider.getString(R.string.validation_empty), viewModel.checkPassword(password))


    }


    @Test
    fun `should return Debe ccompletar el campo when one field is empty`(){

        val email = "juan@gmail.com"
        val password = ""

        viewModel.checkEmail(email)
        viewModel.checkPassword(password)

        assertEquals(null,viewModel.checkEmail(email) )
        assertEquals(resourcesProvider.getString(R.string.validation_empty), viewModel.checkPassword(password))


    }





    @Test
    fun `should return Debe completar un email valido when email does not match regex pattern for email`(){
        var email = "holagmail.com"
        assertEquals(resourcesProvider.getString(R.string.validation_password)
            ,viewModel.checkEmail(email) )

        email = "@holagmail"
        assertEquals(resourcesProvider.getString(R.string.validation_password),
            viewModel.checkEmail(email) )

        email = "hola@gmail"
        assertEquals(resourcesProvider.getString(R.string.validation_password),
            viewModel.checkEmail(email) )

    }

    @Test
    fun `should return La contrasena es demasiado debil when password does not match regex pattern`(){
        var password = "123456"
        assertEquals(resourcesProvider.getString(R.string.validation_password),
            viewModel.checkPassword(password) )

        password = "hola123456"
        assertEquals(resourcesProvider.getString(R.string.validation_password),
            viewModel.checkPassword(password) )


        password = "Hola16"
        assertEquals(resourcesProvider.getString(R.string.validation_password),
            viewModel.checkPassword(password) )

        password = "Hola.12345"
        assertEquals(resourcesProvider.getString(R.string.validation_password),
            viewModel.checkPassword(password) )

        password = "HolaComoestas"
        assertEquals(resourcesProvider.getString(R.string.validation_password),
            viewModel.checkPassword(password) )

        password = "HolaComoEstas!"
        assertEquals(resourcesProvider.getString(R.string.validation_password),
            viewModel.checkPassword(password) )

    }

    @Test
    fun `button should be enabled when fields are correct`(){
        val email = "juan@gmail.com"
        val password = "Hola!123"

        val checkEmail = viewModel.checkEmail(email)
        val checkPassWord = viewModel.checkPassword(password)

        // set true only if the value of the functions is null
        when{
            checkEmail == null-> viewModel.isEmailValid = true
            checkPassWord == null-> viewModel.isPasswordValid = true
        }

        viewModel.checkFields()

        assertEquals(true, viewModel.isLogInBtnEnabled.getOrAwaitValue())
    }

}

