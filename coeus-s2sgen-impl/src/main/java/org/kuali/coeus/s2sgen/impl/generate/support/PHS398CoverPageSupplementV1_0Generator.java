/*
 * Kuali Coeus, a comprehensive research administration system for higher education.
 * 
 * Copyright 2005-2015 Kuali, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.coeus.s2sgen.impl.generate.support;

import gov.grants.apply.forms.phs398CoverPageSupplementV10.PHS398CoverPageSupplementDocument;
import gov.grants.apply.forms.phs398CoverPageSupplementV10.PHS398CoverPageSupplementDocument.PHS398CoverPageSupplement;
import gov.grants.apply.forms.phs398CoverPageSupplementV10.PHS398CoverPageSupplementDocument.PHS398CoverPageSupplement.ClinicalTrial;
import gov.grants.apply.forms.phs398CoverPageSupplementV10.PHS398CoverPageSupplementDocument.PHS398CoverPageSupplement.ContactPersonInfo;
import gov.grants.apply.forms.phs398CoverPageSupplementV10.PHS398CoverPageSupplementDocument.PHS398CoverPageSupplement.PDPI;
import gov.grants.apply.forms.phs398CoverPageSupplementV10.PHS398CoverPageSupplementDocument.PHS398CoverPageSupplement.StemCells;
import gov.grants.apply.system.globalLibraryV10.YesNoDataType;
import gov.grants.apply.system.globalLibraryV10.YesNoDataType.Enum;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlObject;
import org.kuali.coeus.common.api.ynq.YnqConstant;
import org.kuali.coeus.common.questionnaire.api.answer.AnswerHeaderContract;
import org.kuali.coeus.propdev.api.person.ProposalPersonContract;
import org.kuali.coeus.propdev.api.person.ProposalPersonDegreeContract;
import org.kuali.coeus.propdev.api.ynq.ProposalYnqContract;
import org.kuali.coeus.s2sgen.impl.generate.FormGenerator;
import org.kuali.coeus.s2sgen.impl.generate.FormVersion;
import org.kuali.coeus.s2sgen.impl.person.DepartmentalPersonDto;
import org.kuali.coeus.propdev.api.core.ProposalDevelopmentDocumentContract;

import org.kuali.coeus.s2sgen.impl.util.FieldValueConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;


import java.util.List;

/**
 * Class for generating the XML object for grants.gov
 * PHS398CoverPageSupplementV1_0. Form is generated using XMLBean classes and is
 * based on PHS398CoverPageSupplement schema.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
@FormGenerator("PHS398CoverPageSupplementV1_0Generator")
public class PHS398CoverPageSupplementV1_0Generator extends
		PHS398CoverPageSupplementBaseGenerator {

    @Value("http://apply.grants.gov/forms/PHS398_CoverPageSupplement-V1.0")
    private String namespace;

    @Value("PHS398_CoverPageSupplement-V1.0")
    private String formName;

    @Value("classpath:org/kuali/coeus/s2sgen/impl/generate/support/stylesheet/PHS398_CoverPageSupplement-V1.0.fo.xsl")
    private Resource stylesheet;

    @Value("gov.grants.apply.forms.phs398CoverPageSupplementV10")
    private String packageName;

    @Value("185")
    private int sortIndex;

    List<? extends AnswerHeaderContract> answerHeaders;
	/**
	 * 
	 * This method gives information of Cover Page Supplement such as PDPI
	 * details,Clinical Trail information,Contact person information.
	 * 
	 * @return coverPageSupplementDocument {@link XmlObject} of type
	 *         PHS398CoverPageSupplementDocument.
	 */
	private PHS398CoverPageSupplementDocument getCoverPageSupplement() {

		PHS398CoverPageSupplementDocument coverPageSupplementDocument = PHS398CoverPageSupplementDocument.Factory
				.newInstance();
		PHS398CoverPageSupplement coverPageSupplement = PHS398CoverPageSupplement.Factory
				.newInstance();
		answerHeaders = getPropDevQuestionAnswerService().getQuestionnaireAnswerHeaders(pdDoc.getDevelopmentProposal().getProposalNumber());
		coverPageSupplement.setFormVersion(FormVersion.v1_0.getVersion());
		coverPageSupplement.setPDPI(getPDPI());
		coverPageSupplement.setClinicalTrial(getClinicalTrial());
		coverPageSupplement.setContactPersonInfo(getContactPersonInfo(pdDoc));
		StemCells stemCells = getStemCells();
		coverPageSupplement.setStemCells(stemCells);
		coverPageSupplementDocument
				.setPHS398CoverPageSupplement(coverPageSupplement);
		return coverPageSupplementDocument;
	}

	/**
	 * 
	 * This method gives the personal details such as Name, New Investigator,
	 * Degrees of Principal Investigator
	 * 
	 * @return PDPI object containing Principal Investigator details.
	 */
	private PDPI getPDPI() {
		PDPI pdpi = PDPI.Factory.newInstance();
		ProposalPersonContract PI = s2SProposalPersonService.getPrincipalInvestigator(pdDoc);
		pdpi.setPDPIName(globLibV10Generator.getHumanNameDataType(PI));
		// Set default values for mandatory fields
		pdpi.setIsNewInvestigator(YesNoDataType.NO);

		if (PI != null) {
			ProposalYnqContract proposalYnq = getProposalYnQ(IS_NEW_INVESTIGATOR);
			if (proposalYnq != null) {
				YesNoDataType.Enum answer = null;
				if (proposalYnq.getAnswer() != null) {
					answer = (proposalYnq.getAnswer().equals(
							YnqConstant.YES.code()) ? YesNoDataType.YES
							: YesNoDataType.NO);
					pdpi.setIsNewInvestigator(answer);
				}
			}
			String[] degreeArr = null;
			if (PI.getProposalPersonDegrees() != null) {
				degreeArr = new String[PI.getProposalPersonDegrees().size()];
			}
			int size = 0;
			for (ProposalPersonDegreeContract personDegree : PI
					.getProposalPersonDegrees()) {
				// Degrees: 0...3
				if (size > MAX_NUMBER_OF_DEGREES) {
					break;
				}
				if (personDegree.getDegree() != null) {
					if (personDegree.getDegree().length() > PERSON_DEGREE_MAX_LENGTH) {
						degreeArr[size] = personDegree.getDegree().substring(0,
								PERSON_DEGREE_MAX_LENGTH);
					} else {
						degreeArr[size] = personDegree.getDegree();
					}
					size++;
				}
			}
			pdpi.setDegreesArray(degreeArr);
		}
		return pdpi;
	}

	/**
	 * 
	 * This method is used to get Clinical Trial information
	 * 
	 * @return ClinicalTrial object containing Clinical Trail Details.
	 */
	private ClinicalTrial getClinicalTrial() {

        ClinicalTrial clinicalTrial = ClinicalTrial.Factory.newInstance();
        String answer = null;
        String subAnswer = null;
        answer = getAnswer(IS_CLINICAL_TRIAL,answerHeaders);
        if (answer != null) {
            if (!answer.equals(NOT_ANSWERED)) {
                if (YnqConstant.YES.code().equals(answer)) {
                    clinicalTrial.setIsClinicalTrial(YesNoDataType.YES);
                    subAnswer = getAnswer(PHASE_III_CLINICAL_TRIAL,answerHeaders);
                    if (subAnswer != null) {
                        if (!subAnswer.equals(NOT_ANSWERED)) {
                            if (YnqConstant.YES.code().equals(subAnswer)) {
                                clinicalTrial.setIsPhaseIIIClinicalTrial(YesNoDataType.YES);   
                            } else {
                                clinicalTrial.setIsPhaseIIIClinicalTrial(YesNoDataType.NO);   
                            }

                        }
                    }
                } else {
                    clinicalTrial.setIsClinicalTrial(YesNoDataType.NO);
                }
            }
        }
        return clinicalTrial;
    }

	/**
	 * 
	 * This method gives the Contact person information such as contact
	 * Name,Phone,Fax,EmailAddress,Title,Address.
	 * 
	 * @return ContactPersonInfo object containing contact person details.
	 */
	private ContactPersonInfo getContactPersonInfo(
			ProposalDevelopmentDocumentContract pdDoc) {
		ContactPersonInfo contactPersonInfo = ContactPersonInfo.Factory
				.newInstance();
		DepartmentalPersonDto contactPerson = departmentalPersonService.getContactPerson(pdDoc);
		if (contactPerson != null) {
			contactPersonInfo.setContactName(globLibV10Generator
					.getHumanNameDataType(contactPerson));
			contactPersonInfo.setContactPhone(contactPerson.getOfficePhone());
			if (StringUtils.isNotEmpty(contactPerson.getFaxNumber())) {
				contactPersonInfo.setContactFax(contactPerson.getFaxNumber());
			}
			if (contactPerson.getEmailAddress() != null
					&& !contactPerson.getEmailAddress().equals("")) {
				contactPersonInfo.setContactEmail(contactPerson.getEmailAddress());
			}
			contactPersonInfo.setContactTitle(contactPerson.getPrimaryTitle());
			contactPersonInfo.setContactAddress(globLibV10Generator
					.getAddressRequireCountryDataType(contactPerson));
		}
		return contactPersonInfo;
	}

	/**
	 * 
	 * This method is used to get information of Stem Cells for the form
	 * PHS398CoverPage
	 * 
	 * @return StemCells object containing information about Human stem Cells
	 *         involvement.
	 */
	private StemCells getStemCells() {

	    StemCells stemCells = StemCells.Factory.newInstance();	
	    Enum answers = YesNoDataType.NO;
	    String childAnswer = null;  
	    String answer = getAnswer(IS_HUMAN_STEM_CELLS_INVOLVED,answerHeaders);
	    if (answer != null) {
	        if (!answer.equals(NOT_ANSWERED)) {
	            answers = YnqConstant.YES.code().equals(getAnswer(IS_HUMAN_STEM_CELLS_INVOLVED, answerHeaders)) ? YesNoDataType.YES : YesNoDataType.NO;
	            if (YnqConstant.YES.code().equals(answer)) {
	                stemCells.setIsHumanStemCellsInvolved(YesNoDataType.YES);
	                String subAnswer = getAnswer(SPECIFIC_STEM_CELL_LINE,answerHeaders);
	                if (subAnswer != null) {
	                    if(!subAnswer.equals(NOT_ANSWERED)) {
	                        if (YnqConstant.YES.code().equals(subAnswer)) {
	                            stemCells.setStemCellsIndicator(YesNoDataType.NO);
	                            childAnswer = getAnswers(REGISTRATION_NUMBER,answerHeaders);
	                        }
	                        else {
	                            stemCells.setStemCellsIndicator(YesNoDataType.YES);
	                        }
	                    }
	                }
	                if (childAnswer != null) {
	                    if (FieldValueConstants.VALUE_UNKNOWN.equalsIgnoreCase(childAnswer)) {
	                        stemCells.setStemCellsIndicator(answers);
	                    } else {
	                        List<String> cellLines = getCellLines(childAnswer);
	                        if (cellLines.size() > 0) {
	                            stemCells.setCellLinesArray(cellLines.toArray(new String[0]));
	                        }
	                    }
	                }
	            } else {
	                stemCells.setIsHumanStemCellsInvolved(YesNoDataType.NO); 
	            }
	        }
	    }
	    return stemCells;
	}

	/**
	 * This method creates {@link XmlObject} of type
	 * {@link PHS398CoverPageSupplementDocument} by populating data from the
	 * given {@link ProposalDevelopmentDocumentContract}
	 * 
	 * @param proposalDevelopmentDocument
	 *            for which the {@link XmlObject} needs to be created
	 * @return {@link XmlObject} which is generated using the given
	 *         {@link ProposalDevelopmentDocumentContract}
	 */
	public XmlObject getFormObject(
			ProposalDevelopmentDocumentContract proposalDevelopmentDocument) {
		this.pdDoc = proposalDevelopmentDocument;
		return getCoverPageSupplement();
	}

    @Override
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    @Override
    public Resource getStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(Resource stylesheet) {
        this.stylesheet = stylesheet;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
}
