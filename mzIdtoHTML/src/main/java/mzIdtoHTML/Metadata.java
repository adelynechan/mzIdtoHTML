/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;


import java.util.Iterator;
import java.util.ArrayList;
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
    SpectrumIdentificationProtocol sip;
    StringBuilder fixedModificationsBuilder;
    StringBuilder variableModificationsBuilder;
    
    public Metadata(){
        sip = MzidToHTML.unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);
        initMods();
    }
    

    private void initMods(){
        fixedModificationsBuilder = new StringBuilder();
        variableModificationsBuilder = new StringBuilder();
//    <xsd:element name="ModificationParams" type="ModificationParamsType" minOccurs="0"/>
        ModificationParams mp = sip.getModificationParams();
        if(mp==null){
            fixedModificationsBuilder.append("No modifications");
            variableModificationsBuilder.append("No modifications");
            return;
        }
//    <xsd:element name="SearchModification" type="SearchModificationType" maxOccurs="unbounded"/>
        List<SearchModification> modList = mp.getSearchModification();
        if (modList.isEmpty()) {
            fixedModificationsBuilder.append("No modifications");
            variableModificationsBuilder.append("No modifications");
            return;
        } else {
            for (SearchModification modification : modList) {
                boolean fixed = modification.isFixedMod();
                String modStr = getModificationString(modification);
                if (fixed) {
                    fixedModificationsBuilder.append(modStr);
                    fixedModificationsBuilder.append(", ");
                } else {
                    variableModificationsBuilder.append(modStr);
                    variableModificationsBuilder.append(", ");
                }
            }
        }
        if(fixedModificationsBuilder.length()>0){
            fixedModificationsBuilder.deleteCharAt(fixedModificationsBuilder.length()-1);
            fixedModificationsBuilder.deleteCharAt(fixedModificationsBuilder.length()-1);
        }else{
            fixedModificationsBuilder.append("No modifications");
        }
        if(variableModificationsBuilder.length()>0){
            variableModificationsBuilder.deleteCharAt(variableModificationsBuilder.length()-1);
            variableModificationsBuilder.deleteCharAt(variableModificationsBuilder.length()-1);
        }else{
            variableModificationsBuilder.append("No modifications");
        }
    }

    private String getModificationString(SearchModification modification) {
        StringBuilder sb = new StringBuilder();
        List<String> modificationName = modification.getResidues();
        List<CvParam> cvs = modification.getCvParam();
        for(CvParam cv: cvs){
            if(cv.getCvRef().equals("PSI-MOD")||cv.getCvRef().equals("UNIMOD")){
                sb.append(cv.getName());
                sb.append(" on ");
//              <xsd:element name="SpecificityRules" type="SpecificityRulesType" minOccurs="0" maxOccurs="unbounded"/>
                List<SpecificityRules> rules = modification.getSpecificityRules();
                if (rules!=null && rules.size()>0){
//                  <xsd:element name="cvParam" type="CVParamType" minOccurs="1" maxOccurs="unbounded"/>
                    for(CvParam specificityCV:rules.get(0).getCvParam()){
//id: MS:1001189
//name: modification specificity peptide N-term
//id: MS:1001190
//name: modification specificity peptide C-term
//id: MS:1002057
//name: modification specificity protein N-term
//id: MS:1002058
//name: modification specificity protein C-term
                        if(specificityCV.getCvRef().equals("PSI-MS")){
                            String acc = specificityCV.getAccession();
                            switch(acc){
                                case "MS:1001189":
                                    sb.append("N-term ");
                                    break;
                                case "MS:1001190":
                                    sb.append("C-term ");
                                    break;
                                case "MS:1002057":
                                    sb.append("Protein N-term ");
                                    break;
                                case "MS:1002058":
                                    sb.append("Protein C-term ");
                            }
                        }
                    }
                }
                String mod = modificationName.get(0);
                if (mod.equals(".")){//For N or C terminal modifications that can occur on any residue, the . character should be used to specify any
                    mod = "Any";
                }
                sb.append(mod);
                break;
            }
        }
        return sb.toString();
    }
    // Search type 
    // <xsd:element name="SearchType" type="ParamType">  which means minOccurs=1 maxOccurs=1
    // <xsd:documentation> The type of search performed e.g. PMF, Tag searches, MS-MS
    String getSearchType() {
        Param searchTypeParam = sip.getSearchType();
        return searchTypeParam.getCvParam().getName();
    }

    // Name of analysis software used
    // <xsd:element name="AnalysisSoftware" type="AnalysisSoftwareType" maxOccurs="unbounded"/>
    // <xsd:documentation> The software packages used to perform the analyses  
    String getSoftwareName() {
        ArrayList <String> softwareNameList = new ArrayList();
        Iterator <AnalysisSoftware> iterAS = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath(MzIdentMLElement.AnalysisSoftware);
        while (iterAS.hasNext()) {
            AnalysisSoftware as = iterAS.next();
            Param softwareType = as.getSoftwareName();
            if (softwareType != null) {
                String softwareName = as.getSoftwareName().getCvParam().getName();
                if (!softwareNameList.contains(softwareName)) {
                    softwareNameList.add(as.getSoftwareName().getCvParam().getName());
                }          
            }
        } 
        if (softwareNameList.isEmpty()){
            softwareNameList.add("Information not available");
        }
        
        return String.join(", ", softwareNameList);
    }

    // Enzyme(s) (if empty return "Information not available")
    // <xsd:element name="Enzymes" type="EnzymesType" minOccurs="0"/>
    String getEnzymesUsed() {
        List<Enzyme> enzList = sip.getEnzymes().getEnzyme();
        StringBuilder enzymeBuilder = new StringBuilder();

        if (enzList.isEmpty()) { 
            enzymeBuilder.append("Information not available");
        } else {
            for (Enzyme enzymeList : enzList) {
                ParamList enzymeName = enzymeList.getEnzymeName();
                List<CvParam> enzymeParam;
                enzymeParam = enzymeName.getCvParam();

                for (int i = 0; i < enzymeParam.size(); i++) {
                    enzymeBuilder.append(enzymeParam.get(i).getName());
                    enzymeBuilder.append(" ");
                }
            }
            enzymeBuilder.deleteCharAt(enzymeBuilder.length()-1);
        }

        return enzymeBuilder.toString();
    }

    // Fixed modifications searched (if empty return "No modifications")
    String getFixedModifications() {
        return fixedModificationsBuilder.toString();
    }

    // Variable modifications searched (if empty return "No modifications")
    String getVariableModifications() {
        return variableModificationsBuilder.toString();
    }

}
