/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 * Using the element name and XPath to gather information
 */

import java.io.File;
import uk.ac.ebi.jmzidml.model.mzidml.*;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;
import uk.ac.ebi.jmzidml.MzIdentMLElement;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Adelyne
 */
public class Example2 {
    
    public static void main(String[] args) {
          try {

              // Open the input mzIdentML file for parsing
               File file = new File("GalaxyExampleProteoGrouper.mzid");
               System.out.println(file.getAbsolutePath());

                if (file.exists()) {

                   boolean aUseSpectrumCache = true;
                   MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(file);


                    // Get a list of all the PeptideEvidence. The class name is internally converted to its XPath and the
                    // information is retrieved from indexer. Auto-resolve need not be activated here if we are not resolving
                    // any reference on the fly.
                    Iterator<PeptideEvidence> pepEvdIter = unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.PeptideEvidence);

                    // Retrieve information for each PeptideEvidence in the list
                    while (pepEvdIter.hasNext()) {
                        PeptideEvidence element = pepEvdIter.next();
                        System.out.println("PeptideEvidence id: " + element.getId());
                        //System.out.println("PeptideEvidence type" + element.getType());
                        System.out.println("DBSequence ref : " + element.getDBSequenceRef());
                        System.out.println("Start location = " + element.getStart() + ", End location = " + element.getEnd());
                        System.out.println("\n");
                    }

                }
               }catch(Exception e) {
                    e.printStackTrace(); }
        }

}
    
