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
    
    final private String input;
    
    // Check if the file exists (modify later to include mzIdentML verification?)
    // Unmarshals the file using the unmarshaller if the file exists
    // Prints out a message to the console 
    public MzidToHTML(String in) {
        System.out.println("Checking the existance of the file");
        input = in;
        File file = new File(input);
        if (file.exists()) {
            unmarshaller = new MzIdentMLUnmarshaller(file);
        }else{
            System.out.println("Cannot find the input file "+in);
            System.exit(2);
        }
    }
    
    // Build the header portion of the results page 
    // Covers the title (incl. input file name) and references to external stylesheets
    private String getHeader(String input) {
        StringBuilder headerBuilder = new StringBuilder();
        
        headerBuilder.append("<html>");
        headerBuilder.append("\n<head>");
        headerBuilder.append("\n<link rel=\"stylesheet\" href=\"stylesheet.css\">");
        
        // Include the source for Jquery (downloaded, if need be change to hosted link here)
        headerBuilder.append("\n<script src=\"js/jquery-1.11.3.min.js\"></script>");
        // Include the source for Jquery functions written for this project
        headerBuilder.append("<script src=\"js/mzidJqueryFunctions.js\"></script>");  
        //headerBuilder.append("<script src=\"js/mzidJqueryFunctions_DT.js\"></script>");
        //headerBuilder.append("<script src=\"js/sorttable.js\" type=\"text/javascript\"></script>");
        
        // Alternative plugins for pagination: Simple pagination which only does pagination or
        // Data tables which does sorting (does not seem to work) and searching as well
        headerBuilder.append("<script src=\"js/jquery.simplePagination.js\"></script>"); // Pagination
        //headerBuilder.append("<script src=\"http://cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js\"></script>");

        headerBuilder.append("\n<title>mzIdtoHTML</title>");
        headerBuilder.append("\n<h1> Converted From ");
        headerBuilder.append(input);
        headerBuilder.append("\n</h1>");
        headerBuilder.append("\n</head>");  
        
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
             
        metadataBuilder.append("\n<h2> Metadata </h2>");
        
        // Search Type: Can only have one result per mzIdentML file
        metadataBuilder.append("\n<p>");
        metadataBuilder.append("Search Type: ");
        metadataBuilder.append(metadata.getSearchType());
        metadataBuilder.append("</p>");
        
        // Software Name(s)
        metadataBuilder.append("\n<p>");
        metadataBuilder.append("List of Software Used: ");
        metadataBuilder.append(metadata.getSoftwareName());
        metadataBuilder.append("</p>");
        
        // Enzyme(s) Used
        metadataBuilder.append("\n<p>");
        metadataBuilder.append("Enzymes Used: ");
        metadataBuilder.append(metadata.getEnzymesUsed());
        metadataBuilder.append("</p>");
        
        // Fixed modification(s) searched
        metadataBuilder.append("\n<p>");
        metadataBuilder.append("Fixed Modifications: ");
        metadataBuilder.append(metadata.getFixedModifications());
        metadataBuilder.append("</p>");
        
        // Variable modification(s) searched
        metadataBuilder.append("\n<p>");
        metadataBuilder.append("Variable Modifications: ");
        metadataBuilder.append(metadata.getVariableModifications());
        metadataBuilder.append("</p>");
        
        // Convert stringbuilder to a string and return
        return metadataBuilder.toString();
    }
    
    private String getGlobalStatistics() {
        
        // New instance of object GlobalStatistics
        GlobalStatistics globalStatisticsMain = new GlobalStatistics();
        ProteinInfo proteinInfoMain = new ProteinInfo();
        
        // New stringbuilder to which statistics elements and HTML tags can be appended
        // Converted to string and returned as method output
        StringBuilder statisticsBuilder = new StringBuilder();
        
        // Creates the header for this item
        statisticsBuilder.append("\n<h2> Global Statistics </h2>");
        
        int peptideNumber = globalStatisticsMain.getPeptideNumber();
        Double decoyPercentage = globalStatisticsMain.getDecoyPercentage();
        int proteinNumber = globalStatisticsMain.getProteinNumber();
        
        statisticsBuilder.append("\n<p> Peptide Number: ");
        statisticsBuilder.append(Integer.toString(peptideNumber));
        statisticsBuilder.append("</p>");
        statisticsBuilder.append("\n<p> Decoy Percentage: " );
        statisticsBuilder.append(String.format("%.2f", decoyPercentage));
        statisticsBuilder.append("</p>");
        statisticsBuilder.append("\n<p> Protein Number: ");
        statisticsBuilder.append(Integer.toString(proteinNumber));
        statisticsBuilder.append("</p>");
        statisticsBuilder.append("\n<p> Minimum Number of Detected Peptides for a Protein: ");
        statisticsBuilder.append(proteinInfoMain.getProteinInfo().get(2));
        statisticsBuilder.append("</p>");
        statisticsBuilder.append("\n<p> Maximum Number of Detected Peptides for a Protein: ");
        statisticsBuilder.append(proteinInfoMain.getProteinInfo().get(3));
        statisticsBuilder.append("</p>");
        
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
        
        // Create the main header "Peptide View"
        peptideInfoMainBuilder.append("\n<h2> Peptide View </h2>");
        
        // Specify features of table, table width
        peptideInfoMainBuilder.append("<table><table id = \"peptides\" class = \"display\" style = \"width = 100%\">");
        
        
        // Create the header row of the table
        peptideInfoMainBuilder.append("<thead>\n<tr>\n<th> PSM ID></th>\n<th>Sequence</th> \n<th>Calc m/z</th> "
                + "\n<th>Exp m/z</th> \n<th>Charge</th> \n<th>Modifications</th> \n<th>Score: ");
        peptideInfoMainBuilder.append(peptideView.get(1)); // Get type of score and include in header
        peptideInfoMainBuilder.append("</th> \n<th>Associated Proteins</th> \n</tr>\n</thead>");
        peptideInfoMainBuilder.append("\n<tbody>");
        
        // Extract table contents (1 string from the peptideView list) and append
        peptideInfoMainBuilder.append(peptideView.get(0));
        peptideInfoMainBuilder.append("\n</tbody> \n</table>");
        peptideInfoMainBuilder.append("\n<div id=\"peptide-nav\"></div>");
       
        return peptideInfoMainBuilder.toString();
    }
    
    private String getProteinInfoMain() {
        ProteinInfo proteinInfoMain = new ProteinInfo();
        StringBuilder proteinInfoMainBuilder = new StringBuilder();
        
        ProteinDetectionList pdl = proteinInfoMain.getProteinDetectionList();
        List <String> proteinView = proteinInfoMain.getProteinInfo();
        
           
        if (pdl != null) {           
            proteinInfoMainBuilder.append("\n<h2> Protein View </h2>");
            proteinInfoMainBuilder.append("\n<table> <table id = \"proteins\" class = \"display\" style = 'width:100%'>");
            proteinInfoMainBuilder.append("\n<thead>\n<tr>\n<th>Accession #</th>\n<th>Species</th> \n<th>Protein Name</th> \n<th>Score: ");
            proteinInfoMainBuilder.append(proteinView.get(1));
            proteinInfoMainBuilder.append("</th> \n<th>Peptide Coverage</th> \n</tr> \n</thead>");
            proteinInfoMainBuilder.append("<tbody>");
            proteinInfoMainBuilder.append(proteinView.get(0));
            proteinInfoMainBuilder.append("\n</tbody> \n</table>");
            proteinInfoMainBuilder.append("\n<div id=\"protein-nav\"></div>");
        }
        
        return proteinInfoMainBuilder.toString();
    }
    
    public void convert(String output) {
        
        // Name the results file "results.html" (String output in main method)
        File htmlTemplateFile = new File(output);   
        
        try { 
            
            // New filewriter to write items to the results.html file
            FileWriter fileWriter = new FileWriter(htmlTemplateFile, false);
 
            fileWriter.append(getHeader(input)); // Header
            fileWriter.append(getMetadata()); // Metadata menu item 1
//            fileWriter.append(getGlobalStatistics()); // Statistics menu item 2
//            fileWriter.append(getPeptideInfoMain()); // Peptides menu item 3            
//            fileWriter.append(getProteinInfoMain()); // Protein menu item 4 (not always present)
            
            fileWriter.close();
        }catch (Exception e) {
            e.printStackTrace();
        }      
    }

    public static void main(String[] args) {
//        if (args.length!=2){
//            System.out.println("Wrong number of parameters");
//            usage();
//        }
        String output = "resultSP.html";
//        String output = args[1];
//        MzidToHTML converter = new MzidToHTML("GalaxyExampleProteoGrouper.mzid");
//        MzidToHTML converter = new MzidToHTML("GalaxyExamplePeptide.mzid");
//        MzidToHTML converter = new MzidToHTML("ExampleMod.mzid");
//        MzidToHTML converter = new MzidToHTML("ExampleNtermMod.mzid");
        MzidToHTML converter = new MzidToHTML("ExampleCtermMod.mzid");
//        MzidToHTML converter = new MzidToHTML(args[0]);
        converter.convert(output);  
    }
    
    static private void usage(){
        System.out.println("Usage: java -jar MzidToHTML.jar <input.mzid> <output.html>");
        System.out.println("The program ");
        System.exit(1);
    }
}
        

