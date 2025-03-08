package com.example.hanyarunrun.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.compose.runtime.livedata.observeAsState
import com.example.hanyarunrun.data.DataEntity
import com.example.hanyarunrun.viewmodel.DataViewModel
import com.example.hanyarunrun.ui.theme.*

@Composable
fun CreatedDataListScreen(navController: NavHostController, viewModel: DataViewModel) {
    val dataList by viewModel.dataList.observeAsState(emptyList())

    if (dataList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Tidak ada data", style = MaterialTheme.typography.headlineMedium)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dataList) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Provinsi: ${item.namaProvinsi}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kota/Kabupaten: ${item.namaKabupatenKota}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Jumlah Kebakaran: ${item.jumlahKebakaran} ${item.satuan}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tahun: ${item.tahun}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("edit/${item.id}")
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue,
                                    contentColor = White
                                )
                            ) {
                                Text(text = "Edit")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = {
                                    navController.navigate("delete/${item.id}")
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Red,
                                    contentColor = White
                                )
                            ) {
                                Text(text = "Hapus")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationScreen(itemId: Int, data: DataEntity?, viewModel: DataViewModel, navController: NavHostController) {
    // Konfirmasi penghapusan
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = { Text("Hapus Data") },
        text = { Text("Apakah anda yakin ingin menghapus data ini?") },
        confirmButton = {
            Button(onClick = {
                viewModel.deleteDataById(itemId)
                viewModel.deleteCreatedData(
                    data?.namaKabupatenKota ?: "",
                    data?.tahun.toString(),
                    data?.jumlahKebakaran ?: 0
                )
                navController.popBackStack()
            }) {
                Text("Ya")
            }
        },
        dismissButton = {
            Button(onClick = { navController.popBackStack() }) {
                Text("Tidak")
            }
        }
    )
}
