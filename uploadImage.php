<?php

if (isTheseParameterAvailable(["filename"])) {
    if (isset($_FILES["file"])) {
        $target_dir = "Foto/";
        $filename = $_POST["filename"];
        $filename_upload = $_FILES["file"]["name"];

        $fileInfo = pathinfo($filename_upload);
        $file_extension = $fileInfo["extension"];
        $target_file_name = $target_dir . $filename . "." . $file_extension;

        $response = [];
        if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file_name)) {
            $error = false;
            $message = "Success Upload";
        } else {
            $error = true;
            $message = "Error while uploading";
        }

        $response["error"] = $error;
        $response["message"] = $message;
    }
}
