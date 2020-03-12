package gov.uk.courtdata.laaStatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.model.CaseDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * <code>LaaStatusListener</code> a JMS listener to consume laa status message and trigger
 * status update in maat db.
 */
@Slf4j
@AllArgsConstructor
@Service
public class LaaStatusListener {

    private final LaaStatusService laaStatusUpdateService;

    private final Gson gson;

    /**
     * @param message
     * @throws JmsException
     */
    @JmsListener(destination = "${aws.sqs.queue.laaStatus}")
    public void receive(@Payload final String message) throws JmsException {

        try {

            log.info("Received JSON Message  {}", message);
            CaseDetails laaStatusUpdate = gson.fromJson(message, CaseDetails.class);
            log.info("Message converted {} ", laaStatusUpdate);
            laaStatusUpdateService.execute(laaStatusUpdate);

        } catch (MaatCourtDataException mex) {
            log.warn("Laa status update failed.");
            log.error("MaatCourtDataException  {}", mex);
            mex.printStackTrace();
        }
    }


}
