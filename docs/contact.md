# Contact API Spec

## Create Contact

Endpoint: POST /api/contacts

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
  "first name" : "muhammad adlan",
  "last name" : "jazuli",
  "email" : "adlan@example.com",
  "phone" : "081590908989"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "id" : "random string",
    "first name" : "muhammad adlan",
    "last name" : "jazuli",
    "email" : "adlan@example.com",
    "phone" : "081590908989"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Email format invalid, phone format invalid, ..."
}
```

## Update Contact

Endpoint: PUT /api/contacts/{idContact}

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
  "first name" : "muhammad adlan",
  "last name" : "jazuli",
  "email" : "adlan@example.com",
  "phone" : "081590908989"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "id" : "random string",
    "first name" : "muhammad adlan",
    "last name" : "jazuli",
    "email" : "adlan@example.com",
    "phone" : "081590908989"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Email format invalid, phone format invalid, ..."
}
```

## Get Contact

Endpoint: GET /api/contacts/{idContact}

Request Header :
- X-API-TOKEN : Token (Mandatory)


Response Body (Success) :
```json
{
  "data" : {
    "id" : "random string",
    "first name" : "muhammad adlan",
    "last name" : "jazuli",
    "email" : "adlan@example.com",
    "phone" : "081590908989"
  }
}
```

Response Body (Failed, 404) :
```json
{
  "errors" : "Contact is not found"
}
```

## Search Contact

Endpoint: GET /api/contacts

Request Param :
- name : string, contact first name or last name, using like query, optional
- phone : string, contact phone, using like query, optional
- email : string, contact email, using like query, optional
- page : integer, start from 0. default 0
- size : integer, default 10

Request Header :
- X-API-TOKEN : Token (Mandatory)


Response Body (Success) :
```json
{
  "data" : [
    {
      "id" : "random string",
      "first name" : "muhammad adlan",
      "last name" : "jazuli",
      "email" : "adlan@example.com",
      "phone" : "081590908989"
    }
  ],
  "paging" : {
    "currentPage" : 0,
    "totalPage" : 10,
    "size" : 10
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Unauthorized"
}
```

## Remove Contact

Endpoint: DELETE /api/contacts/{idContact}

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"
}
```

Response Body (Failed) :
```json
{
  "errors" : "Contact is not found"  
}   
```

