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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.model.CharacterPersona
import com.example.model.InspirationBoard
import com.example.model.MediaItem
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

@Composable
fun VaultScreen(
  boards: List<InspirationBoard>,
  personas: List<CharacterPersona>,
  myCreations: List<MediaItem>,
  userCredits: Int,
  onCreatePersona: (String, String, String) -> Unit,
  onCreateBoard: (String, String) -> Unit,
  onTopUpCredits: () -> Unit,
  onOpenDetail: (MediaItem) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var showNewBoardDialog by remember { mutableStateOf(false) }
  var showNewPersonaDialog by remember { mutableStateOf(false) }

  val tabs = listOf("Boards", "Personas", "My Creations")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .padding(top = 16.dp)
      .testTag("vault_screen")
  ) {
    // Profile Card Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
          model = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=200&q=80",
          contentDescription = "User Avatar",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .border(2.dp, ElectricViolet, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(text = "Creator Studio", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          Text(text = "@prompt_visionary", fontSize = 12.sp, color = TextSecondary)
        }
      }

      // Top up button
      Button(
        onClick = onTopUpCredits,
        colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceElevated),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AmberGlow.copy(alpha = 0.5f))
      ) {
        Icon(Icons.Filled.ElectricBolt, contentDescription = null, tint = AmberGlow, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("$userCredits", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Stats Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(ObsidianSurface)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
        .padding(vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      StatColumn(label = "Boards", count = "${boards.size}")
      StatColumn(label = "Personas", count = "${personas.size}")
      StatColumn(label = "Creations", count = "${myCreations.size}")
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Tab Row
    TabRow(
      selectedTabIndex = selectedTabIndex,
      containerColor = ObsidianBg,
      contentColor = ElectricViolet,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
          color = ElectricViolet,
          height = 3.dp
        )
      },
      divider = {
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianBorder))
      }
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTabIndex == index,
          onClick = { selectedTabIndex = index },
          text = {
            Text(
              text = title,
              color = if (selectedTabIndex == index) Color.White else TextSecondary,
              fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
              fontSize = 13.sp
            )
          }
        )
      }
    }

    // Tab Contents
    when (selectedTabIndex) {
      0 -> BoardsTab(
        boards = boards,
        onCreateClick = { showNewBoardDialog = true }
      )
      1 -> PersonasTab(
        personas = personas,
        onCreateClick = { showNewPersonaDialog = true }
      )
      2 -> MyCreationsTab(
        creations = myCreations,
        onOpenDetail = onOpenDetail
      )
    }

    // New Board Dialog
    if (showNewBoardDialog) {
      CreateBoardDialog(
        onDismiss = { showNewBoardDialog = false },
        onCreate = { title, desc ->
          onCreateBoard(title, desc)
          showNewBoardDialog = false
        }
      )
    }

    // New Persona Dialog
    if (showNewPersonaDialog) {
      CreatePersonaDialog(
        onDismiss = { showNewPersonaDialog = false },
        onCreate = { name, tagline, desc ->
          onCreatePersona(name, tagline, desc)
          showNewPersonaDialog = false
        }
      )
    }
  }
}

@Composable
private fun StatColumn(label: String, count: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = count, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    Text(text = label, fontSize = 11.sp, color = TextTertiary)
  }
}

@Composable
private fun BoardsTab(boards: List<InspirationBoard>, onCreateClick: () -> Unit) {
  LazyColumn(
    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    modifier = Modifier.fillMaxSize().padding(bottom = 90.dp)
  ) {
    item {
      OutlinedButton(
        onClick = onCreateClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet)
      ) {
        Icon(Icons.Filled.Add, contentDescription = null, tint = ElectricViolet)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Create New Board", color = ElectricViolet, fontWeight = FontWeight.SemiBold)
      }
    }

    items(boards) { board ->
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = ObsidianSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          AsyncImage(
            model = board.coverUrl,
            contentDescription = board.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(board.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(board.description, fontSize = 12.sp, color = TextSecondary, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${board.itemCount} inspirations saved", fontSize = 11.sp, color = ElectricViolet)
          }
        }
      }
    }
  }
}

@Composable
private fun PersonasTab(personas: List<CharacterPersona>, onCreateClick: () -> Unit) {
  LazyColumn(
    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    modifier = Modifier.fillMaxSize().padding(bottom = 90.dp)
  ) {
    item {
      OutlinedButton(
        onClick = onCreateClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
      ) {
        Icon(Icons.Filled.Add, contentDescription = null, tint = CyberCyan)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Build New Character Persona", color = CyberCyan, fontWeight = FontWeight.SemiBold)
      }
    }

    items(personas) { persona ->
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = ObsidianSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
              model = persona.avatarUrl,
              contentDescription = persona.name,
              contentScale = ContentScale.Crop,
              modifier = Modifier.size(50.dp).clip(CircleShape).border(1.dp, ElectricViolet, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(persona.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
              Text(persona.tagline, fontSize = 12.sp, color = TextSecondary)
              Text(
                text = persona.triggerTag,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricViolet
              )
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = persona.description, fontSize = 12.sp, color = TextTertiary, lineHeight = 16.sp)
        }
      }
    }
  }
}

@Composable
private fun MyCreationsTab(creations: List<MediaItem>, onOpenDetail: (MediaItem) -> Unit) {
  if (creations.isEmpty()) {
    Box(
      modifier = Modifier.fillMaxSize().padding(bottom = 90.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("No creations generated yet", color = TextSecondary, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Go to the Create tab to synthesize artwork!", color = TextTertiary, fontSize = 12.sp)
      }
    }
  } else {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp, top = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(creations) { item ->
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(item.aspectRatio)
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianSurfaceElevated)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
            .clickable { onOpenDetail(item) }
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

@Composable
fun CreateBoardDialog(onDismiss: () -> Unit, onCreate: (String, String) -> Unit) {
  var title by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("New Inspiration Board", color = TextPrimary) },
    containerColor = ObsidianSurface,
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Board Name") },
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )
        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Description") },
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )
      }
    },
    confirmButton = {
      Button(onClick = { onCreate(title, desc) }, colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)) {
        Text("Create")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
    }
  )
}

@Composable
fun CreatePersonaDialog(onDismiss: () -> Unit, onCreate: (String, String, String) -> Unit) {
  var name by remember { mutableStateOf("") }
  var tagline by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Build Character Persona", color = TextPrimary) },
    containerColor = ObsidianSurface,
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Character Name") },
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )
        OutlinedTextField(
          value = tagline,
          onValueChange = { tagline = it },
          label = { Text("Role / Archetype") },
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )
        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Visual Traits (hair, clothing, eyes)") },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
        )
      }
    },
    confirmButton = {
      Button(onClick = { onCreate(name, tagline, desc) }, colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)) {
        Text("Save Persona", color = Color.Black)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
    }
  )
}
