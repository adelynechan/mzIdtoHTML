/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.*;

import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

/**
 *
 * @author Adelyne
 */
public class MzidToHTML {

    // Method to unmarshal the file and store contents in memory
    // Public so other objects can access the unmarshalled data
    public static MzIdentMLUnmarshaller unmarshaller;

    public MzidToHTML(File file) {
        if (file.exists()) {
            System.out.println("file exists");
            unmarshaller = new MzIdentMLUnmarshaller(file);
        }
    }

    public void convert(String output) {
        File htmlTemplateFile = new File("template.html");
        
        // Extract information required for metadata menu section
        Metadata metadata = new Metadata();
        String searchType = metadata.getSearchType();
        String softwareName = metadata.getSoftwareName();
        String enzymesUsed = metadata.getEnzymesUsed();
        String fixedModifications = metadata.getFixedModifications();
        String variableModifications = metadata.getVariableModifications();
        
        // Extract information required for peptide view table
        PeptideInfo peptideinfo = new PeptideInfo();
        List<String> peptideView = peptideinfo.getPeptideInfo(); // Method returns a list with 2 elements
        String peptideTable = peptideView.get(0); // String containing contents of HTML table
        String scoreName = peptideView.get(1); // String containing type of score (for table header)
        
        //GlobalStatistics globalStatistics = new GlobalStatistics();
        //int peptideNumber = GlobalStatistics.getPeptideNumber();
        //String peptideNumberString = Integer.toString(peptideNumber);
        
        try {
            String htmlString = FileUtils.readFileToString(htmlTemplateFile);                
            
            htmlString = htmlString.replace("$searchtype", searchType);
            htmlString = htmlString.replace("$softwarename", softwareName);
            htmlString = htmlString.replace("$enzymes", enzymesUsed);
            htmlString = htmlString.replace("$fixedmodifications", fixedModifications);
            htmlString = htmlString.replace("$variablemodifications", variableModifications);
            
            htmlString = htmlString.replace("$peptideinfo", peptideTable);
            htmlString = htmlString.replace("$scorename", scoreName);
            
            htmlString = htmlString.replace("$peptidenumber", peptideNumberString);
            
            File newHtmlFile = new File(output);
            FileUtils.writeStringToFile(newHtmlFile, htmlString);
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }      
    }

    public static void main(String[] args) {
        String input = "GalaxyExampleProteoGrouper.mzid";
//        input = args[0];
        String output = "result.html";
        MzidToHTML converter = new MzidToHTML(new File(input));
        
        converter.convert(output);       
    }
}
        

