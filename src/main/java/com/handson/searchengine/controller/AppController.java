package com.handson.searchengine.controller;


import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.handson.searchengine.crawler.Crawler;
import com.handson.searchengine.kafka.Producer;
import com.handson.searchengine.model.CrawlStatusOut;
import com.handson.searchengine.model.CrawlerRequest;

@RestController
@RequestMapping("/api")
public class AppController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello";
    }

        private static final int ID_LENGTH = 6;
private Random random = new Random();
@Autowired
    Crawler crawler;


    @RequestMapping(value = "/crawl", method = RequestMethod.POST)
    public String crawl(@RequestBody CrawlerRequest request) throws IOException, InterruptedException {
        String crawlId = generateCrawlId();
        if (!request.getUrl().startsWith("http")) {
            request.setUrl("https://" + request.getUrl());
        }
        new Thread(()-> {
            try {
                crawler.crawl(crawlId, request);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return crawlId;
    }
    
    @RequestMapping(value = "/crawl/{crawlId}", method = RequestMethod.GET)
    public CrawlStatusOut getCrawl(@PathVariable String crawlId) throws IOException, InterruptedException {
        return crawler.getCrawlInfo(crawlId);
    }

private String generateCrawlId() {
        String charPool = "ABCDEFHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < ID_LENGTH; i++) {
        res.append(charPool.charAt(random.nextInt(charPool.length())));
        }
        return res.toString();
        }

        @Autowired
        Producer producer;
    
        @RequestMapping(value = "/sendKafka", method = RequestMethod.POST)
        public String sendKafka(@RequestBody CrawlerRequest request) throws IOException, InterruptedException {
                producer.send(request);
                return "OK";
        }
}
