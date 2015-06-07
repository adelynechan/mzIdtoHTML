/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;

/**
 *
 * @author Adelyne
 */
public class GlobalStatistics {
        
    MzidData mzidDataStats = new MzidData(); 
    
    int getPeptideNumber() {           
        return mzidDataStats.getPeptideIdHashmap().size();   
    }
    
    Double getDecoyPercentage() {     
        GlobalStatistics globalStatistics = new GlobalStatistics();
        
        // Get number of peptide matches
        int peptideNumber = globalStatistics.getPeptideNumber();
        
        // Get number of decoy peptide matches
        int decoyNumber = mzidDataStats.getPeptideEvidenceIdHashMap().get(1).size();      
        
        // Calculate decoy percentage and return
        return (double)(decoyNumber) / (double)(peptideNumber) * 100;
    }
}
