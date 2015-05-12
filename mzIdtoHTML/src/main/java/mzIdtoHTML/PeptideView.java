/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.io.File;
import java.util.List;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisData;
import uk.ac.ebi.jmzidml.model.mzidml.DataCollection;
import uk.ac.ebi.jmzidml.model.mzidml.SequenceCollection;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

/**
 *
 * @author Adelyne
 */
public class PeptideView {
    
    public static String getPeptideInfo (File file) {
               
        boolean aUseSpectrumCache = true;
        MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(file);
                                
        SequenceCollection sc = unmarshaller.unmarshal(SequenceCollection.class);
        StringBuilder peptideViewBuilder = new StringBuilder();
        
        List<PeptideEvidence> pepEvidenceList = sc.getPeptideEvidence();
		for (PeptideEvidence pepEvidence : pepEvidenceList)
		{
                    Peptide peptide = pepEvidence.getPeptide();
                    
                    String peptideRef = pepEvidence.getPeptideRef();
                    //String peptideSequence = peptide.getPeptideSequence();
                    
                    peptideViewBuilder.append(peptideRef);                                                                                                    
		}       
        return peptideViewBuilder.toString();		         
    }
}
