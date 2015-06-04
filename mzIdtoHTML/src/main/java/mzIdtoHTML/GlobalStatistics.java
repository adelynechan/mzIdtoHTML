/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

/**
 *
 * @author Adelyne
 */
public class GlobalStatistics {
    PeptideInfo peptideInfo;
    
    public GlobalStatistics(){
        peptideInfo = new PeptideInfo();
    }
    
    int getPeptideNumber() {    
        return peptideInfo.getPeptideIdHashmap().size();   
    }
    
    int getDecoyPercentage() {     
        GlobalStatistics globalStatistics = new GlobalStatistics();
        int peptideNumber = globalStatistics.getPeptideNumber();
        
//        List<SpectrumIdentificationList> sil = peptideInfo.getSpectrumIdentificationList();
//        int decoyNumber = 0;
//        
//        for (SpectrumIdentificationList sIdentList : sil) {
//            for (SpectrumIdentificationResult spectrumIdentResult: sIdentList.getSpectrumIdentificationResult()) {
//                for (SpectrumIdentificationItem spectrumIdentItem: spectrumIdentResult.getSpectrumIdentificationItem()) {
//                    List<PeptideEvidenceRef> peptideEvidenceRefList = spectrumIdentItem.getPeptideEvidenceRef();
//                    
//                    for (int i = 0; i < peptideEvidenceRefList.size(); i++) {
//                        PeptideEvidenceRef peptideEvidenceRef = peptideEvidenceRefList.get(i);
//                        PeptideEvidence peptideEvidence = peptideInfo.getPeptideEvidenceIdHashMap().get(peptideEvidenceRef.getPeptideEvidenceRef());
//                        
//                        if (peptideEvidence.isIsDecoy()) {
//                            decoyNumber += 1;  
//                        }      
//                    }
//                }
//            }
//        }
        return 0;
//        return decoyNumber;
    }
}
