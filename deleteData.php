<?php

$params = [
    "IDMhs",
    "Nama",
    "Umur",
    "MyFoto1",
    "MyFoto2",
];

if (isTheseParameterAvailable($params)) {
    $id = $_POST[$params[0]];
    $nama = $_POST[$params[1]];
    $umur = $_POST[$params[2]];
    $foto1 = $_POST[$params[3]];
    $foto2 = $_POST[$params[4]];

    try {
        $path = "Foto/$foto1";
        unlink($path);

        $stmt = $conn->prepare("DELETE FROM mahasiswa WHERE nim = ?");
        $stmt->bind_param("s", $id);

        if ($stmt->execute()) {
            $response["error"] = false;
            $response["message"] = "Delete Success";
        } else {
            $response["error"] = false;
            $response["message"] = "Delete Failed";
        }
    } catch (\Exception $exception) {
        $response["error"] = false;
        $response["message"] = $exception->getMessage();
    } finally {
        $stmt->close();
    }
}
