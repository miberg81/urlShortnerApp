package com.example.urlshortner.service;


import com.aerospike.client.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlService {

    private final AerospikeService aerospikeService;

    public String shortenAndSaveUrl(String longUrl) {
        validateUrl(longUrl);

        String shortUrl = shortenUrl(longUrl);

        // save the long url bin to db along with the short url as key
        aerospikeService.saveSingleBin(shortUrl, "longUrl", longUrl);

        return shortUrl;
    }

    public String getLongUrlByShortUrl(String shortUrl) {
        validateUrl(shortUrl);

        Record record = aerospikeService.getRecordByKey(shortUrl);
        if (null == record) {
            log.error("Could not find original url by short url: ", shortUrl);
        } else {
            String longUrl = record.getString("longUrl");
            return longUrl;
        }
        return null;
    }

    private void validateUrl(String longUrl) {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(longUrl))
            throw new IllegalArgumentException("This url is not a valid one. Please try again");
    }

    // generate a short domain of form: protocol+domain+shortHash
    private String shortenUrl(String longUrl) {
        // generate the protocol + domain part (stays the same)
        String baseUrl = getBaseUrl(longUrl);

        String remainingUrl = longUrl.replace(baseUrl + "/", "");

        String remainingUrlHash = DigestUtils.sha256Hex(remainingUrl);
        String shorterHash = remainingUrlHash.substring(0, 12);

        String shortUrl = baseUrl + "/" + shorterHash;
        return shortUrl;
    }

    public String getBaseUrl(String longUrl) {
        String[] protocolAndAfter = longUrl.split("//");
        String protocol = protocolAndAfter[0]; // https:
        String afterProtocol = protocolAndAfter[1];
        String domain = afterProtocol.substring(0, afterProtocol.indexOf("/"));
        return protocol + "//" + domain;
    }
}
