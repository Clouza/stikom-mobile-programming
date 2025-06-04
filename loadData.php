<?php

$id = "IDMhs";
if (isTheseParameterAvailable([$id])) {
    $id = $_POST[$id];

    if (strcmp($id, "Kosong") == 0) {
        $stmt = $conn->prepare("SELECT * FROM mhs");
    } else {
        $stmt = $conn->prepare("SELECT * FROM mhs WHERE nim = ?");
        $stmt->bind("s", $id);
    }
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($nim, $nama, $umur, $foto);
        $dataMahasiswa = [];

        while ($stmt->fetch()) {
            $tempData = [];
            $tempData["nim"] = $nim;
            $tempData["nama"] = $nama;
            $tempData["umur"] = $umur;
            $tempData["foto"] = $foto;
            array_push($dataMahasiswa, $tempData);
        }

        $response["error"] = false;
        $response["message"] = "Success";
        $response["data"] = $dataMahasiswa;
    } else {
        if (strcmp($id, "Kosong") == 0) {
            $response["message"] = "Data Tidak Ada";
        } else {
            $response["message"] = "Data Tidak Ada dengan ID: $id";
        }
    }
}
