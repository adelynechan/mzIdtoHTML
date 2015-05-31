/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionList;

import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

/**
 *
 * @author Adelyne
 */
public class MzidToHTML {

    // Unmarshal the file and store contents in memory
    // Public so other objects can access the unmarshalled data
    public static MzIdentMLUnmarshaller unmarshaller;
    
    // Check if the file exists (modify later to include mzIdentML verification?)
    // Unmarshals the file using the unmarshaller if the file exists
    // Prints out a message to the console 
    public MzidToHTML(File file) {
        if (file.exists()) {
            System.out.println("file exists");
            unmarshaller = new MzIdentMLUnmarshaller(file);
        }
    }
    
    // Build the header portion of the results page 
    // Covers the title (incl. input file name) and references to external stylesheets
    private String getHeader(String input) {
        StringBuilder headerBuilder = new StringBuilder();
        
        headerBuilder.append("<html>");
        headerBuilder.append("<head>");
        //headerBuilder.append("<link rel='stylesheet' type='text/css' href='stylesheet.css'>");
        headerBuilder.append("<link rel=\"stylesheet\" "
            + "href=\"stylesheet.css\">"
            + "<script src=\"http://code.jquery.com/jquery-latest.min.js\" type=\"text/javascript\"></script>");
        
        headerBuilder.append("<title>mzIdtoHTML</title>");
        headerBuilder.append("<h1> mzIdentML to HTML for ");
        headerBuilder.append(input);
        headerBuilder.append("</h1>");
        headerBuilder.append("</head>");  
        
        return headerBuilder.toString();
    }
    
    // Builds the metadata portion of the results file
    // Return a single string that can later be written to a file 
    private String getMetadata() {
        
        // New instance of object Metadata
        Metadata metadata = new Metadata(); 
        
        // New stringbuilder to which metadata elements and HTML tags can be appended
        // Converted to string and returned as method output
        StringBuilder metadataBuilder = new StringBuilder(); 
        
        ////metadataBuilder.append("<div data-role=\"collapsible\" data-collapsed = \"true\">");
        metadataBuilder.append("<h2> Metadata </h2>");
        
        // Search Type: Can only have one result per mzIdentML file
        metadataBuilder.append("<p>");
        metadataBuilder.append("Search Type: ");
        metadataBuilder.append(metadata.getSearchType());
        metadataBuilder.append("</p>");
        
        // Software Name(s)
        metadataBuilder.append("<p>");
        metadataBuilder.append("Software Name: ");
        metadataBuilder.append(metadata.getSoftwareName());
        metadataBuilder.append("</p>");
        
        // Enzyme(s) Used
        metadataBuilder.append("<p>");
        metadataBuilder.append("Enzymes Used: ");
        metadataBuilder.append(metadata.getEnzymesUsed());
        metadataBuilder.append("</p>");
        
        // Fixed modification(s) searched
        metadataBuilder.append("<p>");
        metadataBuilder.append("Fixed Modifications: ");
        metadataBuilder.append(metadata.getFixedModifications());
        metadataBuilder.append("</p>");
        
        // Variable modification(s) searched
        metadataBuilder.append("<p>");
        metadataBuilder.append("Variable Modifications: ");
        metadataBuilder.append(metadata.getVariableModifications());
        metadataBuilder.append("</p>");
        
        ////metadataBuilder.append("</div>");
        
        // Convert stringbuilder to a string and return
        return metadataBuilder.toString();
    }
    
    private String getGlobalStatistics() {
        
        // New instance of object GlobalStatistics
        GlobalStatistics globalStatistics = new GlobalStatistics();
        
        // New stringbuilder to which statistics elements and HTML tags can be appended
        // Converted to string and returned as method output
        StringBuilder statisticsBuilder = new StringBuilder();
        
        // Creates the header for this item
        statisticsBuilder.append("<h2> Global Statistics </h2>");
        
        int peptideNumber = globalStatistics.getPeptideNumber();
        //int peptideDecoyPercentage = globalStatistics.getDecoyPercentage();
        //think about how to get the decoy percentage? current method takes too long 
        
        statisticsBuilder.append("Peptide Number: ");
        statisticsBuilder.append(Integer.toString(peptideNumber));
        //statisticsBuilder.append("Decoy Percentage: " );
        //statisticsBuilder.append(peptideDecoyString);
        
        // Convert stringbuilder to a string and return
        return statisticsBuilder.toString();
    }

    private String getPeptideInfoMain() {
        
        // New instance of object PeptideInfo
        PeptideInfo peptideInfoMain = new PeptideInfo();
        
        // New stringbuilder to which peptideinfo elements and HTML tags can be appended
        // HTML tags include those required for formatting in table
        // Converted to string and returned as method output
        StringBuilder peptideInfoMainBuilder = new StringBuilder();
        
        List<String> peptideView = peptideInfoMain.getPeptideInfo(); // Method returns a list with 2 elements
        String peptideTable = peptideView.get(0); // String containing contents of HTML table
        String scoreName = peptideView.get(1); // String containing type of score (for table header)
        
        // Create the main header "Peptide View"
        peptideInfoMainBuilder.append("<h2> Peptide View </h2>");
        
        // Create the header row of the table
        peptideInfoMainBuilder.append("<table> two tables? <table style = 'width:100%'>\n\t<tr>");
        peptideInfoMainBuilder.append("<th>PSM ID</th>\n<th>Sequence</th> \n<th>Calc m/z</th> "
                + "\n<th>Exp m/z</th> \n<th>Charge</th> \n<th>Modifications</th> \n<th>Score: ");
        peptideInfoMainBuilder.append(peptideView.get(1)); // Get type of score and include in header
        peptideInfoMainBuilder.append("</th> <th>Associated Proteins</th> </tr>");
        
        // Extract table contents (1 string from the peptideView list) and append
        peptideInfoMainBuilder.append(peptideView.get(0));
        peptideInfoMainBuilder.append("</table>");
        
        return peptideInfoMainBuilder.toString();
    }
    
    private String getProteinInfoMain() {
        ProteinInfo proteinInfoMain = new ProteinInfo();
        StringBuilder proteinInfoMainBuilder = new StringBuilder();
        ProteinDetectionList pdl = proteinInfoMain.getProteinDetectionList();
           
        if (pdl != null) {
            
            proteinInfoMainBuilder.append("<h2> Protein View </h2>");
            proteinInfoMainBuilder.append("<table> <table style = 'width:100%'>\n\t<tr>");
            proteinInfoMainBuilder.append("<th>Accession #</th>\n<th>Species</th> \n<th>Protein Name</th> "
                + "\n<th>FDR Score</th> \n<th>Observed/Observable</th></tr>");
        }
        
        return proteinInfoMainBuilder.toString();
    }
    
    public void convert(String input, String output) {
        
        // Name the results file "results.html" (String output in main method)
        File htmlTemplateFile = new File(output);      
        
        // Create the file if it does not already exist
        if (!htmlTemplateFile.exists()) {
            try {
                htmlTemplateFile.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }         
        }
        
        try { 
            
            // New filewriter to write items to the results.html file
            FileWriter fileWriter = new FileWriter(htmlTemplateFile, true);
            fileWriter.append(getHeader(input)); // Header
            fileWriter.append(getMetadata()); // Metadata menu item 1
            fileWriter.append(getGlobalStatistics()); // Statistics menu item 2
            fileWriter.append(getPeptideInfoMain()); // Peptides menu item 3
            
            
            
            fileWriter.append(getProteinInfoMain()); // Protein menu item 4 (not always present)
            fileWriter.close();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }      
    }

    public static void main(String[] args) {
        String input = "GalaxyExamplePeptide.mzid";
//        input = args[0];
        String output = "result.html";
        MzidToHTML converter = new MzidToHTML(new File(input));
        converter.convert(input, output);   

    }
}
        

