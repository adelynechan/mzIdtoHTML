/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;

/**
 *
 * @author Adelyne
 */
public class MzidData {
    
    // Hashmap with peptide ID as key and Peptide as value
    public HashMap <String, Peptide> getPeptideIdHashmap () {
        HashMap<String, Peptide> peptideIdHashMap = new HashMap();
        Iterator<Peptide> iterPeptide = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.Peptide);
        while (iterPeptide.hasNext()) {
            Peptide peptide = iterPeptide.next();
            peptideIdHashMap.put(peptide.getId(), peptide);
        }
        return peptideIdHashMap;
    }
    
    // Hashmap with DB sequence ID as key and DBSequence as value
    // <xsd:element name="DBSequence" type="DBSequenceType" minOccurs="1" maxOccurs="unbounded"/>
    public HashMap <String, DBSequence> getDbSequenceIdHashMap() {
        HashMap<String, DBSequence> dbSequenceIdHashMap = new HashMap();
        Iterator<DBSequence> iterDBSequence = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.DBSequence);
        while (iterDBSequence.hasNext()) {
            DBSequence dbSequence = iterDBSequence.next();
            dbSequenceIdHashMap.put(dbSequence.getId(), dbSequence);
        }
        return dbSequenceIdHashMap;
    }
    //Jun: I would check the decoy status here as well, probably generate a decoy list containing ids for decoys
    //then you do not need to iterate again to find out decoys.
    
    public ArrayList <HashMap> getPeptideEvidenceIdHashMap() {
        HashMap <String, PeptideEvidence> peptideEvidenceIdHashMap = new HashMap();
        HashMap <String, PeptideEvidence> peptideEvidenceDecoyHashMap = new HashMap();
        
        Iterator<PeptideEvidence> iterPeptideEvidence = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.PeptideEvidence);
            while (iterPeptideEvidence.hasNext()) {
                PeptideEvidence peptideEvidence = iterPeptideEvidence.next();
                
                // Add the peptide ID and peptideEvidence into the hashmap
                peptideEvidenceIdHashMap.put(peptideEvidence.getId(), peptideEvidence);
                
                // Add the peptide ID to the arraylist if isDecoy = true
                if (peptideEvidence.isIsDecoy()) {
                    peptideEvidenceDecoyHashMap.put(peptideEvidence.getId(), peptideEvidence);
                }
            }
       
        ArrayList <HashMap> returnPeptideEvidence = new ArrayList();
        returnPeptideEvidence.add(peptideEvidenceIdHashMap);
        returnPeptideEvidence.add(peptideEvidenceDecoyHashMap);
        
        return returnPeptideEvidence;
    }  
}
