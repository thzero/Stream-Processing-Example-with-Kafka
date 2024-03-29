package com.example.kafka.service.publish.kafka;

import com.example.kafka.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.kafka.data.WorkforceChangeRequestData;
import com.example.kafka.request.publish.WorkforceChangeRequestPublishRequest;
import com.example.kafka.response.publish.WorkforceChangeRequestPublishResponse;
import com.example.kafka.service.BaseService;
import com.example.kafka.service.publish.IWorkforceChangeRequestPublishService;
import org.springframework.util.StringUtils;

@Service
public class WorkforceChangeRequestPublishService extends BaseService implements IWorkforceChangeRequestPublishService {
    private static final Logger logger = LoggerFactory.getLogger(WorkforceChangeRequestPublishService.class);

    public WorkforceChangeRequestPublishResponse publish(@NonNull WorkforceChangeRequestPublishRequest request) {
        WorkforceChangeRequestPublishResponse response = new WorkforceChangeRequestPublishResponse();
        try {
            String topic = "";
            if (request.topic == WorkforceChangeRequestPublishRequest.Topics.Checkpoint)
                topic = _appConfig.changeRequestCheckpointTopic;
            if (request.topic == WorkforceChangeRequestPublishRequest.Topics.DeadLetter)
                topic = _appConfig.changeRequestDeadLetterTopic;
            else if (request.topic == WorkforceChangeRequestPublishRequest.Topics.Transaction)
                topic = _appConfig.changeRequestTransactionTopic;
            else if (request.topic == WorkforceChangeRequestPublishRequest.Topics.TransactionRedacted)
                topic = _appConfig.changeRequestTransactionRedactedTopic;

            if (StringUtils.isEmpty(topic))
                return error(response, "Invalid topic.");

            _kafkaTemplate.send(topic, request.key, request.value);

            return response;
        }
        catch (Exception ex) {
            logger.error(TAG, ex);
            return error(response);
        }
    }

    @Autowired
    private AppConfig _appConfig;

    @Autowired
    private KafkaTemplate<String, WorkforceChangeRequestData> _kafkaTemplate;

    private static final String TAG = WorkforceChangeRequestPublishService.class.getName();
}
