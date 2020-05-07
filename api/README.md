## Astra Api Manual


### Create License

<pre>

url:
{{url}}license/create/


headers:
Content-Type:application/json

body:
{
	"user_email" : "thakur@gmail.com",
	"validity" : "2020-05-30"
}


response:
{
    "license_key": "gAAAAABes1byelAc2oyu_gOKmwJSp5nIVy89sd4Ow2VUAtjXnXv_NllAYjMCPJbqFU4jtvk-Lp67_GSWQsYcN5MgXK2n0TEcoNvwsdyfmEtTesB2dT6dN1IUQ1ruYn7lG_onqMUuTdF2cHUVzrVNexbNRnRPc_O91APyLA-W4khRM5eRFFn0Ba5nhjbJYhH3If_2YxOEATdHj0cb7XokxzP-qMJWmK4rNMmlX9eqppvi6pl6sogESZMpCGXMxSLLoz6--m53Rx027rGfkl0ooF4AZgzltFjrzpGOs-3CoJHAA6yA680KVjwJXjKxYsTa7LZEAdNNKVgT",
    "user_email": "thakur@gmail.com"
}

</pre>


### Validate License

<pre>

url:
{{url}}license/validate/


headers:
Content-Type:application/json

body:
{
    "license_key": "gAAAAABes1byelAc2oyu_gOKmwJSp5nIVy89sd4Ow2VUAtjXnXv_NllAYjMCPJbqFU4jtvk-Lp67_GSWQsYcN5MgXK2n0TEcoNvwsdyfmEtTesB2dT6dN1IUQ1ruYn7lG_onqMUuTdF2cHUVzrVNexbNRnRPc_O91APyLA-W4khRM5eRFFn0Ba5nhjbJYhH3If_2YxOEATdHj0cb7XokxzP-qMJWmK4rNMmlX9eqppvi6pl6sogESZMpCGXMxSLLoz6--m53Rx027rGfkl0ooF4AZgzltFjrzpGOs-3CoJHAA6yA680KVjwJXjKxYsTa7LZEAdNNKVgT",
    "user_email": "thakur@gmail.com"
}


response:
{
    "day_left": 23,
    "valid": true
}

</pre>


### Signup Admin User

<pre>

url:
{{url}}auth/signup/

headers:
Content-Type:application/json

body:
{
	"email" : "thakur@gmail.com",
	"password" : "riasgrem",
	"license_key": "gAAAAABes1byelAc2oyu_gOKmwJSp5nIVy89sd4Ow2VUAtjXnXv_NllAYjMCPJbqFU4jtvk-Lp67_GSWQsYcN5MgXK2n0TEcoNvwsdyfmEtTesB2dT6dN1IUQ1ruYn7lG_onqMUuTdF2cHUVzrVNexbNRnRPc_O91APyLA-W4khRM5eRFFn0Ba5nhjbJYhH3If_2YxOEATdHj0cb7XokxzP-qMJWmK4rNMmlX9eqppvi6pl6sogESZMpCGXMxSLLoz6--m53Rx027rGfkl0ooF4AZgzltFjrzpGOs-3CoJHAA6yA680KVjwJXjKxYsTa7LZEAdNNKVgT"
}

response:
{
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.p1DRjHVOT55oHgO3ThJ8Vi5GxUeM3yf7wsQHvlPKets"
}   

</pre>


### SignIn Admin User

<pre>

url:
{{url}}auth/signup/

headers:
Content-Type:application/json

body:
{
	"email" : "thakur@gmail.com",
	"password" : "riasgrem",
}

response:
{
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.p1DRjHVOT55oHgO3ThJ8Vi5GxUeM3yf7wsQHvlPKets"
}   

</pre>


### add General user

<pre>
   url:
   POST {{url}}person/
   
   Headers:
   Content-Type : application/json
   
   Body:
       {
           "first_name": "subham",
           "profile_pic" : "base64 encoded data less than 100KB",
           "id_front" : "base64 encoded data less than 100KB",
           "id_back" : "base64 encoded data less than 100KB",
           "father_name" : "subodh singh",
           "username" : "gravert44",
           "dob" : "03-03-1996",
           "aadhar_id" : "155"
       }       
    
    
   Response:
   
   1 : Aadhar exists
       {
           "message": "Aadhar exists"
       } 
       
   2 : Username exists
       {
           "message": "Username exists"
       } 
       
   3 : account created
       {
           "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTN9.FeYvqrpL25ivcMsDEv3T5OSx1CmOk41QZTxKrg39N7s"
       }
    
</pre>

### Search User  by token 
 
<pre>

  url : 
  GET {{url}}person/token/
  
  headers :
  Authorization : Basic TOKEN
  
  response : 
  
  1. no user found
  {}
  
  2. user found
  
  {
        "aadhar_id": "15",
        "created": "2020-05-05T02:19:02.026488",
        "dob": "1996-03-03T00:00:00",
        "father_name": "subodh singh",
        "first_name": "subham",
        "id": 14,
        "id_back": "base64 encoded data less than 100KB",
        "id_front": "base64 encoded data less than 100KB",
        "last_name": null,
        "modified": "2020-05-05T02:19:02.026504",
        "profile_pic": "base64 encoded data less than 100KB",
        "user_id": 93,
        "username": "gravert3"
   }
  
</pre> 
  
### Search user by name, father name and dob

<pre>

url:
{{url}}person/fuzzy/

header:
Content-Type : application/json

body:

1. if found

{
    "aadhar_id": "12",
    "created": "2020-05-05T01:51:17.161157",
    "dob": "1996-03-03T00:00:00",
    "father_name": "subodh singh",
    "first_name": "subham",
    "id": 13,
    "id_back": "base64 encoded data less than 100KB",
    "id_front": "base64 encoded data less than 100KB",
    "last_name": null,
    "modified": "2020-05-05T01:51:17.161172",
    "profile_pic": "base64 encoded data less than 100KB",
    "user_id": 92,
    "username": "gravert2"
}

2. not found
{
    message : "error message
}

</pre>


### Search General user by username

<pre>

url:
{{url}}person/username/{{your_username}}

header:
Content-Type : application/json
Authorization : Basic eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6OTN9.ss88a-eV4uizJuOC398i2w4TMH2-47g4XFAARp_A6uk


response:
{
    "aadhar_id": "155",
    "created": "2020-05-07T06:42:30.596568",
    "dob": "1996-03-03",
    "father_name": "subodh singh",
    "first_name": "subham",
    "id": 1,
    "id_back": "base64 encoded data less than 100KB",
    "id_front": "base64 encoded data less than 100KB",
    "last_name": null,
    "modified": "2020-05-07T06:42:30.596587",
    "profile_pic": "base64 encoded data less than 100KB",
    "user_id": 3,
    "username": "gravert44"
}

</pre>


### Create IN or OUT

url:
{{url}}activity/

headers : 
Content-Type:application/json
Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.p1DRjHVOT55oHgO3ThJ8Vi5GxUeM3yf7wsQHvlPKets

body:
{
	"user_id" : 1,
	"person_id" : 3,
	"location" : "12.22.22,22.2.2",
	"type" : 1
}

response :
{
    "message" : saved
}
