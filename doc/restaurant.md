# Restaurant API
_Replace `{URL}` with your url to page and `{ID}` with restaurant id that you need._

## URL Pages
Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ---------
Get All | GET | `{URL}/restaurant/all` | none | Everyone
Get All By Date | GET | `{URL}/restaurant?date=2019-08-06` | none | Everyone
Get All For Today | GET | `{URL}/restaurant` | none | Everyone
Get | GET | `{URL}/restaurant/{ID}` | none | Everyone
Delete | DELETE  | `{URL}/restaurant/{ID}` | none | Admin Only
Create | POST | `{URL}/restaurant` | Create Body | Admin Only
Update | PUT | `{URL}/restaurant/{ID}` | Update Body | Admin Only


## CURL
_Replace `admin@gmail.com:admin` with user that you need, `Restaurant Name` with restaurant name that you need and `103` with restaurant id that you need._

### Get All Restaurants
`curl -s {URL}/restaurant/all`

### Get All By Date
`curl -s {URL}/restaurant?date=2019-08-06`

_Replace `2019-08-06` with date that you need_

### Get All For Today
`curl -s {URL}/restaurant`

### Get Restaurant By ID
`curl -s {URL}/restaurant/103`

### Delete Restaurant By ID
`curl -s -X DELETE {URL}/restaurant/103 --user admin@gmail.com:admin`

### Create Restaurant
`curl -s -X POST -d '{"name": "Restaurant Name"}' -H 'Content-Type: application/json;charset=UTF-8' {URL}/restaurant --user admin@gmail.com:admin`

### Update Restaurant
`curl -s -X PUT -d '{"id": 103, "name": "Restaurant Name"}' -H 'Content-Type: application/json' {URL}/restaurant/103 --user admin@gmail.com:admin`

## Bodies
_Replace `{RNAME}` with restaurant name_
### Create Body
```json
{
    "name": "{RNAME}"
}
```

### Update Body
```json
{
    "id": 103,
    "name": "{RNAME}"
}
```