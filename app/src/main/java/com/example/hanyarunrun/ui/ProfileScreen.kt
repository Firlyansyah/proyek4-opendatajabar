package com.proyek.jtk.ui.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.hanyarunrun.viewmodel.DataViewModel
import com.example.hanyarunrun.data.ProfileEntity
import com.example.hanyarunrun.utils.saveImageToInternalStorage

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: DataViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val profileList by viewModel.profileList.observeAsState(emptyList())

    if (profileList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Belum ada profil", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("addProfile") }) {
                    Text("Buat Profile Baru")
                }
            }
        }
    } else {
        val profile = profileList.first()
        var isEditing by remember { mutableStateOf(false) }
        var profileImageUri by remember { mutableStateOf(profile.profilePicture ?: "") }

        LaunchedEffect(profile.profilePicture) {
            profileImageUri = profile.profilePicture ?: ""
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val savedPath = saveImageToInternalStorage(context, it)  // Save image to internal storage
                if (savedPath != null) {
                    profileImageUri = savedPath
                    val updatedData = profile.copy(profilePicture = savedPath)
                    viewModel.updateProfile(updatedData)
                }
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = if (profileImageUri.startsWith("/")) "file://$profileImageUri" else profileImageUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .background(Color.LightGray, CircleShape),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isEditing) {
                    var studentName by remember { mutableStateOf(profile.nama) }
                    var studentId by remember { mutableStateOf(profile.nim) }
                    var studentEmail by remember { mutableStateOf(profile.email) }

                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = studentId,
                        onValueChange = { studentId = it },
                        label = { Text("Student ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = studentEmail,
                        onValueChange = { studentEmail = it },
                        label = { Text("Student Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Upload Photo")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            isEditing = false
                            val updatedData = ProfileEntity(
                                id = profile.id,
                                profilePicture = profileImageUri,
                                nama = studentName,
                                nim = studentId,
                                email = studentEmail
                            )
                            viewModel.updateProfile(updatedData)
                            Toast.makeText(context, "Profil berhasil diupdate!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save")
                    }
                } else {
                    Text(text = profile.nama, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "ID: " + profile.nim, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = profile.email, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Edit Profil")
                    }
                }
            }
        }
    }
}
