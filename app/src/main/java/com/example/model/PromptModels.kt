package com.example.model

enum class NavigationTab(val label: String) {
  HOME("Explore"),
  REELS("Reels"),
  SEARCH("Search"),
  CREATE("Create"),
  VAULT("Vault")
}

enum class MediaCategory(val title: String) {
  TRENDING("Trending"),
  ADS_PRODUCT("Ads & Product"),
  BRAND_LOGO("Brand & Logo"),
  ILLUSTRATION_3D("Illustration & 3D"),
  POSTERS_VISUALS("Posters & Visuals"),
  PORTRAITS("Portraits"),
  FASHION("Editorial Fashion"),
  ARCHITECTURE("Architecture")
}

data class GenerationParams(
  val aspectRatio: String = "1:1",
  val steps: Int = 30,
  val guidanceScale: Float = 7.5f,
  val seed: Long = 42893821L,
  val stylePreset: String = "Photorealistic",
  val modelUsed: String = "Lumina Ultra v3"
)

data class MediaItem(
  val id: String,
  val title: String,
  val creatorHandle: String,
  val creatorAvatar: String,
  val imageUrl: String,
  val videoDuration: String? = null, // e.g. "15s", null means static image
  val promptText: String,
  val negativePrompt: String = "blurry, distorted anatomy, bad hands, lowres, watermark",
  val tags: List<String> = emptyList(),
  val category: MediaCategory,
  val aspectRatio: Float = 1.0f, // width / height
  val likesCount: Int = 0,
  val isLiked: Boolean = false,
  val isSaved: Boolean = false,
  val params: GenerationParams = GenerationParams()
)

data class CharacterPersona(
  val id: String,
  val name: String,
  val tagline: String,
  val avatarUrl: String,
  val description: String,
  val triggerTag: String,
  val presetPrefix: String,
  val samplesCount: Int = 12
)

data class InspirationBoard(
  val id: String,
  val title: String,
  val description: String,
  val coverUrl: String,
  val itemCount: Int = 0,
  val isPrivate: Boolean = false
)

data class CreationResult(
  val id: String,
  val imageUrl: String,
  val prompt: String,
  val style: String,
  val aspectRatio: String,
  val timestamp: String = "Just now"
)
