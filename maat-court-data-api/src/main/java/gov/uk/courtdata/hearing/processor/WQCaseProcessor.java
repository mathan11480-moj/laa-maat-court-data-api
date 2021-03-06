package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQCaseEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQCaseRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
@RequiredArgsConstructor
public class WQCaseProcessor {

    private final WQCaseRepository wqCaseRepository;

    /**
     * @param magsCourtDTO
     */
    public void process(final HearingDTO magsCourtDTO) {


        WQCaseEntity wqCaseEntity = WQCaseEntity.builder().caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asn(magsCourtDTO.getAsn())
                .docLanguage(magsCourtDTO.getDocLanguage())
                .inactive(magsCourtDTO.getInActive())
                .libraCreationDate(getCreationDate(magsCourtDTO.getCaseCreationDate()))
                .cjsAreaCode(magsCourtDTO.getCjsAreaCode())
                .proceedingId(magsCourtDTO.getProceedingId())
                .build();
        wqCaseRepository.save(wqCaseEntity);
    }

    /**
     * Get the creation date in request format it else return system date.
     *
     * @param creationDate the given date
     * @return
     */
    private LocalDate getCreationDate(final String creationDate) {
        return
                isNotEmpty(creationDate) ? DateUtil.parse(creationDate) : LocalDate.now();

    }


}
