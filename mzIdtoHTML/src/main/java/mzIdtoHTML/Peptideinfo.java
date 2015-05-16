/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.List;
import java.util.ArrayList;
import uk.ac.ebi.jmzidml.model.mzidml.*;

/**
 *
 * @author Adelyne
 */
public class Peptideinfo {
    
    public static List<String> getPeptideInfo () {
                               
        DataCollection dc =  MzidToHTML.unmarshaller.unmarshal(DataCollection.class);
        AnalysisData ad = dc.getAnalysisData();
        Peptide pept = MzidToHTML.unmarshaller.unmarshal(Peptide.class);
                        
        StringBuilder peptideInfoBuilder = new StringBuilder();
        String scoreName = new String();

        // Get the list of Spectrum Identification elements
        List<SpectrumIdentificationList> sil = ad.getSpectrumIdentificationList();

        for (SpectrumIdentificationList sIdentList : sil) {
            for (SpectrumIdentificationResult spectrumIdentResult: sIdentList.getSpectrumIdentificationResult()) {

                // Get the name of SpectrumIdentificationResult
                //String spectrumID =  spectrumIdentResult.getSpectrumID();

                for (SpectrumIdentificationItem spectrumIdentItem: spectrumIdentResult.getSpectrumIdentificationItem()) {

                    // Column 1: SII number
                    String spectrumIdItem = spectrumIdentItem.getId();
                    
                    // Column 2: Peptide sequence
                    Peptide peptide = new Peptide();
                    peptide = spectrumIdentItem.getPeptide(); //WHY IS THIS NULL????
                    //String sequence = peptide.getPeptideSequence();                                   
                                       
                    // Column 3: Calculated mass to charge ratio
                    Double calculatedMassToCharge =  spectrumIdentItem.getCalculatedMassToCharge();
                    
                    // Column 4: Experimental mass to charge ratio
                    Double experimentalMassToCharge = spectrumIdentItem.getExperimentalMassToCharge();
                    
                    // Column 5: Charge
                    int charge = spectrumIdentItem.getChargeState();
                    
                    // Column 6: Modifications
//                    List<Modification> modificationsList = peptide.getModification();
//                    
//                    for (Modification modification: modificationsList) {
//                        List<String> residues = modification.getResidues();
//                    }
                    
                    // Column 7: Score
                    // The first element of score is used 
                    // The type of score is represented in the header column
                    List<CvParam> spectIdentParam = spectrumIdentItem.getCvParam();
                    CvParam firstScore = spectIdentParam.get(0);
                    scoreName = firstScore.getName();
                    String scoreValue = firstScore.getValue();
                    
                    // Column 8: Associated proteins
                                                            
                    String printSpectrumIdItem = "<td> " + spectrumIdItem + " </td>";
                    String printSequence = "<td> " + peptide + " </td>";
                    String printCalculatedMassToCharge = "<td> " + calculatedMassToCharge + " </td>";
                    String printExperimentalMassToCharge = "<td> " + experimentalMassToCharge + " </td>";
                    String printCharge = "<td> " + charge + " </td>";
                    String printModifications = "<td> " + " " + " </td>";
                    String printScore = "<td> " + scoreValue + " </td>";
                    String printAssociatedProteins = "<td> " + " " + " </td>";
                    
                    peptideInfoBuilder.append("<tr>");
                    peptideInfoBuilder.append(printSpectrumIdItem);
                    peptideInfoBuilder.append(printSequence); 
                    peptideInfoBuilder.append(printCalculatedMassToCharge);
                    peptideInfoBuilder.append(printExperimentalMassToCharge);
                    peptideInfoBuilder.append(printCharge);
                    peptideInfoBuilder.append(printModifications);
                    peptideInfoBuilder.append(printScore);
                    peptideInfoBuilder.append(printAssociatedProteins);
                    peptideInfoBuilder.append("</tr>");                                    
                }                                                                                  
            }                         
        }  
                                    
        //return peptideInfoBuilder.toString();
        
        List<String> peptideInfo = new ArrayList<String>();
        peptideInfo.add(peptideInfoBuilder.toString());
        peptideInfo.add(scoreName);
        
        return peptideInfo;
    }         
}
