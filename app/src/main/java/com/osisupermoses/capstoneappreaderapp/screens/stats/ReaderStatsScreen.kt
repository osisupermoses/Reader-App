package com.osisupermoses.capstoneappreaderapp.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.osisupermoses.capstoneappreaderapp.components.ReaderAppBar
import com.osisupermoses.capstoneappreaderapp.model.Item
import com.osisupermoses.capstoneappreaderapp.model.MBook
import com.osisupermoses.capstoneappreaderapp.navigation.ReaderScreens
import com.osisupermoses.capstoneappreaderapp.screens.home.HomeScreenViewModel
import com.osisupermoses.capstoneappreaderapp.screens.search.BookRow
import com.osisupermoses.capstoneappreaderapp.utils.formatDate
import java.util.*

@Composable
fun StatsScreen(navController: NavController,
                viewModel: HomeScreenViewModel = hiltViewModel()) {

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            ReaderAppBar(title = "Book Stats",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController){
                navController.popBackStack()
            }
        },
    ) {
        Surface {
            //only show books by this user that have been read
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            } else {
                emptyList()
            }

            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Person,
                            contentDescription = "Icon"
                        )
                    }
                    Text(
                        text = "Hi, ${
                            currentUser?.email.toString().split("@")[0]
                                .uppercase(Locale.getDefault())
                        }"
                    )
                }

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                    shape = CircleShape,
                    elevation = 5.dp
                ) {
                    val readBooksList: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        books.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    } else {
                        emptyList()
                    }

                    val readingBooks = books.filter { mBook ->
                        (mBook.startedReading != null) && (mBook.finishedReading == null)
                    }

                    Column(modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start) {

                        Text(text = "Your Stats", style = MaterialTheme.typography.h5)
                        Divider()
                        Text(text = "You're reading: ${readingBooks.size} books")
                        Text(text = "You're read: ${readBooksList.size} books")

                    }
                }

                if (viewModel.data.value.loading == true) {
                    Row {
                        LinearProgressIndicator(modifier = Modifier.padding(10.dp))
                        Text(text = "Loading...")
                    }
                } else {
                    Divider()
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                            contentPadding = PaddingValues(16.dp)) {
                        //filter books by finished ones
                        val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                            viewModel.data.value.data!!.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                            }
                        } else {
                            emptyList()
                        }
                        items(items = readBooks) { book ->
                            BookRowStats(book = book)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun BookRowStats(book: MBook) {
    Card(modifier = Modifier
        .clickable {}
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {

        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {

            val imageUrl: String = if (book.photoUrl.toString().isEmpty()) {
                "https://th.bing.com/th/id/R.5c7521eeee964a51d8fcfd1996cc648e?rik=gMf%2f7wk7FJwhGg&riu=http%3a%2f%2fprodimage.images-bn.com%2fpimages%2f9781908556431_p0_v1_s1200x630.jpg&ehk=vPgQQuVRzmE4fZRXmcmsjTLYgKpFiqgtxngWNBXGNx8%3d&risl=&pid=ImgRaw&r=0"
            }
            else {
                book.photoUrl.toString()
            }

            Image(painter = rememberImagePainter(data = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxWidth()
                    .padding(end = 4.dp))
            Column {

                Row(horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(text = book.title.toString(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(0.7f))
                    if (book.rating!! >= 3) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Thumb Up",
                            tint = Color.Green.copy(alpha = 0.5f))
                    } else if (book.rating!! <= 2) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(imageVector = Icons.Default.ThumbDown,
                            contentDescription = "Thumb Down",
                            tint = Color.Red.copy(alpha = 0.5f))
                    } else Box {}
                }

                Text(text = "Author: ${book.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text = "Started: ${formatDate(book.startedReading!!)}",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text = "Finished: ${formatDate(book.finishedReading!!)}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)
            }
        }
    }
}
