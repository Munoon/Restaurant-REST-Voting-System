# Admin API
_Replace `{URL}` with your url to page, `{EMAIL}` with user email that you need, `{ID}` with user id that you need, `{UID}` with user id that you need and `{VDATE}` with vote date that you need._

## URL Pages
Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ----
Get All Profiles | GET | `{URL}/admin/users` | none | Admin Only
Get Profile | GET | `{URL}/admin/users/{ID}` | none | Admin Only
Create Profile | POST | `{URL}/admin/users` | Create Body | Admin Only
Delete Profile | DELETE | `{URL}/admin/users/{ID}` | none | Admin Only
Update Profile | PUT | `{URL}/admin/users/{ID}` | Update Body | Admin Only
Get Profile By Email | GET | `{URL}/admin/users/by?email={EMAIL}` | none | Admin Only
Get All Votes | GET | `{URL}/admin/users/votes` | none | Admin Only
Get All Votes By User Id | GET | `{URL}/admin/users/votes/{UID}` | none | Admin Only
Get All Votes By User Id And Date | GET | `{URL}/admin/users/votes/{UID}?date={VDATE}` | none | Admin Only

## CURL
_Replace `admin@gmail.com:admin` with user that you need, `admin@gmail.com` with user email that you need, `2019-07-01` with date that you need and `101` with user id that you need._

### Get All Profiles
`curl -s {URL}/admin/users --user admin@gmail.com:admin`

### Get Profile
`curl -s {URL}/admin/users/101 --user admin@gmail.com:admin`

### Create Profile
`curl -s -X POST -d '{"id": null,"name": "New User","email": "newEmail2@gmail.com","password": "stupidPass","registered": "2019-07-03T10:00:00","enabled": true,"roles": ["ROLE_USER"]}' -H 'Content-Type: application/json;charset=UTF-8' {URL}/admin/users --user admin@gmail.com:admin`

### Update Profile
`curl -s -X PUT -d '{"id": 101,"name": "Another Name","email": "SecondPetrsEmail@gmail.com","password": "PertHasStrongPass123","registered": "2019-07-03T10:00:00","enabled": true,"roles": ["ROLE_USER"]}' -H 'Content-Type: application/json' {URL}/admin/users/101 --user admin@gmail.com:admin`

### Get Profile By Email
`curl -s {URL}/admin/users/by?email=admin@gmail.com --user admin@gmail.com:admin`

### Get All Votes
`curl -s {URL}/admin/users/votes/ --user admin@gmail.com:admin`

### Get All Votes By User Id
`curl -s {URL}/admin/users/votes/101 --user admin@gmail.com:admin`

### Get All Votes By User Id And Date
`curl -s {URL}/admin/users/votes/101?date=2019-07-01 --user admin@gmail.com:admin`

## Bodies
### Create Body
```json
{
    "id": null,
    "name": "New User",
    "email": "newEmail2@gmail.com",
    "password": "stupidPass",
    "registered": "2019-07-03T10:00:00",
    "enabled": true,
    "roles": [
        "ROLE_USER"
    ]
}
```

### Update Body
```json
{
    "id": {ID},
    "name": "Another Name",
    "email": "SecondPetrsEmail@gmail.com",
    "password": "PertHasStrongPass123",
    "registered": "2019-07-03T10:00:00",
    "enabled": true,
    "roles": [
        "ROLE_USER"
    ]
}
```