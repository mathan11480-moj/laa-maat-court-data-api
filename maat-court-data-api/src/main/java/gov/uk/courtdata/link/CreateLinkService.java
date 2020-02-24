package gov.uk.courtdata.link;

import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.error.MaatCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.ValidationProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <code>CreateLinkService</code> front handler for save and link with transaction boundry.
 */
@Slf4j
@AllArgsConstructor
@Service
public class CreateLinkService {

    private final SaveAndLinkImpl saveAndLinkImpl;

    private final ValidationProcessor validationProcessor;

    /**
     * @param linkMessage
     * @throws ValidationException
     * @throws MaatCourtDataException
     */
    public void saveAndLink(final CaseDetails linkMessage) throws ValidationException, MaatCourtDataException {

        try {

            final CreateLinkDto createLinkDto = validationProcessor.validate(linkMessage);
            log.info("Validation success!!!");
            saveAndLinkImpl.execute(createLinkDto);
            log.info("Create link success!!!");

        } catch (ValidationException vex) {
            throw vex;
        } catch (Exception ex) {
            throw new MaatCourtDataException(ex.getMessage());
        }

    }

}