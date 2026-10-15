package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaCategory
import com.example.model.MediaItem
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
  items: List<MediaItem>,
  selectedCategory: MediaCategory?,
  userCredits: Int,
  onSelectCategory: (MediaCategory?) -> Unit,
  onOpenDetail: (MediaItem) -> Unit,
  onToggleLike: (String) -> Unit,
  onTopUpCredits: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .padding(top = 16.dp)
      .testTag("home_screen")
  ) {
    // Header Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Find Inspiration.",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = TextPrimary,
          letterSpacing = (-0.5).sp
        )
        Text(
          text = "Start Creating.",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = ElectricViolet,
          letterSpacing = (-0.5).sp
        )
      }

      // Credits Top-up Pill
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = ObsidianSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier
          .clickable(onClick = onTopUpCredits)
          .testTag("credit_pill")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Filled.ElectricBolt,
            contentDescription = null,
            tint = AmberGlow,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "$userCredits Credits",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Categories Horizontal Filter Row
    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        FilterChip(
          selected = selectedCategory == null,
          onClick = { onSelectCategory(null) },
          label = { Text("All Inspos") },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = ElectricViolet,
            selectedLabelColor = Color.White,
            containerColor = ObsidianSurfaceElevated,
            labelColor = TextSecondary
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selectedCategory == null,
            borderColor = ObsidianBorder,
            selectedBorderColor = ElectricViolet
          )
        )
      }

      items(MediaCategory.values()) { category ->
        FilterChip(
          selected = selectedCategory == category,
          onClick = { onSelectCategory(category) },
          label = { Text(category.title) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = ElectricViolet,
            selectedLabelColor = Color.White,
            containerColor = ObsidianSurfaceElevated,
            labelColor = TextSecondary
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selectedCategory == category,
            borderColor = ObsidianBorder,
            selectedBorderColor = ElectricViolet
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Masonry Staggered Grid
    LazyVerticalStaggeredGrid(
      columns = StaggeredGridCells.Fixed(2),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp, top = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalItemSpacing = 10.dp,
      modifier = Modifier.fillMaxSize()
    ) {
      items(items, key = { it.id }) { item ->
        MasonryCard(
          item = item,
          onClick = { onOpenDetail(item) },
          onToggleLike = { onToggleLike(item.id) }
        )
      }
    }
  }
}

@Composable
fun MasonryCard(
  item: MediaItem,
  onClick: () -> Unit,
  onToggleLike: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(ObsidianSurfaceElevated)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("masonry_card_${item.id}")
  ) {
    // Media image with correct aspect ratio
    AsyncImage(
      model = item.imageUrl,
      contentDescription = item.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(item.aspectRatio)
    )

    // Top media indicator (Reel or resolution)
    if (item.videoDuration != null) {
      Row(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(8.dp)
          .clip(CircleShape)
          .background(Color(0xBB000000))
          .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Filled.PlayArrow,
          contentDescription = "Video",
          tint = Color.White,
          modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
          text = item.videoDuration,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }

    // Bottom gradient overlay with title & like button
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .background(
          Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color(0xCC000000), Color(0xEE000000))
          )
        )
        .padding(start = 10.dp, end = 6.dp, top = 24.dp, bottom = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = item.title,
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          maxLines = 1,
          modifier = Modifier.weight(1f)
        )

        IconButton(
          onClick = onToggleLike,
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = if (item.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Like",
            tint = if (item.isLiked) Color(0xFFF43F5E) else Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}
