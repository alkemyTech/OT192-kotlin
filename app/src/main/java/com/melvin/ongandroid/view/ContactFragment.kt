package com.melvin.ongandroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.melvin.ongandroid.databinding.FragmentContactBinding
import com.melvin.ongandroid.model.Contact
import com.melvin.ongandroid.utils.Resource
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

        binding.progressLoader.root.visibility = View.GONE
        checkContactFormFragment()
        setupOnClickListener()
        showErrorMesage()

        /** Observe the state of button given [checkContactFormFragment]*/
        contactViewModel.isButtonEneabled.observe(viewLifecycleOwner){
            binding.filledButton.isEnabled = it
        }

        /* Observe the state of progress loader, if it's true, show the progress loader
        * If it's false, hide the progress loader, show dialog and clean contact fields
        * */
        contactViewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressLoader.root.visibility = if(it) View.VISIBLE else View.GONE
            if (it == false){
                showDialog()
                cleanContactForm()
            }
        }



        return binding.root
    }

    /**
     * Function that shows error textView if API call fails.
     */
    private fun showErrorMesage(){
        contactViewModel.contactResponseState.observe(viewLifecycleOwner){resource->
            when(resource){
                is Resource.ErrorApi -> binding.textViewError.visibility = View.VISIBLE
                else ->Unit
        }
        }
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
            binding.textViewError.visibility = View.GONE
            contactViewModel.checkContactFormViewModel()
        }

        lastNameEditText?.doAfterTextChanged {
            contactViewModel.lastName.value = it.toString()
            if (!it.toString().checkFirstOrLastName()){
                lastNameEditText.error = "El Apellido debe tener más de 3 letras"
            }
            binding.textViewError.visibility = View.GONE
            contactViewModel.checkContactFormViewModel()
        }

        emailEditText?.doAfterTextChanged {
            contactViewModel.email.value = it.toString()
            if (!it.toString().isEmailValid()){
                emailEditText.error = "E-mail incorrecto"
            }
            binding.textViewError.visibility = View.GONE
            contactViewModel.checkContactFormViewModel()
        }

        contactEditText?.doAfterTextChanged {
            contactViewModel.contactMessage.value = it.toString()
            if (!it.toString().checkContactMessage()){
                contactEditText.error = "La consulta debe tener al menos 30 carácteres."
            }
            binding.textViewError.visibility = View.GONE
            contactViewModel.checkContactFormViewModel()
        }

    }

    /*
    * Function that takes all the edit Text and send to [ContactViewModel] the information
    * when the user click on the button "Enviar".
     */
    private fun setupOnClickListener(){
        binding.filledButton.setOnClickListener {
            val firstName = binding.fragmentContactFirstname.editText?.text.toString()
            val lastName = binding.fragmentContactLastname.editText?.text.toString()
            val email = binding.fragmentContactEmail.editText?.text.toString()
            val message = binding.fragmentContactMessage.editText?.text.toString()
            Toast.makeText(context, "Enviando...", Toast.LENGTH_SHORT).show()
            contactViewModel.sendFormContact(
                Contact(0, "$firstName $lastName", email, "", message)
            )
        }
    }

    // Function that clean all the edit text
    private fun cleanContactForm(){
        with (binding){
            fragmentContactFirstname.editText?.text?.clear()
            fragmentContactLastname.editText?.text?.clear()
            fragmentContactEmail.editText?.text?.clear()
            fragmentContactMessage.editText?.text?.clear()
        }
    }

    // Function that show a simple dialog with a message of the successful process
    private fun showDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            dialog.setTitle("Enviado")
            dialog.setMessage("Tu mensaje ha sido enviado con éxito.")
            dialog.show()
    }

}