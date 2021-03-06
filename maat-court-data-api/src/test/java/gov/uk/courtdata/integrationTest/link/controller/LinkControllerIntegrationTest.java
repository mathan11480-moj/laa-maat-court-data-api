package gov.uk.courtdata.integrationTest.link.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integrationTest.MockServicesConfig;
import gov.uk.courtdata.link.controller.LinkController;
import gov.uk.courtdata.model.CaseDetailsValidate;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class LinkControllerIntegrationTest {


    private static final String LINK_VALIDATE_URI = "/link/validate";

    private MockMvc mockMvc;

    @Autowired
    private LinkController linkController;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepOrderRepository repOrderRepository;

    @Autowired
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        repOrderRepository.deleteAll();
        repOrderCPDataRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
    }

    @Test
    public void testWhenMaatIdMissing_Returns400ClientError() throws Exception {

        //Request
        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        //Assert response
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("MAAT id is missing.")));
    }


    @Test
    public void testWhenMaatIdInvalid_Returns400ClientError() throws Exception {

        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .maatId(1000)
                        .build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("1000 is Not a Valid MAAT ID")));
    }

    @Test
    public void testWhenCaseUrnHasNoCPDataExists_Returns400ClientError() throws Exception {

        final Integer maatId = 1000;

        // Rep order with maat
        repOrderRepository.save(createRepOrderEntity(maatId));

        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .maatId(maatId)
                        .caseUrn("testUrn")
                        .build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",
                        is("MaatId 1000 has no common platform data created against Maat application.")));
    }

    @Test
    public void testWhenCaseUrnNotExists_Returns400ClientError() throws Exception {

        final Integer maatId = 1000;

        // Create Rep order with maatId
        repOrderRepository.save(createRepOrderEntity(maatId));

        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .maatId(maatId)
                        .build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("CaseURN can't be null or empty on request.")));
    }


    @Test
    public void testWhenMaatIdAlreadyLinked_Returns400ClientError() throws Exception {

        final Integer maatId = 1000;

        // Rep order with maat
        repOrderRepository.save(createRepOrderEntity(maatId));

        repOrderCPDataRepository.save(createRepOrderCPDataEntity(maatId, "testUrn"));

        //Create link to validate exists.
        wqLinkRegisterRepository.save(createWqLinkRegisterEntity(maatId));

        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .maatId(1000)
                        .caseUrn("testUrn")
                        .build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        // Assert Http Response.
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",
                        is("1000: MaatId already linked to the application.")));
    }


    @Test
    public void testWhenPreConditionValidationPasses_Returns200Success() throws Exception {

        final Integer maatId = 1000;

        // Rep order with maat
        repOrderRepository.save(createRepOrderEntity(maatId));

        repOrderCPDataRepository.save(createRepOrderCPDataEntity(maatId, "testUrn"));

        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .maatId(1000)
                        .caseUrn("testUrn")
                        .build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        // Assert Http Response.
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @AfterEach
    public void clearUp() {
        repOrderRepository.deleteAll();
        repOrderCPDataRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
    }


    private WqLinkRegisterEntity createWqLinkRegisterEntity(final Integer maatId) {
        return WqLinkRegisterEntity.builder()
                .caseId(100)
                .createdTxId(10)
                .proceedingId(100)
                .removedTxId(null)
                .cjsAreaCode("16")
                .maatCat(1)
                .mlrCat(1)
                .maatId(maatId)
                .build();
    }


    public RepOrderCPDataEntity createRepOrderCPDataEntity(final Integer maatId, final String caseUrn) {
        return RepOrderCPDataEntity.builder()
                .repOrderId(maatId)
                .caseUrn(caseUrn)
                .build();
    }


    public RepOrderEntity createRepOrderEntity(final Integer maatId) {

        return RepOrderEntity.builder()
                .id(maatId)
                .dateModified(LocalDateTime.now())
                .build();
    }

}
