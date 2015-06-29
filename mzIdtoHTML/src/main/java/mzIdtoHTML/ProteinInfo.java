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
        ProteinPeptideData ppdProtein = new ProteinPeptideData();
        MzidData mzidDataProtein = new MzidData();
        ProteinData proteinData = new ProteinData();

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
                    String pdhTableRow = proteinData.getProteinDataTable(pdh).get(0);
                    pdhScoreType = proteinData.getProteinDataTable(pdh).get(1);                  
                    proteinInfoBuilder.append(pdhTableRow);
                    
                    
                    // Get all the protein sequences associated with the pdh
                    ArrayList <String> peptideSeqList = pdhPeptideSeqHashMap.get(pdh);
                    
                    // Get peptide number for the pdh
                    int currentPdhPeptideCount = peptideSeqList.size();
                    
                    if (currentPdhPeptideCount > maxPeptides) {
                        maxPeptides = currentPdhPeptideCount;
                    }                  
                    else if (currentPdhPeptideCount < minPeptides && currentPdhPeptideCount > 0) {
                        minPeptides = currentPdhPeptideCount;
                    }
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
    
