package com.example.kafka.service.consumer;

import java.util.stream.StreamSupport;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.kafka.data.WorkforceChangeRequestData;
import com.example.kafka.request.SaveExternalStoreWorkforceRequest;
import com.example.kafka.request.communication.WorkforceChangeRequestTransactionCommunicationRequest;
import com.example.kafka.service.BaseService;
import com.example.kafka.service.communication.ICommunicationService;
import com.example.kafka.service.store.IExternalStoreWorkforceService;

@Service
public class GenericConsumerService extends BaseService implements IGenericConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(GenericConsumerService.class);

//    @KafkaListener(topics = "${workforce.topics.change-request-transaction.name}", clientIdPrefix = "json", containerFactory = "kafkaListenerContainerFactory")
//    public void listenAsObject(ConsumerRecord<String, WorkforceChangeRequestData> cr, @Payload WorkforceChangeRequestData payload, Acknowledgment ack) throws Exception {
//        logger.info("listenAsObject received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
//        ack.acknowledge();
//
//        _communicationService.transaction(cr.key(), payload);
//        _communicationService.transaction(new WorkforceChangeRequestTransactionCommunicationRequest(cr.key(), payload));
//    }

//    @KafkaListener(topics = "${workforce.topics.change-request-checkpoint.name}", clientIdPrefix = "string", containerFactory = "kafkaListenerContainerFactory")
//    public void listenAsObjectCheckpoint(ConsumerRecord<String, WorkforceChangeRequestData> cr, @Payload WorkforceChangeRequestData payload, Acknowledgment ack) throws Exception {
//        logger.debug("listenAsObjectCheckpoint received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
//
//        ack.acknowledge();
//
//        _storeService.saveCheckpoint(new SaveExternalStoreWorkforceRequest(payload));
//    }
//
//    @KafkaListener(topics = "${workforce.topics.change-request-output.name}", clientIdPrefix = "string", containerFactory = "kafkaListenerContainerFactory")
//    public void listenAsObjectOutput(ConsumerRecord<String, WorkforceChangeRequestData> cr,  @Payload WorkforceChangeRequestData payload, Acknowledgment ack) throws Exception {
//        logger.debug("listenAsObjectOutput received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
//        ack.acknowledge();
//
//        _storeService.saveOutput(new SaveExternalStoreWorkforceRequest(payload));
//    }

    @KafkaListener(topics = "${workforce.topics.change-request-transaction.name}", clientIdPrefix = "string", containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObjectTransaction(ConsumerRecord<String, WorkforceChangeRequestData> cr, @Payload WorkforceChangeRequestData payload, Acknowledgment ack) throws Exception {
        logger.debug("listenAsObjectTransaction received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
        ack.acknowledge();

        _storeService.saveTransaction(new SaveExternalStoreWorkforceRequest(payload));
        _communicationService.transaction(new WorkforceChangeRequestTransactionCommunicationRequest(cr.key(), payload));
    }

    @KafkaListener(topics = "${workforce.topics.change-request-transaction-redacted.name}", clientIdPrefix = "string", containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObjectTransactionRedacted(ConsumerRecord<String, WorkforceChangeRequestData> cr, @Payload WorkforceChangeRequestData payload, Acknowledgment ack) throws Exception {
        logger.debug("listenAsObjectTransactionRedacted received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
        ack.acknowledge();

        _storeService.saveTransactionRedacted(new SaveExternalStoreWorkforceRequest(payload));
        _communicationService.transaction(new WorkforceChangeRequestTransactionCommunicationRequest(cr.key(), payload));
    }

//    @KafkaListener(topics = "${workforce.topics.topic-output.name}", clientIdPrefix = "bytearray", containerFactory = "kafkaListenerByteArrayContainerFactory")
//    public void listenAsByteArray(ConsumerRecord<String, byte[]> cr, @Payload byte[] payload, Acknowledgment ack) throws Exception {
//        logger.info("listenAsByteArray received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(), typeIdHeader(cr.headers()), payload, cr.toString());
//        ack.acknowledge();
//    }

    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                .filter(header -> header.key().equals("__TypeId__"))
                .findFirst().map(header -> new String(header.value())).orElse("N/A");
    }

    @Autowired
    private ICommunicationService _communicationService;

    @Autowired
    private IExternalStoreWorkforceService _storeService;
}
