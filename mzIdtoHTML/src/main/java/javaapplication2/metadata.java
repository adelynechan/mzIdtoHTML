/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.File;
import org.apache.commons.io.FileUtils;

import uk.ac.ebi.jmzidml.model.mzidml.*;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

import java.util.List;
//import java.lang.StringBuilder;

/**
 *
 * @author Adelyne
 */
public class metadata {

    public static void main(String[] args) {

        try {

            File file = new File("GalaxyExampleProteoGrouper.mzid");
            
            if (file.exists()) {

                boolean aUseSpectrumCache = true;
                MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(file);

                SpectrumIdentificationProtocol sip = unmarshaller.unmarshal(SpectrumIdentificationProtocol.class);
                AnalysisSoftware as = unmarshaller.unmarshal(AnalysisSoftware.class);

                // Extract search type information
                Param searchType = sip.getSearchType();
                CvParam searchCv = searchType.getCvParam();
                String searchString = searchCv.getName();

                // Extract software name information
                Param softwareType = as.getSoftwareName();
                CvParam softwareCv = softwareType.getCvParam();
                String softwareString = softwareCv.getName();

                // Extract enzymes as a list
                Enzymes enzymes = sip.getEnzymes();
                List<Enzyme> enzList = enzymes.getEnzyme();

                // Extract search modifications as a list
                ModificationParams mp = sip.getModificationParams();
                List<SearchModification> modList = mp.getSearchModification();
                
                // Print out extracted metadata to output file
                // Based on HTML template
                String printSearchType = "Type of Search: " + searchString;
                String printSoftwareName = "Analysis Software: " + softwareString;
                
                StringBuilder enzymeBuilder = new StringBuilder();

                for (Enzyme enzymeList : enzList) {
                    ParamList enzymeName = enzymeList.getEnzymeName();
                    List<CvParam> enzymeParam;
                    enzymeParam = enzymeName.getCvParam();

                    for (int i = 0; i < enzymeParam.size(); i++) {
                        enzymeBuilder.append(enzymeParam.get(i).getName());
                    }
                }

                String printEnzymes = "Enzymes: " + enzymeBuilder.toString();
                                
                StringBuilder modificationsBuilder = new StringBuilder();
                
                if (modList.isEmpty()) {
                    modificationsBuilder.append("No modifications");
                }
               
                else {
                    for (SearchModification modificationList : modList) {
                    List<String> modificationName = modificationList.getResidues();
                    
                    for (int i = 0; i < modificationName.size(); i++) {
                            modificationsBuilder.append(modificationName.get(i));
                            modificationsBuilder.append(modificationName.size());
                        }                                              
                    }
                }
                String printModifications = "Modifications: " + modificationsBuilder.toString();
                                               
                File htmlTemplateFile = new File("template.html");
                String htmlString = FileUtils.readFileToString(htmlTemplateFile);
                
                htmlString = htmlString.replace("$searchtype", printSearchType);
                htmlString = htmlString.replace("$softwarename", printSoftwareName);
                htmlString = htmlString.replace("$enzymes", printEnzymes);
                htmlString = htmlString.replace("$modifications", printModifications);
                File newHtmlFile = new File("template1.html");
                FileUtils.writeStringToFile(newHtmlFile, htmlString);
                }
        } 
        catch (Exception e) {
            e.printStackTrace();
    }

}
}
