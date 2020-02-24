package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.RepOrderCommonPlatformDataEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class RepOrderDataRepositoryTest {


    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private RepOrderCommonPlatformDataRepository repOrderDataRepository;

    @Test
    public void givenRepOrderDataRepository_whenSaveAndRetrieveEntity_thenOK() {

        //given
        RepOrderCommonPlatformDataEntity repOrderEntity = testEntityDataBuilder.getRepOrderEntity();
        repOrderDataRepository.save(repOrderEntity);

        // when
        Optional<RepOrderCommonPlatformDataEntity> foundOptionalRepOrderEntity = repOrderDataRepository.findById(repOrderEntity.getRepOrderId());
        RepOrderCommonPlatformDataEntity found = foundOptionalRepOrderEntity.orElse(null);

        // then
        assert found != null;
        assertThat(found.getCaseUrn()).isEqualTo(repOrderEntity.getCaseUrn());
        assertThat(found.getRepOrderId()).isEqualTo(repOrderEntity.getRepOrderId());
        assertThat(found.getDateModified()).isEqualTo(repOrderEntity.getDateModified());
        assertThat(found.getDefendantId()).isEqualTo(repOrderEntity.getDefendantId());

    }
}
