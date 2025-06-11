# Web Services Module

### Database
```sql
CREATE TABLE mahasiswa (
    nim VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(255),
    umur VARCHAR(10),
    foto VARCHAR(255)
);
```

## Params Available
`IDMhs`
`nim`
`nama`
`umur`
`foto1`
`foto2`

## Api Calls
### Insert

**Curl**
```cpp
curl.exe -X POST -F "IDMhs=01" -F "nama=John" -F "umur=30" -F "foto1=anjay.jpg" http://localhost/maintenance/api.php?apicall=insertData
```

**Powershell**
```cpp
$uri = "http://localhost/maintenance/api.php?apicall=insertData"
$formParams = @{
    IDMhs = "01"
    nama  = "John"
    umur  = "30"
    foto1 = "anjay.jpg"
}

Invoke-WebRequest -Uri $uri -Method POST -Body $formParams -ContentType "application/x-www-form-urlencoded"
```
---
### Update
---
### Delete
---
### Upload Files