# Vote API
_Replace `{URL}` with your url to page, `{ID}` with vote id that you need and `{RID}` with restaurant id that you need._

## URL Pages
Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ---------
Get All | GET | `{URL}/vote` | none | Authorized
Get | GET | `{URL}/vote/{ID}` | none | Authorized
Delete | DELETE  | `{URL}/vote/{ID}` | none | Authorized
Create | POST | `{URL}/vote` | Create Body | Authorized
Update | PUT | `{URL}/vote/{ID}` | Update Body | Authorized


## CURL
_Replace `admin@gmail.com:admin` with user that you need, `113` with vote id that you need and `102` with restaurant id that you need._

### Get All Votes
`curl -s {URL}/vote --user admin@gmail.com:admin`

### Get Vote By ID
`curl -s {URL}/vote/113 --user admin@gmail.com:admin`

### Delete Vote By ID
`curl -s -X DELETE {URL}/vote/113 --user admin@gmail.com:admin`

### Create Vote
`curl -s -X POST -d '{"restaurantId": 102}' -H 'Content-Type: application/json;charset=UTF-8' {URL}/vote --user admin@gmail.com:admin`

### Update Vote
`curl -s -X PUT -d '{"id": 113, "restaurantId": 102}' -H 'Content-Type: application/json' {URL}/vote/113 --user admin@gmail.com:admin`

## Bodies
### Create Body
```json
{
    "restaurantId": {RID}
}
```

### Update Body
```json
{
    "id": {ID},
    "restaurantId": {RID}
}
```