/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Iterator;
import uk.ac.ebi.jmzidml.model.mzidml.*;
import uk.ac.ebi.jmzidml.MzIdentMLElement;

/**
 *
 * @author Adelyne
 */
public class PeptideInfo {
    
    private HashMap <String, String> getSoftwareScoreHashMap() {
        HashMap <String, String> softwareScoreHashMap = new HashMap();       
        
        softwareScoreHashMap.put("MS-GF+", "MS:1002049"); // MS-GF+, MS-GF+ Raw Score
        softwareScoreHashMap.put("Mascot", "MS:1001171"); // Mascot, Mascot: Score
        softwareScoreHashMap.put("Sequest", "MS:1001154"); // Sequest, Sequest Probability
              
        return softwareScoreHashMap;
    }
    
    private SortedMap <Double, ArrayList<SpectrumIdentificationItem>> getScoreSiiSortedMap() {
        SortedMap <Double, ArrayList<SpectrumIdentificationItem>> scoreSiiSortedMap = new TreeMap();
        
        PeptideInfo peptideInfo = new PeptideInfo();      
        HashMap <String, String> softwareScoreHashMap = peptideInfo.getSoftwareScoreHashMap();
        ArrayList <String> softwareList = new ArrayList <String>(softwareScoreHashMap.keySet());
              
        Metadata metadataPeptide = new Metadata();
        String analysisSoftware = metadataPeptide.getSoftwareName();

                
        Iterator <SpectrumIdentificationItem> iterSII = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath
                (MzIdentMLElement.SpectrumIdentificationItem);
        while (iterSII.hasNext()) {
            SpectrumIdentificationItem sii = iterSII.next();
            //Jun: how safe it is to assume that first CV param is the score. remember coding according to the xsd
            
            int cvParamNum = 0;
        
            if (softwareList.contains(analysisSoftware)) {
                String scoreName = softwareScoreHashMap.get(analysisSoftware);
                String softwareNameCvParam = sii.getCvParam().get(cvParamNum).getName();
                while (!softwareNameCvParam.equals(scoreName)) {
                    cvParamNum = cvParamNum + 1;
                }
            }
            
            Double score = Double.parseDouble(sii.getCvParam().get(cvParamNum).getValue());
        
            if (scoreSiiSortedMap.containsKey(score)) {
                ArrayList siiList = scoreSiiSortedMap.get(score);
                siiList.add(sii);
                scoreSiiSortedMap.put(score, siiList);
            }
            
            else {
                ArrayList <SpectrumIdentificationItem> siiList = new ArrayList <SpectrumIdentificationItem>();
                siiList.add(sii);
                scoreSiiSortedMap.put(score, siiList);            
            }
        }
        return scoreSiiSortedMap;
    }        
                             
    private List getSpectrumIdentificationList() {  
        DataCollection dc =  MzidToHTML.unmarshaller.unmarshal(DataCollection.class);
        AnalysisData ad = dc.getAnalysisData();  
        List<SpectrumIdentificationList> sil = ad.getSpectrumIdentificationList();
        
        return sil;
    }
    
    List<String> getPeptideInfo() {
        
        MzidData mzidDataPeptide = new MzidData();     
        PeptideInfo peptideInfo = new PeptideInfo();
        Metadata metadataPeptide = new Metadata();
        PeptideEvidenceData peptideEvidencePeptide = new PeptideEvidenceData();
        
        
        // Get the hashmaps required for accessing information for each SpectrumIdentificationItem       
        HashMap<String, Peptide> peptideIdHashMap = mzidDataPeptide.getPeptideIdHashMap();
        HashMap<String, DBSequence> dbSequenceIdHashMap = mzidDataPeptide.getDbSequenceIdHashMap();
        HashMap<String, PeptideEvidence> peptideEvidenceIdHashMap = peptideEvidencePeptide.getPeptideEvidenceIdHashMap();      
        HashMap <String, String> softwareScoreHashMap = peptideInfo.getSoftwareScoreHashMap();
        
        
        // Sorted map for arranging by score
        // Keys are the first element of score
        // Values are the SpectrumIdentificationItems
        SortedMap <Double, ArrayList <SpectrumIdentificationItem>> scoreSiiSortedMap = peptideInfo.getScoreSiiSortedMap();
        ArrayList <Double> scores = new ArrayList<Double>(scoreSiiSortedMap.keySet());
                   
        // Initialise variables for the items that are to be returned
            // StringBuilder to be returned as a string containing all the peptide information
            // Single string representing a single row in the HTML table (with all elements included)
        StringBuilder peptideInfoBuilder = new StringBuilder();
        
            // ScoreName returned separately to be incorporated in table header
        String scoreName = new String();
        
        // Get the SII for each key-value pair in the SortedMap
        // Extract peptide info for table from the SII value                  
        for (int scoreNum = scores.size()-1; scoreNum >= 0; scoreNum--) {
            ArrayList <SpectrumIdentificationItem> spectrumIdentItemList = scoreSiiSortedMap.get(scores.get(scoreNum));
            
            for (int siiNum = 0; siiNum < spectrumIdentItemList.size(); siiNum ++) {
                SpectrumIdentificationItem spectrumIdentItem = spectrumIdentItemList.get(siiNum);
                
                int cvParamNum = 0;
                String analysisSoftware = metadataPeptide.getSoftwareName();
                ArrayList <String> softwareList = new ArrayList <String>(softwareScoreHashMap.keySet());
               
                if (softwareList.contains(analysisSoftware)) {
                    String scoreNameLookup = softwareScoreHashMap.get(analysisSoftware);
                    String softwareNameCvParam = spectrumIdentItem.getCvParam().get(cvParamNum).getName();
                    while (!softwareNameCvParam.equals(scoreNameLookup)) {
                        cvParamNum = cvParamNum + 1;
                    }
                }
                scoreName = spectrumIdentItem.getCvParam().get(cvParamNum).getName();

                // PSM ID
                String spectrumIdItem = spectrumIdentItem.getId();
                      
                // Peptide Sequence
                String peptideSequence = peptideIdHashMap.get(spectrumIdentItem.getPeptideRef()).getPeptideSequence();
            
                // Calculated m/z ratio
                Double calculatedMassToCharge =  spectrumIdentItem.getCalculatedMassToCharge();
            
                // Experimental m/z ratio
                Double experimentalMassToCharge = spectrumIdentItem.getExperimentalMassToCharge(); 
            
                // Charge
                int charge = spectrumIdentItem.getChargeState();
            
                // Modifications
                List <Modification> modifications = peptideIdHashMap.get(spectrumIdentItem.getPeptideRef()).getModification();
                    StringBuilder modificationsBuilder = new StringBuilder();
                    
                    if (peptideIdHashMap.get(spectrumIdentItem.getPeptideRef()).getModification().isEmpty()) {
                        modificationsBuilder.append("None");
                    }
                    
                    else {
                        for (Modification modification: peptideIdHashMap.get(spectrumIdentItem.getPeptideRef()).getModification()) {
                            modificationsBuilder.append(modification);
                        }
                    }

                // Associated Proteins                   
                List<PeptideEvidenceRef> peptideEvidenceRefList = spectrumIdentItem.getPeptideEvidenceRef();
                String proteinName = new String();
            
                for (int i = 0; i < peptideEvidenceRefList.size(); i++) {
                    PeptideEvidence peptideEvidence = peptideEvidenceIdHashMap.get(peptideEvidenceRefList.get(i).getPeptideEvidenceRef());

                    DBSequence dbSeq = dbSequenceIdHashMap.get(peptideEvidence.getDBSequenceRef());
                    List<CvParam> dbSeqCvParamList = dbSeq.getCvParam();
                        
                    for (int x = 0; x < dbSeqCvParamList.size(); x++) {
                        proteinName = dbSeqCvParamList.get(x).getValue();
                    }
                }
            
            String printSpectrumIdItem = "<td> " + spectrumIdItem + " </td>";
            String printSequence = "<td> " + peptideSequence + " </td>";
            String printCalculatedMassToCharge = "<td> <div style = \"text-align:right\">" + String.format("%.2f", calculatedMassToCharge) + " </td>";
            String printExperimentalMassToCharge = "<td> <div style = \"text-align:right\">" + String.format("%.2f", experimentalMassToCharge) + " </td>";
            String printCharge = "<td> <div style = \"text-align:center\">" + charge + " </td>";
            String printModifications = "<td> " + modificationsBuilder.toString() + " </td>";
            String printScore = "<td> <div style = \"text-align:center\">" + scores.get(scoreNum) + " </td>";
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
                                 
        List<String> peptideInfoReturn = new ArrayList<String>();
        peptideInfoReturn.add(peptideInfoBuilder.toString());
        peptideInfoReturn.add(scoreName);

        return peptideInfoReturn;
    }
}
        

 