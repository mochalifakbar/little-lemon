package com.embul.littlelemon

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@Composable
fun Home(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(context.applicationContext as Application)
    )
    val menuItems by viewModel.filteredMenuItems.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        AppBar(navController)
        Card(
            onSearch = { query ->
                viewModel.searchQuery = query
                viewModel.updateFilter()
            },
            searchQuery = viewModel.searchQuery
        )
        Content(
            menuItems = menuItems,
            selectedCategories = viewModel.selectedCategories,
            onCategorySelected = { category ->
                viewModel.selectedCategories = if (viewModel.selectedCategories.contains(category)) {
                    viewModel.selectedCategories - category
                } else {
                    viewModel.selectedCategories + category
                }
                viewModel.updateFilter()
            }
        )
    }
}

class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(application) as T
    }
}

@Composable
fun AppBar(
navController: NavHostController
) {
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.55f)
                    .height(100.dp),
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = {
                    navController.navigate(Profile.route)
                },
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.CenterEnd)
                    .size(45.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = CircleShape
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun Card(
    onSearch: (String) -> Unit,
    searchQuery: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp),
    ) {
        Column {
            Text(
                text = "Little Lemon",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Chicago",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.background,
            )
        }
        Text(
            text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.align(Alignment.CenterStart).width(230.dp)
        )

        Image(
            painter = painterResource(R.drawable.hero_image),
            contentDescription = "Restaurant",
            modifier = Modifier
                .padding(bottom = 15.dp)
                .width(150.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.CenterEnd),
            contentScale = ContentScale.Crop
        )

        SearchBar(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            searchQuery = searchQuery,
            onSearch = onSearch
        )
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearch,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colorScheme.surface
            )
        },
        placeholder = {
            Text("Enter search phrase", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.surface
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun Content(
    menuItems: List<Menu>,
    selectedCategories: Set<String>,
    onCategorySelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "ORDER FOR DELIVERY!",
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.W900,
            color = MaterialTheme.colorScheme.surface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("starters", "mains", "desserts", "drinks").forEach { category ->
                FilterButton(
                    category = category,
                    isSelected = selectedCategories.contains(category),
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        Divider(modifier = Modifier.padding(16.dp).height(2.dp))

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(menuItems) { item ->
                MenuItem(item)
                Divider(modifier = Modifier.height(1.dp))
            }
        }
    }
}

@Composable
fun FilterButton(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.background,
            contentColor = if (isSelected) MaterialTheme.colorScheme.background
            else MaterialTheme.colorScheme.primary
        ),
    ) {
        Text(category)
    }
}

@Composable
fun MenuItem(menuItem: Menu) {
    Box(
        modifier = Modifier.padding(15.dp).fillMaxWidth(),
    ) {
        Text(
            text = menuItem.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(bottom = 5.dp).align(Alignment.TopStart)
        )
        Text(
            text = menuItem.description,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.surface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 5.dp).align(Alignment.CenterStart).width(220.dp)
        )
        Text(
            text = menuItem.price,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomStart)
        )

        Image(
            painter = rememberAsyncImagePainter(menuItem.image),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.CenterEnd),
            contentScale = ContentScale.Crop
        )
    }
}