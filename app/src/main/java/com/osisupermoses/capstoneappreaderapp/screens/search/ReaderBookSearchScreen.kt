package com.osisupermoses.capstoneappreaderapp.screens.search

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import com.osisupermoses.capstoneappreaderapp.components.ReaderAppBar
import com.osisupermoses.capstoneappreaderapp.navigation.ReaderScreens
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.compose.rememberImagePainter
import com.osisupermoses.capstoneappreaderapp.components.InputField
import com.osisupermoses.capstoneappreaderapp.components.showToast
import com.osisupermoses.capstoneappreaderapp.data.Resource
import com.osisupermoses.capstoneappreaderapp.model.Item

@ExperimentalComposeUiApi
//@Preview
@Composable
fun SearchScreen(navController: NavController, viewModel: BooksSearchViewModel = hiltViewModel()) {
    
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false){
//                navController.popBackStack()
                navController.navigate(ReaderScreens.ReaderHomeScreen.name)
            }
        }
    ) {
        Surface() {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){ searchQuery ->
                    viewModel.searchBook(query = searchQuery)
                }

                Spacer(modifier = Modifier.height(13.dp))

                BookList(navController)

            }

        }

    }

}

@Composable
fun BookList(navController: NavController, viewModel: BooksSearchViewModel = hiltViewModel()) {

    val listOfBooks = viewModel.list
    if (viewModel.isLoading) {
        Row {
            LinearProgressIndicator(modifier = Modifier.padding(10.dp))
            Text(text = "Loading...")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)) {
            items(items = listOfBooks) { book ->
                BookRow(book, navController)
            }
        }
    }
}

@Composable
fun BookRow(book: Item, navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {

        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {

            val imageUrl: String = if (book.volumeInfo.imageLinks.smallThumbnail.isEmpty()) {
                "https://th.bing.com/th/id/R.5c7521eeee964a51d8fcfd1996cc648e?rik=gMf%2f7wk7FJwhGg&riu=http%3a%2f%2fprodimage.images-bn.com%2fpimages%2f9781908556431_p0_v1_s1200x630.jpg&ehk=vPgQQuVRzmE4fZRXmcmsjTLYgKpFiqgtxngWNBXGNx8%3d&risl=&pid=ImgRaw&r=0"
            }
            else {
                book.volumeInfo.imageLinks.smallThumbnail
            }

            Image(painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxWidth()
                    .padding(end = 4.dp))
            Column {

                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)

                Text(text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text = "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text = "${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
    ) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) { searchQueryState.value.trim().isNotEmpty() }

        InputField(
            modifier = modifier,
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }
}