/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;

/**
 *
 * @author Adelyne
 */
public class MzidData {

    
    // Hashmap with peptide ID as key and Peptide as value
    public HashMap <String, Peptide> getPeptideIdHashMap () {
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
    
    public HashMap <ProteinDetectionHypothesis, List<String>> getPdhPeptideSeqHashMap() {
        MzidData mzidData = new MzidData();
        PeptideEvidenceData peptideEvidenceMzidData = new PeptideEvidenceData();
        
        HashMap <String, PeptideEvidence> peptideEvidenceIdHashMap = peptideEvidenceMzidData.getPeptideEvidenceIdHashMap();
        HashMap <String, Peptide> peptideIdHashMap = mzidData.getPeptideIdHashMap();
        
        HashMap <ProteinDetectionHypothesis, List<String>> pdhPeptideSeqHashMap = new HashMap();
        
        // Unmarshal the ProteinDetectionHypothesis class and create an iterator to go through all entries
        Iterator <ProteinDetectionHypothesis> iterPDH = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath
                (MzIdentMLElement.ProteinDetectionHypothesis);
        
        // Get a list of all PeptideHypothesis which are associated with the ProteinDetectionHypothesis
        // Loop over all ProteinDetectionHypothesis entries using the iterator
        while (iterPDH.hasNext()) {
            ProteinDetectionHypothesis pdh = iterPDH.next();
            
            List <PeptideHypothesis> peptideHypothesisList = pdh.getPeptideHypothesis();
            List <String> peptideEvidenceList = new ArrayList();
            for (int pepHypNum = 0; pepHypNum < peptideHypothesisList.size(); pepHypNum++) {
                PeptideHypothesis peptideHypothesis = peptideHypothesisList.get(pepHypNum);
                PeptideEvidence peptideEvidence = peptideEvidenceIdHashMap.get(peptideHypothesis.getPeptideEvidenceRef());
                peptideEvidenceList.add((peptideIdHashMap.get(peptideEvidence.getPeptideRef())).getPeptideSequence());
            }
            pdhPeptideSeqHashMap.put(pdh, peptideEvidenceList);
        }      
        return pdhPeptideSeqHashMap;
    }
}
