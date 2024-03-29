package com.example.kafka.service.merge;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import com.example.kafka.service.BaseService;
import com.example.kafka.data.WorkforceChangeRequestData;
import com.example.kafka.response.ISuccessResponse;

public abstract class BaseActionMergeService extends BaseService {
    public ISuccessResponse valid(@NonNull WorkforceChangeRequestData changeRequest) {
        if (changeRequest.request == null)
            return error("Invalid 'request' element.");

        if (StringUtils.isEmpty(changeRequest.request.id))
            return error("Invalid 'request.id' element - most not be empty.");

        try {
            UUID.fromString(changeRequest.request.id);
        }
        catch (IllegalArgumentException ex) {
            return error("Invalid 'request.id' element - must be a valid UUID.");
        }

        return success();
    }
}
