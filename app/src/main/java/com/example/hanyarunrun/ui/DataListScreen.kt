package com.example.hanyarunrun.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.hanyarunrun.viewmodel.DataViewModel
import com.example.hanyarunrun.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataListScreen(navController: NavHostController, viewModel: DataViewModel) {

    val allBencana by viewModel.bencanaList.observeAsState(initial = emptyList())

    val kabupatenList by remember(allBencana) {
        mutableStateOf(allBencana.map { it.nama_kabupaten_kota }.distinct())
    }

    var selectedKabupaten by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Data Kebakaran di Jawa Barat",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Text(
            text = "Pilih Kabupaten/Kota",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Dropdown Menu
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedKabupaten ?: "Pilih Kabupaten/Kota",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                kabupatenList.forEach { kabupaten ->
                    DropdownMenuItem(
                        text = { Text(kabupaten) },
                        onClick = {
                            selectedKabupaten = kabupaten
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tampilkan tabel hanya jika kabupaten/kota dipilih
        if (selectedKabupaten != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Kejadian Kebakaran di\n$selectedKabupaten",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Tahun",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Kasus",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }

                LazyColumn {
                    items(allBencana.filter { it.nama_kabupaten_kota == selectedKabupaten && it.jumlah_kebakaran > 0 }) { bencana ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Black)
                                .padding(8.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = bencana.tahun.toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically), // Untuk memastikan teks berada di tengah vertikal
                                textAlign = TextAlign.Center // Menambahkan TextAlign untuk teks rata tengah secara horizontal
                            )
                            Text(
                                text = bencana.jumlah_kebakaran.toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

