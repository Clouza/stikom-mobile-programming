<?php

$server = "localhost";
$user = "root";
$password = "";
$database = "mhs";

$conn = new mysqli($server, $user, $password, $database);
if ($conn->connect_error) {
    die($conn->connect_error);
}
