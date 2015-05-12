/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;
import uk.ac.ebi.jmzidml.model.mzidml.*;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;
import uk.ac.ebi.jmzidml.MzIdentMLElement;

import java.util.List;

/**
 *
 * @author Adelyne
 */
public class Example1 {

    public static void main(String[] args) {
        
        try {

              // Open the input mzIdentML file for parsing
                //System.out.println("test");
                File file = new File("GalaxyExampleProteoGrouper.mzid");
                //URL xmlFileURL = JavaApplication2.class.getClassLoader().getResource("GalaxyExample.mzid");

                
                //if (file != null) {
                if (file.exists()) {
                   
                    // test that input file opened
                   //System.out.println("test2");       
                   //System.out.println(file.getAbsolutePath());
//                   
                   boolean aUseSpectrumCache = true;
                   MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(file);

                   //System.out.println("test3");
                   
                   DataCollection dc =  unmarshaller.unmarshal(DataCollection.class);
                   AnalysisData ad = dc.getAnalysisData();
                   //System.out.println("test4");

                   // Get the list of SpectrumIdentification elements
                   List<SpectrumIdentificationList> sil = ad.getSpectrumIdentificationList();

                   for (SpectrumIdentificationList sIdentList : sil) {
                        for (SpectrumIdentificationResult spectrumIdentResult
                                : sIdentList.getSpectrumIdentificationResult()) {

                            // Get the name of SpectrumIdentificationResult
                            String spectrumID =  spectrumIdentResult.getSpectrumID();

                            for (SpectrumIdentificationItem spectrumIdentItem
                                 : spectrumIdentResult.getSpectrumIdentificationItem()) {

                                // Get the following information for SpectrumIdentificationItem element
                                String spectrumIdItem = spectrumIdentItem.getId();
                                Double calculatedMassToCharge =  spectrumIdentItem.getCalculatedMassToCharge();
                                Double experimentalMassToCharge = spectrumIdentItem.getExperimentalMassToCharge();
                                int rank = spectrumIdentItem.getRank();
                                int charge = spectrumIdentItem.getChargeState();

                                System.out.println("Spectrum Identification ID = " + spectrumIdItem);
                                System.out.println("Calculated Mass/Charge = " + calculatedMassToCharge);
                                System.out.println("Experimental Mass/Charge = " + experimentalMassToCharge);
                                System.out.println("Search rank = " + rank);
                                System.out.println("Charge = " + charge);

                                // If the auto-resolve mechanism is activated for SpectrumIdentificationItem
                                // then automatically resolve the Peptide Object
                                // Default option sets auto-resolve False
                                if (MzIdentMLElement.SpectrumIdentificationItem.isAutoRefResolving()
                                        && spectrumIdentItem.getPeptideRef() != null) {
                                     Peptide peptide = spectrumIdentItem.getPeptide();
                                     String peptideSequence = peptide.getPeptideSequence();

                                    System.out.println("Peptide Sequence = " + peptideSequence);
                                }  
                                
                                }

                                System.out.println("\n");

                            } // end spectrum identification item
                        } // end spectrum identification results
                   }
                }
               catch(Exception e) {
                    e.printStackTrace(); }
    }
}
    