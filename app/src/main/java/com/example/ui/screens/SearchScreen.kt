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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaCategory
import com.example.model.MediaItem
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun SearchScreen(
  searchQuery: String,
  onQueryChange: (String) -> Unit,
  filteredItems: List<MediaItem>,
  allItems: List<MediaItem>,
  onOpenDetail: (MediaItem) -> Unit,
  onCategorySelected: (MediaCategory) -> Unit,
  onToast: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .padding(top = 16.dp)
      .testTag("search_screen")
  ) {
    // Header
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
      Text(
        text = "Your Next Favorite,",
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        color = TextPrimary,
        letterSpacing = (-0.5).sp
      )
      Text(
        text = "One Search Away.",
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        color = ElectricViolet,
        letterSpacing = (-0.5).sp
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Search Bar with Camera Visual Search button
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onQueryChange,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .testTag("search_input"),
      placeholder = { Text("Search prompts, styles, creators...", color = TextTertiary, fontSize = 14.sp) },
      leadingIcon = {
        Icon(Icons.Filled.Search, contentDescription = "Search", tint = TextSecondary)
      },
      trailingIcon = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }) {
              Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = TextSecondary)
            }
          }
          IconButton(
            onClick = { onToast("Visual Prompt Camera Search activated! 📷") },
            modifier = Modifier.testTag("visual_search_camera")
          ) {
            Icon(Icons.Filled.CameraAlt, contentDescription = "Visual Search", tint = ElectricViolet)
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(20.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = ObsidianSurface,
        unfocusedContainerColor = ObsidianSurface,
        focusedBorderColor = ElectricViolet,
        unfocusedBorderColor = ObsidianBorder,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
      )
    )

    Spacer(modifier = Modifier.height(16.dp))

    if (searchQuery.isNotBlank()) {
      // Results Grid
      Text(
        text = "Found ${filteredItems.size} results for \"$searchQuery\"",
        color = TextSecondary,
        fontSize = 13.sp,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
      )

      LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp, top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp,
        modifier = Modifier.fillMaxSize()
      ) {
        items(filteredItems, key = { it.id }) { item ->
          MasonryCard(
            item = item,
            onClick = { onOpenDetail(item) },
            onToggleLike = {}
          )
        }
      }
    } else {
      // Showcase Categories Shelves (matches Screenshot 1!)
      LazyColumn(
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        val categoriesToShow = listOf(
          MediaCategory.ADS_PRODUCT,
          MediaCategory.BRAND_LOGO,
          MediaCategory.ILLUSTRATION_3D,
          MediaCategory.POSTERS_VISUALS,
          MediaCategory.PORTRAITS
        )

        items(categoriesToShow) { category ->
          val categoryItems = allItems.filter { it.category == category }
          CategoryShelf(
            category = category,
            items = categoryItems,
            onCategoryClick = { onCategorySelected(category) },
            onItemClick = onOpenDetail
          )
        }
      }
    }
  }
}

@Composable
private fun CategoryShelf(
  category: MediaCategory,
  items: List<MediaItem>,
  onCategoryClick: () -> Unit,
  onItemClick: (MediaItem) -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    // Header with arrow
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onCategoryClick)
        .padding(horizontal = 20.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = category.title,
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
      )
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
        contentDescription = "See more",
        tint = TextSecondary,
        modifier = Modifier.size(14.dp)
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // 4 preview items horizontal row
    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      if (items.isEmpty()) {
        item {
          Text("No inspirations yet", color = TextTertiary, fontSize = 12.sp)
        }
      } else {
        items(items.take(6)) { item ->
          Box(
            modifier = Modifier
              .size(width = 82.dp, height = 82.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(ObsidianSurfaceElevated)
              .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
              .clickable { onItemClick(item) }
          ) {
            AsyncImage(
              model = item.imageUrl,
              contentDescription = item.title,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }
        }
      }
    }
  }
}
