/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Point;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;

/**
 *
 * @author Adelyne
 */
public class PeptideLocation {
    
    HashMap <DBSequence, ArrayList<Point>> getPeptideCoordinatesHashMap()  {
        MzidData mzidDataPepLocation = new MzidData();
        HashMap <String, DBSequence> dbSequenceIdHashMap = mzidDataPepLocation.getDbSequenceIdHashMap();
        
        Iterator <PeptideEvidence> iterPepEvid = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath
                (MzIdentMLElement.PeptideEvidence);
        
        HashMap <DBSequence, ArrayList<Point>> dbSeqCoordsHashMap = new HashMap <DBSequence, ArrayList<Point>> ();
        
        while (iterPepEvid.hasNext()) {
            PeptideEvidence peptideEvidence = iterPepEvid.next();
            
            String dbSeqRef = peptideEvidence.getDBSequenceRef();
            DBSequence dbSequence = dbSequenceIdHashMap.get(dbSeqRef);
            Point peptideCoordinates = new Point(peptideEvidence.getStart(), peptideEvidence.getEnd());

            if (dbSeqCoordsHashMap.containsKey(dbSequence)) {
                ArrayList peptideCoordinatesList = dbSeqCoordsHashMap.get(dbSequence);
                peptideCoordinatesList.add(peptideCoordinates);
                dbSeqCoordsHashMap.put(dbSequence, peptideCoordinatesList);
            }
            
            else {
                ArrayList peptideCoordinatesList = new ArrayList <Point> ();
                peptideCoordinatesList.add(peptideCoordinates);
                dbSeqCoordsHashMap.put(dbSequence, peptideCoordinatesList);
            }
        }
        return dbSeqCoordsHashMap;                        
    }
    
    boolean isOverlap(Point pep1, Point pep2) {
        Double x1 = pep1.getX();
        Double y1 = pep1.getY();
        
        Double x2 = pep2.getX();
        Double y2 = pep2.getY();
        
        if (y1 >= x2 && y2 >= x1) {
            return true;
        }
        
        else {
            return false;
        }
    }
    
    Double getOverlapLength (Point pep1, Point pep2) {
        Double x1 = pep1.getX();
        Double y1 = pep1.getY();
        
        Double x2 = pep2.getX();
        Double y2 = pep2.getY();
        
        PeptideLocation peptideLocation = new PeptideLocation();
        boolean overlap = peptideLocation.isOverlap(pep1, pep2);
        Double overlapLength = 0.0;
        
        if (overlap) {
            if (x1 < x2) {
                overlapLength = y1 - x2 + 1;
            }
            
            else if (x2 < x1) {
                overlapLength = y2 - x1 + 1;
            }
            
            else {
                if (y1 < y2) {
                    overlapLength = y2 - y1;
                }
                
                else if (y2 < y1) {
                    overlapLength = y1 - y2;
                }
            }
        } 
        return overlapLength;     
    }
    
    Double getPeptideCoverage(DBSequence dbSeq) {
        PeptideLocation peptideLocation = new PeptideLocation();
        HashMap <DBSequence, ArrayList<Point>> dbSeqCoordsPeptideHashMap = peptideLocation.getPeptideCoordinatesHashMap();
        
        Double totalOverlapLength = 0.0;
        Double totalPeptideLength = 0.0;
        
        ArrayList <Point> peptideCoordinatesList = new ArrayList <Point>();
        peptideCoordinatesList.add(new Point(0,0));
        //peptideCoordinatesList.addAll(dbSeqCoordsPeptideHashMap.get(dbSeq));                                
        
        //if (peptideCoordinatesList != null) {
            for (int peptideNum = 0; peptideNum < peptideCoordinatesList.size() - 1; peptideNum++) {
                Point pep1 = peptideCoordinatesList.get(peptideNum);
                Point pep2 = peptideCoordinatesList.get(peptideNum + 1);
            
                totalOverlapLength = totalOverlapLength + peptideLocation.getOverlapLength(pep1, pep2);
                totalPeptideLength = totalPeptideLength + pep1.getY() - pep1.getX();     
            }      
        //}
                     
        int proteinLength = dbSeq.getLength();
        
        return (totalPeptideLength - totalOverlapLength) / proteinLength * 100;
    }
}
