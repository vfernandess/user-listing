package com.voidx.userlisting.feature.profile.view

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.voidx.userlisting.R
import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.databinding.FragmentUserProfileBinding
import com.voidx.userlisting.feature.profile.presentation.UserProfileState
import com.voidx.userlisting.feature.profile.presentation.UserProfileViewModel
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

const val EXTRA_USER = "extra_user"
const val REQUEST_KEY_CREATE_PROFILE = "create-user-profile"
const val REQUEST_KEY_EDIT_PROFILE = "edit-user-profile"

class UserProfileFragment : BottomSheetDialogFragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()

    private val viewModel: UserProfileViewModel by viewModel()

    private var binding: FragmentUserProfileBinding? = null

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private var requestPickFromGalley: ActivityResultLauncher<Intent>? = null

    override fun onAttach(context: Context) {
        checkPermissions(false)
        registerGalleryForPick()
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val height = Resources.getSystem().displayMetrics.heightPixels * 0.85

            bottomSheetDialog.behavior.skipCollapsed = true
            bottomSheetDialog.behavior.peekHeight = height.toInt()
            bottomSheetDialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        val height = Resources.getSystem().displayMetrics.heightPixels * 0.85

        binding?.root?.layoutParams?.height = height.toInt()
        binding?.root?.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.save?.setOnClickListener {
            viewModel.save(
                name = binding?.name?.editText?.text.toString(),
                age = binding?.age?.editText?.text.toString(),
            )
        }

        binding?.avatar?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                pickImageFromGallery()
            } else {
                checkPermissions(true)
            }
        }

        viewModel.state().observe(this) {

            when (it) {
                UserProfileState.InvalidAgeError ->
                    showUserAgeError()

                UserProfileState.InvalidNameError ->
                    showUserNameError()

                UserProfileState.UnknownError ->
                    showUnknownError()

                is UserProfileState.Saved ->
                    deliverUserResult(REQUEST_KEY_CREATE_PROFILE, it.user)

                is UserProfileState.Edited ->
                    deliverUserResult(REQUEST_KEY_EDIT_PROFILE, it.user)

                is UserProfileState.UserLoaded ->
                    loadUserAttributes(it.user)
            }
        }

        viewModel.load(arguments?.getParcelable(EXTRA_USER))
    }

    private fun checkPermissions(pickImage: Boolean) {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted && pickImage) {
                    pickImageFromGallery()
                }
            }
        requestPermissionLauncher?.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun registerGalleryForPick() {
        requestPickFromGalley =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                binding?.let { binding ->
                    viewModel.setImage(it.data?.data?.toString())
                    Glide
                        .with(this)
                        .load(it.data?.data)
                        .placeholder(R.drawable.ic_account_circle)
                        .error(R.drawable.ic_account_circle)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.avatar)
                }
            }
    }

    private fun pickImageFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        requestPickFromGalley?.launch(galleryIntent)
    }

    private fun loadUserAttributes(user: User) {
        binding?.name?.editText?.setText(user.name)
        binding?.age?.editText?.setText(user.age)

        binding?.let {

            val uri = if (user.avatar.isNullOrBlank())
                Uri.EMPTY
            else
                Uri.parse(user.avatar)

            Glide
                .with(this)
                .load(uri)
                .placeholder(R.drawable.ic_account_circle)
                .error(R.drawable.ic_account_circle)
                .apply(RequestOptions.circleCropTransform())
                .into(it.avatar)
        }
    }

    private fun deliverUserResult(kindOfResult: String, user: User) {
        setFragmentResult(kindOfResult, Bundle().apply { putParcelable(EXTRA_USER, user) })
        dismiss()
    }

    private fun showUnknownError() {
        binding?.let {
            Snackbar
                .make(it.save, R.string.general_error, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun showUserNameError() {
        binding?.name?.error = getString(R.string.invalid_name)
    }

    private fun showUserAgeError() {
        binding?.age?.error = getString(R.string.invalid_age)
    }

    companion object {

        fun show(manager: FragmentManager, user: User? = null): UserProfileFragment {
            val fragment = UserProfileFragment()
            fragment.arguments = Bundle().apply {
                user?.let { putParcelable(EXTRA_USER, it) }
            }
            fragment.show(manager, "UPF")

            return fragment
        }
    }
}