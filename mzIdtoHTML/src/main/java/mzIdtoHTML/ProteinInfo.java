/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
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
    
    public SortedMap <Double, ArrayList<ProteinDetectionHypothesis>> getScorePdhSortedMap() {
        SortedMap <Double, ArrayList <ProteinDetectionHypothesis>> scorePdhSortedMap = new TreeMap();
         
        ProteinInfo proteinInfo = new ProteinInfo();
        ProteinDetectionList pdl = proteinInfo.getProteinDetectionList();
        
        Iterator <ProteinDetectionHypothesis> iterPDH = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath
                (MzIdentMLElement.ProteinDetectionHypothesis);
        
        while (iterPDH.hasNext()) {
            ProteinDetectionHypothesis pdh = iterPDH.next();
            
            List <CvParam> pdhCvParamList = pdh.getCvParam();
            
            int pdhCvParamNum = 0;
            CvParam pdhCvParam = pdhCvParamList.get(pdhCvParamNum);
            while (!pdhCvParam.getName().contains("score")) {
                pdhCvParamNum = pdhCvParamNum + 1;
                pdhCvParam = pdhCvParamList.get(pdhCvParamNum);
            }
            
            Double pdhScore = Double.parseDouble(pdhCvParam.getValue());
            if (scorePdhSortedMap.containsKey(pdhScore)) {
                ArrayList pdhList = scorePdhSortedMap.get(pdhScore);
                pdhList.add(pdh);
                scorePdhSortedMap.put(pdhScore, pdhList);
            }
            
            else {
                ArrayList <ProteinDetectionHypothesis> pdhList = new ArrayList <ProteinDetectionHypothesis>();
                pdhList.add(pdh);
                scorePdhSortedMap.put(pdhScore, pdhList);
            }
        }      
        return scorePdhSortedMap;
    }
                           

   
    List <String> getProteinInfo() {
        ProteinInfo proteinInfo = new ProteinInfo();
        SortedMap <Double, ArrayList<ProteinDetectionHypothesis>> scorePdhSortedMap = proteinInfo.getScorePdhSortedMap();
        
        // Retrieve the HashMap which maps DBSequence IDs to DBSequence
        MzidData mzidDataProtein = new MzidData();
        HashMap<String, DBSequence> proteinDbSequenceIdHashMap = mzidDataProtein.getDbSequenceIdHashMap();
        
        StringBuilder proteinInfoBuilder = new StringBuilder();
        String pdhScoreType = new String();
        
        ArrayList <Double> scoresPdh = new ArrayList<Double>(scorePdhSortedMap.keySet());
        
        for (int scorePdhNum = scoresPdh.size()-1; scorePdhNum >= 0; scorePdhNum--) {
            ArrayList <ProteinDetectionHypothesis> pdhList = scorePdhSortedMap.get(scoresPdh.get(scorePdhNum));
            if (pdhList.size() != 0) {
            
                for (int pdhNum = 0; pdhNum < pdhList.size(); pdhNum ++) {
                    ProteinDetectionHypothesis pdh = pdhList.get(pdhNum);
                
                    // Initialise the strings for values which are going to be extracted from the CVParam            
                    String accessionCode = new String();
                    String speciesName = new String();
                    String proteinName = new String();
                    String pdhScore = new String();
                    String obsObs = new String();           
                
                    // Get the DBSequence ID from the PDH and then look up the DBSequence from the hashmap    
                    // Get the sequence of each PDH from the DbSequence HashMap
                    DBSequence proteinDbSeq = proteinDbSequenceIdHashMap.get(pdh.getDBSequenceRef());
                
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
                            Pattern patternAcc = Pattern.compile("tr\\|(.*?)\\|");
                            Matcher matcherAcc = patternAcc.matcher(proteinNameFull);
                    
                            Pattern patternSpe = Pattern.compile("OS=(.*?)GN");
                            Matcher matcherSpe = patternSpe.matcher(proteinNameFull);
                    
                            Pattern patternName = Pattern.compile("\\s(.*?)OS=");
                            Matcher matcherName = patternName.matcher(proteinNameFull);
                    
                            // Format with HTML tags <td> because these variables are to be added to protein view table
                            if (matcherAcc.find() && matcherSpe.find() && matcherName.find()) {
                                accessionCode = "<td>" + matcherAcc.group(1) + "</td>";                                             
                                speciesName = "<td>" + matcherSpe.group(1) + "</td>"; 
                                proteinName = "<td>" + matcherName.group(1) + "</td>";                         
                            }  
                        }
                    }
                
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
                            pdhScore = "<td>" + String.format("%.2f", pdhScoreDouble) + "</td>";
                        }
                    }              
                    proteinInfoBuilder.append("<tr>");
                    proteinInfoBuilder.append(accessionCode);
                    proteinInfoBuilder.append(speciesName);
                    proteinInfoBuilder.append(proteinName);
                    proteinInfoBuilder.append(pdhScore);
                    proteinInfoBuilder.append(obsObs);
                    proteinInfoBuilder.append("</tr>");                      
                }
            }
        }
        
        List proteinInfoReturn = new ArrayList <String>();
        proteinInfoReturn.add(proteinInfoBuilder.toString());
        proteinInfoReturn.add(pdhScoreType);
        
        return proteinInfoReturn;
    }
}
    
//    List <String> getProteinInfo() {
//        MzidData mzidDataProtein = new MzidData();
//        ProteinInfo proteinInfo = new ProteinInfo();
//        
//        // Get ProteinDetectionList 
//        // <xsd:element name="ProteinDetectionList" type="ProteinDetectionListType" minOccurs="0"/>
//        ProteinDetectionList pdl = proteinInfo.getProteinDetectionList();
//        
//        // Get Protein Ambiguity Groups from the pdl if pdl is not null
//        // <xsd:element name="ProteinAmbiguityGroup" type="ProteinAmbiguityGroupType" minOccurs="0" maxOccurs="unbounded"/>
//        // <xsd:complexType name="ProteinAmbiguityGroupType">
//        // <xsd:documentation> A set of logically related results from a protein detection, 
//        // for example to represent conflicting assignments of peptides to proteins
//        List <ProteinAmbiguityGroup> pagList = new ArrayList <ProteinAmbiguityGroup>();
//        if (pdl != null) {
//            pagList = pdl.getProteinAmbiguityGroup();
//        }
//        
//        // Retrieve the HashMap which maps DBSequence IDs to DBSequence 
//        HashMap<String, DBSequence> proteinDbSequenceIdHashMap = mzidDataProtein.getDbSequenceIdHashMap();
//        
//        StringBuilder proteinInfoBuilder = new StringBuilder();
//        String pdhScoreType = new String();
//        
//        for (ProteinAmbiguityGroup pag : pagList) {   
//                        
//            // For each pag, get a list of the ProteinDetectionHypothesis it contains
//            // <xsd:complexType name="ProteinDetectionHypothesisType">
//            // <xsd:documentation> A single result of the ProteinDetection analysis (i.e. a protein)
//            
//            // <ProteinDetectionHypothesis> (PDH) elements in mzIdentML (representing single protein database entries)
//            // are constructed by referencing all PSMs (that pass the threshold, if specified) 
//            // that have a mapping to the current protein accession (Ghali 2013)
//            List <ProteinDetectionHypothesis> pdhList = pag.getProteinDetectionHypothesis();   
//            
//            // Iterate through the list of PDHs 
//            for (int pdhNum = 0; pdhNum < pdhList.size(); pdhNum++) {
//                
//                // Initialise the strings for values which are going to be extracted from the CVParam            
//                String accessionCode = new String();
//                String speciesName = new String();
//                String proteinName = new String();
//                String pdhScore = new String();
//                String obsObs = new String();
//                
//                // Get the CV Param list associated with each PDH 
//                // <xsd:group ref="ParamGroup" minOccurs="0" maxOccurs="unbounded">
//                // <xsd:documentation> Scores or parameters associated with this ProteinDetectionHypothesis e.g. p-value
//                List <CvParam> pdhCvParamList = pdhList.get(pdhNum).getCvParam();
//                
//                // Extract the score which is stored as a value in the CvParam
//                // Also extract the type of score 
//                for (int pdhCvParamNum = 0; pdhCvParamNum < pdhCvParamList.size(); pdhCvParamNum++) {
//                    CvParam pdhCvParam = pdhCvParamList.get(pdhCvParamNum);
//                    if (pdhCvParam.getName().contains("score")) {
//                        // Type of score that is displayed in the Protein View table
//                        // Will usually be PDH Score 
//                        // A PDHScore is created for each PDH by summing the PSM scores according to the user parameters (Ghali 2013)
//                        pdhScoreType = pdhCvParam.getName();
//                        Double pdhScoreDouble = Double.parseDouble(pdhCvParam.getValue());
//                        pdhScore = "<td>" + String.format("%.2f", pdhScoreDouble) + "</td>";
//                    }
//                }
//                                            
//                // Get the DBSequence ID from the PDH and then look up the DBSequence from the hashmap    
//                // Get the sequence of each PDH from the DbSequence HashMap
//                DBSequence proteinDbSeq = proteinDbSequenceIdHashMap.get(pdhList.get(pdhNum).getDBSequenceRef());
//                
//                // Get the list of CvParam which are associated with DBSequence
//                // <xsd:group ref="ParamGroup" minOccurs="0" maxOccurs="unbounded">
//                // <xsd:documentation> Additional descriptors for the sequence, such as taxon, description line etc
//                List<CvParam> DbSeqCvParamList = proteinDbSeq.getCvParam();
//                
//                // Iterate through the list of DBSeq CV Param (includes protein sequence)
//                for (int DbSeqCvParamNum = 0; DbSeqCvParamNum < DbSeqCvParamList.size(); DbSeqCvParamNum++) {                   
//                    
//                    // Get an individual DbSeq CV Param from the list
//                    CvParam DbSeqCvParam = DbSeqCvParamList.get(DbSeqCvParamNum);
//                    
//                    // The protein name is the value of the CV Param with the associated name = "protein description"
//                    if (DbSeqCvParam.getName().equals("protein description")) {                      
//                        String proteinNameFull = DbSeqCvParam.getValue(); 
//                    
//                        // Regex to extract the accession code, species name and protein name
//                        Pattern patternAcc = Pattern.compile("tr\\|(.*?)\\|");
//                        Matcher matcherAcc = patternAcc.matcher(proteinNameFull);
//                    
//                        Pattern patternSpe = Pattern.compile("OS=(.*?)GN");
//                        Matcher matcherSpe = patternSpe.matcher(proteinNameFull);
//                    
//                        Pattern patternName = Pattern.compile("\\s(.*?)OS=");
//                        Matcher matcherName = patternName.matcher(proteinNameFull);
//                    
//                        // Format with HTML tags <td> because these variables are to be added to protein view table
//                        if (matcherAcc.find() && matcherSpe.find() && matcherName.find()) {
//                            accessionCode = "<td>" + matcherAcc.group(1) + "</td>";                                             
//                            speciesName = "<td>" + matcherSpe.group(1) + "</td>"; 
//                            proteinName = "<td>" + matcherName.group(1) + "</td>";                         
//                        }  
//                    }
//                
//                proteinInfoBuilder.append("<tr>");
//                proteinInfoBuilder.append(accessionCode);
//                proteinInfoBuilder.append(speciesName);
//                proteinInfoBuilder.append(proteinName);
//                proteinInfoBuilder.append(pdhScore);
//                proteinInfoBuilder.append(obsObs);
//                proteinInfoBuilder.append("</tr>");
//                    
//                }                                        
//            }                    
//        }
//        
//        List <String> proteinInfoReturn = new ArrayList();        
//        proteinInfoReturn.add(proteinInfoBuilder.toString());
//        proteinInfoReturn.add(pdhScoreType);
//        
//        return proteinInfoReturn;
//    }                        
//}

