# Meals API
_Replace `{URL}` with your url to page, `{RID}` with restaurant id that you need and `{ID}` with meal id that you need._

## URL Pages
Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ----
Get All | GET | `{URL}/meals/all/{RID}` | none | Everyone
Get All By Date | GET | `{URL}/meals/all/{RID}?date=2019-08-06` | none | Everyone
Get All For Today | GET | `{URL}/meals/today/{RID}` | none | Everyone
Get | GET | `{URL}/meals/{ID}` | none | Everyone
Get With Restaurant | GET | `{URL}/meals/with/{ID}` | none | Everyone
Delete | DELETE  | `{URL}/meals/{ID}` | none | Admin Only
Update | PUT | `{URL}/meals/{ID}` | Update Body | Admin Only
Create | POST | `{URL}/meals` | Create Body | Admin Only
Create All | POST | `{URL}/meals/all` | Create All Body | Admin Only

## CURL
_Replace `admin@gmail.com:admin` with user that you need, `106` with meal id that you need and `103` with restaurant id that you need._

### Get All
`curl -s {URL}/meals/all/103`

### Get All By Date
`curl -s {URL}/meals/all/103?date=2019-08-06`

_Replace `2019-08-06` with date that you need._

### Get All For Today
`curl -s {URL}/meals/today/103`

### Get Meal By ID
`curl -s {URL}/meals/106`

### Get By ID With Restaurant
`curl -s {URL}/meals/with/106`

### Delete By ID
`curl -s -X DELETE {URL}/meals/106 --user admin@gmail.com:admin`

### Update Meal
`curl -s -X PUT -d '{"id": 106,"name": "Other Name","price": 40, "restaurantId": 103}' -H 'Content-Type: application/json' {URL}/meals/106 --user admin@gmail.com:admin`

### Create Meal
`curl -s -X POST -d '{"name": "New Meal 1","price": 30, "restaurantId": 103}' -H 'Content-Type: application/json;charset=UTF-8' {URL}/meals --user admin@gmail.com:admin`

### Create Many Meal
`curl -s -X POST -d '[{"name": "New Meal 1","price": 30, "restaurantId": 103},{"name": "New Meal 2","price": 40, "restaurantId": 103}]' -H 'Content-Type: application/json;charset=UTF-8' {URL}/meals/all --user admin@gmail.com:admin`

## Bodies
### Create Body
```json
{
    "name": "New Meal 1",
    "price": 30,
    "restaurantId": {RID}
}
```

### Update Body
```json
{
    "id": {ID},
    "name": "New Meal 1",
    "price": 30,
    "restaurantId": {RID}
}
```

### Create All Body
```json
[
	{
	    "name": "New Meal 1",
	    "price": 30,
	    "restaurantId": {RID}
	},
	{
	    "name": "New Meal 2",
	    "price": 40,
	    "restaurantId": {RID}
	}
]
```