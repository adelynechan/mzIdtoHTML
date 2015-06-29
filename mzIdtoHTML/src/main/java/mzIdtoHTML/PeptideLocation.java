/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzIdtoHTML;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.SortedSet;
import java.awt.Point;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;

/**
 *
 * @author Adelyne
 */
public class PeptideLocation {
    
    HashMap <String, ArrayList<Point>> getPeptideCoordinatesHashMap()  {      
        Iterator <PeptideEvidence> iterPepEvid = MzidToHTML.unmarshaller.unmarshalCollectionFromXpath
                (MzIdentMLElement.PeptideEvidence);
        
        HashMap <String, ArrayList<Point>> dbSeqCoordsHashMap = new HashMap <String, ArrayList<Point>> ();
        
        while (iterPepEvid.hasNext()) {
            PeptideEvidence peptideEvidence = iterPepEvid.next();
            
            String dbSeqRef = peptideEvidence.getDBSequenceRef();
            Point peptideCoordinates = new Point(peptideEvidence.getStart(), peptideEvidence.getEnd());

            if (dbSeqCoordsHashMap.containsKey(dbSeqRef)) {
                ArrayList peptideCoordinatesList = dbSeqCoordsHashMap.get(dbSeqRef);
                peptideCoordinatesList.add(peptideCoordinates);
                dbSeqCoordsHashMap.put(dbSeqRef, peptideCoordinatesList);
            }
            
            else {
                ArrayList peptideCoordinatesList = new ArrayList <Point> ();
                peptideCoordinatesList.add(peptideCoordinates);
                dbSeqCoordsHashMap.put(dbSeqRef, peptideCoordinatesList);
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
}
    

