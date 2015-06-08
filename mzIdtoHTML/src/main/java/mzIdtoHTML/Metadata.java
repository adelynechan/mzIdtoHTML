/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;


import java.util.Iterator;
import uk.ac.ebi.jmzidml.model.mzidml.*;

import java.util.List;
import uk.ac.ebi.jmzidml.MzIdentMLElement;

/**
 *
 * @author Adelyne Extracts metadata from the mzIdentML file and returns a
 * string This string is accessed by the results file for integration into HTML
 * template
 */
public class Metadata {

    // Search type 
    // <xsd:element name="SearchType" type="ParamType">
    // <xsd:documentation> The type of search performed e.g. PMF, Tag searches, MS-MS
    String getSearchType() {

        SpectrumIdentificationProtocol sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);
        Param searchTypeParam = sip.getSearchType();
        CvParam searchCv = searchTypeParam.getCvParam();
        String searchType = searchCv.getName();

        return searchType;
    }

    // Name of analysis software used
    // <xsd:element name="AnalysisSoftware" type="AnalysisSoftwareType" maxOccurs="unbounded"/>
    // <xsd:documentation> The software packages used to perform the analyses  
    String getSoftwareName() {
        
        StringBuilder softwareNameBuilder = new StringBuilder();
        
        Iterator <AnalysisSoftware> iterAS = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.AnalysisSoftware);
        while (iterAS.hasNext()) {
            AnalysisSoftware as = iterAS.next();
            Param softwareType = as.getSoftwareName();
            
            if (softwareType == null) {
                softwareNameBuilder.append("Information not available");
            }
            
            else {
                softwareNameBuilder.append(as.getSoftwareName().getCvParam().getName());
            }
        } 
        return softwareNameBuilder.toString();
    }

    // Enzyme(s) (if empty return "Information not available")
    // <xsd:element name="Enzymes" type="EnzymesType" minOccurs="0"/>
    String getEnzymesUsed() {

        SpectrumIdentificationProtocol sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);
        List<Enzyme> enzList = sip.getEnzymes().getEnzyme();
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

    // Fixed modifications searched (if empty return "No modifications")
    String getFixedModifications() {
        SpectrumIdentificationProtocol sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);

        ModificationParams mp = sip.getModificationParams();
        List<SearchModification> modList = mp.getSearchModification();

        StringBuilder fixedModificationsBuilder = new StringBuilder();

        if (modList.isEmpty()) {
            fixedModificationsBuilder.append("No modifications");
        } else {

            for (SearchModification modificationList : modList) {
                Boolean fixed = modificationList.isFixedMod();

                if (fixed) {
                    List<String> modificationName = modificationList.getResidues();
                    for (int i = 0; i < modificationName.size(); i++) {
                        fixedModificationsBuilder.append(modificationName.get(i));
                    }
                }
            }
        }
        return fixedModificationsBuilder.toString();
    }

    // Variable modifications searched (if empty return "No modifications")
    String getVariableModifications() {
        SpectrumIdentificationProtocol sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);

        ModificationParams mp = sip.getModificationParams();
        List<SearchModification> modList = mp.getSearchModification();

        StringBuilder variableModificationsBuilder = new StringBuilder();

        if (modList.isEmpty()) {
            variableModificationsBuilder.append("No modifications");
        } else {

            for (SearchModification modificationList : modList) {
                Boolean fixed = modificationList.isFixedMod();

                if (!fixed) {
                    List<String> modificationName = modificationList.getResidues();
                    for (int i = 0; i < modificationName.size(); i++) {
                        variableModificationsBuilder.append(modificationName.get(i));
                    }
                }
            }
        }
        return variableModificationsBuilder.toString();
    }

}
