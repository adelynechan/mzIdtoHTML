/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.ebi.jmzidml.model.mzidml.*;
import uk.ac.ebi.jmzidml.MzIdentMLElement;

/**
 *
 * @author Adelyne
 */
public class PeptideInfo {
    
    public HashMap <String, Peptide> getPeptideIdHashmap () {
        HashMap<String, Peptide> peptideIdHashMap = new HashMap();
        Iterator<Peptide> iterPeptide = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.Peptide);
            while (iterPeptide.hasNext()) {
                Peptide peptide = iterPeptide.next();
                peptideIdHashMap.put(peptide.getId(), peptide);
            }   
        return peptideIdHashMap;
    }
    
    public HashMap <String, DBSequence> getDbSequenceIdHashMap() {
        HashMap<String, DBSequence> dbSequenceIdHashMap = new HashMap();
        Iterator<DBSequence> iterDBSequence = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.DBSequence);
            while (iterDBSequence.hasNext()) {
                DBSequence dbSequence = iterDBSequence.next();
                dbSequenceIdHashMap.put(dbSequence.getId(), dbSequence);
            }
        return dbSequenceIdHashMap;
    }
    
    public HashMap <String, PeptideEvidence> getPeptideEvidenceIdHashMap() {
        HashMap<String, PeptideEvidence> peptideEvidenceIdHashMap = new HashMap();
        Iterator<PeptideEvidence> iterPeptideEvidence = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.PeptideEvidence);
            while (iterPeptideEvidence.hasNext()) {
                PeptideEvidence peptideEvidence = iterPeptideEvidence.next();
                peptideEvidenceIdHashMap.put(peptideEvidence.getId(), peptideEvidence);
            }
        return peptideEvidenceIdHashMap;
    }
       
    public List getSpectrumIdentificationList() {  
        DataCollection dc =  MzidToHTML.unmarshaller.unmarshal(DataCollection.class);
        AnalysisData ad = dc.getAnalysisData();  
        List<SpectrumIdentificationList> sil = ad.getSpectrumIdentificationList();
        
        return sil;
    }
    
    List<String> getPeptideInfo() {
        
        // Get the hashmaps required for accessing information for each SpectrumIdentificationItem
        PeptideInfo peptideInfo = new PeptideInfo();
        HashMap<String, Peptide> peptideIdHashMap = peptideInfo.getPeptideIdHashmap();
        HashMap<String, DBSequence> dbSequenceIdHashMap = peptideInfo.getDbSequenceIdHashMap();
        HashMap<String, PeptideEvidence> peptideEvidenceIdHashMap = peptideInfo.getPeptideEvidenceIdHashMap();
        
        // Get the list of Spectrum Identification elements
        List<SpectrumIdentificationList> sil = peptideInfo.getSpectrumIdentificationList();
        
        // Initialise variables for the items that are to be returned
            // StringBuilder to be returned as a string containing all the peptide information
            // Single string representing a single row in the HTML table (with all elements included)
        StringBuilder peptideInfoBuilder = new StringBuilder();
        
            // ScoreName returned separately to be incorporated in table header
        String scoreName = new String();

        // For each element in the list, extract the SpectrumIdentificationResult
        // For each of the SpectrumIdentificationResult, extract the SpectrumIdentificationItem
        // SpectrumIdentificationItem contains information related to the PSM
        for (SpectrumIdentificationList sIdentList : sil) {
            for (SpectrumIdentificationResult spectrumIdentResult: sIdentList.getSpectrumIdentificationResult()) {
                for (SpectrumIdentificationItem spectrumIdentItem: spectrumIdentResult.getSpectrumIdentificationItem()) {

                    // Column 1: SII number
                    String spectrumIdItem = spectrumIdentItem.getId();
                    
                    // Column 2: Peptide sequence
                    String peptideRef = spectrumIdentItem.getPeptideRef();
                    Peptide peptide = peptideIdHashMap.get(peptideRef);
                    String sequence = peptide.getPeptideSequence();                                 
                                       
                    // Column 3: Calculated mass to charge ratio
                    Double calculatedMassToCharge =  spectrumIdentItem.getCalculatedMassToCharge();
                    
                    // Column 4: Experimental mass to charge ratio
                    Double experimentalMassToCharge = spectrumIdentItem.getExperimentalMassToCharge();
                    
                    // Column 5: Charge
                    int charge = spectrumIdentItem.getChargeState();
                    
                    // Column 6: Modifications
                    
                    
                    // Column 7: Score
                    // The first element of score is used 
                    // The type of score is represented in the header column
                    List<CvParam> spectIdentParam = spectrumIdentItem.getCvParam();
                    CvParam firstScore = spectIdentParam.get(0);
                    scoreName = firstScore.getName();
                    String scoreValue = firstScore.getValue();
                    
                    // Column 8: Associated proteins
                    List<PeptideEvidenceRef> peptideEvidenceRefList = spectrumIdentItem.getPeptideEvidenceRef();
                    String proteinName = new String();
                    
                    for (int i = 0; i < peptideEvidenceRefList.size(); i++) {
                        PeptideEvidenceRef peptideEvidenceRef = peptideEvidenceRefList.get(i);
                        PeptideEvidence peptideEvidence = peptideEvidenceIdHashMap.get(peptideEvidenceRef.getPeptideEvidenceRef());

                        DBSequence dbSeq = dbSequenceIdHashMap.get(peptideEvidence.getDBSequenceRef());
                        List<CvParam> dbSeqCvParamList = dbSeq.getCvParam();
                        
                        for (int x = 0; x < dbSeqCvParamList.size(); x++) {
                            CvParam dbSeqCvParam = dbSeqCvParamList.get(x);
                            proteinName = dbSeqCvParam.getValue();
                        }
                    }    
                                                                               
                    String printSpectrumIdItem = "<td> " + spectrumIdItem + " </td>";
                    String printSequence = "<td> " + sequence + " </td>";
                    String printCalculatedMassToCharge = "<td> " + calculatedMassToCharge + " </td>";
                    String printExperimentalMassToCharge = "<td> " + experimentalMassToCharge + " </td>";
                    String printCharge = "<td> " + charge + " </td>";
                    String printModifications = "<td> " + " " + " </td>";
                    String printScore = "<td> " + scoreValue + " </td>";
                    String printAssociatedProteins = "<td> " + proteinName + " </td>";
                    
                    peptideInfoBuilder.append("<tr>");
                    peptideInfoBuilder.append(printSpectrumIdItem);
                    peptideInfoBuilder.append(printSequence); 
                    peptideInfoBuilder.append(printCalculatedMassToCharge);
                    peptideInfoBuilder.append(printExperimentalMassToCharge);
                    peptideInfoBuilder.append(printCharge);
                    peptideInfoBuilder.append(printModifications);
                    peptideInfoBuilder.append(printScore);
                    peptideInfoBuilder.append(printAssociatedProteins);
                    peptideInfoBuilder.append("</tr>");                                    
                }                                                                                  
            }                         
        }  
        
        List<String> peptideInfoReturn = new ArrayList<String>();
        peptideInfoReturn.add(peptideInfoBuilder.toString());
        peptideInfoReturn.add(scoreName);
        
        return peptideInfoReturn;
    }
}
