/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;

/**
 *
 * @author Adelyne
 */
public class PeptideEvidenceData {

    HashMap <String, PeptideEvidence> peptideEvidenceIdHashMap = new HashMap();
    ArrayList <String> peptideEvidenceDecoyIdList = new ArrayList();
    
    HashMap <String, PeptideEvidence> getPeptideEvidenceIdHashMap() {
        
        Iterator<PeptideEvidence> iterPeptideEvidence = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.PeptideEvidence);
            while (iterPeptideEvidence.hasNext()) {
                PeptideEvidence peptideEvidence = iterPeptideEvidence.next();
                
                // Add the peptide ID and peptideEvidence into the hashmap
                peptideEvidenceIdHashMap.put(peptideEvidence.getId(), peptideEvidence);
                
                // Add the peptide ID to the arraylist if isDecoy = true
                if (peptideEvidence.isIsDecoy()) {
                    peptideEvidenceDecoyIdList.add(peptideEvidence.getId());
                }
            }      
        return peptideEvidenceIdHashMap;
    }
    
    public ArrayList <String> getPeptideEvidenceDecoyIdList() {
        return peptideEvidenceDecoyIdList;
    }
}
