package com.example.kafka.service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.kafka.data.WorkforceChangeRequestData;
import com.example.kafka.request.processor.MergeProcessorRequest;
import com.example.kafka.response.processor.MergeProcessorResponse;
import com.example.kafka.service.BaseService;
import com.example.kafka.service.processor.IMergeProcessorService;

//@Service
public class MergeConsumerService extends BaseService implements IMergeConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(MergeConsumerService.class);

    @KafkaListener(topics = "${workforce.topics.change-request.name}", clientIdPrefix = "string", containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObjectTest(ConsumerRecord<String, WorkforceChangeRequestData> cr, @Payload WorkforceChangeRequestData payload, Acknowledgment ack) {
        logger.debug("listenAsObjectCheckpoint received key {}: Payload: {} | Record: {}", cr.key(), payload, cr.toString());

        try {
            MergeProcessorResponse response = _processService.process(new MergeProcessorRequest(cr.key(), payload));
            if (!response.isSuccess()) {
                if (response.getError() != null)
                    logger.error(response.getError().message);
                return;
            }

            ack.acknowledge();
        }
        catch (Exception ex) {
            logger.debug(TAG, ex);
        }
    }

    @Autowired
    private IMergeProcessorService _processService;

    private static final String TAG = MergeConsumerService.class.getName();
}
