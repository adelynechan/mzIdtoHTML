/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;

/**
 *
 * @author Adelyne
 */
public class ProteinDetails {
    
    public HashMap <ProteinDetectionHypothesis, ArrayList <String>> getPeptideList() {
        MzidData mzidDataDetails = new MzidData();
        HashMap <ProteinDetectionHypothesis, ArrayList <String>> pdhPeptideSeqHashMap = mzidDataDetails.getPdhPeptideSeqHashMap();
        return pdhPeptideSeqHashMap;
    }
    
}
