package com.example.urlshortner.controller;

import com.example.urlshortner.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class UrlShorteningController {

    private final UrlService urlService;

    /**
     * Will shorten the url and store the short-long pair as key-value to
     * Aerospike database
     *
     * @param longUrl the original url
     * @return shortUrl the short version of the url
     */
    @PostMapping("/shorten")
    public String shortenAndStoreUrl(@RequestBody String longUrl) {
        return urlService.shortenAndSaveUrl(longUrl);
    }

    /**
     * Given a short url will redirect to the original url.
     *
     * @param model    model from framework
     * @param shortUrl the short url to use for redirect
     * @return viewToRedirectTo
     */
    @PostMapping("/redirect")
    public ModelAndView redirectByShortUrl(ModelMap model, @RequestBody String shortUrl) {
        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        String longUrl = urlService.getLongUrlByShortUrl(shortUrl);
        return new ModelAndView("redirect:" + longUrl, model);
    }
}
