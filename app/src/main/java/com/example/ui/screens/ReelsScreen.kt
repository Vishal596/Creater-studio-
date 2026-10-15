package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaItem
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.ObsidianBg

@Composable
fun ReelsScreen(
  reels: List<MediaItem>,
  onUseIdea: (MediaItem) -> Unit,
  onToggleLike: (String) -> Unit,
  onToast: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val pagerState = rememberPagerState(pageCount = { reels.size })

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .testTag("reels_screen")
  ) {
    if (reels.isEmpty()) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No video reels available yet", color = Color.Gray)
      }
    } else {
      VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
      ) { page ->
        val reel = reels[page]
        ReelPlayerItem(
          reel = reel,
          onUseIdea = { onUseIdea(reel) },
          onToggleLike = { onToggleLike(reel.id) },
          onCopyPrompt = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Prompt", reel.promptText)
            clipboard.setPrimaryClip(clip)
            onToast("Copied video prompt to clipboard! 📋")
          },
          onToast = onToast
        )
      }
    }

    // Top Header Title Overlay
    Surface(
      color = Color(0x99000000),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 28.dp)
    ) {
      Text(
        text = "Every Video Comes With a Prompt.",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
      )
    }
  }
}

@Composable
fun ReelPlayerItem(
  reel: MediaItem,
  onUseIdea: () -> Unit,
  onToggleLike: () -> Unit,
  onCopyPrompt: () -> Unit,
  onToast: (String) -> Unit
) {
  var isPlaying by remember { mutableStateOf(true) }
  var isMuted by remember { mutableStateOf(false) }
  var isPromptExpanded by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .clickable { isPlaying = !isPlaying }
  ) {
    // 9:16 Video Canvas Simulation
    AsyncImage(
      model = reel.imageUrl,
      contentDescription = reel.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // Play / Pause central visual flash indicator
    AnimatedVisibility(
      visible = !isPlaying,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier.align(Alignment.Center)
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(Color(0x88000000)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Filled.PlayArrow,
          contentDescription = "Paused",
          tint = Color.White,
          modifier = Modifier.size(36.dp)
        )
      }
    }

    // Bottom Ambient Dark Scrim for readability
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .background(
          Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color(0xAA000000), Color(0xFA000000))
          )
        )
        .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 96.dp)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {

        // ✨ Floating Action: Use Idea Button (matches screenshot!)
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
          contentAlignment = Alignment.Center
        ) {
          Button(
            onClick = onUseIdea,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xDD1E1E2C)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
              .border(1.dp, Brush.horizontalGradient(listOf(ElectricViolet, Color(0xFF00E5FF))), RoundedCornerShape(24.dp))
              .testTag("use_idea_button")
          ) {
            Icon(
              imageVector = Icons.Filled.AutoAwesome,
              contentDescription = null,
              tint = Color(0xFF00E5FF),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Use Idea",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }

        // Action controls bar (Like, HD, Share, Copy Prompt, Sound)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Likes
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onToggleLike)
          ) {
            Icon(
              imageVector = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Like",
              tint = if (reel.isLiked) Color(0xFFF43F5E) else Color.White,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "${reel.likesCount}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          // HD Download
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onToast("Downloaded reel in 4K HD! 📥") }
          ) {
            Icon(Icons.Filled.Download, contentDescription = "Download HD", tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "HD", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          // Share
          IconButton(
            onClick = { onToast("Share link copied! 🔗") },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(18.dp))
          }

          // Copy Prompt Button
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0x44FFFFFF),
            modifier = Modifier.clickable(onClick = onCopyPrompt)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Copy prompt", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
          }

          // Sound Toggle
          IconButton(
            onClick = {
              isMuted = !isMuted
              onToast(if (isMuted) "Audio muted 🔇" else "Audio unmuted 🔊")
            },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = if (isMuted) Icons.Filled.VolumeMute else Icons.Filled.VolumeUp,
              contentDescription = "Sound Toggle",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Creator Handle
        Text(
          text = reel.creatorHandle,
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Prompt & Style Tags
        Text(
          text = reel.promptText,
          color = Color(0xFFE2E8F0),
          fontSize = 12.sp,
          lineHeight = 16.sp,
          maxLines = if (isPromptExpanded) 10 else 2,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.clickable { isPromptExpanded = !isPromptExpanded }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Video Progress Scrubber
        LinearProgressIndicator(
          progress = { 0.65f },
          modifier = Modifier
            .fillMaxWidth()
            .height(2.dp),
          color = ElectricViolet,
          trackColor = Color(0x44FFFFFF)
        )
      }
    }
  }
}
