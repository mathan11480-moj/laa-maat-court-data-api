package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_OFFENCE", schema = "MLA")
public class OffenceEntity {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "ASN_SEQ")
    private String asnSeq;
    @Column(name = "OFFENCE_SHORT_TITLE")
    private String offenceShortTitle;
    @Column(name = "OFFENCE_CLASSIFICATION")
    private String offenceClassification;
    @Column(name = "OFFENCE_DATE")
    private LocalDate offenceDate;
    @Column(name = "OFFENCE_WORDING")
    private String offenceWording;
    @Column(name = "MODE_OF_TRIAL")
    private Integer modeOfTrial;
    @Column(name = "LEGAL_AID_STATUS")
    private String legalAidStatus;
    @Column(name = "LEGAL_AID_STATUS_DATE")
    private LocalDate legalAidStatusDate;
    @Column(name = "OFFENCE_CODE")
    private String offenceCode;
    @Column(name = "LEGALAID_REASON")
    private String legalaidReason;
    @Column(name = "IOJ_DECISION")
    private Integer iojDecision;
    @Column(name = "WQ_OFFENCE")
    private Integer wqOffence;
    @Column(name = "APPLICATION_FLAG")
    private Integer applicationFlag;
    @Column(name = "ATTRIBUTE1")
    private String attribute1;
    @Column(name = "ATTRIBUTE2")
    private String attribute2;
    @Column(name = "ATTRIBUTE3")
    private String attribute3;
    @Column(name = "ATTRIBUTE4")
    private String attribute4;
    @Column(name = "ATTRIBUTE5")
    private String attribute5;
    @Column(name = "OFFENCE_ID")
    private String offenceId;

}
