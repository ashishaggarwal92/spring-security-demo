# spring-security-demo




![image](https://github.com/user-attachments/assets/a76624cf-e177-4e1b-9f36-6773834b1945)

PKCE - Proof key for key exchange
 -- to prevent cross site request forgery


Before PKCE we have implicit grant flow


PKCE - Proof key for key exchange
 -- to prevent cross site request forgery


Before PKCE we have implicit grant flow


1) User login to any client application(React)
2) Client app generates code challenger and verifier

Use PKCE code generator library(OAuth2 playground) to generate code challenger and code verifier which uses sha 256
Code-Challenge-Method = S256

Code challenge will be used in later step to be verified with code verifier

3) Client will make a request to OAuth server using /authorize endpoint
- Client will send code challenge only (no code verifier) and code challeneg method
- Client can also pass state also for CSRF attack
- Client also give redirect uri and response type

When authorization server receive this /authorize request, it will do:

- Store redirect uri, code challeneg, state, code challeneg method

4) Auth server will Present login page to the user for user name and passowrd authentication
- This login page is owned by authorization server
-

5) User enters user name and password and provide consent
- consent is (authorize via google/github) where it fetch user details (like profile email, public data)

6) After receiving these user details - authorization server sends 
- authorization code and state to client
- State is same which we receive from client in step 3


7) Client got authorization code and state
Authorization Code - this is some type of token, later of time you can trade this token with access token
We haven't receive access token, we receive authorization code in this step

State - same step which client send with /authorize, this will be match from client side. If state matches
it means we are communication with right authorization server.

8) Client will send /token request to auth server along with:
- authorization code
- code verifier(here we are only sending code verifier no code challenge)
this requires in order to verify the same person send teh code challnege and now he want to get the access token. Very very curicial step
- grant type - (PKCE)

After receving /token request, 
1) Auth server will validate code verifier with code challenege
2) Alos validate redirect uri, it should match

9) Auth server will send - access token/ id token/and refersh token(optional) to client(based on grant type)

This token is based on grant type.
Grant type could be - Authorization code flow/ Client Credential flow,  Authorization code + PKCE

this flow is for grant type - Authorization code + PKCE


10) We can present access token to our Server(Backend)

Backend will validate access token. if token is valid, server send data back to client


Oauth2.0 playground can be used for auth server and generate client id/ code challeneg and code verifyer


![image](https://github.com/user-attachments/assets/fb530327-64e7-4dc3-97da-b7b936ae7d2a)

Do client Registration.
- it will give client id and client secret
- Click on PKCE

Step 1) Generate code verifier and code challenege

Send authorization request.
It will contain 
response_type = code - which means we are expecting authorization code
client_id
redirect_url
scope - 
state- 
![image](https://github.com/user-attachments/assets/01bfb2ef-24ec-41f9-8b8a-e76f3e2bcedd)

  


