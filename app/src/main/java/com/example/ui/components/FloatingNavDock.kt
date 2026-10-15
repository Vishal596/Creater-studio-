package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.NavigationTab
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.FloatingDockBg
import com.example.ui.theme.FloatingDockBorder
import com.example.ui.theme.TextSecondary

@Composable
fun FloatingNavDock(
  currentTab: NavigationTab,
  onTabSelected: (NavigationTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 24.dp, vertical = 16.dp),
    contentAlignment = Alignment.Center
  ) {
    Surface(
      shape = RoundedCornerShape(32.dp),
      color = FloatingDockBg,
      tonalElevation = 8.dp,
      shadowElevation = 16.dp,
      modifier = Modifier
        .shadow(20.dp, RoundedCornerShape(32.dp), spotColor = Color(0x66000000))
        .border(1.dp, FloatingDockBorder, RoundedCornerShape(32.dp))
        .height(64.dp)
        .testTag("floating_nav_dock")
    ) {
      Row(
        modifier = Modifier
          .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        NavDockItem(
          selected = currentTab == NavigationTab.HOME,
          selectedIcon = Icons.Filled.Home,
          unselectedIcon = Icons.Outlined.Home,
          contentDescription = "Explore Home",
          onClick = { onTabSelected(NavigationTab.HOME) }
        )

        NavDockItem(
          selected = currentTab == NavigationTab.REELS,
          selectedIcon = Icons.Filled.PlayCircle,
          unselectedIcon = Icons.Outlined.PlayCircleOutline,
          contentDescription = "Video Reels",
          onClick = { onTabSelected(NavigationTab.REELS) }
        )

        NavDockItem(
          selected = currentTab == NavigationTab.SEARCH,
          selectedIcon = Icons.Filled.Search,
          unselectedIcon = Icons.Outlined.Search,
          contentDescription = "Search & Categories",
          onClick = { onTabSelected(NavigationTab.SEARCH) }
        )

        // Center prominent Create action button
        CreateDockItem(
          selected = currentTab == NavigationTab.CREATE,
          onClick = { onTabSelected(NavigationTab.CREATE) }
        )

        NavDockItem(
          selected = currentTab == NavigationTab.VAULT,
          selectedIcon = Icons.Filled.Bookmark,
          unselectedIcon = Icons.Outlined.BookmarkBorder,
          contentDescription = "Vault & Personas",
          onClick = { onTabSelected(NavigationTab.VAULT) }
        )
      }
    }
  }
}

@Composable
private fun NavDockItem(
  selected: Boolean,
  selectedIcon: ImageVector,
  unselectedIcon: ImageVector,
  contentDescription: String,
  onClick: () -> Unit
) {
  val iconColor by animateColorAsState(
    targetValue = if (selected) Color.White else TextSecondary,
    label = "iconColor"
  )
  val scale by animateFloatAsState(
    targetValue = if (selected) 1.15f else 1.0f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "scale"
  )

  Box(
    modifier = Modifier
      .size(44.dp)
      .clip(CircleShape)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      ),
    contentAlignment = Alignment.Center
  ) {
    if (selected) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(Color(0x338B5CF6))
      )
    }
    Icon(
      imageVector = if (selected) selectedIcon else unselectedIcon,
      contentDescription = contentDescription,
      tint = iconColor,
      modifier = Modifier
        .size(24.dp)
        .scale(scale)
    )
  }
}

@Composable
private fun CreateDockItem(
  selected: Boolean,
  onClick: () -> Unit
) {
  val scale by animateFloatAsState(
    targetValue = if (selected) 1.15f else 1.05f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "createScale"
  )

  Box(
    modifier = Modifier
      .size(46.dp)
      .scale(scale)
      .clip(CircleShape)
      .background(
        Brush.linearGradient(
          listOf(ElectricViolet, Color(0xFF6366F1))
        )
      )
      .border(1.dp, Color(0x66FFFFFF), CircleShape)
      .clickable(onClick = onClick)
      .testTag("nav_create_button"),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = if (selected) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
      contentDescription = "Create Artwork",
      tint = Color.White,
      modifier = Modifier.size(22.dp)
    )
  }
}
