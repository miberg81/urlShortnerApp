Hello and welcome to UrlShortner app!

This is a REST api containing 2 methods:

When working with localhost 

**1. Api to shorten the url** <br />
POST to: <br />
http://localhost:8080/shorten <br />
with long url in the request body, like: <br />
https://en.wikipedia.org/wiki/URL_shortening#Techniques <br />
returns the short version <br />
which looks like: <br />
https://en.wikipedia.org/adab71058e65

**2. Api which will redirect to the original url** <br />
POST to: <br />
http://localhost:8080/redirect <br />
with the short version (should be created earlier) <br />
redirect to the original page using the long url <br />
(preview response in postman to see the page preview) <br />

Please note:<br />
**The app contains 2 services:** <br />
1. AerospikeService.java  - to manage requests to the database. <br />
2. UrlService.java - to manage the url conversions. <br />

**The app contains 3 tests which tests all major functionality in:** <br />
AerospikeServiceTest.java <br />

Enjoy the app! :) <br />