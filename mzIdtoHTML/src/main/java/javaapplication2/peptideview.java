/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.File;
import java.util.List;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationList;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

/**
 *
 * @author Adelyne
 */
public class peptideview {
    
    public static String getPeptideInfo (File file) {
               
        boolean aUseSpectrumCache = true;
        MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(file);
                                
        DataCollection dc =  unmarshaller.unmarshal(DataCollection.class);
        AnalysisData ad = dc.getAnalysisData();
                
        StringBuilder peptideInfoBuilder = new StringBuilder();

        // Get the list of SpectrumIdentification elements
        List<SpectrumIdentificationList> sil = ad.getSpectrumIdentificationList();

        for (SpectrumIdentificationList sIdentList : sil) {
            for (SpectrumIdentificationResult spectrumIdentResult: sIdentList.getSpectrumIdentificationResult()) {

                // Get the name of SpectrumIdentificationResult
                String spectrumID =  spectrumIdentResult.getSpectrumID();

                for (SpectrumIdentificationItem spectrumIdentItem: spectrumIdentResult.getSpectrumIdentificationItem()) {

                    // Get the following information for SpectrumIdentificationItem element
                    String spectrumIdItem = spectrumIdentItem.getId();
                    Double calculatedMassToCharge =  spectrumIdentItem.getCalculatedMassToCharge();
                    Double experimentalMassToCharge = spectrumIdentItem.getExperimentalMassToCharge();
                    int charge = spectrumIdentItem.getChargeState();
                    
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
                    peptideInfoBuilder.append(printSequence); // for sequence
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
