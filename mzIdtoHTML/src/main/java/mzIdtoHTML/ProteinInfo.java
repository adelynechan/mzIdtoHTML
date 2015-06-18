/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionList;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;

/**
 *
 * @author Adelyne
 */
public class ProteinInfo {
    
    ProteinDetectionList getProteinDetectionList() {  
        DataCollection dc =  MzidToHTML.unmarshaller.unmarshal(DataCollection.class);
        AnalysisData ad = dc.getAnalysisData();  
        ProteinDetectionList pdl = ad.getProteinDetectionList();
        
        return pdl;
    }
   
    List <String> getProteinInfo() {
        ProteinInfo proteinInfo = new ProteinInfo();
        ProteinPeptideData ppdProtein = new ProteinPeptideData();
        MzidData mzidDataProtein = new MzidData();

        SortedMap <Double, ArrayList<ProteinDetectionHypothesis>> scorePdhSortedMap = ppdProtein.getScorePdhSortedMap();
        HashMap <ProteinDetectionHypothesis, ArrayList<String>> pdhPeptideSeqHashMap = ppdProtein.getPdhPeptideSeqHashMap();
        HashMap<String, DBSequence> proteinDbSequenceIdHashMap = mzidDataProtein.getDbSequenceIdHashMap();
        
        StringBuilder proteinInfoBuilder = new StringBuilder();
        String pdhScoreType = new String();
        int minPeptides = 1000;
        int maxPeptides = 0;
        
        // Start extracting information from each individual pdh
        ArrayList <Double> scoresPdh = new ArrayList<Double>(scorePdhSortedMap.keySet());
        
        for (int scorePdhNum = scoresPdh.size()-1; scorePdhNum >= 0; scorePdhNum--) {
            ArrayList <ProteinDetectionHypothesis> pdhList = scorePdhSortedMap.get(scoresPdh.get(scorePdhNum));
            if (!pdhList.isEmpty()) {
            
                for (int pdhNum = 0; pdhNum < pdhList.size(); pdhNum ++) {
                    ProteinDetectionHypothesis pdh = pdhList.get(pdhNum);
                
                    // Initialise the strings for values which are going to be extracted from the CVParam            
                    String accessionCode = new String();
                    String speciesName = new String();
                    String proteinName = "<td> Not Available </td>"; // Cv param with full name is not always available
                    String pdhScore = new String();
                
                    // Get the DBSequence ID from the PDH and then look up the DBSequence from the hashmap    
                    // Get the sequence of each PDH from the DbSequence HashMap
                    DBSequence proteinDbSeq = proteinDbSequenceIdHashMap.get(pdh.getDBSequenceRef());
                    String proteinDbAccession = proteinDbSeq.getAccession();
                    
                    Pattern patternAcc = Pattern.compile("(.*?)\\|(.*?)\\|");
                    Matcher matcherAcc = patternAcc.matcher(proteinDbAccession);
                    if (matcherAcc.find()) {
                        accessionCode = "<td>" + matcherAcc.group(2) + "</td>";
                    }
                    
                    // The code for the species is extracted from the Db Accession 
                    // Because sometimes the full species name (in the CvParam) is not available
                    // In these cases only the 5-alphanumeric character code for species is shown
                    Pattern patternSpeShort = Pattern.compile(".*_(.*)$");
                    Matcher matcherSpeShort = patternSpeShort.matcher(proteinDbAccession);
                    String speciesNameShort = new String();

                    String speciesNameFull = " ";
                    if (matcherSpeShort.find()) {
                        speciesNameShort = matcherSpeShort.group(1);
                    }
                                
                    // Get the list of CvParam which are associated with DBSequence
                    // <xsd:group ref="ParamGroup" minOccurs="0" maxOccurs="unbounded">
                    // <xsd:documentation> Additional descriptors for the sequence, such as taxon, description line etc
                    List<CvParam> DbSeqCvParamList = proteinDbSeq.getCvParam();
                
                    // Iterate through the list of DBSeq CV Param (includes protein sequence)
                    for (int DbSeqCvParamNum = 0; DbSeqCvParamNum < DbSeqCvParamList.size(); DbSeqCvParamNum++) {                   
                    
                        // Get an individual DbSeq CV Param from the list
                        CvParam DbSeqCvParam = DbSeqCvParamList.get(DbSeqCvParamNum);
                    
                        // The protein name is the value of the CV Param with the associated name = "protein description"
                        if (DbSeqCvParam.getName().equals("protein description")) {                      
                            String proteinNameFull = DbSeqCvParam.getValue(); 
                    
                            // Regex to extract the accession code, species name and protein name
                    
                            Pattern patternSpe = Pattern.compile("OS=(.*?)GN");
                            Matcher matcherSpe = patternSpe.matcher(proteinNameFull);
                             
                            Pattern patternName = Pattern.compile("\\s(.*?)OS=");
                            Matcher matcherName = patternName.matcher(proteinNameFull);
                   
                            if (matcherSpe.find()) {
                                speciesNameFull = "- " + matcherSpe.group(1);
                            }
                            
                            if (matcherName.find()) {                                           
                                proteinName = "<td>" + matcherName.group(1) + "</td>";                         
                            }  
                        }              
                    }
                    
                    speciesName = "<td>" + speciesNameShort + speciesNameFull + "</td>";
                
                    // Get the CV Param list associated with each PDH 
                    // <xsd:group ref="ParamGroup" minOccurs="0" maxOccurs="unbounded">
                    // <xsd:documentation> Scores or parameters associated with this ProteinDetectionHypothesis e.g. p-value
                    List <CvParam> pdhCvParamList = pdhList.get(pdhNum).getCvParam();
                
                    // Extract the score which is stored as a value in the CvParam
                    // Also extract the type of score 
                    for (int pdhCvParamNum = 0; pdhCvParamNum < pdhCvParamList.size(); pdhCvParamNum++) {
                        CvParam pdhCvParam = pdhCvParamList.get(pdhCvParamNum);
                        if (pdhCvParam.getName().contains("score")) {
                            // Type of score that is displayed in the Protein View table
                            // Will usually be PDH Score 
                            // A PDHScore is created for each PDH by summing the PSM scores according to the user parameters (Ghali 2013)
                            pdhScoreType = pdhCvParam.getName();
                            Double pdhScoreDouble = Double.parseDouble(pdhCvParam.getValue());
                            pdhScore = "<td> <div style = \"text-align:center\">" + String.format("%.2f", pdhScoreDouble) + "</td>";
                        }           
                    }                  
                    
                    int proteinLength = proteinDbSequenceIdHashMap.get(pdh.getDBSequenceRef()).getLength();
                    Double totalPeptideLength = 0.0;
                    
                    ArrayList <String> peptideSeqList = pdhPeptideSeqHashMap.get(pdh);
                    if (peptideSeqList != null) {
                        for (int pepSeqNum = 0; pepSeqNum < peptideSeqList.size(); pepSeqNum++) {
                            String peptideSeq = peptideSeqList.get(pepSeqNum);
                            int peptideLength = peptideSeq.length();
                            totalPeptideLength = totalPeptideLength + peptideLength;
                        }
                    }
                    
                    Double totalPeptideCoverage = (totalPeptideLength / proteinLength) * 100;                   
                    String peptideCoverageString = "<td> <div style = \"text-align:center\">" 
                            + String.format("%.2f", totalPeptideCoverage) + "</td>";
                    
                    // Get peptide number for the pdh
                    int currentPdhPeptideCount = peptideSeqList.size();
                    if (currentPdhPeptideCount > maxPeptides) {
                        maxPeptides = currentPdhPeptideCount;
                    }
                    
                    else if (currentPdhPeptideCount < minPeptides && currentPdhPeptideCount > 0) {
                        minPeptides = currentPdhPeptideCount;
                    }
                    
                    proteinInfoBuilder.append("\n<tr>");
                    proteinInfoBuilder.append(accessionCode);
                    proteinInfoBuilder.append(speciesName);
                    proteinInfoBuilder.append(proteinName);
                    proteinInfoBuilder.append(pdhScore);
                    proteinInfoBuilder.append(peptideCoverageString); // for peptide coverage
                    proteinInfoBuilder.append("</tr>");                      
                }
            }
        }
        
        List proteinInfoReturn = new ArrayList <String>();
        proteinInfoReturn.add(proteinInfoBuilder.toString());
        proteinInfoReturn.add(pdhScoreType);
        proteinInfoReturn.add(Integer.toString(minPeptides));
        proteinInfoReturn.add(Integer.toString(maxPeptides));
        
        
        return proteinInfoReturn;
    }
}
    
