<?php

require_once "koneksi.php";

$apicall = $_GET["apicall"];
$response = [];

if (isset($apicall)) {
    switch ($apicall) {
        case "loadData":
            require_once "loadData.php";
            break;

        case "insertData":
            require_once "insertData.php";
            break;

        case "uploadData":
            require_once "uploadData.php";
            break;

        case "deleteData":
            require_once "deleteData.php";
            break;

        case "upload":
            require_once "uploadImage.php";
            break;

        default:
            $response["error"] = true;
            $response["message"] = "Invalid Operation Called";
            break;
    }
} else {
    $response["error"] = true;
    $response["message"] = "Invalid API Call";
}

echo json_encode($response);
function isTheseParameterAvailable($params)
{
    foreach ($params as $param) {
        if (!isset($_POST[$param])) {
            return false;
        }
    }

    return true;
}
