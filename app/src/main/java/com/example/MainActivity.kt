package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.NavigationTab
import com.example.ui.components.FloatingNavDock
import com.example.ui.components.InspectRecipeSheet
import com.example.ui.screens.CreateStudioScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ReelsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.VaultScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBg
import com.example.viewmodel.PromptAuraViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: PromptAuraViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        PromptAuraApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun PromptAuraApp(viewModel: PromptAuraViewModel) {
  val context = LocalContext.current
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle()
  val videoReels by viewModel.videoReels.collectAsStateWithLifecycle()
  val selectedItemForDetail by viewModel.selectedItemForDetail.collectAsStateWithLifecycle()
  val creationState by viewModel.creationState.collectAsStateWithLifecycle()
  val userCredits by viewModel.userCredits.collectAsStateWithLifecycle()
  val personas by viewModel.personas.collectAsStateWithLifecycle()
  val boards by viewModel.boards.collectAsStateWithLifecycle()
  val myCreations by viewModel.myCreations.collectAsStateWithLifecycle()
  val toastEvent by viewModel.toastEvent.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  // Handle Toast feedback
  LaunchedEffect(toastEvent) {
    toastEvent?.let { msg ->
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
      viewModel.clearToast()
    }
  }

  // Back handling
  BackHandler(enabled = selectedItemForDetail != null || currentTab != NavigationTab.HOME) {
    if (selectedItemForDetail != null) {
      viewModel.closeDetail()
    } else {
      viewModel.setTab(NavigationTab.HOME)
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = ObsidianBg,
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(ObsidianBg)
    ) {
      // Main Content Screen switcher
      when (currentTab) {
        NavigationTab.HOME -> {
          HomeScreen(
            items = filteredItems,
            selectedCategory = selectedCategory,
            userCredits = userCredits,
            onSelectCategory = viewModel::selectCategory,
            onOpenDetail = viewModel::openDetail,
            onToggleLike = viewModel::toggleLike,
            onTopUpCredits = viewModel::topUpCredits
          )
        }

        NavigationTab.REELS -> {
          ReelsScreen(
            reels = videoReels,
            onUseIdea = viewModel::remixItem,
            onToggleLike = viewModel::toggleLike,
            onToast = viewModel::showToast
          )
        }

        NavigationTab.SEARCH -> {
          SearchScreen(
            searchQuery = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            filteredItems = filteredItems,
            allItems = filteredItems,
            onOpenDetail = viewModel::openDetail,
            onCategorySelected = { category ->
              viewModel.selectCategory(category)
              viewModel.setTab(NavigationTab.HOME)
            },
            onToast = viewModel::showToast
          )
        }

        NavigationTab.CREATE -> {
          CreateStudioScreen(
            state = creationState,
            userCredits = userCredits,
            personas = personas,
            onPromptChange = viewModel::updatePrompt,
            onNegativePromptChange = viewModel::updateNegativePrompt,
            onAspectRatioChange = viewModel::updateAspectRatio,
            onStylePresetChange = viewModel::updateStylePreset,
            onAppendModifier = viewModel::appendPromptModifier,
            onInsertPersona = viewModel::insertPersonaTag,
            onSurprisePrompt = viewModel::surprisePrompt,
            onGenerateArt = viewModel::generateArt,
            onTopUpCredits = viewModel::topUpCredits,
            onSaveToBoard = viewModel::toggleSave,
            onToast = viewModel::showToast
          )
        }

        NavigationTab.VAULT -> {
          VaultScreen(
            boards = boards,
            personas = personas,
            myCreations = myCreations,
            userCredits = userCredits,
            onCreatePersona = viewModel::createNewPersona,
            onCreateBoard = viewModel::createNewBoard,
            onTopUpCredits = viewModel::topUpCredits,
            onOpenDetail = viewModel::openDetail
          )
        }
      }

      // Floating Island Navigation Dock
      FloatingNavDock(
        currentTab = currentTab,
        onTabSelected = viewModel::setTab,
        modifier = Modifier.align(Alignment.BottomCenter)
      )

      // Inspect Recipe & Remix Modal Bottom Sheet
      selectedItemForDetail?.let { item ->
        InspectRecipeSheet(
          item = item,
          onDismiss = viewModel::closeDetail,
          onRemix = viewModel::remixItem,
          onLikeToggle = viewModel::toggleLike,
          onSaveToggle = viewModel::toggleSave,
          onToast = viewModel::showToast
        )
      }
    }
  }
}
