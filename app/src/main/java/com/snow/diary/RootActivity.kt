package com.snow.diary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricManager.from
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.location.AllLocations
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.model.preferences.ColorMode
import com.snow.diary.ui.DiaryApplicationRoot
import com.snow.diary.ui.rememberDiaryState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.theme.color.OneUIColorTheme
import javax.inject.Inject


@AndroidEntryPoint
class RootActivity : FragmentActivity() {

    @Inject
    lateinit var getPreferences: GetPreferences

    @Inject
    lateinit var allPersons: AllPersons

    @Inject
    lateinit var allDreams: AllDreams

    @Inject
    lateinit var allLocations: AllLocations

    private val viewModel: RootActivityViewModel by viewModels()

    private lateinit var authInfo: PromptInfo

    private lateinit var authPrompt: BiometricPrompt

    private lateinit var authManager: BiometricManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        var rootState: RootState by mutableStateOf(RootState.Loading)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rootState
                    .onEach {
                        rootState = it
                    }
                    .collect()
            }
        }
        splash.setKeepOnScreenCondition { rootState == RootState.Loading }

        setupAuth()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            if (rootState == RootState.Loading) return@setContent
            val state = rootState as RootState.Success

            val didAuth by viewModel.didAuth.collectAsStateWithLifecycle()
            val needsAuth = state.preferences.requireAuth
            if (!needsAuth) viewModel.changeDidAuth(true)

            val canProceed = when (needsAuth) {
                true -> didAuth
                false -> true
            }

            val isDarkMode = isDarkMode(
                system = isSystemInDarkTheme(),
                pref = state.preferences.colorMode
            )

            val diaryState = rememberDiaryState(
                getPreferences = getPreferences,
                allPersons = allPersons,
                allDreams = allDreams,
                allLocations = allLocations
            )

            if (canProceed) {
                OneUITheme(
                    colorTheme = OneUIColorTheme.getTheme(dark = isDarkMode)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        DiaryApplicationRoot(diaryState)
                    }
                }
            } else {
                showAuth()
            }
        }
    }

    private fun setupAuth() {
        authInfo = PromptInfo.Builder()
            .setTitle(resources.getString(R.string.auth_title))
            .setDescription(resources.getString(R.string.auth_desc))
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        authPrompt = BiometricPrompt(
            this,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    viewModel.changeDidAuth(true)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    finishAndRemoveTask()
                }
            }
        )

        authManager = from(this)
    }

    private fun showAuth() {
        if (authManager
                .canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) ==
            BIOMETRIC_SUCCESS
        ) {
            authPrompt
                .authenticate(authInfo)
        }

    }

    private fun isDarkMode(
        system: Boolean,
        pref: ColorMode
    ): Boolean = when (pref) {
        ColorMode.Light -> false
        ColorMode.Dark -> true
        ColorMode.System -> system
    }
}