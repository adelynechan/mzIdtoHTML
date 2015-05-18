/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;

/**
 *
 * @author Adelyne
 */
public class GlobalStatistics {
    PeptideInfo peptideInfo = new PeptideInfo();
    private static HashMap<String, Peptide> peptideIdHashMap = PeptideInfo.getPeptideIdHashmap();
    
    public static int getPeptideNumber() {    
        int peptideNumber = peptideIdHashMap.size();   
        return peptideNumber;
    }
    
    
    
}
