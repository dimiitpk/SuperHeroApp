package com.dimi.superheroapp.presentation.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.MessageType
import com.dimi.superheroapp.business.domain.state.Response
import com.dimi.superheroapp.business.domain.state.StateMessageCallback
import com.dimi.superheroapp.business.domain.state.UIComponentType
import com.dimi.superheroapp.util.PreferenceKeys
import com.dimi.superheroapp.presentation.common.gone
import com.dimi.superheroapp.presentation.common.visible
import com.dimi.superheroapp.presentation.main.viewmodel.getClickedSuperHero
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.StringBuilder
import javax.inject.Inject


@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    UIController {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface MainActivityEntryPoint {

        fun getFragmentManager(): FragmentManager
        fun getFragmentFactory(): MainFragmentFactory
    }

    private var dialogInView: MaterialDialog? = null

    @Inject
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {

        val themeMode = applicationContext.getSharedPreferences(
            PreferenceKeys.APP_PREFERENCE,
            Context.MODE_PRIVATE
        ).getInt(PreferenceKeys.THEME_MODE, -1)
        if (themeMode != -1)
            AppCompatDelegate.setDefaultNightMode(themeMode)

        if (isDeviceTablet()) {
            val entryPoint =
                EntryPointAccessors.fromActivity(this, MainActivityEntryPoint::class.java)

            val fragmentManager = entryPoint.getFragmentManager()
            fragmentManager.fragmentFactory = entryPoint.getFragmentFactory()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isDeviceTablet()) {
            clearFragmentsBackStack()
        }
        super.onSaveInstanceState(outState)
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        if (isDisplayed)
            progress_bar.visible()
        else
            progress_bar.gone()
    }

    override fun expandAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    override fun changeThemeMode() {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            saveThemeMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            saveThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun saveThemeMode(themeMode: Int) {
        editor.putInt(PreferenceKeys.THEME_MODE, themeMode)
        editor.apply()
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        when (response.uiComponentType) {

            is UIComponentType.Toast -> {
                response.message?.let {
                    displayToast(
                        message = it,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is UIComponentType.Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.None -> {
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }

    override fun sendSuperHeroAsMessage(superHero: SuperHero) {
        val sb = StringBuilder()
        superHero.let {
            sb.append("Name: ${it.name}")
            sb.append("\n")
            sb.append("Full name: ${superHero.fullName}")
            sb.append("\n")
            sb.append("Place of birth: ${it.getValidPlaceOfBirth()}")
            sb.append("\n")
            sb.append("Publisher: ${it.getValidPublisher()}")
        }

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sb.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


    private fun displayToast(
        message: String,
        stateMessageCallback: StateMessageCallback
    ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        stateMessageCallback.removeMessageFromStack()
    }

    private fun displayDialog(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        response.message?.let { message ->

            dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    displayErrorDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Success -> {
                    displaySuccessDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Info -> {
                    displayInfoDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        } ?: stateMessageCallback.removeMessageFromStack()
    }

    override fun onPause() {
        super.onPause()
        if (dialogInView != null) {
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }

    private fun displaySuccessDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_success)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayErrorDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_error)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayInfoDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_info)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun clearFragmentsBackStack() {
        if (supportFragmentManager.fragments.size > 0) {
            for (fragment in supportFragmentManager.fragments) {
                fragment?.let {
                    supportFragmentManager.beginTransaction().remove(fragment)
                        .commitAllowingStateLoss()
                }
            }
        }
    }

    override fun isDeviceTabletInLandscapeMode(): Boolean {
        return (resources.getBoolean(R.bool.is_tablet) && resources.getBoolean(R.bool.is_landscape))
    }

    private fun isDeviceTablet(): Boolean {
        return (resources.getBoolean(R.bool.is_tablet))
    }

    private fun setupActionBar() {
        setSupportActionBar(tool_bar)
    }
}