/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.File;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Adelyne
 */
public class Results {
    
    public static void main(String[] args) {
        
        try {
            
            File file = new File("GalaxyExampleProteoGrouper.mzid");
            File htmlTemplateFile = new File("template.html");
            
            Metadata metadata = new Metadata();
            Peptideinfo peptideinfo = new Peptideinfo();
            
            String searchType = Metadata.getSearchType(file);
            String softwareName = Metadata.getSoftwareName(file);
            String enzymesUsed = Metadata.getEnzymesUsed(file);
            String fixedModifications = Metadata.getFixedModifications(file);
            String variableModifications = Metadata.getVariableModifications(file);
            
            String peptideInfo = Peptideinfo.getPeptideInfo(file);
                                             
            String htmlString = FileUtils.readFileToString(htmlTemplateFile);
                
            htmlString = htmlString.replace("$searchtype", searchType);
            htmlString = htmlString.replace("$softwarename", softwareName);
            htmlString = htmlString.replace("$enzymes", enzymesUsed);
            htmlString = htmlString.replace("$fixedmodifications", fixedModifications);
            htmlString = htmlString.replace("$variablemodifications", variableModifications);
            
            htmlString = htmlString.replace("$peptideinfo", peptideInfo);
            
            File newHtmlFile = new File("template1.html");
            FileUtils.writeStringToFile(newHtmlFile, htmlString);
            
        }
        
        catch(Exception e) {
            e.printStackTrace(); }
    }
    
}
