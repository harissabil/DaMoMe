package com.harissabil.damome.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.harissabil.damome.core.camera.rememberCameraManager
import com.harissabil.damome.core.permission.PermissionCallback
import com.harissabil.damome.core.permission.PermissionStatus
import com.harissabil.damome.core.permission.PermissionType
import com.harissabil.damome.core.permission.createPermissionsManager
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.domain.model.Category
import com.harissabil.damome.ui.components.AddTransactionBottomSheet
import com.harissabil.damome.ui.components.DaMommyIcon
import com.harissabil.damome.ui.components.HomeFab
import com.harissabil.damome.ui.components.speeddial_by_leinardi.SpeedDialOverlay
import com.harissabil.damome.ui.components.speeddial_by_leinardi.SpeedDialState
import com.harissabil.damome.ui.navigation.components.CustomNavigationBar
import com.harissabil.damome.ui.screen.damommy.DamommyScreen
import com.harissabil.damome.ui.screen.damommy_chat.DamommyChatScreen
import com.harissabil.damome.ui.screen.home.HomeScreen
import com.harissabil.damome.ui.screen.home.HomeViewModel
import com.harissabil.damome.ui.screen.more.MoreScreen
import com.harissabil.damome.ui.screen.onboarding.OnboardingScreen
import com.harissabil.damome.ui.screen.records.RecordsScreen
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerDialog
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import top.yukonga.miuix.kmp.basic.MiuixFabPosition
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(
    KoinExperimentalAPI::class, ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    startDestination: Route,
    intentFilterByteArray: ByteArray? = null,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    val routes = listOf(
        Route.Onboarding,
        Route.Home,
        Route.Records,
        Route.DaMommy,
        Route.DaMommyChat(),
        Route.More,
    )
    val navigationBarItems = remember {
        listOf(
            NavigationItem(
                label = "Home",
                icon = Icons.Default.Home
            ),
            NavigationItem(
                label = "Records",
                icon = Icons.AutoMirrored.Filled.ReceiptLong
            ),
            NavigationItem(
                label = "DaMommy",
                icon = DaMommyIcon
            ),
            NavigationItem(
                label = "More",
                icon = Icons.Default.MoreHoriz
            )
        )
    }

    val navController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination
    var currentRoute by remember { mutableStateOf<Route?>(null) }
    routes.forEach { route ->
        if (currentDestination?.hierarchy?.any { it.hasRoute(route::class) } == true) {
            currentRoute = route
        }
    }
    val showBottomBar: (route: Route) -> Boolean = { route ->
        when (route) {
            Route.Onboarding -> false
            is Route.DaMommyChat -> false
            else -> true
        }
    }

    var selectedItem by rememberSaveable { mutableStateOf(0) }
    selectedItem = when (currentRoute) {
        Route.Home -> 0
        Route.Records -> 1
        Route.DaMommy -> 2
        Route.More -> 3
        else -> selectedItem
    }

    var speedDialState by rememberSaveable { mutableStateOf(SpeedDialState.Collapsed) }
    var overlayVisible: Boolean by rememberSaveable { mutableStateOf(speedDialState.isExpanded()) }

    val homeViewModel: HomeViewModel = koinViewModel()

    val scope = rememberCoroutineScope()

    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val transactionToSubmitState by homeViewModel.transactionToSubmitState.collectAsStateWithLifecycle()

    val addTransactionBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isAddTransactionBottomSheetVisible by rememberSaveable { mutableStateOf(false) }

    var isDateTimePickerExpanded by rememberSaveable { mutableStateOf(false) }

    // FileKit Compose
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Pick a image to extract text",
    ) { files ->
        if (files != null) {
            scope.launch {
                homeViewModel.onUploadedImageChanged(files.readBytes())
                isAddTransactionBottomSheetVisible = true
            }
        }
    }

    LaunchedEffect(key1 = intentFilterByteArray) {
        if (intentFilterByteArray != null) {
            homeViewModel.onUploadedImageChanged(intentFilterByteArray)
            isAddTransactionBottomSheetVisible = true
        }
    }

    var launchCamera by remember { mutableStateOf(value = false) }
    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus,
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> launchCamera = true
                        PermissionType.GALLERY -> {}
                    }
                }

                else -> {
                    // Handle permission denied
                }
            }
        }
    })

    val cameraManager = rememberCameraManager {
        scope.launch {
            val byteArray = withContext(Dispatchers.Default) {
                it?.toByteArray()
            }
            if (byteArray != null) {
                withContext(Dispatchers.Main) {
                    isAddTransactionBottomSheetVisible = true
                }
                homeViewModel.onUploadedImageChanged(byteArray)
            }
        }
    }

    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }

    LaunchedEffect(key1 = transactionToSubmitState.isFailed) {
        if (transactionToSubmitState.isFailed) {
            isAddTransactionBottomSheetVisible = false
            addTransactionBottomSheetState.hide()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar(currentRoute ?: startDestination),
                enter = slideInVertically(initialOffsetY = { it / 3 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it / 3 }) + fadeOut(),
            ) {
                CustomNavigationBar(
                    items = navigationBarItems,
                    selected = selectedItem,
                    skipColorFilterAtIndex = 2,
                    onClick = {
                        when (it) {
                            0 -> navController.navigateToTab(Route.Home)
                            1 -> navController.navigateToTab(Route.Records)
                            2 -> navController.navigateToTab(Route.DaMommy)
                            3 -> navController.navigateToTab(Route.More)
                        }
                    },
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedItem == 0 && showBottomBar(currentRoute ?: startDestination),
                enter = slideInVertically(initialOffsetY = { it / 4 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it / 4 }) + fadeOut(),
            ) {
                HomeFab(
                    modifier = Modifier.padding(MaterialTheme.spacing.small),
                    speedDialState = speedDialState,
                    onFabClick = { expanded ->
                        overlayVisible = !expanded
                        speedDialState = speedDialState.toggle()
                    },
                    onCameraClick = {
                        launchCamera = true
                        overlayVisible = false
                        speedDialState = speedDialState.toggle()
                    },
                    onGalleryClick = {
                        launcher.launch()
                        overlayVisible = false
                        speedDialState = speedDialState.toggle()
                    },
                    onTypeClick = {
                        overlayVisible = false
                        speedDialState = speedDialState.toggle()
                        isAddTransactionBottomSheetVisible = true
                    },
                )
            }
        },
        floatingActionButtonPosition = MiuixFabPosition.End,
    ) { innerPadding ->
        SharedTransitionLayout {
            NavHost(
                modifier = modifier.fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                navController = navController,
                startDestination = startDestination
            ) {
                composable<Route.Onboarding> {
                    OnboardingScreen(
                        modifier = modifier,
                        onNavigateToHome = {
                            navController.navigate(Route.Home) {
                                popUpTo(Route.Onboarding) { inclusive = true }
                            }
                        }
                    )
                }

                composable<Route.Home> {
                    HomeScreen(
                        viewModel = homeViewModel,
                    )
                }

                composable<Route.Records> {
                    RecordsScreen()
                }

                composable<Route.DaMommy> {
                    DamommyScreen(
                        animatedVisibilityScope = this,
                        onFabClick = { navController.navigate(Route.DaMommyChat()) },
                        onHistoryClick = { navController.navigate(Route.DaMommyChat(it)) }
                    )
                }

                composable<Route.DaMommyChat> {
                    val args = it.toRoute<Route.DaMommyChat>()
                    DamommyChatScreen(
                        animatedVisibilityScope = this,
                        chatGroupId = args.chatGroupId,
                        onNavigateUp = { navController.navigateUp() }
                    )
                }

                composable<Route.More> {
                    MoreScreen(
                        onBackupClick = onBackupClick,
                        onRestoreClick = onRestoreClick,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = selectedItem == 0,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            SpeedDialOverlay(
                visible = overlayVisible,
                onClick = {
                    overlayVisible = false
                    speedDialState = speedDialState.toggle()
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (isAddTransactionBottomSheetVisible) {
            AddTransactionBottomSheet(
                onDismissRequest = {
                    scope.launch { addTransactionBottomSheetState.hide() }.invokeOnCompletion {
                        if (!addTransactionBottomSheetState.isVisible) {
                            isAddTransactionBottomSheetVisible = false
                        }
                    }
                },
                sheetState = addTransactionBottomSheetState,
                currency = transactionToSubmitState.currency ?: homeState.currency,
                amount = transactionToSubmitState.amount ?: 0.0,
                scannedAmount = transactionToSubmitState.scannedAmount,
                onAmountChange = homeViewModel::onAmoutChanged,
                dateAndTime = transactionToSubmitState.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()),
                onDateAndTimeChange = homeViewModel::onDateAndTimeChanged,
                isDateTimePickerExpanded = isDateTimePickerExpanded,
                onDateTimePickerExpandedChange = { isDateTimePickerExpanded = it },
                category = transactionToSubmitState.category ?: Category.BILLS.value,
                onCategoryChange = homeViewModel::onCategoryChanged,
                description = transactionToSubmitState.description,
                onDescriptionChange = homeViewModel::onDescriptionChanged,
                transactionType = transactionToSubmitState.type,
                onTransactionTypeChange = homeViewModel::onTransactionTypeChanged,
                isLoading = transactionToSubmitState.isLoading,
                submitText = if (transactionToSubmitState.transactionToEdit != null) "Update" else "Save",
                onSubmitTransaction = {
                    homeViewModel.saveTransaction()
                    scope.launch { addTransactionBottomSheetState.hide() }.invokeOnCompletion {
                        if (!addTransactionBottomSheetState.isVisible) {
                            isAddTransactionBottomSheetVisible = false
                        }
                    }
                },
            )
        }

        WheelDateTimePickerDialog(
            height = 200.dp,
            onDismiss = { isDateTimePickerExpanded = false },
            startDate = transactionToSubmitState.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()),
            containerColor = MiuixTheme.colorScheme.surface,
            dateTextColor = MiuixTheme.colorScheme.onSurface,
            dateTextStyle = MiuixTheme.textStyles.title4,
            showDatePicker = isDateTimePickerExpanded,
            hideHeader = true,
            showMonthAsNumber = true,
            onDateChangeListener = homeViewModel::onDateAndTimeChanged,
        )
    }
}

private fun NavController.navigateToTab(route: Route) {
    this.navigate(route) {
        popUpTo(this@navigateToTab.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}