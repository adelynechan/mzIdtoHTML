/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

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
    
    private String getMetadata() {
        Metadata metadata = new Metadata();
        StringBuilder metadataBuilder = new StringBuilder();
        
        //metadataBuilder.append("<div data-role=\"collapsible\" data-collapsed = \"true\">");
        metadataBuilder.append("<h2> Metadata </h2>");
        
        metadataBuilder.append("<p>");
        metadataBuilder.append("Search Type: ");
        metadataBuilder.append(metadata.getSearchType());
        metadataBuilder.append("</p>");
        
        metadataBuilder.append("<p>");
        metadataBuilder.append("Software Name: ");
        metadataBuilder.append(metadata.getSoftwareName());
        metadataBuilder.append("</p>");
        
        metadataBuilder.append("<p>");
        metadataBuilder.append("Enzymes Used: ");
        metadataBuilder.append(metadata.getEnzymesUsed());
        metadataBuilder.append("</p>");
        
        metadataBuilder.append("<p>");
        metadataBuilder.append("Fixed Modifications: ");
        metadataBuilder.append(metadata.getFixedModifications());
        metadataBuilder.append("</p>");
        
        metadataBuilder.append("<p>");
        metadataBuilder.append("Variable Modifications: ");
        metadataBuilder.append(metadata.getVariableModifications());
        metadataBuilder.append("</p>");
        
        //metadataBuilder.append("</div>");
        
        return metadataBuilder.toString();
    }
    
    private String getGlobalStatistics() {
        GlobalStatistics globalStatistics = new GlobalStatistics();
        StringBuilder statisticsBuilder = new StringBuilder();
        
        statisticsBuilder.append("<h2> Global Statistics </h2>");
        

//        int peptideNumber = globalStatistics.getPeptideNumber();
//        //String peptideNumberString = Integer.toString(peptideNumber);
//        int peptideEvidenceNumber = globalStatistics.getDecoyPercentage();
//        System.out.println(peptideEvidenceNumber);
        
        return statisticsBuilder.toString();
    }

    private String getPeptideInfoMain() {
        PeptideInfo peptideInfoMain = new PeptideInfo();
        StringBuilder peptideInfoMainBuilder = new StringBuilder();
        
        List<String> peptideView = peptideInfoMain.getPeptideInfo(); // Method returns a list with 2 elements
        String peptideTable = peptideView.get(0); // String containing contents of HTML table
        String scoreName = peptideView.get(1); // String containing type of score (for table header)
        
        peptideInfoMainBuilder.append("<h2> Peptide View </h2>");
        peptideInfoMainBuilder.append("<table> <table style = 'width:100%'> <tr>");
        peptideInfoMainBuilder.append("<th>PSM ID</th> <th>Sequence</th> <th>Calc m/z</th> <th>Exp m/z</th> <th>Charge</th> <th>Modifications</th> <th>Score: ");
        peptideInfoMainBuilder.append(peptideView.get(1));
        peptideInfoMainBuilder.append("</th> <th>Associated Proteins</th> </tr>");
        peptideInfoMainBuilder.append(peptideView.get(0));
        peptideInfoMainBuilder.append("</table>");
        
        return peptideInfoMainBuilder.toString();
    }
    
    private String getProteinInfoMain() {
        //ProteinInfo proteinInfoMain = new ProteinInfo();
        StringBuilder proteinInfoMainBuilder = new StringBuilder();
        
        proteinInfoMainBuilder.append("<h2> Protein View </h2>");
        
        return proteinInfoMainBuilder.toString();
    }
    
    public void convert(String input, String output) {
        File htmlTemplateFile = new File(output);      
        
        if (!htmlTemplateFile.exists()) {
            try {
                htmlTemplateFile.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }         
        }
        
        try {         
            FileWriter fileWriter = new FileWriter(htmlTemplateFile, true);
            fileWriter.append(getHeader(input));
            fileWriter.append(getMetadata());
            fileWriter.append(getGlobalStatistics());
            fileWriter.append(getPeptideInfoMain());
            fileWriter.append(getProteinInfoMain());
            fileWriter.close();
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
        converter.convert(input, output);              
    }
}
        

