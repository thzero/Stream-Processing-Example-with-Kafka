package com.example.kafka.service.delete;

import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.kafka.data.PhoneData;
import com.example.kafka.data.ChangeSubTypes;
import com.example.kafka.data.WorkforceChangeRequestData;
import com.example.kafka.data.WorkforceData;
import com.example.kafka.response.ISuccessResponse;
import com.example.kafka.service.BaseActionMergeService;
import com.example.kafka.service.IDeleteActionMergeService;

@Service
public class PhoneDeleteActionMergeService extends BaseActionMergeService implements IDeleteActionMergeService {
    @Override
    public ChangeSubTypes getChangeSubTypeCd() {
        return ChangeSubTypes.Phone;
    }

    @Override
    public ISuccessResponse delete(WorkforceData workforce, @NonNull WorkforceChangeRequestData changeRequest) {
        ISuccessResponse response = valid(changeRequest);
        if (!response.isSuccess())
            return error(response);

        if (workforce == null)
            return error("Invalid workforce object.");

        Optional<PhoneData> phone;
        for (PhoneData item : changeRequest.request.phones) {
            phone = workforce.phones.stream().filter(l -> l.id.equalsIgnoreCase(item.id)).findFirst();
            if (!phone.isPresent())
                continue;;

            workforce.phones.remove(phone.get());
        }

        return success();
    }

    @Override
    public ISuccessResponse valid(@NonNull WorkforceChangeRequestData changeRequest) {
        ISuccessResponse isValid = super.valid(changeRequest);
        if (!isValid.isSuccess())
            return isValid;

        if ((changeRequest.request.phones != null) && (changeRequest.request.phones.size() > 0))
            return success();

        return error("Invalid 'request.phone' element - most contain at least one phone node.");
    }
}
