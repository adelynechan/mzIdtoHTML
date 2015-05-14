/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;

import uk.ac.ebi.jmzidml.model.mzidml.*;

import java.util.List;

/**
 *
 * @author Adelyne
 * Extracts metadata from the mzIdentML file and returns a string 
 * This string is accessed by the results file for integration into HTML template
 */
public class Metadata {
    
    // Search type
    public static String getSearchType () { 
        
        SpectrumIdentificationProtocol sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);
            
        Param searchTypeParam = sip.getSearchType();
        CvParam searchCv = searchTypeParam.getCvParam();
        String searchType = searchCv.getName();  
            
        return searchType;
    }
    
    // Name of analysis software used
    public static String getSoftwareName () {
        
        AnalysisSoftware as = MzidToHTML.unmarshaller.unmarshal(AnalysisSoftware.class);
        
        Param softwareType = as.getSoftwareName();
        CvParam softwareCv = softwareType.getCvParam();
        String softwareName = softwareCv.getName();
        
        return softwareName;
    }   
    
    // Enzyme(s) (if empty return "Information not available")
    public static String getEnzymesUsed () {
        
        SpectrumIdentificationProtocol sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);
        Enzymes enzymes = sip.getEnzymes();
        
        List<Enzyme> enzList = enzymes.getEnzyme();
                
        StringBuilder enzymeBuilder = new StringBuilder();              
        
        if (enzList.isEmpty()) {
            enzymeBuilder.append("Information not available");
        }
       
        else {
            for (Enzyme enzymeList : enzList) {
                ParamList enzymeName = enzymeList.getEnzymeName();
                List<CvParam> enzymeParam;
                enzymeParam = enzymeName.getCvParam();

                for (int i = 0; i < enzymeParam.size(); i++) {
                    enzymeBuilder = enzymeBuilder.append(enzymeParam.get(i).getName()); 
                }
            }
        }
        
        return enzymeBuilder.toString();
    }   
    
    // Fixed modifications (if empty return "No modifications")
    public static String getFixedModifications () {
        SpectrumIdentificationProtocol sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);

        ModificationParams mp = sip.getModificationParams();
        List<SearchModification> modList = mp.getSearchModification();       
             
        StringBuilder fixedModificationsBuilder = new StringBuilder();
                        
        if (modList.isEmpty()) {
            fixedModificationsBuilder.append("No modifications");
        }
                       
        else {
                        
            for (SearchModification modificationList : modList) {
                Boolean fixed = modificationList.isFixedMod();
                
                if (fixed) {
                    List <String> modificationName = modificationList.getResidues();
                    for (int i = 0; i < modificationName.size(); i++) {
                    fixedModificationsBuilder.append(modificationName.get(i));
                    }                                              
                }
            }
        }
        return fixedModificationsBuilder.toString();
    }
    
    // Variable modifications (if empty return "No modifications")
    public static String getVariableModifications () {
        SpectrumIdentificationProtocol sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);

        ModificationParams mp = sip.getModificationParams();
        List<SearchModification> modList = mp.getSearchModification();       
             
        StringBuilder variableModificationsBuilder = new StringBuilder();
                        
        if (modList.isEmpty()) {
            variableModificationsBuilder.append("No modifications");
        }
                       
        else {
                        
            for (SearchModification modificationList : modList) {
                Boolean fixed = modificationList.isFixedMod();
                
                if (!fixed) {
                    List <String> modificationName = modificationList.getResidues();
                    for (int i = 0; i < modificationName.size(); i++) {
                    variableModificationsBuilder.append(modificationName.get(i));
                    }                                              
                }
            }
        }
        return variableModificationsBuilder.toString();
    }
      
}
