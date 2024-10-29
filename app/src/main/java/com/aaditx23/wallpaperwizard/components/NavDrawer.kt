package com.aaditx23.wallpaperwizard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.wallpaperwizard.R
import com.aaditx23.wallpaperwizard.components.models.NavDrawerItem


@Composable
fun NavDrawer(
    scrollState: ScrollState,
    selectedIndex: Int,
    onClick: (item: NavDrawerItem) -> Unit
){
    val items = NavDrawerItem.navDrawerItems
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .width(250.dp)
    ) {


        Text(
            text = "BUCC",
            color = Color(28, 48, 92),
            modifier = Modifier
                .padding(start = 16.dp, end = 20.dp, bottom = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )


        items.forEachIndexed{ index, item ->
            when {
                item.isDivider -> {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(bottom = 20.dp, top = 20.dp)
                    )
                }

                item.isHeader -> {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                    )
                }

                else -> {
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = selectedIndex == index,
                        onClick = { onClick(item) },
                        icon = {
                            if (selectedIndex == index){
                                Icon(imageVector = item.selectedIcon!!, contentDescription = item.title)
                            }
                            else{
                                Icon(imageVector = item.unselectedIcon!!, contentDescription = item.title)
                            }
                        }
                    )
                }
            }

        }
    }
}