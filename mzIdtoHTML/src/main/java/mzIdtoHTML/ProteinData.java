/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;

/**
 *
 * @author Adelyne
 */
public class ProteinData {
    
    MzidData mzidDataProteinData = new MzidData();
    HashMap<String, DBSequence> proteinDbSequenceIdHashMap = mzidDataProteinData.getDbSequenceIdHashMap();
    
    ProteinPeptideData ppdProteinData = new ProteinPeptideData();
    SortedMap <Double, ArrayList <ProteinDetectionHypothesis>> scorePdhSortedMap = ppdProteinData.getScorePdhSortedMap();
    HashMap <ProteinDetectionHypothesis, ArrayList <String>> pdhPeptideSeqHashMap = ppdProteinData.getPdhPeptideSeqHashMap();
    
    PeptideLocation pepLocationProteinData = new PeptideLocation();
    
    HashMap <String, ProteinDetectionHypothesis> proteinAccPdhHashMap = new HashMap();
    
    ArrayList <String> getProteinDataTable(ProteinDetectionHypothesis pdh) {
        
        StringBuilder proteinTableRowBuilder = new StringBuilder();
        
        // Initialise the strings for values which are going to be extracted from the CVParam            
        String accessionCode = new String();
        String speciesName = new String();
        String proteinName = "<td> Not Available </td>"; // Cv param with full name is not always available
        String pdhScore = new String();
        String pdhScoreType = new String();
                
        // Get the DBSequence ID from the PDH and then look up the DBSequence from the hashmap    
        // Get the sequence of each PDH from the DbSequence HashMap
        DBSequence proteinDbSeq = proteinDbSequenceIdHashMap.get(pdh.getDBSequenceRef());
        String proteinDbAccession = proteinDbSeq.getAccession();
        
        // Extract protein accession code from the Accession Number associated with DBSequence
        Pattern patternAcc = Pattern.compile("(.*?)\\|(.*?)\\|");
        Matcher matcherAcc = patternAcc.matcher(proteinDbAccession);
        if (matcherAcc.find()) {
            accessionCode = "<td>" + matcherAcc.group(2)  
                + "<div class = \"proteins\" id=" + matcherAcc.group(2) + ">Details</a></div>" // Section Id value same as protein accession code           
                + "<br><a href=\"http://www.uniprot.org/uniprot/" + matcherAcc.group(2) + "\">UniProt</a> \n</td>";
            proteinAccPdhHashMap.put(matcherAcc.group(2), pdh);
            
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
            // If there is a CvParam where the name is protein description then the following code is executed
            // Extract full protein name and full species name
            if (DbSeqCvParam.getName().equals("protein description")) {                      
                String proteinNameFull = DbSeqCvParam.getValue(); 
                    
                // Regex to extract the accession code, species name and protein name from CvParam
                // This is not always available, filler values e.g. "Not Available" 
                // or short forms extracted from DB Sequence will be put if this is not available             
                
                // Full name of species (short form extracted from DB Sequence)
                Pattern patternSpe = Pattern.compile("OS=(.*?)GN");
                Matcher matcherSpe = patternSpe.matcher(proteinNameFull);              
                if (matcherSpe.find()) {
                    speciesNameFull = "- " + matcherSpe.group(1);
                }
                
                // Full name of protein (replace default "Not Available" value)               
                Pattern patternName = Pattern.compile("\\s(.*?)OS=");
                Matcher matcherName = patternName.matcher(proteinNameFull);                                                             
                if (matcherName.find()) {                                           
                    proteinName = "<td>" + matcherName.group(1) + "</td>";                         
                }  
            }              
        }
                    
        speciesName = "<td>" + speciesNameShort + speciesNameFull + "</td>";
                
        // Get the CV Param list associated with each PDH 
        // <xsd:group ref="ParamGroup" minOccurs="0" maxOccurs="unbounded">
        // <xsd:documentation> Scores or parameters associated with this ProteinDetectionHypothesis e.g. p-value
        List <CvParam> pdhCvParamList = pdh.getCvParam();
                
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
                       
        Double totalPeptideCoverage = pepLocationProteinData.getPeptideCoverage(proteinDbSeq);
        String peptideCoverageString = "<td> <div style = \"text-align:center\">" 
            + String.format("%.2f", totalPeptideCoverage) + "</td>";       
        
        proteinTableRowBuilder.append("\n<tr>");
        proteinTableRowBuilder.append(accessionCode);
        proteinTableRowBuilder.append(speciesName);
        proteinTableRowBuilder.append(proteinName);
        proteinTableRowBuilder.append(pdhScore);
        proteinTableRowBuilder.append(peptideCoverageString); // for peptide coverage
        proteinTableRowBuilder.append("</tr>");
            
        ArrayList <String> proteinDataReturn = new ArrayList();
        proteinDataReturn.add(proteinTableRowBuilder.toString()); // add all the table rows here
        proteinDataReturn.add(pdhScoreType); // add the type of PDH score (to be used in table header)
        
        return proteinDataReturn;
    } 
    
    HashMap <String, ProteinDetectionHypothesis> getproteinAccDbSeqHashMap() {
        return proteinAccPdhHashMap;
    }
}
