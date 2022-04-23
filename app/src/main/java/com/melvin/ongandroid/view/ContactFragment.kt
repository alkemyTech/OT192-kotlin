package com.melvin.ongandroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.melvin.ongandroid.databinding.FragmentContactBinding
import com.melvin.ongandroid.utils.checkContactMessage
import com.melvin.ongandroid.utils.checkFirstOrLastName
import com.melvin.ongandroid.utils.isEmailValid
import com.melvin.ongandroid.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentContactBinding
    private val contactViewModel: ContactViewModel by activityViewModels()

    // Initialization
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(layoutInflater, container, false)

        checkContactFormFragment()


        /** Observe the state of button given [checkContactFormFragment]*/
        contactViewModel.isButtonEneabled.observe(viewLifecycleOwner){
            binding.filledButton.isEnabled = it
        }

        return binding.root
    }

    /**
     * Function that takes all the edit Text and send to [ContactViewModel] the information.
     * There Performs checks of all parameters with [checkContactFormViewModel].
     * In Case that are invalid, shows Error in input Edit text.
     */
    private fun checkContactFormFragment(){
        val firstNameEditText = binding.fragmentContactFirstname.editText
        val lastNameEditText = binding.fragmentContactLastname.editText
        val emailEditText = binding.fragmentContactEmail.editText
        val contactEditText = binding.fragmentContactMessage.editText

        firstNameEditText?.doAfterTextChanged {
            contactViewModel.firstName.value = it.toString()
            if (!it.toString().checkFirstOrLastName()){
                firstNameEditText.error = "El nombre debe tener más de 3 letras"
            }
            contactViewModel.checkContactFormViewModel()
        }

        lastNameEditText?.doAfterTextChanged {
            contactViewModel.lastName.value = it.toString()
            if (!it.toString().checkFirstOrLastName()){
                lastNameEditText.error = "El Apellido debe tener más de 3 letras"
            }
            contactViewModel.checkContactFormViewModel()
        }

        emailEditText?.doAfterTextChanged {
            contactViewModel.email.value = it.toString()
            if (!it.toString().isEmailValid()){
                emailEditText.error = "E-mail incorrecto"
            }
            contactViewModel.checkContactFormViewModel()
        }

        contactEditText?.doAfterTextChanged {
            contactViewModel.contactMessage.value = it.toString()
            if (!it.toString().checkContactMessage()){
                contactEditText.error = "La consulta debe tener al menos 30 carácteres."
            }
            contactViewModel.checkContactFormViewModel()
        }

    }

}