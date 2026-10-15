package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CharacterPersona
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.viewmodel.CreationUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateStudioScreen(
  state: CreationUiState,
  userCredits: Int,
  personas: List<CharacterPersona>,
  onPromptChange: (String) -> Unit,
  onNegativePromptChange: (String) -> Unit,
  onAspectRatioChange: (String) -> Unit,
  onStylePresetChange: (String) -> Unit,
  onAppendModifier: (String) -> Unit,
  onInsertPersona: (CharacterPersona) -> Unit,
  onSurprisePrompt: () -> Unit,
  onGenerateArt: () -> Unit,
  onTopUpCredits: () -> Unit,
  onSaveToBoard: (String) -> Unit,
  onToast: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showNegativePrompt by remember { mutableStateOf(false) }

  val promptModifiers = listOf(
    "Cinematic lighting",
    "35mm lens f/1.4",
    "Volumetric mist",
    "Octane 3D render",
    "Hyper-detailed",
    "Editorial lookbook",
    "Anamorphic flare",
    "Ray-traced reflections"
  )

  val stylePresets = listOf(
    "Photorealism",
    "Anime",
    "Clay 3D",
    "Cyberpunk",
    "Cinematic Noir",
    "Retro Vector"
  )

  val aspectRatios = listOf("1:1", "9:16", "16:9", "4:5")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 16.dp)
      .padding(bottom = 90.dp)
      .testTag("create_studio_screen")
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Creation Studio",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = TextPrimary
        )
        Text(
          text = "Turn ideas into original art",
          fontSize = 13.sp,
          color = TextSecondary
        )
      }

      Surface(
        shape = RoundedCornerShape(20.dp),
        color = ObsidianSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.clickable(onClick = onTopUpCredits)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Filled.ElectricBolt, contentDescription = null, tint = AmberGlow, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("$userCredits Credits", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Prompt Input Card
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = ObsidianSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("PROMPT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricViolet, letterSpacing = 1.sp)
          // 🎲 Surprise Prompt Button
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(ObsidianSurfaceElevated)
              .clickable(onClick = onSurprisePrompt)
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Filled.Casino, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Surprise Me", fontSize = 11.sp, color = CyberCyan, fontWeight = FontWeight.SemiBold)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = state.prompt,
          onValueChange = onPromptChange,
          placeholder = {
            Text(
              "Describe the visual you want to create... (e.g. cybernetic samurai in rain, octane render)",
              color = TextTertiary,
              fontSize = 13.sp
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .testTag("prompt_input_field"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ObsidianSurfaceElevated,
            unfocusedContainerColor = ObsidianSurfaceElevated,
            focusedBorderColor = ElectricViolet,
            unfocusedBorderColor = ObsidianBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          ),
          shape = RoundedCornerShape(12.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Personas Injection Chips
    if (personas.isNotEmpty()) {
      Text(
        text = "INJECT CHARACTER PERSONA",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextTertiary,
        letterSpacing = 0.8.sp
      )
      Spacer(modifier = Modifier.height(6.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(personas) { persona ->
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = ObsidianSurfaceElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
            modifier = Modifier.clickable { onInsertPersona(persona) }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              AsyncImage(
                model = persona.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(20.dp).clip(CircleShape)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(persona.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Quick Modifier Tags
    Text(
      text = "QUICK MODIFIERS",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = TextTertiary,
      letterSpacing = 0.8.sp
    )
    Spacer(modifier = Modifier.height(6.dp))
    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      promptModifiers.forEach { tag ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ObsidianSurfaceElevated)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(8.dp))
            .clickable { onAppendModifier(tag) }
            .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
          Text(text = "+ $tag", fontSize = 11.sp, color = TextSecondary)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Aspect Ratio Segmented Buttons
    Text(
      text = "ASPECT RATIO",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = TextTertiary,
      letterSpacing = 0.8.sp
    )
    Spacer(modifier = Modifier.height(6.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      aspectRatios.forEach { ratio ->
        val isSelected = state.aspectRatio == ratio
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ElectricViolet else ObsidianSurfaceElevated)
            .border(1.dp, if (isSelected) ElectricViolet else ObsidianBorder, RoundedCornerShape(12.dp))
            .clickable { onAspectRatioChange(ratio) }
            .padding(vertical = 10.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = ratio,
            color = if (isSelected) Color.White else TextSecondary,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Style Presets
    Text(
      text = "STYLE PRESET",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = TextTertiary,
      letterSpacing = 0.8.sp
    )
    Spacer(modifier = Modifier.height(6.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(stylePresets) { preset ->
        val isSelected = state.stylePreset == preset
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ElectricViolet.copy(alpha = 0.2f) else ObsidianSurfaceElevated)
            .border(1.dp, if (isSelected) ElectricViolet else ObsidianBorder, RoundedCornerShape(12.dp))
            .clickable { onStylePresetChange(preset) }
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = preset,
            color = if (isSelected) ElectricViolet else TextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Negative Prompt Collapsible Toggle
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { showNegativePrompt = !showNegativePrompt }
        .padding(vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (showNegativePrompt) "Hide Negative Prompt ▲" else "Add Negative Prompt ▼",
        color = TextTertiary,
        fontSize = 12.sp
      )
    }

    if (showNegativePrompt) {
      Spacer(modifier = Modifier.height(4.dp))
      OutlinedTextField(
        value = state.negativePrompt,
        onValueChange = onNegativePromptChange,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = ObsidianSurfaceElevated,
          unfocusedContainerColor = ObsidianSurfaceElevated,
          focusedBorderColor = ElectricViolet,
          unfocusedBorderColor = ObsidianBorder,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(12.dp)
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Generation Action Button
    Button(
      onClick = onGenerateArt,
      enabled = !state.isGenerating,
      shape = RoundedCornerShape(18.dp),
      colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .background(
          Brush.horizontalGradient(listOf(ElectricViolet, Color(0xFF6366F1))),
          RoundedCornerShape(18.dp)
        )
        .testTag("generate_art_button")
    ) {
      if (state.isGenerating) {
        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Synthesizing Canvas...", fontWeight = FontWeight.Bold, color = Color.White)
      } else {
        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Generate Artwork  (Cost: 2 Credits)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
      }
    }

    // Live Generation Progress Indicator
    AnimatedVisibility(visible = state.isGenerating) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, ElectricViolet.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = state.stageMessage, fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
          Text(text = "${(state.progress * 100).toInt()}%", fontSize = 13.sp, color = ElectricViolet, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
          progress = { state.progress },
          color = ElectricViolet,
          trackColor = ObsidianBorder,
          modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape)
        )
      }
    }

    // Generated Result Card
    state.generatedResult?.let { result ->
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        text = "LATEST CREATION",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = ElectricViolet,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(8.dp))
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = ObsidianSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(result.aspectRatio)
              .clip(RoundedCornerShape(14.dp))
          ) {
            AsyncImage(
              model = result.imageUrl,
              contentDescription = "Generated Artwork",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(text = result.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = result.promptText, fontSize = 12.sp, color = TextSecondary, maxLines = 3)

          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Prompt", result.promptText)
                clipboard.setPrimaryClip(clip)
                onToast("Copied generation prompt! 📋")
              },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
            ) {
              Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Copy", color = TextPrimary, fontSize = 12.sp)
            }

            Button(
              onClick = { onSaveToBoard(result.id) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
            ) {
              Icon(Icons.Filled.Bookmark, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Save", color = Color.White, fontSize = 12.sp)
            }
          }
        }
      }
    }
  }
}
