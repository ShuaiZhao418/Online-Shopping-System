package com.example.webapplication.controller;
import com.example.webapplication.metrics.MerticsClientBean;
import com.google.gson.JsonObject;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthz")
public class Test {

    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    @Autowired
    private StatsDClient statsDClient;

    @GetMapping
    public String TestHealth() {
        logger.info("This is the new health -  get api");
        statsDClient.incrementCounter("endpoint.healthz.http.get");
        JsonObject jsonObject = new JsonObject();
        return jsonObject.toString();
    }
}
