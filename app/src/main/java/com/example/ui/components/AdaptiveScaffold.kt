package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppThemePreset
import com.example.ui.theme.NeonPurpleLight
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceHigh
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.AppTab

data class NavItem(
    val tab: AppTab,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val navigationItems = listOf(
    NavItem(AppTab.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem(AppTab.CALENDAR, "Calendar", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    NavItem(AppTab.DISCOVERY, "Discovery", Icons.Filled.Explore, Icons.Outlined.Explore),
    NavItem(AppTab.SOCIAL, "Social", Icons.Filled.Forum, Icons.Outlined.Forum),
    NavItem(AppTab.LIBRARY, "Library", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary),
    NavItem(AppTab.SETTINGS, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
)

@Composable
fun AdaptiveScaffold(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (isTablet: Boolean) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBackground)
    ) {
        val isTablet = maxWidth >= 600.dp

        if (isTablet) {
            // Tablet: Persistent Left NavigationRail + Content
            Row(modifier = Modifier.fillMaxSize()) {
                TabletNavigationRail(
                    currentTab = currentTab,
                    onTabSelected = onTabSelected
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    content(true)
                }
            }
        } else {
            // Phone: Fullscreen Content + Floating Translucent Glassmorphic Bottom Bar
            Box(modifier = Modifier.fillMaxSize()) {
                content(false)

                FloatingGlassmorphicBottomBar(
                    currentTab = currentTab,
                    onTabSelected = onTabSelected,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun TabletNavigationRail(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    val preset = LocalAppThemePreset.current

    NavigationRail(
        containerColor = preset.cards,
        contentColor = TextPrimary,
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
            ) {
                // Brand Crest Icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(preset.glow, preset.glow.copy(alpha = 0.6f))
                            )
                        )
                        .glowingBorder(
                            shape = RoundedCornerShape(12.dp),
                            glowColor = preset.glow,
                            glowRadius = 8.dp,
                            glowAlpha = 0.35f
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "黒", // Kuro / Black Kanji
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "SLATE",
                    color = preset.glow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            }
        },
        modifier = Modifier
            .fillMaxHeight()
            .border(
                width = 1.dp,
                color = preset.border.copy(alpha = 0.5f),
                shape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp)
            )
            .glowingBorder(
                shape = RoundedCornerShape(0.dp),
                glowColor = preset.glow,
                glowRadius = 8.dp,
                glowAlpha = 0.18f
            )
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        navigationItems.forEach { item ->
            val selected = currentTab == item.tab
            NavigationRailItem(
                selected = selected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = TextMuted,
                    selectedTextColor = preset.glow,
                    unselectedTextColor = TextMuted,
                    indicatorColor = preset.glow
                ),
                modifier = Modifier
                    .testTag("nav_${item.tab.name.lowercase()}")
                    .padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Profile Avatar Indicator at bottom of rail
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(36.dp)
                .clip(CircleShape)
                .border(2.dp, preset.glow, CircleShape)
                .background(SlateSurfaceHigh),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AS",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FloatingGlassmorphicBottomBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val preset = LocalAppThemePreset.current

    Box(
        modifier = modifier
            .testTag("floating_bottom_bar")
            .fillMaxWidth()
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color.Black.copy(alpha = 0.7f)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(preset.cards.copy(alpha = 0.88f))
            .border(1.dp, preset.border.copy(alpha = 0.6f), RoundedCornerShape(28.dp))
            .glowingBorder(
                shape = RoundedCornerShape(28.dp),
                glowColor = preset.glow,
                glowRadius = 12.dp,
                glowAlpha = 0.30f
            )
            .height(64.dp)
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEach { item ->
                val selected = currentTab == item.tab
                val itemColor by animateColorAsState(
                    targetValue = if (selected) preset.glow else Color(0x9964748B),
                    label = "itemColor"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(item.tab) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = itemColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = item.title.uppercase(),
                        color = itemColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                }
            }
        }
    }
}
