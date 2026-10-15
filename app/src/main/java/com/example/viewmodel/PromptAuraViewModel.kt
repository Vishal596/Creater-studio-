package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PromptRepository
import com.example.model.CharacterPersona
import com.example.model.GenerationParams
import com.example.model.InspirationBoard
import com.example.model.MediaCategory
import com.example.model.MediaItem
import com.example.model.NavigationTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreationUiState(
  val prompt: String = "",
  val negativePrompt: String = "blurry, deformed fingers, low resolution, watermark, bad anatomy",
  val aspectRatio: String = "1:1",
  val stylePreset: String = "Photorealism",
  val isGenerating: Boolean = false,
  val progress: Float = 0f,
  val stageMessage: String = "",
  val generatedResult: MediaItem? = null
)

class PromptAuraViewModel(
  private val repository: PromptRepository = PromptRepository()
) : ViewModel() {

  private val _currentTab = MutableStateFlow(NavigationTab.HOME)
  val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

  private val _selectedCategory = MutableStateFlow<MediaCategory?>(null)
  val selectedCategory: StateFlow<MediaCategory?> = _selectedCategory.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedItemForDetail = MutableStateFlow<MediaItem?>(null)
  val selectedItemForDetail: StateFlow<MediaItem?> = _selectedItemForDetail.asStateFlow()

  private val _creationState = MutableStateFlow(CreationUiState())
  val creationState: StateFlow<CreationUiState> = _creationState.asStateFlow()

  private val _userCredits = MutableStateFlow(28)
  val userCredits: StateFlow<Int> = _userCredits.asStateFlow()

  private val _toastEvent = MutableStateFlow<String?>(null)
  val toastEvent: StateFlow<String?> = _toastEvent.asStateFlow()

  val personas: StateFlow<List<CharacterPersona>> = repository.personas
  val boards: StateFlow<List<InspirationBoard>> = repository.boards
  val myCreations: StateFlow<List<MediaItem>> = repository.myCreations

  // Combined filtered feed
  val filteredItems: StateFlow<List<MediaItem>> = combine(
    repository.items,
    _selectedCategory,
    _searchQuery
  ) { items, category, query ->
    var result = items
    if (category != null) {
      result = result.filter { it.category == category }
    }
    if (query.isNotBlank()) {
      val trimmed = query.trim().lowercase()
      result = result.filter { item ->
        item.title.lowercase().contains(trimmed) ||
          item.promptText.lowercase().contains(trimmed) ||
          item.creatorHandle.lowercase().contains(trimmed) ||
          item.tags.any { it.lowercase().contains(trimmed) }
      }
    }
    result
  }.stateIn(viewModelScope, SharingStarted.Lazily, PromptRepository.initialMediaItems)

  val videoReels: StateFlow<List<MediaItem>> = repository.items.combine(_searchQuery) { items, _ ->
    items.filter { it.videoDuration != null }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  fun setTab(tab: NavigationTab) {
    _currentTab.value = tab
  }

  fun selectCategory(category: MediaCategory?) {
    _selectedCategory.value = category
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun openDetail(item: MediaItem) {
    _selectedItemForDetail.value = item
  }

  fun closeDetail() {
    _selectedItemForDetail.value = null
  }

  fun toggleLike(id: String) {
    repository.toggleLike(id)
  }

  fun toggleSave(id: String) {
    repository.toggleSave(id)
    showToast("Board collection updated! 🔖")
  }

  fun remixItem(item: MediaItem) {
    _creationState.update {
      it.copy(
        prompt = item.promptText,
        negativePrompt = item.negativePrompt,
        aspectRatio = item.params.aspectRatio,
        stylePreset = item.params.stylePreset,
        generatedResult = null
      )
    }
    _selectedItemForDetail.value = null
    _currentTab.value = NavigationTab.CREATE
    showToast("Prompt copied to Creation Studio! ✨")
  }

  fun updatePrompt(newPrompt: String) {
    _creationState.update { it.copy(prompt = newPrompt) }
  }

  fun updateNegativePrompt(newNeg: String) {
    _creationState.update { it.copy(negativePrompt = newNeg) }
  }

  fun updateAspectRatio(ratio: String) {
    _creationState.update { it.copy(aspectRatio = ratio) }
  }

  fun updateStylePreset(style: String) {
    _creationState.update { it.copy(stylePreset = style) }
  }

  fun appendPromptModifier(tag: String) {
    val current = _creationState.value.prompt
    val updated = if (current.isBlank()) tag else "$current, $tag"
    _creationState.update { it.copy(prompt = updated) }
  }

  fun insertPersonaTag(persona: CharacterPersona) {
    val current = _creationState.value.prompt
    val tag = persona.triggerTag
    val updated = if (current.isBlank()) tag else "$tag $current"
    _creationState.update { it.copy(prompt = updated) }
    showToast("Added ${persona.name} persona to prompt! 🎭")
  }

  fun surprisePrompt() {
    val randomPrompts = listOf(
      "A bioluminescent jellyfish floating through a vintage library corridor, soft amber study lamps, dust motes in light beams, octane 3d render",
      "Macro photography of a miniature glass terrarium containing a neon Tokyo crossroads, raindrops on crystal glass, 8k resolution",
      "Cyberpunk street vendor selling glowing ramen noodles in rain-slicked alleyway, anamorphic purple reflections, cinematic 35mm film",
      "Whimsical clay stop-motion kitten wearing a tiny astronaut backpack on the moon, soft studio rim light, Pixar inspired"
    )
    val chosen = randomPrompts.random()
    _creationState.update { it.copy(prompt = chosen) }
    showToast("🎲 Injected inspiration prompt!")
  }

  fun generateArt() {
    val current = _creationState.value
    if (current.prompt.isBlank()) {
      showToast("Please enter a prompt to generate art!")
      return
    }
    if (_userCredits.value < 2) {
      showToast("Not enough credits! Top up to continue creating.")
      return
    }

    viewModelScope.launch {
      _userCredits.update { (it - 2).coerceAtLeast(0) }
      _creationState.update {
        it.copy(
          isGenerating = true,
          progress = 0.1f,
          stageMessage = "Initializing neural latent space...",
          generatedResult = null
        )
      }

      delay(600)
      _creationState.update {
        it.copy(progress = 0.45f, stageMessage = "Denoising diffusion steps & lighting...")
      }

      delay(700)
      _creationState.update {
        it.copy(progress = 0.85f, stageMessage = "Upscaling 4K details & texture synthesis...")
      }

      delay(600)

      // Curated stunning high-res outcome image according to style preset
      val sampleImage = when (current.stylePreset) {
        "Anime" -> "https://images.unsplash.com/photo-1578632767115-351597cf2477?auto=format&fit=crop&w=800&q=80"
        "Clay 3D" -> "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=800&q=80"
        "Cyberpunk" -> "https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80"
        else -> "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80"
      }

      val newItem = MediaItem(
        id = "gen-${System.currentTimeMillis()}",
        title = "Original Creation #${(100..999).random()}",
        creatorHandle = "@you_creator",
        creatorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=120&q=80",
        imageUrl = sampleImage,
        promptText = current.prompt,
        negativePrompt = current.negativePrompt,
        tags = listOf("AI Art", current.stylePreset, "Original"),
        category = MediaCategory.TRENDING,
        aspectRatio = when (current.aspectRatio) {
          "9:16" -> 0.5625f
          "16:9" -> 1.77f
          "4:5" -> 0.8f
          else -> 1.0f
        },
        likesCount = 1,
        isLiked = true,
        params = GenerationParams(
          aspectRatio = current.aspectRatio,
          stylePreset = current.stylePreset,
          steps = 35
        )
      )

      repository.addCreation(newItem)

      _creationState.update {
        it.copy(
          isGenerating = false,
          progress = 1.0f,
          stageMessage = "Artwork Synthesized Successfully!",
          generatedResult = newItem
        )
      }
      showToast("✨ Artwork created! 2 Credits used.")
    }
  }

  fun topUpCredits() {
    _userCredits.update { it + 25 }
    showToast("🎉 Added +25 Creation Credits!")
  }

  fun createNewPersona(name: String, tagline: String, desc: String) {
    if (name.isBlank()) return
    val cleanTag = "{{" + name.replace(" ", "") + "}}"
    val persona = CharacterPersona(
      id = "persona-${System.currentTimeMillis()}",
      name = name,
      tagline = tagline.ifBlank { "Custom Character Persona" },
      avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80",
      description = desc,
      triggerTag = cleanTag,
      presetPrefix = "$name, $desc"
    )
    repository.addPersona(persona)
    showToast("New Persona $cleanTag created!")
  }

  fun createNewBoard(title: String, desc: String) {
    if (title.isBlank()) return
    val board = InspirationBoard(
      id = "board-${System.currentTimeMillis()}",
      title = title,
      description = desc.ifBlank { "Custom Moodboard" },
      coverUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=400&q=80",
      itemCount = 1
    )
    repository.addBoard(board)
    showToast("Inspiration Board '$title' created!")
  }

  fun showToast(msg: String) {
    _toastEvent.value = msg
  }

  fun clearToast() {
    _toastEvent.value = null
  }
}
