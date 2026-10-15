package com.example.data

import com.example.model.CharacterPersona
import com.example.model.GenerationParams
import com.example.model.InspirationBoard
import com.example.model.MediaCategory
import com.example.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PromptRepository {

  private val _items = MutableStateFlow<List<MediaItem>>(initialMediaItems)
  val items: StateFlow<List<MediaItem>> = _items.asStateFlow()

  private val _personas = MutableStateFlow<List<CharacterPersona>>(initialPersonas)
  val personas: StateFlow<List<CharacterPersona>> = _personas.asStateFlow()

  private val _boards = MutableStateFlow<List<InspirationBoard>>(initialBoards)
  val boards: StateFlow<List<InspirationBoard>> = _boards.asStateFlow()

  private val _myCreations = MutableStateFlow<List<MediaItem>>(emptyList())
  val myCreations: StateFlow<List<MediaItem>> = _myCreations.asStateFlow()

  fun toggleLike(id: String) {
    _items.update { list ->
      list.map { item ->
        if (item.id == id) {
          val newLiked = !item.isLiked
          item.copy(
            isLiked = newLiked,
            likesCount = if (newLiked) item.likesCount + 1 else (item.likesCount - 1).coerceAtLeast(0)
          )
        } else item
      }
    }
  }

  fun toggleSave(id: String) {
    _items.update { list ->
      list.map { item ->
        if (item.id == id) {
          item.copy(isSaved = !item.isSaved)
        } else item
      }
    }
  }

  fun addCreation(item: MediaItem) {
    _myCreations.update { listOf(item) + it }
    _items.update { listOf(item) + it }
  }

  fun addPersona(persona: CharacterPersona) {
    _personas.update { listOf(persona) + it }
  }

  fun addBoard(board: InspirationBoard) {
    _boards.update { listOf(board) + it }
  }

  companion object {
    val initialMediaItems = listOf(
      MediaItem(
        id = "reel-1",
        title = "AR Street Marker Magic",
        creatorHandle = "@luffy_creator",
        creatorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1519501025264-65ba15a82390?auto=format&fit=crop&w=800&q=80",
        videoDuration = "15s",
        promptText = "First-person POV hand holding black marker drawing animated glowing blue line-art subway train running across concrete highway overpass in modern Tokyo street, 2D doodle animated sticker hybrid, afternoon golden hour lighting, 8k resolution cinematic vlog style",
        tags = listOf("AR Art", "Animation", "Urban Vlog", "Street Magic"),
        category = MediaCategory.TRENDING,
        aspectRatio = 0.5625f, // 9:16 vertical
        likesCount = 428,
        isLiked = true,
        params = GenerationParams(aspectRatio = "9:16", steps = 35, stylePreset = "Mixed Reality")
      ),
      MediaItem(
        id = "img-2",
        title = "Palawan Portal View",
        creatorHandle = "@wanderlust_ai",
        creatorAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
        promptText = "Macro photography of hand holding bezel-less glass smartphone displaying tropical lagoon portal with lush limestone karst cliffs, exotic Rafflesia flowers, floating wooden outrigger boat in turquoise water, typography PALAWAN PHILIPPINES travel magazine cover style, volumetric sunrise rays",
        tags = listOf("Travel", "Magazine Cover", "Surrealism", "Tropical"),
        category = MediaCategory.POSTERS_VISUALS,
        aspectRatio = 0.7f,
        likesCount = 1290,
        params = GenerationParams(aspectRatio = "4:5", steps = 40, stylePreset = "Editorial Photo")
      ),
      MediaItem(
        id = "img-3",
        title = "Echo Minimalist Monolith",
        creatorHandle = "@studio_echo",
        creatorAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80",
        promptText = "Minimalist brutalist obsidian monolith fragment disintegrating into stardust particles, geometric golden ratio wireframe overlays, monochrome deep black aesthetic, editorial typographic layout titled ECHO RESONANCE IN EVERY FRACTURE, architectural lighting, Swiss design poster",
        tags = listOf("Brutalism", "Graphic Design", "Monochrome", "Typography"),
        category = MediaCategory.BRAND_LOGO,
        aspectRatio = 0.65f,
        likesCount = 852,
        params = GenerationParams(aspectRatio = "4:5", steps = 28, stylePreset = "Swiss Minimalist")
      ),
      MediaItem(
        id = "img-4",
        title = "Monocle Acoustic Sanctuary",
        creatorHandle = "@korea_spaces",
        creatorAvatar = "https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=800&q=80",
        promptText = "Editorial lifestyle portrait of serene woman in cozy cream knitted loungewear relaxing next to sleek minimal spherical speaker, warm mid-century modern wooden interior, soft amber diffused evening lamps, MONOCLE THE SOUND OF HOME magazine edition typography",
        tags = listOf("Interior", "Lifestyle", "Warm Minimal", "Editorial"),
        category = MediaCategory.PORTRAITS,
        aspectRatio = 0.68f,
        likesCount = 943,
        params = GenerationParams(aspectRatio = "4:5", steps = 32, stylePreset = "Warm Editorial")
      ),
      MediaItem(
        id = "img-5",
        title = "Pastel Hillside Villa",
        creatorHandle = "@wes_vibes",
        creatorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80",
        promptText = "Symmetrical Wes Anderson cinematic composition, candy pink grand villa manor nestled on rolling vibrant green Scottish grassy hill, vintage turquoise sedan parked outside, cloudy overcast sky with soft pastel color grading, 35mm film grain aesthetic",
        tags = listOf("Cinematic", "Pastel", "Architecture", "Symmetry"),
        category = MediaCategory.ARCHITECTURE,
        aspectRatio = 1.33f, // landscape
        likesCount = 612,
        params = GenerationParams(aspectRatio = "16:9", steps = 30, stylePreset = "Film Grain")
      ),
      MediaItem(
        id = "img-6",
        title = "Cold Caramel Iced Coffee Ad",
        creatorHandle = "@brew_masters",
        creatorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1517256064527-09c73fc73e38?auto=format&fit=crop&w=800&q=80",
        promptText = "Commercial food ad photography of tall crystal glass filled with layered iced caramel latte, whipped cream swirl topping with drizzled rich toffee sauce, condensation water droplets on glass, retro sticker badges 'Fresh Brew Limited Time Cold & Refreshing', vibrant studio lighting",
        tags = listOf("Commercial", "Product Photography", "Food Ad", "Beverage"),
        category = MediaCategory.ADS_PRODUCT,
        aspectRatio = 0.85f,
        likesCount = 1430,
        isSaved = true,
        params = GenerationParams(aspectRatio = "4:5", steps = 35, stylePreset = "Commercial Studio")
      ),
      MediaItem(
        id = "reel-2",
        title = "Cybernetic Samurai Night",
        creatorHandle = "@neo_tokyo_art",
        creatorAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?auto=format&fit=crop&w=800&q=80",
        videoDuration = "10s",
        promptText = "High-octane camera fly-through in rain-drenched cyberpunk alleyway, futuristic ronin in illuminated carbon-fiber trench coat holding glowing holographic katana, neon reflections in asphalt puddles, anamorphic lens flares, unreal engine 5 render, 9:16 vertical video",
        tags = listOf("Cyberpunk", "Animation", "Sci-Fi", "Action Reel"),
        category = MediaCategory.TRENDING,
        aspectRatio = 0.5625f,
        likesCount = 2104,
        params = GenerationParams(aspectRatio = "9:16", steps = 40, stylePreset = "Unreal 5 Sci-Fi")
      ),
      MediaItem(
        id = "img-7",
        title = "Little Astronaut's Dream",
        creatorHandle = "@clay_universe",
        creatorAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=800&q=80",
        promptText = "Pixar animation style 3D character render of young boy astronaut in puffy spacesuit sitting on asteroid, holding glowing paper constellation map, floating purple nebulae and friendly miniature aliens around him, soft volumetric rim lighting, Octane render 8k",
        tags = listOf("3D Character", "Pixar Style", "Whimsical", "Space"),
        category = MediaCategory.ILLUSTRATION_3D,
        aspectRatio = 0.8f,
        likesCount = 1870,
        params = GenerationParams(aspectRatio = "1:1", steps = 30, stylePreset = "3D Animated")
      ),
      MediaItem(
        id = "img-8",
        title = "Astronomical Brass Telescope",
        creatorHandle = "@vintage_archive",
        creatorAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=800&q=80",
        promptText = "Antique brass scientific telescope on polished tripod surrounded by parchment celestial star maps, planetary orbits diagrams, dark mahogany observatory study, dramatic Rembrandt chiaroscuro lighting, Da Vinci schematic engraving poster",
        tags = listOf("Vintage", "Schematics", "Astronomy", "Historical"),
        category = MediaCategory.POSTERS_VISUALS,
        aspectRatio = 0.75f,
        likesCount = 590,
        params = GenerationParams(aspectRatio = "4:5", steps = 32, stylePreset = "Engraving")
      ),
      MediaItem(
        id = "img-9",
        title = "Break Ice Arena Mascot",
        creatorHandle = "@cartoon_pulse",
        creatorAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=800&q=80",
        promptText = "Furious comic book style hockey player purple creature swinging hockey stick with ice shards shattering towards camera, bold BREAK ICE typography, comic halftone dot textures, high-energy sports action poster, Marvel comic coloring",
        tags = listOf("Comic Art", "Sports Mascot", "Illustration", "Halftone"),
        category = MediaCategory.ILLUSTRATION_3D,
        aspectRatio = 0.75f,
        likesCount = 780,
        params = GenerationParams(aspectRatio = "4:5", steps = 25, stylePreset = "Comic Halftone")
      ),
      MediaItem(
        id = "img-10",
        title = "Sand Dune Lines Editorial",
        creatorHandle = "@desert_couture",
        creatorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80",
        imageUrl = "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?auto=format&fit=crop&w=800&q=80",
        promptText = "High-fashion haute couture model draped in billowing sand-colored silk chiffon walking across endless Sahara wind-sculpted sand dunes, harsh desert midday sun casting geometric shadows, bold SAND DUNE LINES typography header, Vogue editorial styling",
        tags = listOf("Fashion", "Desert", "Haute Couture", "Editorial"),
        category = MediaCategory.FASHION,
        aspectRatio = 0.7f,
        likesCount = 1120,
        params = GenerationParams(aspectRatio = "4:5", steps = 35, stylePreset = "High Fashion")
      )
    )

    val initialPersonas = listOf(
      CharacterPersona(
        id = "persona-1",
        name = "Aria Vance",
        tagline = "Cyberpunk Investigative Reporter",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80",
        description = "Short asymmetrical silver bob hair, electric violet cybernetic left iris, high-collar leather coat with illuminated seams.",
        triggerTag = "{{AriaVance}}",
        presetPrefix = "Portrait of Aria Vance, silver cropped hair, glowing violet cybernetic eye, trenchcoat"
      ),
      CharacterPersona(
        id = "persona-2",
        name = "Kaito Ren",
        tagline = "Neo-Tokyo Street Alchemist",
        avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=200&q=80",
        description = "Tousled dark messy hair, amber eyes, minimalist dark linen streetwear, brass mechanical cuff on right wrist.",
        triggerTag = "{{KaitoRen}}",
        presetPrefix = "Kaito Ren, tousled black hair, sharp jawline, minimalist dark aesthetic, brass mechanical watch"
      ),
      CharacterPersona(
        id = "persona-3",
        name = "Luna Paws",
        tagline = "3D Claymation Explorer Pup",
        avatarUrl = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?auto=format&fit=crop&w=200&q=80",
        description = "Chubby golden retriever puppy wearing an aviator goggles hat and a tiny brown leather backpack.",
        triggerTag = "{{LunaPaws}}",
        presetPrefix = "Luna Paws, 3D clay golden puppy, aviator leather helmet, big sparkling brown eyes, stop-motion clay texture"
      )
    )

    val initialBoards = listOf(
      InspirationBoard(
        id = "board-1",
        title = "Cinematic Lighting",
        description = "Volumetric fog, chiaroscuro, and anamorphic flares",
        coverUrl = "https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=400&q=80",
        itemCount = 14
      ),
      InspirationBoard(
        id = "board-2",
        title = "Product Ads & Moods",
        description = "Luxury perfumes, specialty coffee, and brand aesthetics",
        coverUrl = "https://images.unsplash.com/photo-1517256064527-09c73fc73e38?auto=format&fit=crop&w=400&q=80",
        itemCount = 9
      ),
      InspirationBoard(
        id = "board-3",
        title = "3D Mascots & Clay",
        description = "Whimsical stop-motion character concepts",
        coverUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=400&q=80",
        itemCount = 6
      )
    )
  }
}
