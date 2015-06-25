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
     
    String getProteinAccession(ProteinDetectionHypothesis pdh) {
        ProteinData proteinDataDetails = new ProteinData();
        ArrayList <String> proteinDataTableRow = proteinDataDetails.getProteinDataTable(pdh);
    }
    
}
