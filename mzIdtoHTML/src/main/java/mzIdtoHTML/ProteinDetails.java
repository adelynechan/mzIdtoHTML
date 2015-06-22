/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.SortedMap;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItemRef;

/**
 *
 * @author Adelyne
 */
public class ProteinDetails {
    
    ProteinPeptideData ppdDetails = new ProteinPeptideData();
    SortedMap <Double, ArrayList<ProteinDetectionHypothesis>> scorePdhSortedMap = ppdDetails.getScorePdhSortedMap();
    HashMap <ProteinDetectionHypothesis, List <SpectrumIdentificationItemRef>> pdhSiiRefHashMap = ppdDetails.getPdhSiiRefHashMap();
    
    // Test - only the pdh of the highest score (it will take a long time to implement for every single pdh)
    // Need to implement some javascript to only generate the following on click
    ArrayList scoreSet = new ArrayList(scorePdhSortedMap.keySet());
    Object topScore = scoreSet.get(0);
    ProteinDetectionHypothesis topPdh = scorePdhSortedMap.get(topScore).get(0);
    
    String getProteinPeptideDetails() {
        List <SpectrumIdentificationItemRef> proteinSiiRefList = pdhSiiRefHashMap.get(topPdh);
        
        for (int siiRefNum = 0; siiRefNum < proteinSiiRefList.size(); siiRefNum++) {
            SpectrumIdentificationItemRef siiRef = proteinSiiRefList.get(siiRefNum);
            SpectrumIdentificationItem sii = siiRef.getSpectrumIdentificationItem();
            
            
            
        }
    }
    
    
}
