package com.example.hanyarunrun.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hanyarunrun.viewmodel.DataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataEntryScreen(navController: NavHostController, viewModel: DataViewModel) {
    val context = LocalContext.current
    var jumlahKebakaran by remember { mutableStateOf("") }
    var tahun by remember { mutableStateOf<String?>(null) }

    val allBencana by viewModel.bencanaList.observeAsState(initial = emptyList())
    val kabupatenList by remember(allBencana) {
        mutableStateOf(allBencana.map { it.nama_kabupaten_kota }.distinct())
    }
    val tahunList = List(31) { it + 2000 }

    var selectedKabupaten by remember { mutableStateOf<String?>(null) }
    var selectedTahun by remember { mutableStateOf<String?>(null) }

    var expandedKabupaten by remember { mutableStateOf(false) }
    var expandedTahun by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Input Data Kebakaran di Jawa Barat",
                style = MaterialTheme.typography.headlineMedium
            )

            // Dropdown untuk Kabupaten/Kota
            ExposedDropdownMenuBox(
                expanded = expandedKabupaten,
                onExpandedChange = { expandedKabupaten = it }
            ) {
                OutlinedTextField(
                    value = selectedKabupaten ?: "Pilih Kabupaten/Kota",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKabupaten) }
                )

                ExposedDropdownMenu(
                    expanded = expandedKabupaten,
                    onDismissRequest = { expandedKabupaten = false }
                ) {
                    kabupatenList.forEach { kabupaten ->
                        DropdownMenuItem(
                            text = { Text(kabupaten) },
                            onClick = {
                                selectedKabupaten = kabupaten
                                expandedKabupaten = false
                            }
                        )
                    }
                }
            }

            // Dropdown untuk Tahun
            ExposedDropdownMenuBox(
                expanded = expandedTahun,
                onExpandedChange = { expandedTahun = it }
            ) {
                OutlinedTextField(
                    value = selectedTahun ?: "Pilih Tahun",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTahun) }
                )

                ExposedDropdownMenu(
                    expanded = expandedTahun,
                    onDismissRequest = { expandedTahun = false }
                ) {
                    tahunList.forEach { tahun ->
                        DropdownMenuItem(
                            text = { Text(tahun.toString()) },
                            onClick = {
                                selectedTahun = tahun.toString()
                                expandedTahun = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = jumlahKebakaran,
                onValueChange = { jumlahKebakaran = it },
                label = { Text("Jumlah Kebakaran") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.insertData(
                        namaProvinsi = "Jawa Barat",
                        namaKabupatenKota = selectedKabupaten ?: "",
                        jumlahKebakaran = jumlahKebakaran,
                        satuan = "KEJADIAN",
                        tahun = selectedTahun ?: ""
                    )
                    viewModel.inserOrUpdateBencana(
                        namaKabupatenKota = selectedKabupaten ?: "",
                        jumlahKebakaran = jumlahKebakaran.toIntOrNull() ?: 0,
                        satuan = "KEJADIAN",
                        tahun = selectedTahun ?: ""
                    )
                    Toast.makeText(context, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    navController.navigate("created_list") {
                        popUpTo("form") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Data")
            }
        }
    }
}

