/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionList;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinAmbiguityGroup;
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
    
    String getProteinInfo() {
        PeptideInfo peptideInfoProtein = new PeptideInfo();
        ProteinInfo proteinInfo = new ProteinInfo();
        
        HashMap<String, DBSequence> proteinDbSequenceIdHashMap = peptideInfoProtein.getDbSequenceIdHashMap();
        List<ProteinAmbiguityGroup> pagList = proteinInfo.getProteinDetectionList().getProteinAmbiguityGroup();
        
        StringBuilder proteinInfoBuilder = new StringBuilder();
        String proteinName = new String();
        
        for (ProteinAmbiguityGroup pag : pagList) {
           List<ProteinDetectionHypothesis> pdhList = pag.getProteinDetectionHypothesis();
           
            for (int i = 0; i < pdhList.size(); i++) {
                DBSequence proteinDbSeq = proteinDbSequenceIdHashMap.get(pdhList.get(i).getDBSequenceRef());
                List<CvParam> proteinCvParamList = proteinDbSeq.getCvParam();
                int listSize = proteinCvParamList.size();
                
                for (int x = 0; x < proteinCvParamList.size(); x++) {
                    CvParam proteinCvParam = proteinCvParamList.get(x);
                    proteinName = proteinCvParam.getValue(); 
                }     
                
                
            
            } 
        }
        
        return proteinInfoBuilder.toString();
    }
    
 
                
                
                        
        
    }

