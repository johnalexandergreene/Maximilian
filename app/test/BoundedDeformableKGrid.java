package org.fleen.maximilian.app.test;

import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;

public class BoundedDeformableKGrid{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  BoundedDeformableKGrid(MShape shape){
    if(shape instanceof MPolygon)
      init((MPolygon)shape);
    else
      init(((MYard)shape).mpolygons.get(0));}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  private void init(MPolygon mpolygon){
    
  }

}
