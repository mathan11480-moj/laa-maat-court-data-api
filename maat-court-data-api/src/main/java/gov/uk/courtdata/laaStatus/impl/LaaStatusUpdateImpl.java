package gov.uk.courtdata.laaStatus.impl;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.laaStatus.processor.UpdateDefendantInfoProcessor;
import gov.uk.courtdata.laaStatus.processor.UpdateOffenceInfoProcessor;
import gov.uk.courtdata.laaStatus.processor.UpdateWqCoreInfoProcessor;
import gov.uk.courtdata.laaStatus.processor.UpdateWqLinkRegisterProcessor;
import gov.uk.courtdata.link.processor.*;
import gov.uk.courtdata.repository.IdentifierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class LaaStatusUpdateImpl {

    private final IdentifierRepository identifierRepository;
    private final CaseInfoProcessor caseInfoProcessor;
    private final UpdateWqCoreInfoProcessor updateWqCoreInfoProcessor;
    private final UpdateWqLinkRegisterProcessor updateWqLinkRegisterProcessor;
    private final SolicitorInfoProcessor solicitorInfoProcessor;
    private final UpdateDefendantInfoProcessor updateDefendantInfoProcessor;
    private final SessionInfoProcessor sessionInfoProcessor;
    private final UpdateOffenceInfoProcessor updateOffenceInfoProcessor;


    @Transactional(rollbackFor = MaatCourtDataException.class)
    public void execute(CourtDataDTO courtDataDTO) {

        log.info("LAA Status Update - Transaction Processing - Start");
        mapTxnID(courtDataDTO);
        caseInfoProcessor.process(courtDataDTO);
        log.info("LAA Status Update - Case Details are processed");
        updateWqCoreInfoProcessor.process(courtDataDTO);
        log.info("LAA Status Update - WQ Core details are processed");
        updateWqLinkRegisterProcessor.process(courtDataDTO);
        log.info("LAA Status Update - WQ Link Register Details are processed");
        solicitorInfoProcessor.process(courtDataDTO);
        log.info("LAA Status Update - Solicitor Details are processed");
        updateDefendantInfoProcessor.process(courtDataDTO);
        log.info("LAA Status Update - Defendant Details are processed");
        sessionInfoProcessor.process(courtDataDTO);
        log.info("LAA Status Update - Session Details are processed");
        updateOffenceInfoProcessor.process(courtDataDTO);
        log.info("LAA Status Update - Offence Details are processed");
        log.info("LAA Status Update -  Transaction Processing - End");
    }

    private void mapTxnID(CourtDataDTO courtDataDTO) {

        courtDataDTO.setTxId(identifierRepository.getTxnID());

    }
}