package com.example.urlshortner.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AerospikeServiceTest {

    private UrlService urlService;
    Map<String, String> inputMap;

    @BeforeEach
    void setUp() {
        AerospikeService aerospikeService = new AerospikeService();
        urlService = new UrlService(aerospikeService);
        inputMap = new HashMap<>() {{
            put("https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/index.html", null);
            put("https://www.google.com/", null);
            put("https://university.mongodb.com/courses/M320/about", null);
            put("https://www.jpa-buddy.com/documentation/", null);
            put("https://www.baeldung.com/java-convert-pdf-to-base64", null);
        }};
    }

    @Test
    public void shouldShortenAllAndSave() {
        shortenAllUrls(inputMap);

        inputMap.forEach((longUrl, shortUrl) -> {
            assertNotNull(shortUrl); // it means the corresponding short url has been created

            // check that both long and short url share the same base url
            String baseUrlByKey = urlService.getBaseUrl(longUrl);
            String baseUrlByValue = urlService.getBaseUrl(longUrl);
            assertEquals(baseUrlByKey, baseUrlByValue);

            // check that the actual hash value in short url is as expected
            String remainingUrl = longUrl.replace(baseUrlByKey + "/", "");
            final String actualHashOfShortUrl = shortUrl.replace(baseUrlByKey + "/", "");
            final String expectedHashShortened = DigestUtils.sha256Hex(remainingUrl).substring(0, 12);
            assertEquals(expectedHashShortened, actualHashOfShortUrl);
        });
    }

    @Test
    public void shouldThrowOnInvalidUrls() {
        String invalidUrl1 = "fdbdfs/com/fsgs/gmail";
        String invalidUrl2 = "httpss://balalalala/dfghdgh";
        String invalidUrl3 = "http://balalalala//dfghdgh";
        assertThrows(IllegalArgumentException.class, () -> urlService.shortenAndSaveUrl(invalidUrl1));
        assertThrows(IllegalArgumentException.class, () -> urlService.shortenAndSaveUrl(invalidUrl2));
        assertThrows(IllegalArgumentException.class, () -> urlService.shortenAndSaveUrl(invalidUrl3));
    }

    @Test
    public void shouldGetOriginalUrlsByShortOnes() {
        shortenAllUrls(inputMap); // will populate all values by short urls

        // here we check that after short urls were created,
        // we can actually use them to retrieve the long value from db
        // we compare this value to the original value from the map
        inputMap.forEach((expectedLongUrl, shortUrl) -> {
            String actualShortUrl = urlService.getLongUrlByShortUrl(shortUrl);
            assertEquals(expectedLongUrl, actualShortUrl);
        });
    }

    private void shortenAllUrls(Map<String, String> inputMap) {
        inputMap.entrySet().forEach(entry -> {
            String longUrl = entry.getKey();
            String shortUrl = urlService.shortenAndSaveUrl(longUrl);
            entry.setValue(shortUrl);
        });
    }
}
