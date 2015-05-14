/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;
import java.util.List;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidenceRef;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationProtocol;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

/**
 *
 * @author Adelyne
 */
public class Peptideinfo {
    
    public static String getPeptideInfo () {
                               
        DataCollection dc =  MzidToHTML.unmarshaller.unmarshal(DataCollection.class);
        AnalysisData ad = dc.getAnalysisData();
        Peptide pept = MzidToHTML.unmarshaller.unmarshal(Peptide.class);
        
                
        StringBuilder peptideInfoBuilder = new StringBuilder();

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
                    //Peptide peptide = new Peptide();
                    //peptide = spectrumIdentItem.getPeptide();
                    //String sequence = peptide.getPeptideSequence();                                   
                                       
                    // Column 3: Calculated mass to charge ratio
                    Double calculatedMassToCharge =  spectrumIdentItem.getCalculatedMassToCharge();
                    
                    // Column 4: Experimental mass to charge ratio
                    Double experimentalMassToCharge = spectrumIdentItem.getExperimentalMassToCharge();
                    
                    // Column 5: Charge
                    int charge = spectrumIdentItem.getChargeState();
                    
                    // Column 6: Modifications
                    
                    
                    // Column 7: Score
                    
                    
                    // Column 8: Associated proteins
                                                            
                    String printSpectrumIdItem = "<td> " + spectrumIdItem + " </td>";
                    String printSequence = "<td> " + " " + " </td>";
                    String printCalculatedMassToCharge = "<td> " + calculatedMassToCharge + " </td>";
                    String printExperimentalMassToCharge = "<td> " + experimentalMassToCharge + " </td>";
                    String printCharge = "<td> " + charge + " </td>";
                    String printModifications = "<td> " + " " + " </td>";
                    String printScore = "<td> " + " " + " </td>";
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
                                    
        return peptideInfoBuilder.toString();
    }         
}
