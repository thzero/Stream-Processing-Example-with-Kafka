package com.example.kafka.topology.processor.local;

import java.util.Objects;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.kafka.data.WorkforceData;

// This processor updates the the local store for WorkforcecData from the load topic.
public class LocalStoreWorkforceProcessor extends AbstractProcessor<String, WorkforceData> {
    private static final Logger logger = LoggerFactory.getLogger(LocalStoreWorkforceProcessor.class);

    public LocalStoreWorkforceProcessor() {}
    public LocalStoreWorkforceProcessor(String storeName) {
        _storeName = storeName;
    }

    @Override
    public void process(String key, WorkforceData workforce) {
        logger.debug("process - load for request id '{}'", workforce.id);
        _workforceStore.put(workforce.id, workforce);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        _workforceStore = (KeyValueStore<String, WorkforceData>)context.getStateStore(_storeName);
        Objects.requireNonNull(_workforceStore, "State store can't be null");
    }

    private String _storeName;
    private KeyValueStore<String, WorkforceData> _workforceStore;

    public static final String TAG = LocalStoreWorkforceProcessor.class.getName();
}
