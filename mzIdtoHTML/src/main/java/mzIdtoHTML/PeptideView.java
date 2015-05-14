/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;
import java.util.List;
import java.util.Iterator;
import uk.ac.ebi.jmzidml.model.mzidml.*;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;
import uk.ac.ebi.jmzidml.MzIdentMLElement;

/**
 *
 * @author Adelyne
 */
public class PeptideView {
    
    public static String getPeptideInfo (File file) {
               
        boolean aUseSpectrumCache = true;
        MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(file);
                                
        SequenceCollection sc = unmarshaller.unmarshal(SequenceCollection.class);
        StringBuilder peptideViewBuilder = new StringBuilder();
        
        Iterator<PeptideEvidence> iterPeptideEvidence = unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.PeptideEvidence);
            while (iterPeptideEvidence.hasNext()) {
                PeptideEvidence peptideEvidence = iterPeptideEvidence.next();
                String id = peptideEvidence.getId();
                DBSequence peptide = peptideEvidence.getDBSequence();
                
                peptideViewBuilder.append(peptide);
            }

            

          
        
//        List<Peptide> peptideList = sc.getPeptide();
//        for (Peptide peptide : peptideList) {
//            String id = peptide.getId();
//            String sequence = peptide.getPeptideSequence();   
//            
//            
//            
//            String printPeptideId = "<td> " + id + " </td>";
//            String printSequence = "<td> " + sequence + " </td>";
//            //String printCalculatedMassToCharge = "<td> " + calculatedMassToCharge + " </td>";
//            //String printExperimentalMassToCharge = "<td> " + experimentalMassToCharge + " </td>";
//            //String printCharge = "<td> " + charge + " </td>";
//            //String printModifications = "<td> " + " " + " </td>";
//            //String printScore = "<td> " + " " + " </td>";
//            //String printAssociatedProteins = "<td> " + " " + " </td>";
//            
//            peptideViewBuilder.append("<tr>");
//            peptideViewBuilder.append(printPeptideId);
//            peptideViewBuilder.append(printSequence); 
//            //peptideViewBuilder.append(printCalculatedMassToCharge);
//            //peptideViewBuilder.append(printExperimentalMassToCharge);
//            //peptideViewBuilder.append(printCharge);
//            //peptideViewBuilder.append(printModifications);
//            //peptideViewBuilder.append(printScore);
//            //peptideViewBuilder.append(printAssociatedProteins);
//            peptideViewBuilder.append("</tr>");
//        }
        
//        List<DBSequence> dbSequenceList = sc.getDBSequence();
//        for (DBSequence dbSequence : dbSequenceList) {
//            List<CvParam> dbCvParamList = dbSequence.getCvParam();
//            
//            for (CvParam dbCvParam : dbCvParamList) {
//                String name = dbCvParam.getValue();
//                
//                peptideViewBuilder.append(name);
//            }
//        }
        
//        List<PeptideEvidence> pepEvidenceList = sc.getPeptideEvidence();
//            for (PeptideEvidence pepEvidence : pepEvidenceList) {                  
//                String peptideRef = pepEvidence.getPeptideRef();
//                DBSequence dbSequence = pepEvidence.getDBSequence();
//                //String peptideSequence = peptide.getPeptideSequence();
//                    
//                peptideViewBuilder.append(dbSequence);                                                                                                    
//            }       
        return peptideViewBuilder.toString();		         
    }
}
