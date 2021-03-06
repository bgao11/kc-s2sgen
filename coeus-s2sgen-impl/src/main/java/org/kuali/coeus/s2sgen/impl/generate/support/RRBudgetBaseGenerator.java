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

import gov.grants.apply.coeus.additionalEquipment.AdditionalEquipmentListDocument;
import gov.grants.apply.coeus.additionalEquipment.AdditionalEquipmentListDocument.AdditionalEquipmentList;
import gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument;
import gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument.ExtraKeyPersonList;
import gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument.ExtraKeyPersonList.KeyPersons.Compensation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.kuali.coeus.common.budget.api.nonpersonnel.BudgetLineItemContract;
import org.kuali.coeus.common.budget.api.period.BudgetPeriodContract;
import org.kuali.coeus.common.budget.api.personnel.BudgetPersonnelDetailsContract;
import org.kuali.coeus.propdev.api.budget.ProposalDevelopmentBudgetExtContract;
import org.kuali.coeus.propdev.api.person.ProposalPersonContract;
import org.kuali.coeus.s2sgen.impl.budget.*;
import org.kuali.coeus.s2sgen.impl.datetime.S2SDateTimeService;
import org.kuali.coeus.s2sgen.impl.validate.S2SErrorHandlerService;
import org.kuali.coeus.sys.api.model.KcFile;
import org.kuali.coeus.propdev.api.core.ProposalDevelopmentDocumentContract;
import org.kuali.coeus.s2sgen.api.core.S2SException;
import org.kuali.coeus.propdev.api.attachment.NarrativeContract;
import org.kuali.coeus.propdev.api.attachment.NarrativeService;
import org.kuali.coeus.s2sgen.impl.generate.S2SBaseFormGenerator;
import org.kuali.coeus.s2sgen.impl.print.GenericPrintable;
import org.kuali.coeus.s2sgen.impl.print.S2SPrintingService;
import org.kuali.coeus.s2sgen.api.core.AuditError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract class has methods that are common to all the versions of
 * RRBudget form.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public abstract class RRBudgetBaseGenerator extends S2SBaseFormGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(RRBudgetBaseGenerator.class);

    private static final String ADDITIONAL_EQUIPMENT = "ADDITIONAL_EQUIPMENT";
	public static final String OTHERCOST_DESCRIPTION = "Other";
	public static final String OTHERPERSONNEL_POSTDOC = "PostDoc";
	public static final String OTHERPERSONNEL_GRADUATE = "Grad";
	public static final String OTHERPERSONNEL_UNDERGRADUATE = "UnderGrad";
	public static final String OTHERPERSONNEL_SECRETARIAL = "Sec";
	public static final int BUDGET_JUSTIFICATION_ATTACHMENT = 7;

	protected static final int OTHERPERSONNEL_MAX_ALLOWED = 6;
	protected static final int ARRAY_LIMIT_IN_SCHEMA = 4;
	protected static final String NID_PD_PI = "PD/PI";
	protected static final String NID_CO_PD_PI = "CO-INVESTIGATOR";		
	protected static final String KEYPERSON_CO_PD_PI = "CO-PD/PI";
	
	private static final String EXTRA_KEYPERSONS = "EXTRA_KEYPERSONS";
	private static final String EQUIPMENT_NARRATIVE_TYPE_CODE = "12";

	private static final String EXTRA_KEYPERSONS_COMMENT = "EXTRA_KEYPERSONS";
	private static final String EXTRA_KEYPERSONS_TYPE = "11";
    private static final String PARTICIPANT_COUNT_REQUIRED = "s2s.budget.participantcount.required";
    private static final String PARTICIPANT_COSTS_REQUIRED = "s2s.budget.participantcost.required";


    @Autowired
    @Qualifier("s2SBudgetCalculatorService")
    protected S2SBudgetCalculatorService s2sBudgetCalculatorService;

    @Autowired
    @Qualifier("s2SDateTimeService")
    protected S2SDateTimeService s2SDateTimeService;

    @Autowired
    @Qualifier("narrativeService")
    protected NarrativeService narrativeCleanupService;

    @Autowired
    @Qualifier("s2SPrintingService")
    protected S2SPrintingService s2SPrintingService;

    @Autowired
    @Qualifier("s2SErrorHandlerService")
    protected S2SErrorHandlerService s2SErrorHandlerService;

    @Autowired
    @Qualifier("s2SCommonBudgetService")
    protected S2SCommonBudgetService s2SCommonBudgetService;

    @Value("classpath:org/kuali/coeus/s2sgen/impl/generate/support/stylesheet/AdditionalEquipmentAttachment.xsl")
    private Resource additionalEquipmentAttachmentStyleSheet;

    @Value("classpath:org/kuali/coeus/s2sgen/impl/generate/support/stylesheet/ExtraKeyPersonAttachment.xsl")
    private Resource extraKeyPersonAttachmentStyleSheet;

	protected void deleteAutoGenNarratives() {
        narrativeCleanupService.deleteAutoGeneratedNarratives(pdDoc.getDevelopmentProposal().getNarratives());
	}
	
	protected NarrativeContract saveAdditionalEquipments(BudgetPeriodDto periodInfo,List<CostDto> extraEquipmentArrayList) {
        NarrativeContract narrative = null;
		if (extraEquipmentArrayList.size() > 0) {
			AdditionalEquipmentList additionalEquipmentList = AdditionalEquipmentList.Factory
					.newInstance();
			additionalEquipmentList.setProposalNumber(pdDoc
					.getDevelopmentProposal().getProposalNumber());
			additionalEquipmentList.setBudgetPeriod(new BigInteger(Integer
					.toString(periodInfo.getBudgetPeriod())));
			additionalEquipmentList
					.setEquipmentListArray(getEquipmentListArray(extraEquipmentArrayList));

			AdditionalEquipmentListDocument additionalEquipmentDoc = AdditionalEquipmentListDocument.Factory.newInstance();
			additionalEquipmentDoc.setAdditionalEquipmentList(additionalEquipmentList);
			Source xsltSource = null;
            try {
              xsltSource =  new StreamSource(additionalEquipmentAttachmentStyleSheet.getInputStream());
            } catch(IOException e) {
               throw new RuntimeException("the stream could not be opened",e);
            }
			Map<String, Source> xSLTemplateWithBookmarks = new HashMap<String, Source>();
			xSLTemplateWithBookmarks.put("", xsltSource);

			String xmlData = additionalEquipmentDoc.xmlText();
			Map<String, byte[]> streamMap = new HashMap<String, byte[]>();
			streamMap.put("", xmlData.getBytes());
			GenericPrintable printable = new GenericPrintable();
			printable.setXSLTemplateWithBookmarks(xSLTemplateWithBookmarks);
			printable.setStreamMap(streamMap);
			try {
				KcFile printData = s2SPrintingService
						.print(printable);
				String fileName=pdDoc.getDevelopmentProposal().getProposalNumber()+periodInfo.getBudgetPeriod()+ADDITIONAL_EQUIPMENT+".pdf";
				narrative = saveNarrative(printData.getData(),
						EQUIPMENT_NARRATIVE_TYPE_CODE,fileName,ADDITIONAL_EQUIPMENT);
			} catch (S2SException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return narrative;
	}

	private gov.grants.apply.coeus.additionalEquipment.AdditionalEquipmentListDocument.AdditionalEquipmentList.EquipmentList[] getEquipmentListArray(
			List<CostDto> extraEquipmentArrayList) {
		List<AdditionalEquipmentList.EquipmentList> additionalEquipmentListList = new ArrayList<AdditionalEquipmentList.EquipmentList>();
		AdditionalEquipmentList.EquipmentList equipmentList = null;
		for (CostDto costInfo : extraEquipmentArrayList) {
			equipmentList = AdditionalEquipmentList.EquipmentList.Factory
					.newInstance();
			equipmentList.setFundsRequested(costInfo.getCost()
					.bigDecimalValue());
			equipmentList
					.setEquipmentItem(costInfo.getDescription() != null ? costInfo
							.getDescription()
							: costInfo.getCategory());
			additionalEquipmentListList.add(equipmentList);
		}
		return additionalEquipmentListList
				.toArray(new gov.grants.apply.coeus.additionalEquipment.AdditionalEquipmentListDocument.AdditionalEquipmentList.EquipmentList[0]);
	}
	protected NarrativeContract saveExtraKeyPersons(BudgetPeriodDto periodInfo) {
        NarrativeContract extraKPNarrative = null;
		if (periodInfo.getExtraKeyPersons() != null && !periodInfo.getExtraKeyPersons().isEmpty()) {
			ExtraKeyPersonListDocument  extraKeyPersonListDocument = ExtraKeyPersonListDocument.Factory.newInstance();
			ExtraKeyPersonList extraKeyPersonList = ExtraKeyPersonList.Factory.newInstance(); 
			extraKeyPersonList.setProposalNumber(pdDoc.getDevelopmentProposal().getProposalNumber());
			extraKeyPersonList.setBudgetPeriod(new BigInteger(""+periodInfo.getBudgetPeriod()));
			extraKeyPersonList.setKeyPersonsArray(getExtraKeyPersons(periodInfo.getExtraKeyPersons()));
			extraKeyPersonListDocument.setExtraKeyPersonList(extraKeyPersonList);
			String xmlData = extraKeyPersonListDocument.xmlText();
			Map<String, byte[]> streamMap = new HashMap<String, byte[]>();
			streamMap.put("", xmlData.getBytes());
            Source xsltSource = null;
            try {
                xsltSource =  new StreamSource(extraKeyPersonAttachmentStyleSheet.getInputStream());
            } catch(IOException e) {
                throw new RuntimeException("the stream could not be opened",e);
            }
			Map<String, Source> xSLTemplateWithBookmarks = new HashMap<String, Source>();
			xSLTemplateWithBookmarks.put("", xsltSource);
			
			GenericPrintable printable = new GenericPrintable();
			printable.setXSLTemplateWithBookmarks(xSLTemplateWithBookmarks);
			printable.setStreamMap(streamMap);
			try {
				KcFile printData = s2SPrintingService.print(printable);
				String fileName = pdDoc.getDevelopmentProposal().getProposalNumber()+periodInfo.getBudgetPeriod()+"_"+EXTRA_KEYPERSONS+".pdf";
				extraKPNarrative = saveNarrative(printData.getData(), EXTRA_KEYPERSONS_TYPE, fileName, EXTRA_KEYPERSONS_COMMENT);
			} catch (S2SException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return extraKPNarrative;
	}
	private gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument.ExtraKeyPersonList.KeyPersons[] getExtraKeyPersons(List<KeyPersonDto> keyPersonList) {
		List<gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument.ExtraKeyPersonList.KeyPersons> keypersonslist = new ArrayList<gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument.ExtraKeyPersonList.KeyPersons>();
		for(KeyPersonDto keyPersonInfo : keyPersonList){
			gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument.ExtraKeyPersonList.KeyPersons keyPerson = gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument.ExtraKeyPersonList.KeyPersons.Factory.newInstance();
			keyPerson.setFirstName(keyPersonInfo.getFirstName());
			keyPerson.setMiddleName(keyPersonInfo.getMiddleName());
			keyPerson.setLastName(keyPersonInfo.getLastName());
			keyPerson.setProjectRole(keyPersonInfo.getRole());
			keyPerson.setCompensation(getExtraKeyPersonCompensation(keyPersonInfo));
			keypersonslist.add(keyPerson);
		}
		return keypersonslist.toArray(new gov.grants.apply.coeus.extraKeyPerson.ExtraKeyPersonListDocument.ExtraKeyPersonList.KeyPersons[0]);
	}
	private Compensation getExtraKeyPersonCompensation(
			KeyPersonDto keyPersonInfo) {
		Compensation compensation = Compensation.Factory.newInstance();
		if(keyPersonInfo.getAcademicMonths() != null){
			compensation.setAcademicMonths(keyPersonInfo.getAcademicMonths().bigDecimalValue());
		}
		if(keyPersonInfo.getBaseSalary() != null){
			compensation.setBaseSalary(keyPersonInfo.getBaseSalary().bigDecimalValue());
		}
		if(keyPersonInfo.getCalendarMonths() != null){
			compensation.setCalendarMonths(keyPersonInfo.getCalendarMonths().bigDecimalValue());
		}
		if(keyPersonInfo.getFringe() != null){
			compensation.setFringeBenefits(keyPersonInfo.getFringe().bigDecimalValue());
		}
		if(keyPersonInfo.getFundsRequested() != null){
			compensation.setFundsRequested(keyPersonInfo.getFundsRequested().bigDecimalValue());
		}
		if(keyPersonInfo.getRequestedSalary() != null){
			compensation.setRequestedSalary(keyPersonInfo.getRequestedSalary().bigDecimalValue());
		}
		if(keyPersonInfo.getSummerMonths() != null){
			compensation.setSummerMonths(keyPersonInfo.getSummerMonths().bigDecimalValue());
		}
		return compensation;
	}
   protected boolean isProposalPersonEqualsKeyPerson(ProposalPersonContract proposalPerson, KeyPersonDto keyPerson) {
        if(keyPerson.getPersonId()!=null){
            return keyPerson.getPersonId().equals(proposalPerson.getPersonId());
        }else if(keyPerson.getRolodexId()!=null){
            return keyPerson.getRolodexId().equals(proposalPerson.getRolodexId());
        }
        return false;
    }

   /**
    * This method check whether the key person has a personnel budget  
    * 
    * @param keyPerson
    *            (KeyPersonInfo) key person entry.
    * @param period
    *            budget period
    * @return true if key person has personnel budget else false.
    */
   protected Boolean hasPersonnelBudget(KeyPersonDto keyPerson,int period){
       List<? extends BudgetLineItemContract> budgetLineItemList = new ArrayList<BudgetLineItemContract>();

       ProposalDevelopmentBudgetExtContract budget = s2SCommonBudgetService.getBudget(pdDoc.getDevelopmentProposal());

        budgetLineItemList = budget.getBudgetPeriods().get(period-1).getBudgetLineItems();

        for(BudgetLineItemContract budgetLineItem : budgetLineItemList) {
            for(BudgetPersonnelDetailsContract budgetPersonnelDetails : budgetLineItem.getBudgetPersonnelDetailsList()){
                if( budgetPersonnelDetails.getPersonId().equals(keyPerson.getPersonId())){
                    return true;
                } else if (keyPerson.getRolodexId() != null && budgetPersonnelDetails.getPersonId().equals(keyPerson.getRolodexId().toString())) {
                    return true;
                }
            }
        }
       return false;       
   }
   
   /**
    * Perform manual validations on the budget. Similarly done in RRFedNonFedBudgetBaseGenerator due to object graph.
    * @param pdDoc
    * @return
    * @throws S2SException
    */
   protected boolean validateBudgetForForm(ProposalDevelopmentDocumentContract pdDoc) throws S2SException {
       boolean valid = true;

       ProposalDevelopmentBudgetExtContract budget = s2SCommonBudgetService.getBudget(pdDoc.getDevelopmentProposal());
       if(budget != null){
           for (BudgetPeriodContract period : budget.getBudgetPeriods()) {
               List<String> participantSupportCode = new ArrayList<String>();
               participantSupportCode.add(s2sBudgetCalculatorService.getParticipantSupportCategoryCode());
               List<? extends BudgetLineItemContract> participantSupportLineItems =
                       s2sBudgetCalculatorService.getMatchingLineItems(period.getBudgetLineItems(), participantSupportCode);
               int numberOfParticipants = period.getNumberOfParticipants() == null ? 0 : period.getNumberOfParticipants();
               if (!participantSupportLineItems.isEmpty() && numberOfParticipants == 0) {
                   AuditError auditError= s2SErrorHandlerService.getError(PARTICIPANT_COUNT_REQUIRED, getFormName());
                   AuditError error= new AuditError(auditError.getErrorKey(),
                          auditError.getMessageKey()+period.getBudgetPeriod(),auditError.getLink());
                   getAuditErrors().add(error);
                   valid = false;
               } else if (numberOfParticipants > 0 && participantSupportLineItems.isEmpty()) {
                   getAuditErrors().add(s2SErrorHandlerService.getError(PARTICIPANT_COSTS_REQUIRED, getFormName()));
                   valid = false;
               }
           }
       }
       return valid;
   }

    public S2SBudgetCalculatorService getS2sBudgetCalculatorService() {
        return s2sBudgetCalculatorService;
    }

    public NarrativeService getNarrativeCleanupService() {
        return narrativeCleanupService;
    }

    public void setS2sBudgetCalculatorService(S2SBudgetCalculatorService s2sBudgetCalculatorService) {
        this.s2sBudgetCalculatorService = s2sBudgetCalculatorService;
    }

    public void setNarrativeCleanupService(NarrativeService narrativeCleanupService) {
        this.narrativeCleanupService = narrativeCleanupService;
    }

    public S2SPrintingService getS2SPrintingService() {
        return s2SPrintingService;
    }

    public void setS2SPrintingService(S2SPrintingService s2SPrintingService) {
        this.s2SPrintingService = s2SPrintingService;
    }

    public S2SDateTimeService getS2SDateTimeService() {
        return s2SDateTimeService;
    }

    public void setS2SDateTimeService(S2SDateTimeService s2SDateTimeService) {
        this.s2SDateTimeService = s2SDateTimeService;
    }

    public S2SErrorHandlerService getS2SErrorHandlerService() {
        return s2SErrorHandlerService;
    }

    public void setS2SErrorHandlerService(S2SErrorHandlerService s2SErrorHandlerService) {
        this.s2SErrorHandlerService = s2SErrorHandlerService;
    }

    public S2SCommonBudgetService getS2SCommonBudgetService() {
        return s2SCommonBudgetService;
    }

    public void setS2SCommonBudgetService(S2SCommonBudgetService s2SCommonBudgetService) {
        this.s2SCommonBudgetService = s2SCommonBudgetService;
    }

    public Resource getAdditionalEquipmentAttachmentStyleSheet() {
        return additionalEquipmentAttachmentStyleSheet;
    }

    public void setAdditionalEquipmentAttachmentStyleSheet(Resource additionalEquipmentAttachmentStyleSheet) {
        this.additionalEquipmentAttachmentStyleSheet = additionalEquipmentAttachmentStyleSheet;
    }

    public Resource getExtraKeyPersonAttachmentStyleSheet() {
        return extraKeyPersonAttachmentStyleSheet;
    }

    public void setExtraKeyPersonAttachmentStyleSheet(Resource extraKeyPersonAttachmentStyleSheet) {
        this.extraKeyPersonAttachmentStyleSheet = extraKeyPersonAttachmentStyleSheet;
    }
}
