package gov.uk.courtdata.unlink.controller;

import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.validator.UnLinkValidationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unlink")
@Slf4j
public class UnLinkController {

        @Autowired
        private UnLinkValidationProcessor unLinkValidationProcessor;

        @PostMapping("/validate")
        public ResponseEntity unLinkCase(@RequestHeader("Laa-Transaction-Id") String laaTransactionId,
                                         @RequestBody Unlink unlink) {

            log.info("LAA Status Update Request received - laa-transaction-id:{}", laaTransactionId);

            log.info("Request received: {}", unlink.toString());
            unLinkValidationProcessor.validate(unlink);

            return new ResponseEntity(null, HttpStatus.OK);
        }




}

