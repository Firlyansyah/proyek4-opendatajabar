package com.example.hanyarunrun.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.hanyarunrun.R
import com.example.hanyarunrun.viewmodel.DataViewModel
import androidx.compose.ui.unit.dp
import com.example.hanyarunrun.utils.saveImageToInternalStorage

@Composable
fun AddProfileScreen(navController: NavHostController, viewModel: DataViewModel) {

    val context = LocalContext.current
    var studentName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var studentEmail by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gambar Profil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_placeholder), // Ganti dengan gambar default
                        contentDescription = "Placeholder",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Tombol Pilih Gambar
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pilih Gambar")
            }

            // Input Data Profil
            OutlinedTextField(
                value = studentName,
                onValueChange = { studentName = it },
                label = { Text("Student Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("Student ID") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = studentEmail,
                onValueChange = { studentEmail = it },
                label = { Text("Student Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tombol Simpan
            Button(
                onClick = {
                    val imageFilePath = saveImageToInternalStorage(context, imageUri)
                    viewModel.insertProfile(
                        nama = studentName,
                        nim = studentId,
                        email = studentEmail,
                        profilePicture = imageFilePath // Simpan URI gambar yang sudah disalin ke internal storage
                    )
                    Toast.makeText(context, "Profil berhasil dibuat!", Toast.LENGTH_SHORT).show()
                    navController.navigate("profile")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
