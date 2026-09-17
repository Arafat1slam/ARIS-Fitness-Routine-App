package com.example.arisfitness

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.arisfitness.data.model.UserProfile
import com.example.arisfitness.engine.DateResolutionEngine
import com.example.arisfitness.notification.AlarmScheduler
import com.example.arisfitness.theme.DarkBackground
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.ui.character.CharacterSelectionScreen
import com.example.arisfitness.ui.home.HomeScreen
import com.example.arisfitness.ui.onboarding.OnboardingScreen
import com.example.arisfitness.ui.progress.ProgressScreen
import com.example.arisfitness.ui.routine.RoutineDetailScreen
import com.example.arisfitness.ui.settings.SettingsScreen

@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val repository = ArisApplication.instance.repository

    val userProfile by repository.userProfile.collectAsStateWithLifecycle()
    val todayLog by repository.todayLog.collectAsStateWithLifecycle()

    // Request notification permission on Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { _ -> }
    )
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Temporary storage during onboarding flow before committing
    var tempHeight by remember { mutableStateOf(175f) }
    var tempWeight by remember { mutableStateOf(70f) }
    var tempAge by remember { mutableStateOf(24) }
    var tempGender by remember { mutableStateOf("male") }
    var tempActivity by remember { mutableStateOf("moderate") }
    var tempBmr by remember { mutableStateOf(1680f) }
    var tempTdee by remember { mutableStateOf(2350f) }

    val initialDestination = if (userProfile != null && userProfile!!.isConfigured) {
        HomeNavKey
    } else {
        OnboardingNavKey
    }

    val backStack = rememberNavBackStack(initialDestination)

    // Schedule alarms whenever userProfile changes
    LaunchedEffect(userProfile) {
        val profile = userProfile
        if (profile != null && profile.isConfigured) {
            val routine = repository.getDailyRoutine(
                profile.selectedCharacterId,
                profile.currentDayIndex,
                profile.tdee
            )
            AlarmScheduler.scheduleRoutineAlarms(context, routine)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            // 1. ONBOARDING
            entry<OnboardingNavKey> {
                OnboardingScreen(
                    initialHeight = userProfile?.heightCm ?: 175f,
                    initialWeight = userProfile?.weightKg ?: 70f,
                    initialAge = userProfile?.age ?: 24,
                    initialGender = userProfile?.gender ?: "male",
                    initialActivity = userProfile?.activityLevel ?: "moderate",
                    onCompleteOnboarding = { h, w, a, g, act, bmr, tdee ->
                        tempHeight = h
                        tempWeight = w
                        tempAge = a
                        tempGender = g
                        tempActivity = act
                        tempBmr = bmr
                        tempTdee = tdee
                        backStack.add(CharacterSelectionNavKey)
                    }
                )
            }

            // 2. CHARACTER SELECTION
            entry<CharacterSelectionNavKey> {
                CharacterSelectionScreen(
                    userTdee = tempTdee,
                    currentSelectedId = userProfile?.selectedCharacterId ?: "char_titan",
                    onCharacterSelected = { character ->
                        val today = DateResolutionEngine.getTodayString()
                        val newProfile = (userProfile ?: UserProfile()).copy(
                            heightCm = tempHeight,
                            weightKg = tempWeight,
                            age = tempAge,
                            gender = tempGender,
                            activityLevel = tempActivity,
                            bmr = tempBmr,
                            tdee = tempTdee,
                            selectedCharacterId = character.characterId,
                            startDate = today,
                            currentDayIndex = 1
                        )
                        repository.saveUserProfile(newProfile)

                        // Clear backstack and go to Home
                        while (backStack.size > 0) {
                            backStack.removeLastOrNull()
                        }
                        backStack.add(HomeNavKey)
                    }
                )
            }

            // 3. HOME SCREEN
            entry<HomeNavKey> {
                val profile = userProfile
                if (profile == null || !profile.isConfigured) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(DarkBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonMint)
                    }
                } else {
                    val routine = repository.getDailyRoutine(
                        profile.selectedCharacterId,
                        profile.currentDayIndex,
                        profile.tdee
                    )

                    val missedStatus = DateResolutionEngine.checkMissedDays(
                        startDateStr = profile.startDate,
                        currentDayIndex = profile.currentDayIndex,
                        pausedDays = profile.pausedDays,
                        completedDayIndices = emptySet()
                    )

                    HomeScreen(
                        userProfile = profile,
                        routine = routine,
                        todayLog = todayLog,
                        missedDayStatus = missedStatus,
                        onToggleItem = { itemId ->
                            repository.toggleChecklistItem(itemId, profile.selectedCharacterId, profile.currentDayIndex)
                        },
                        onUpdateCalories = { calories ->
                            repository.updateCaloriesConsumed(calories, profile.selectedCharacterId, profile.currentDayIndex)
                        },
                        onHandleMissedDays = { resumeFromSaved ->
                            repository.handleMissedDays(
                                resumeFromSaved = resumeFromSaved,
                                missedDates = missedStatus.missedDates,
                                expectedCalendarDay = missedStatus.expectedCalendarDayIndex
                            )
                        },
                        onNavigateRoutineDetail = { backStack.add(RoutineDetailNavKey) },
                        onNavigateProgress = { backStack.add(ProgressNavKey) },
                        onNavigateSettings = { backStack.add(SettingsNavKey) }
                    )
                }
            }

            // 4. ROUTINE DETAIL
            entry<RoutineDetailNavKey> {
                val profile = userProfile
                if (profile != null) {
                    RoutineDetailScreen(
                        initialDayIndex = profile.currentDayIndex,
                        characterId = profile.selectedCharacterId,
                        userTdee = profile.tdee,
                        onLoadDayRoutine = { dayIdx ->
                            repository.getDailyRoutine(profile.selectedCharacterId, dayIdx, profile.tdee)
                        },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }

            // 5. PROGRESS SCREEN
            entry<ProgressNavKey> {
                val profile = userProfile
                if (profile != null) {
                    ProgressScreen(
                        userProfile = profile,
                        completedDaysCount = repository.getTotalCompletedDaysCount(),
                        onLogWeight = { w -> repository.logWeight(w) },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }

            // 6. SETTINGS SCREEN
            entry<SettingsNavKey> {
                val profile = userProfile
                if (profile != null) {
                    SettingsScreen(
                        userProfile = profile,
                        onNavigateChangeCharacter = {
                            tempTdee = profile.tdee
                            backStack.add(CharacterSelectionNavKey)
                        },
                        onNavigateEditBiometrics = {
                            backStack.add(OnboardingNavKey)
                        },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        }
    )
}
