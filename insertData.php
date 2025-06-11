<?php

$params = [
    "IDMhs",
    "nama",
    "umur",
    "foto1",
    "foto2",
];

if (isTheseParameterAvailable($params)) {
    $id = $_POST[$params[0]];
    $nama = $_POST[$params[1]];
    $umur = $_POST[$params[2]];
    $foto1 = $_POST[$params[3]];
    $foto2 = $_POST[$params[4]];

    try {
        $stmt = $conn->prepare("INSERT INTO mahasiswa VALUES (?, ?, ?, ?)");
        $stmt->bind_param("ssis", $id, $nama, $umur, $foto1);

        if ($stmt->execute()) {
            $response["error"] = false;
            $response["message"] = "Insert Success";
        } else {
            $response["error"] = true;
            $response["message"] = "Insert Failed";
        }
    } catch (\Exception $exception) {
        $response["error"] = false;
        $response["message"] = $exception->getMessage();
    } finally {
        $stmt->close();
    }
}
