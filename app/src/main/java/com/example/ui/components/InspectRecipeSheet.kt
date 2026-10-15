package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaItem
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun InspectRecipeSheet(
  item: MediaItem,
  onDismiss: () -> Unit,
  onRemix: (MediaItem) -> Unit,
  onLikeToggle: (String) -> Unit,
  onSaveToggle: (String) -> Unit,
  onToast: (String) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val context = LocalContext.current

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = ObsidianSurface,
    contentColor = TextPrimary,
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(top = 12.dp, bottom = 8.dp)
          .size(width = 44.dp, height = 4.dp)
          .clip(CircleShape)
          .background(ObsidianBorder)
      )
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .testTag("inspect_recipe_sheet")
    ) {
      // Header preview image with rounded aesthetic
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurfaceElevated)
      ) {
        AsyncImage(
          model = item.imageUrl,
          contentDescription = item.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxWidth().height(260.dp)
        )

        // Creator badge & likes chip overlay
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(12.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xCC0E1118))
            .padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          AsyncImage(
            model = item.creatorAvatar,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(24.dp).clip(CircleShape)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = item.creatorHandle,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Title & Action Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = item.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Category: ${item.category.title}",
            fontSize = 13.sp,
            color = TextSecondary
          )
        }

        Row {
          IconButton(
            onClick = { onLikeToggle(item.id) },
            modifier = Modifier.testTag("detail_like_button")
          ) {
            Icon(
              imageVector = if (item.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Like",
              tint = if (item.isLiked) Color(0xFFF43F5E) else TextSecondary
            )
          }

          IconButton(
            onClick = { onSaveToggle(item.id) },
            modifier = Modifier.testTag("detail_save_button")
          ) {
            Icon(
              imageVector = if (item.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = "Save to Board",
              tint = if (item.isSaved) ElectricViolet else TextSecondary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Primary CTA: Remix in Studio & Copy Prompt
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = { onRemix(item) },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .background(
              Brush.linearGradient(listOf(ElectricViolet, Color(0xFF6366F1))),
              RoundedCornerShape(16.dp)
            )
            .testTag("remix_button")
        ) {
          Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("✨ Remix in Studio", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        OutlinedButton(
          onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Prompt", item.promptText)
            clipboard.setPrimaryClip(clip)
            onToast("Prompt copied to clipboard! 📋")
          },
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
          modifier = Modifier
            .height(50.dp)
            .testTag("copy_prompt_button")
        ) {
          Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Copy Prompt", color = TextPrimary, fontWeight = FontWeight.Medium)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Full Generation Prompt Card
      Text(
        text = "PROMPT RECIPE",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = ElectricViolet,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(6.dp))
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = ObsidianSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = item.promptText,
          fontSize = 14.sp,
          color = TextPrimary,
          lineHeight = 20.sp,
          fontFamily = FontFamily.SansSerif,
          modifier = Modifier.padding(14.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Negative Prompt
      Text(
        text = "NEGATIVE PROMPT",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextTertiary,
        letterSpacing = 0.8.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = ObsidianSurfaceElevated.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = item.negativePrompt,
          fontSize = 12.sp,
          color = TextSecondary,
          modifier = Modifier.padding(12.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Generation Metadata Matrix
      Text(
        text = "GENERATION PARAMETERS",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextTertiary,
        letterSpacing = 0.8.sp
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ParamBox(label = "Model", value = item.params.modelUsed, modifier = Modifier.weight(1f))
        ParamBox(label = "Ratio", value = item.params.aspectRatio, modifier = Modifier.weight(1f))
        ParamBox(label = "Steps", value = "${item.params.steps}", modifier = Modifier.weight(1f))
        ParamBox(label = "Style", value = item.params.stylePreset, modifier = Modifier.weight(1f))
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Tags
      if (item.tags.isNotEmpty()) {
        Text(
          text = "TAGS",
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
          item.tags.forEach { tag ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(ObsidianSurfaceElevated)
                .border(1.dp, ObsidianBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(text = "#$tag", fontSize = 12.sp, color = TextSecondary)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

@Composable
private fun ParamBox(label: String, value: String, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(ObsidianSurfaceElevated)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(10.dp))
      .padding(vertical = 8.dp, horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = label, fontSize = 10.sp, color = TextTertiary)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
  }
}
