/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItemRef;

/**
 *
 * @author Adelyne
 */
public class ProteinPeptideData {
    SortedMap <Double, ArrayList <ProteinDetectionHypothesis>> scorePdhSortedMap = new TreeMap();
    HashMap <ProteinDetectionHypothesis, ArrayList <String>> pdhPeptideSeqHashMap = new HashMap();
    HashMap <ProteinDetectionHypothesis, ArrayList <Peptide>> pdhPeptideHashMap = new HashMap();
    HashMap <ProteinDetectionHypothesis, List <SpectrumIdentificationItemRef>> pdhSiiRefHashMap = new HashMap();
    
    PeptideEvidenceData peptideEvidencePpd = new PeptideEvidenceData();
    MzidData mzidDataPpd = new MzidData();
    HashMap <String, PeptideEvidence> peptideEvidenceIdHashMap = peptideEvidencePpd.getPeptideEvidenceIdHashMap();
    HashMap <String, Peptide> peptideIdHashMap = mzidDataPpd.getPeptideIdHashMap();
    
    public SortedMap <Double, ArrayList<ProteinDetectionHypothesis>> getScorePdhSortedMap() {
        
        Iterator <ProteinDetectionHypothesis> iterPDH = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath
                (MzIdentMLElement.ProteinDetectionHypothesis);
        
        while (iterPDH.hasNext()) {
            ProteinDetectionHypothesis pdh = iterPDH.next();         
            
            // Get score for score-Pdh SortedMap
            List <CvParam> pdhCvParamList = pdh.getCvParam();
            
            int pdhCvParamNum = 0;
            CvParam pdhCvParam = pdhCvParamList.get(pdhCvParamNum);
            while (!pdhCvParam.getName().contains("score")) {
                pdhCvParamNum = pdhCvParamNum + 1;
                pdhCvParam = pdhCvParamList.get(pdhCvParamNum);
            }
            
            Double pdhScore = Double.parseDouble(pdhCvParam.getValue());
            if (scorePdhSortedMap.containsKey(pdhScore)) {
                ArrayList pdhList = scorePdhSortedMap.get(pdhScore);
                pdhList.add(pdh);
                scorePdhSortedMap.put(pdhScore, pdhList);
            }
            
            else {
                ArrayList <ProteinDetectionHypothesis> pdhList = new ArrayList <ProteinDetectionHypothesis>();
                pdhList.add(pdh);
                scorePdhSortedMap.put(pdhScore, pdhList);
            }
            
            // Get a list of PeptideHypothesis associated with the ProteinDetectionHypothesis
            List <PeptideHypothesis> peptideHypothesisList = pdh.getPeptideHypothesis();
            
            // Get peptide sequences for pdh-sequence HashMap   
            // Initialise a new ArrayList to store the peptide sequences associated with each pdh
            ArrayList <String> peptideSeqList = new ArrayList();
            ArrayList <Peptide> peptideList = new ArrayList();
            
            // For each PeptideHypothesis, retrieve the peptide sequence and store in list 
            for (int pepHypNum = 0; pepHypNum < peptideHypothesisList.size(); pepHypNum++) {
                PeptideHypothesis peptideHypothesis = peptideHypothesisList.get(pepHypNum);
                
                // Get the list of SIIs for producing the table of peptides which are associated with a particular protein
                List <SpectrumIdentificationItemRef> siiRefList = peptideHypothesis.getSpectrumIdentificationItemRef();
                pdhSiiRefHashMap.put(pdh, siiRefList);
                
                String peptideEvidenceRef = peptideHypothesis.getPeptideEvidenceRef();
                PeptideEvidence peptideEvidence = peptideEvidenceIdHashMap.get(peptideEvidenceRef); // K:PeptideEvidenceID (String), V: PeptideEvidence 
                
                if (!peptideEvidence.isIsDecoy()) {
                    String peptideRef = peptideEvidence.getPeptideRef();
                    Peptide peptide = peptideIdHashMap.get(peptideRef); // K:PeptideRef (String), V:Peptide
                    String peptideSequence = peptide.getPeptideSequence();
                    
                    if (!peptideSeqList.contains(peptideSequence)) {
                       peptideSeqList.add(peptideSequence); 
                       peptideList.add(peptide);
                    }
                }
            }
            
            // Add to the HashMap with the ProteinDetectionHypothesis as key and list of peptide sequences as value
            pdhPeptideSeqHashMap.put(pdh, peptideSeqList);
        }      
        return scorePdhSortedMap;
    }
    
    public HashMap <ProteinDetectionHypothesis, ArrayList <String>> getPdhPeptideSeqHashMap() {
        return pdhPeptideSeqHashMap;
    }
    
    public HashMap <ProteinDetectionHypothesis, List <SpectrumIdentificationItemRef>> getPdhSiiRefHashMap() {
        return pdhSiiRefHashMap;
    }

}
