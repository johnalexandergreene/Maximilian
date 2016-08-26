package org.fleen.maximilian.app.test;

import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;

public class BoundedDeformableKGrid{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  BoundedDeformableKGrid(MShape shape,int density){
    if(shape instanceof MPolygon)
      init((MPolygon)shape,density);
    else
      init(((MYard)shape).mpolygons.get(0),density);}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  /*
   * Create kpolygon at specified density using default everything
   * get the edge vertices
   * get the interior segs
   * get the interior vertices
   * create definitions for interior segs
   *   edgepoint - edgepoint
   * create definitions for interior points
   *   An interior point definition is in terms of an interior seg
   *   each interior point has 1..n definitions.
   *   
   * now we have a model of the interior points in terms of the edge points
   *   
   * get real edge points
   *   get interior points
   *   put all real points on in a map keyed by kvertex
   */
  private void init(MPolygon mpolygon,int density){
    boolean twist=mpolygon.dpolygon.getTwist();
    KPolygon kpolygon=mpolygon.mmetagon.getPolygon(density,twist);
    
  }

}
