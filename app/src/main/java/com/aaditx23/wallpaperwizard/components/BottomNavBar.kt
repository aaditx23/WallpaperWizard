package com.aaditx23.wallpaperwizard.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aaditx23.wallpaperwizard.components.models.BottomNavItem
import com.aaditx23.wallpaperwizard.components.models.BottomNavItem.Companion.bottomNavItemList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    items: List<BottomNavItem> =  bottomNavItemList,
    selectedIndex: Int,
    onItemSelcted: (Int) -> Unit,
    fabOnClick: () -> Unit
){
    var navSelect by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier.height(120.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    val idx = items.indexOf(item)
                    onItemSelcted(idx)
                    navSelect = idx
                },
                label = {
                    if(item.title!= "Empty"){ Text(text = item.title) }
                },
                alwaysShowLabel = true,
                icon = {
                    BadgedBox(
                        badge = {

                        }
                    ) {
                        if(item.title != "Empty"){
                            Icon(
                                imageVector =
                                if (index == navSelect) item.selectedIcon
                                else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                        else{
                            ElevatedCard(
                                onClick = fabOnClick,
                                elevation = CardDefaults.cardElevation(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Add",
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .size(40.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}


