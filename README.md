Hello and welcome to UrlShortner app!

This is a REST api containing 2 methods:

When working with localhost 

**1. Api to shorten the url**
POST to:
http://localhost:8080/shorten
With long url in the request body, like:
https://en.wikipedia.org/wiki/URL_shortening#Techniques
returns the short version

**2. Api which will redirect to the original url**
POST to:
http://localhost:8080/redirect
with the short version (should be created earlier)
redirect to the original page using the long url
(preview response in postman to see the page preview)

Please note:
**The app containes to services:**
1. AerospikeService.java  - to manage requests to the database.
2. UrlService.java - to manage the url conversions.

**The app contains 3 tests which tests all major functionality in:**
AerospikeServiceTest.java

Enjoy the app! :)