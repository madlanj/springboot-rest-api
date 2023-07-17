# Uer API Spec

## Register User
Endpoint : POST /api/users

Request Body :
```json
{ 
  "username" : "adlan",
  "password" : "adlan",
  "name" : "Adlan Jazuli"
}
```

Response Body (Success) :
```json
{
  "data" : "OK"
}
```

Response Body (Failed) :
```json
{
  "errors" : "Username must not blank, ???"
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body :

```json
{ 
  "username" : "adlan",
  "password" : "adlan"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 123123131 // millisecond
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Username or Password wrong"
}
```

## Get User

Endpoint : GET /api/users/current

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : {
    "username" : "adlan",
    "name" : "Adlan Jazuli"
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors" : "Unauthorized"
}
```

## Update User


Endpoint : PATCH /api/users/current

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{ 
  "name" : "Muhammad Adlan Jazuli", // put if only want to update
  "password" : "new password" // put if only want to update
}
```

Response Body (Success) :
```json
{
  "data" : {
    "username" : "adlan",
    "name" : "Adlan Jazuli"
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors" : "Unauthorized"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"
}
```