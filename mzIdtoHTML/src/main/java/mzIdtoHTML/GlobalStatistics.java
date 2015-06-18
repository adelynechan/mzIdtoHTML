/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;

/**
 *
 * @author Adelyne
 */
public class GlobalStatistics {
        
    MzidData mzidDataStats = new MzidData();
    PeptideEvidenceData peptideEvidenceStats = new PeptideEvidenceData();
    
    int getPeptideNumber() {           
        return mzidDataStats.getPeptideIdHashMap().size();   
    }
    
    Double getDecoyPercentage() {     
        GlobalStatistics globalStatistics = new GlobalStatistics();
        
        // Get number of peptide matches
        int peptideNumber = globalStatistics.getPeptideNumber();
        
        // Get number of decoy peptide matches
        // mzidDataStats.getPeptideEvidenceIdHashMap returns an arraylist of 2 hashmaps
        // The second hashmap in the arraylist is the decoy entries in the input file
        HashMap <String, PeptideEvidence> peptideEvidenceIdHashMap = peptideEvidenceStats.getPeptideEvidenceIdHashMap();
        int decoyNumber =  peptideEvidenceStats.getPeptideEvidenceDecoyIdList().size();
        
        // Calculate decoy percentage and return
        return (double)(decoyNumber) / (double)(peptideNumber) * 100;
    }
    
    int getProteinNumber() {
        return mzidDataStats.getDbSequenceIdHashMap().size();     
    }
}
