# Profile API
_Replace `{URL}` with your url to page._

## URL Pages
Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ----
Get Profile | GET | `{URL}/profile` | none | Authorized
Update Profile | PUT | `{URL}/profile` | Update Body | Authorized
Delete Profile | DELETE | `{URL}/profile` | none | Authorized
Register Profile | POST | `{URL}/profile/register` | Register Body | Not Authorized

## CURL
_Replace `admin@gmail.com:admin` with user that you need._

### Get Profile
`curl -s {URL}/profile --user admin@gmail.com:admin`

### Update Profile
`curl -s -X PUT -d '{"name": "Other Name","email": "admin@gmail.com","password": "hardPass"}' -H 'Content-Type: application/json' {URL}/profile --user admin@gmail.com:admin`

### Delete Profile
`curl -s -X DELETE {URL}/profile --user admin@gmail.com:admin`

### Register Profile
`curl -s -X POST -d '{"name": "New Profile","email": "newEmail@gmail.com","password": "newPassword"}' -H 'Content-Type: application/json;charset=UTF-8' {URL}/profile/register`

## Bodies
### Register Body
```json
{
    "name": "New User",
    "email": "newUser@gmail.com",
    "password": "12345"
}
```

### Update Body
```json
{
    "name": "Nikita",
    "email": "admin@gmail.com",
    "password": "hardPass"
}
```