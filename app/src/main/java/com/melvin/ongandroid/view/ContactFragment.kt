package com.melvin.ongandroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.melvin.ongandroid.R
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

        // Change toolbar title
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.contacto)

        binding.progressLoader.root.visibility = View.GONE
        checkContactFormFragment()
        setupOnClickListener()
        setObservers()

        return binding.root
    }


    private fun setObservers(){
        /** Observe the state of button given [checkContactFormFragment]*/
        contactViewModel.isButtonEneabled.observe(viewLifecycleOwner){
            binding.filledButton.isEnabled = it
        }

        // Observe the state of the contact response
        contactViewModel.contactResponseState.observe(viewLifecycleOwner){resource->
            when(resource){
                is Resource.Success -> {
                    // Hide Progress bar
                    enableUI(true)
                    // Show dialog
                    showDialog()

                    // Reset error after being displayed
                    contactViewModel.setIdle()
                }
                is Resource.Loading -> {
                    // Show Progress bar
                    enableUI(false)
                }
                is Resource.ErrorThrowable -> {
                    // Hide Progress bar
                    enableUI(true)

                    binding.textViewError.visibility = View.VISIBLE

                    // Reset error after being displayed
                    contactViewModel.setIdle()

                }
                is Resource.ErrorApi -> {
                    // Hide Progress bar
                    enableUI(true)

                    binding.textViewError.visibility = View.VISIBLE

                    // Reset error after being displayed
                    contactViewModel.setIdle()
                }
                is Resource.Idle -> {
                    // Hide Progress bar
                    enableUI(true)
                }
        }
        }
    }

    // Enable UI when loading data is finished
    private fun enableUI(enable: Boolean) {
        binding.progressLoader.root.visibility = if (enable) View.GONE else View.VISIBLE
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
            // Show Progress bar
            enableUI(false)
        }
    }

    // Function that show a simple dialog with a message of the successful process
    private fun showDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            dialog.setTitle("Enviado")
            dialog.setMessage("Tu mensaje ha sido enviado con éxito.")
            dialog.setOnCancelListener {

                // remove the fragment and replace with the new ContactFragment
                parentFragmentManager.beginTransaction().remove(this).replace(R.id.fragmentContainerView, ContactFragment()).commit()
                contactViewModel.resetContactForm()
            }
            dialog.show()
    }

}