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
    
    ProteinDetectionList getProteinDetectionHypothesisList() {  
        DataCollection dc =  MzidToHTML.unmarshaller.unmarshal(DataCollection.class);
        AnalysisData ad = dc.getAnalysisData();  
        ProteinDetectionList pdl = ad.getProteinDetectionList();
        
        return pdl;
    }
    
    ProteinAmbiguityGroup getProteinDBSequence() {
        PeptideInfo peptideInfoProtein = new PeptideInfo();
        ProteinInfo proteinInfo = new ProteinInfo();
        
        HashMap<String, DBSequence> proteinDbSequenceIdHashMap = peptideInfoProtein.getDbSequenceIdHashMap();
        List<ProteinAmbiguityGroup> pagList = proteinInfo.getProteinDetectionHypothesisList().getProteinAmbiguityGroup();
        
        for (ProteinAmbiguityGroup)
        
       
        
        ArrayList<String> proteinDbSeqList = new ArrayList<String>();
        
        for (int i = 0; i < pdhList.size(); i++) {
            DBSequence proteinDbSeq = proteinDbSequenceIdHashMap.get(pdhList.get(i).getDBSequenceRef());
            proteinDbSeqList.add(proteinDbSeq.toString());
        }
        
        return proteinDbSeqList;
    }
    
    String getProteinInfo() {
        ProteinInfo proteinInfo = new ProteinInfo();
        
        
    }
                
                
                        
        
    }
}
