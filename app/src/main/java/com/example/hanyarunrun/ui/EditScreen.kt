package com.example.hanyarunrun.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hanyarunrun.data.DataEntity
import com.example.hanyarunrun.viewmodel.DataViewModel

private const val s = "Pilih Tahun"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    navController: NavHostController,
    viewModel: DataViewModel,
    dataId: Int
) {
    val context = LocalContext.current

    var namaProvinsi by remember { mutableStateOf("") }
    var namaKabupatenKota by remember { mutableStateOf<String?>(null) }
    var jumlahKebakaran by remember { mutableStateOf("") }
    var jumlahKebakaranLama by remember { mutableStateOf("") }
    var satuan by remember { mutableStateOf("") }
    var tahun by remember { mutableStateOf("") }

    val allBencana by viewModel.bencanaList.observeAsState(initial = emptyList())
    val kabupatenList by remember(allBencana) {
        mutableStateOf(allBencana.map { it.nama_kabupaten_kota }.distinct())
    }
    val tahunList = List(31) { it + 2000 }

    var selectedKabupaten by remember { mutableStateOf<String?>(null) }
    var selectedTahun by remember { mutableStateOf<String?>(null) }

    var expandedKabupaten by remember { mutableStateOf(false) }
    var expandedTahun by remember { mutableStateOf(false) }

    val tahunInt = tahun.toIntOrNull() ?: 0

    LaunchedEffect(dataId) {
        viewModel.getDataById(dataId)?.let { data ->
            namaProvinsi = data.namaProvinsi
            namaKabupatenKota = data.namaKabupatenKota
            jumlahKebakaran = data.jumlahKebakaran.toString()
            satuan = data.satuan
            tahun = data.tahun.toString()
            jumlahKebakaranLama = jumlahKebakaran
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Data",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            ExposedDropdownMenuBox(
                expanded = expandedKabupaten,
                onExpandedChange = { expandedKabupaten = it }
            ) {
                OutlinedTextField(
                    value = selectedKabupaten ?: namaKabupatenKota ?: "",
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
            ExposedDropdownMenuBox(
                expanded = expandedTahun,
                onExpandedChange = { expandedTahun = it }
            ) {
                OutlinedTextField(
                    value = selectedTahun ?: tahun ?: "",
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
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.updateDataWithCheck(
                        dataId = dataId,
                        selectedKabupaten = selectedKabupaten,
                        namaKabupatenKota = namaKabupatenKota,
                        selectedTahun = selectedTahun,
                        satuan = satuan,
                        tahunInt = tahunInt,
                        jumlahKebakaran = jumlahKebakaran,
                        jumlahKebakaranLama = jumlahKebakaranLama,
                        context = context,
                        navController = navController
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update Data")
            }
        }
    }
}
