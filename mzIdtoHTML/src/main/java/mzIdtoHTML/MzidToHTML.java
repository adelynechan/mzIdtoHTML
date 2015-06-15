/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;
import java.io.FileWriter;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionList;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;

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
        headerBuilder.append("\n<head>");
        headerBuilder.append("\n<link rel=\"stylesheet\" href=\"stylesheet.css\">");
        
        // Include the source for Jquery (downloaded, if need be change to hosted link here)
        headerBuilder.append("\n<script src=\"jquery-1.11.3.min.js\"></script>");
        // Include the source for Jquery functions written for this project
        headerBuilder.append("<script src=\"mzidJqueryFunctions.js\"></script>");
      
   
            //+ "<script src=\"http://code.jquery.com/jquery-latest.min.js\" type=\"text/javascript\"></script>"
        //headerBuilder.append("<script src=\"sorttable.js\" type=\"text/javascript\"></script>");

        headerBuilder.append("\n<title>mzIdtoHTML</title>");
        headerBuilder.append("\n<h1> mzIdentML to HTML for ");
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
        
        // New stringbuilder to which statistics elements and HTML tags can be appended
        // Converted to string and returned as method output
        StringBuilder statisticsBuilder = new StringBuilder();
        
        // Creates the header for this item
        statisticsBuilder.append("\n<h2> Global Statistics </h2>");
        
        int peptideNumber = globalStatisticsMain.getPeptideNumber();
        Double decoyPercentage = globalStatisticsMain.getDecoyPercentage();
        Double globalFDR = globalStatisticsMain.getGlobalFDR();
        int proteinNumber = globalStatisticsMain.getProteinNumber();
        
        statisticsBuilder.append("\n<p> Peptide Number: ");
        statisticsBuilder.append(Integer.toString(peptideNumber));
        statisticsBuilder.append("</p>");
        statisticsBuilder.append("\n<p> Decoy Percentage: " );
        statisticsBuilder.append(String.format("%.2f", decoyPercentage));
        statisticsBuilder.append("</p>");
        statisticsBuilder.append("\n<p> Global FDR: ");
        statisticsBuilder.append(String.format("%.2f ", globalFDR));
        statisticsBuilder.append("</p>");
        statisticsBuilder.append("\n<p> Protein Number: ");
        statisticsBuilder.append(Integer.toString(proteinNumber));
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
        
        // Specify features of table - sortable (takes time!), table width
        // for sortable plugin //peptideInfoMainBuilder.append("<table><table class = \"sortable\" table style = \"width = 100%\"");
        peptideInfoMainBuilder.append("\n<table><table style = \"width = 100%\"");
        
        // Create the header row of the table
        peptideInfoMainBuilder.append("<tr>\n<th>PSM ID</th>\n<th>Sequence</th> \n<th>Calc m/z</th> "
                + "\n<th>Exp m/z</th> \n<th>Charge</th> \n<th>Modifications</th> \n<th>Score: ");
        peptideInfoMainBuilder.append(peptideView.get(1)); // Get type of score and include in header
        peptideInfoMainBuilder.append("</th> \n<th>Associated Proteins</th> \n</tr>");
        
        // Extract table contents (1 string from the peptideView list) and append
        peptideInfoMainBuilder.append(peptideView.get(0));
        peptideInfoMainBuilder.append("\n</table>");
        
        return peptideInfoMainBuilder.toString();
    }
    
    private String getProteinInfoMain() {
        ProteinInfo proteinInfoMain = new ProteinInfo();
        StringBuilder proteinInfoMainBuilder = new StringBuilder();
        
        ProteinDetectionList pdl = proteinInfoMain.getProteinDetectionList();
        List <String> proteinView = proteinInfoMain.getProteinInfo();
        
           
        if (pdl != null) {           
            proteinInfoMainBuilder.append("\n<h2> Protein View </h2>");
            proteinInfoMainBuilder.append("\n<table> <table style = 'width:100%'>\n\t<tr>");
            proteinInfoMainBuilder.append("\n<th>Accession #</th>\n<th>Species</th> \n<th>Protein Name</th> \n<th>Score: ");
            proteinInfoMainBuilder.append(proteinView.get(1));
            proteinInfoMainBuilder.append("</th> \n<th>Detected Peptide Coverage (%)</th>\n</tr>");
            proteinInfoMainBuilder.append(proteinView.get(0));
        }
        
        return proteinInfoMainBuilder.toString();
    }
    
    public void convert(String input, String output) {
        
        // Name the results file "results.html" (String output in main method)
        File htmlTemplateFile = new File(output);   
        
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
        String input = "GalaxyExampleProteoGrouper.mzid"; // Smaller mzid test file
        //String input = "GalaxyExampleBig.mzid"; // Full dataset test file - currently takes very long to run
//        input = args[0];
        String output = "result.html";
        MzidToHTML converter = new MzidToHTML(new File(input));
        converter.convert(input, output);
    }
}

        

