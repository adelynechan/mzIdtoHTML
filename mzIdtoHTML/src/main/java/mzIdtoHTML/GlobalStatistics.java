/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import java.util.List;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidenceRef;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;

/**
 *
 * @author Adelyne
 */
public class GlobalStatistics {
    PeptideInfo peptideInfo = new PeptideInfo();
    private HashMap<String, Peptide> peptideIdHashMap = peptideInfo.getPeptideIdHashmap();
    private HashMap<String, PeptideEvidence> peptideEvidenceIdHashMap = peptideInfo.getPeptideEvidenceIdHashMap();
    private HashMap<String, DBSequence> dbSequenceIdHashMap = peptideInfo.getDbSequenceIdHashMap();
    
    int getPeptideNumber() {    
        int peptideNumber = peptideIdHashMap.size();   
        return peptideNumber;
    }
    
    int getDecoyPercentage() {     
        List<SpectrumIdentificationList> sil = peptideInfo.getSpectrumIdentificationList();
        int decoyNumber = 0;
        
        for (SpectrumIdentificationList sIdentList : sil) {
            for (SpectrumIdentificationResult spectrumIdentResult: sIdentList.getSpectrumIdentificationResult()) {
                for (SpectrumIdentificationItem spectrumIdentItem: spectrumIdentResult.getSpectrumIdentificationItem()) {
                    List<PeptideEvidenceRef> peptideEvidenceRefList = spectrumIdentItem.getPeptideEvidenceRef();
                    
                    for (int i = 0; i < peptideEvidenceRefList.size(); i++) {
                        PeptideEvidenceRef peptideEvidenceRef = peptideEvidenceRefList.get(i);
                        PeptideEvidence peptideEvidence = peptideEvidenceIdHashMap.get(peptideEvidenceRef.getPeptideEvidenceRef());
                        
                        if (peptideEvidence.isIsDecoy()) {
                            decoyNumber += 1;  
                        }      
                    }
                }
            }
        }
        return decoyNumber;
    }
}
