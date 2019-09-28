package com.example.kafka.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.response.LoadProducerResponse;
import com.example.kafka.service.LoadProducerService;

@RestController
public class LoadController {
    private static final Logger logger = LoggerFactory.getLogger(LoadController.class);

    @GetMapping(value = "/v1/load/json", produces = { "application/json" })
    public LoadProducerResponse loadJson() throws Exception {
        return _loadProducerService.loadJson();
    }

    @GetMapping(value = "/v1/load/random", produces = { "application/json" })
    public LoadProducerResponse loadRandom() throws Exception {
        return _loadProducerService.loadRandom();
    }

    @Autowired
    private LoadProducerService _loadProducerService;
}