package com.handson.searchengine.kafka;

import java.io.IOException;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handson.searchengine.crawler.Crawler;
import static com.handson.searchengine.kafka.Producer.APP_TOPIC;
import com.handson.searchengine.model.CrawlerRecord;

@Component
public class Consumer {

    @Autowired
    ObjectMapper om;
    @Autowired
    Crawler crawler;

    @KafkaListener(topics = {APP_TOPIC})
    public void listen(ConsumerRecord<?, ?> record) throws IOException, InterruptedException  {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            Object message = kafkaMessage.get();
            CrawlerRecord rec = om.readValue(message.toString(), CrawlerRecord.class);
            crawler.crawlOneRecord(rec.getCrawlId(), rec);

        }
    }
}
