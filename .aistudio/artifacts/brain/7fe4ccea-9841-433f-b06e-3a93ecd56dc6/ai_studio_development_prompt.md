# Master Development Prompt: PromptAura (AI Inspiration & Creation Studio)

> **App Identity:** PromptAura — The Visual AI Discovery & Creation Hub  
> **Target Framework:** Android (Kotlin, Jetpack Compose, Material Design 3, Coroutines/Flow)  
> **Core Concept:** A creative platform where creators explore curated AI artwork & video reels, inspect full prompt recipes, remix visual ideas into their own generation studio, maintain reusable character personas, and organize assets into moodboards.

---

## 1. System Overview & Product Architecture

PromptAura addresses the gap between admiring generated generative media and understanding *how* it was made. Every asset in the discovery feed exposes its complete generation recipe (prompt, negative prompt, aspect ratio, camera style, lighting cues, model archetype). Users can tap "Use Idea" or "Remix" to prefill a Creation Studio, test iterative modifications, generate new artworks, and save curated inspirations into custom boards.

---

## 2. Core Functional Requirements & Feature Breakdown

### A. Discovery Feed (Inspiration Masonry)
1. **Dynamic Staggered Grid (Masonry):**
   - 2-column Pinterest-style staggered layout displaying mixed aspect ratio cards (1:1, 4:5, 9:16, 16:9).
   - High-fidelity visual presentation with subtle rounded corners (`16.dp`), elevation, and shimmer placeholders during image loading.
   - Micro-tags on each card indicating media type (e.g., `Image`, `Video Reel`, `Cinematic`, `Editorial`, `3D Render`).
2. **Category & Theme Filter Chips:**
   - Horizontally scrollable row: *Trending, Ads & Products, Brand & Typography, Character & 3D, Editorial & Fashion, Sci-Fi & Concept, Architecture*.
3. **Card Micro-Interactions:**
   - Tap opens full-screen **Inspect & Remix Sheet**.
   - Long-press triggers rapid "Quick Save to Board" or "Copy Prompt" sheet.
   - Bookmark/Heart button with animated spring feedback.

### B. Immersive Video Reel Stream ("Every Reel Comes With a Prompt")
1. **9:16 Vertical Paging Feed:**
   - Full-bleed vertical swipe reel player (`VerticalPager`).
   - Seamless loop playback simulation with play/pause tap gesture, mute/unmute audio toggle, and seek scrubber.
2. **Floating Action "✨ Use Idea" Button:**
   - Prominently anchored pill button floating above creator metadata (`Modifier.align(Alignment.BottomCenter)`).
   - Instant routing to Creation Studio with this video's exact seed prompt, motion parameters, and aesthetic style pre-populated.
3. **Prompt Peek Bottom Drawer:**
   - Creator attribution (`@handle`), creation timestamp, like count, and resolution badge (`HD / 4K`).
   - Full prompt viewable with expandable disclosure (`Read more...`).
   - Quick utility bar:
     - `❤️ Like / Favorite` (with counter)
     - `📋 One-Tap Copy Prompt` (shows toast + clipboard confirmation)
     - `🔖 Save to Board`
     - `📤 Share Card / Share Prompt`
     - `🔊 Sound Toggle`

### C. Visual & Prompt Search Engine
1. **Omni-Search Bar:**
   - Search by keyword, aesthetic style ("cyberpunk", "wes anderson pastel", "cinematic lighting"), or model genre.
   - Visual Search Simulation button (camera icon) allowing users to select an image from device or preset palette to find visually similar prompts.
2. **Curated Showcase Categories:**
   - 4-item preview blocks with navigation carousels:
     - *Ads & Product Photography*
     - *Brand Identity & Geometric Logos*
     - *3D Figurines & Claymation Characters*
     - *Posters, Typography & Schematics*
     - *Cinematic Portraits & Studio Lighting*

### D. Creation Studio (Generation Engine & Prompt Builder)
1. **Interactive Prompt Crafting Console:**
   - Multi-line dynamic text input with intelligent auto-complete helper chips (`+ 35mm lens`, `+ volumetric fog`, `+ editorial lighting`, `+ 8k octane render`).
   - Negative Prompt drawer (e.g. `blurry, deformed fingers, low resolution, watermark`).
2. **Generation Configuration Controls:**
   - **Aspect Ratio Selector:** Visual segmented picker (`1:1 Square`, `9:16 Story/Reel`, `16:9 Landscape`, `4:5 Portrait`).
   - **Generation Mode Toggle:** `Image Generation` vs `Video Clip (5s / 10s)`.
   - **Style Preset Carousel:** Visual thumbnail selection (*Photorealistic, Anime/Manga, Cybernetic, Clay 3D, Oil Painting, Minimalist Vector*).
   - **Model Selector:** High-performance generative models (e.g., *Lumina Ultra v3, FastDraft, Photoreal XL*).
3. **Execution & Live Progress:**
   - "Generate Artwork" CTA displaying credit deduction badge (e.g. `✨ Generate (Cost: 2 Credits)`).
   - Generative loading state with ambient gradient pulse, live progress meter, and step status indicator ("Synthesizing latent space...", "Refining details...").
   - Result preview canvas with Comparison view, Download, and Add to Board options.

### E. Consistent Persona Studio (Character Vault)
1. **Character Definition:**
   - Ability to create and save persistent characters with fixed face structures, hair styles, and clothing palettes.
   - Persona tag injection: when generating, users can insert `{{CharacterName}}` into any prompt to preserve subject consistency across diverse scenes.
2. **Character Gallery:**
   - Card deck showing character headshots, total generated variations, and quick "Create in New Scene" shortcut.

### F. Boards & Inspiration Library
1. **User Collections:**
   - Custom boards (e.g. "Cyberpunk Fashion", "Coffee Ad Moods", "3D Creature Concepts").
   - Multi-select curation, private/public toggle, and board export options.
2. **History & Liked Prompts:**
   - Reverse chronological timeline of all copied or remixed prompts.

---

## 3. UI/UX Layout & Information Architecture

### Floating Island Bottom Navigation
To match the modern, sleek aesthetic of the inspiration apps:
- Implement a floating navigation pill elevated above the screen bottom (`padding(bottom = 24.dp)`).
- Frosted glass effect: `Surface` with high corner radius (`28.dp`), container color `MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)` with 90% alpha, subtle border stroke, and shadow.
- **5 Primary Destinations:**
  1. **Home (`Home`):** Masonry inspiration feed + curated daily drops.
  2. **Reels (`PlayCircle`):** Immersive vertical 9:16 prompt-backed video player.
  3. **Explore (`Search`):** Visual search & categorized prompt collections.
  4. **Create (`AutoAwesome`):** Prompt builder and generative creation studio.
  5. **Vault (`Bookmarks / Person`):** User boards, saved character personas, and generation history.

---

## 4. Visual Design System (Colors & Typography)

### Color Palette (Modern Dark / High-Contrast Editorial Theme)
- **Background:** Rich Deep Obsidian `#0E1116`
- **Surface / Cards:** `#181C24` and `#222733` with 1dp border `#2E3545`
- **Primary Accent:** Radiant Electric Violet `#7C4DFF` / `#8B5CF6`
- **Secondary Accent:** Cyber Coral `#FF5376` & Neon Cyan `#00E5FF`
- **Text Primary:** `#F8FAFC` (High legibility pure off-white)
- **Text Secondary:** `#94A3B8` (Muted cool slate)
- **Accent Floating Badge ("Use Idea"):** High-contrast Pill with gradient `#8B5CF6` to `#6366F1`

### Typography Hierarchy
- **Hero Title:** Bold Display (30–34sp, Tight Letter Spacing `-0.5sp`, FontWeight 800)
- **Section Headers:** Bold Headline (20–22sp, FontWeight 700)
- **Card Metadata & Prompts:** Clean Mono / Sans (12–14sp, LineHeight 18sp)
- **Buttons & Tags:** Medium Caps / Title Medium (14sp, FontWeight 600)

---

## 5. Responsive & Tablet Adaptation
- **Phone (Compact < 600dp):** 2-column staggered masonry grid; floating pill dock anchored at bottom.
- **Tablet / Foldable (Expanded >= 600dp):**
  - Staggered grid automatically expands to 3 or 4 columns (`LazyVerticalStaggeredGrid(StaggeredGridCells.Adaptive(minSize = 220.dp))`).
  - Floating dock transitions to a vertical ergonomic `NavigationRail` on the left.
  - Studio screen adopts side-by-side layout: Left column for prompt parameters and character controls; Right column for live canvas preview.

---

## 6. Technical Stack & Architecture
- **Language:** Kotlin 2.0+
- **UI Toolkit:** 100% Jetpack Compose with Material 3
- **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture
- **State Flow:** Kotlin Coroutines `StateFlow` & `collectAsStateWithLifecycle`
- **Image Pipeline:** Coil 3 / AsyncImage with crossfade and cache strategy
- **Testing:** Unit tests & Robolectric JVM tests for state transitions and prompt parser logic
