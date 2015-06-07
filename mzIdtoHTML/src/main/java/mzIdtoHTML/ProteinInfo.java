/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionList;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinAmbiguityGroup;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;

/**
 *
 * @author Adelyne
 */
public class ProteinInfo {
    
    ProteinDetectionList getProteinDetectionList() {  
        DataCollection dc =  MzidToHTML.unmarshaller.unmarshal(DataCollection.class);
        AnalysisData ad = dc.getAnalysisData();  
        ProteinDetectionList pdl = ad.getProteinDetectionList();
        
        return pdl;
    }
    
    String getProteinInfo() {
        MzidData mzidDataProtein = new MzidData();
        ProteinInfo proteinInfo = new ProteinInfo();
        
        HashMap<String, DBSequence> proteinDbSequenceIdHashMap = mzidDataProtein.getDbSequenceIdHashMap();
        ProteinDetectionList pdl = proteinInfo.getProteinDetectionList();
        List<ProteinAmbiguityGroup> pagList = pdl.getProteinAmbiguityGroup();
        
        StringBuilder proteinInfoBuilder = new StringBuilder();
        
        for (ProteinAmbiguityGroup pag : pagList) {
            List<ProteinDetectionHypothesis> pdhList = pag.getProteinDetectionHypothesis();        
           
            for (int i = 0; i < pdhList.size(); i++) {
                DBSequence proteinDbSeq = proteinDbSequenceIdHashMap.get(pdhList.get(i).getDBSequenceRef());
                List<CvParam> proteinCvParamList = proteinDbSeq.getCvParam();
                int listSize = proteinCvParamList.size();
                
                for (int x = 0; x < proteinCvParamList.size(); x++) {
                    String accessionCode = new String();
                    String speciesName = new String();
                    String proteinName = new String();
                    String fdrScore = new String();
                    String obsObs = new String();
                    
                    CvParam proteinCvParam = proteinCvParamList.get(x);
                    String proteinNameFull = proteinCvParam.getValue(); 
                                    
                    Pattern patternAcc = Pattern.compile("tr\\|(.*?)\\|");
                    Matcher matcherAcc = patternAcc.matcher(proteinNameFull);
                    
                    Pattern patternSpe = Pattern.compile("OS=(.*?)GN");
                    Matcher matcherSpe = patternSpe.matcher(proteinNameFull);
                    
                    Pattern patternName = Pattern.compile("\\s(.*?)OS=");
                    Matcher matcherName = patternName.matcher(proteinNameFull);
                                        
                    if (matcherAcc.find() && matcherSpe.find() && matcherName.find()) {
                        accessionCode = "<td>" + matcherAcc.group(1) + "</td>";                                             
                        speciesName = "<td>" + matcherSpe.group(1) + "</td>"; 
                        proteinName = "<td>" + matcherName.group(1) + "</td>";                         
                    }
                    
                    proteinInfoBuilder.append("<tr>");
                    proteinInfoBuilder.append(accessionCode);
                    proteinInfoBuilder.append(speciesName);
                    proteinInfoBuilder.append(proteinName);
                    proteinInfoBuilder.append(fdrScore);
                    proteinInfoBuilder.append(obsObs);
                    proteinInfoBuilder.append("</tr>");
                } 
            }
        }   
        return proteinInfoBuilder.toString();
    }                        
}

