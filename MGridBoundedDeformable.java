package org.fleen.maximilian;


/*
 * This is a bounded deformable grid
 * we use it in the process of creating new shapes.
 *   That is, we create a a grid and then we create new shapes in terms of that grid.
 * 
 */
public class MGridBoundedDeformable{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  /*
   * BOUNDED DEFORMABLE GRID
   * 
   * get a standard kpolygon given the mpolygon and the density
   * get all of the edge vertices. All, not just the corners
   * for each edge vertex, get the segs that span the polygon connecting to a seg across the polygon
   *   the seg must cross the interior of the polygon
   *   if the polygon is concave then the seg must not cross to the outside of the polygon, it's gotta stay in the polygon
   * cull duplicated segs
   * for each seg, get the interior vertices that the seg crosses
   *   for each of those interior vertices, get a definition for that vertex in terms of the seg.
   *     That is, as a proportional offset between the endpoints of the seg.
   *     
   * Now we have
   *   0..n spanning segs (SS)
   *   for each interior vertex
   *     1..n definitions (IVD), each in terms of one spanning seg
   * 
   * get the 2d coordinates of the edge vertices of the polygon (at specified density)
   *   corners corrospond directly, other edge points can be gotten by proportion
   * use those in place of the corrosponding endpoint-vertices in the SSs
   * derive interior points corrosponding to interior vertices using IVDs.
   *   if an interior vertex has multiple IVDs then use the average.
   * 
   * Now we have all edge and interior 2D points corrosponding to all edge and interior vertices in the polygon
   * Store them.
   * When we do a getPoint2D(KVertex) (or rather, getDPoint) we will use these 2D points.
   * return the 2D Point corrosponding to the KVertex
   * if KVertex does not have a corrosponding DPoint then the KPoint is outside the bounds of the grid so fail. 
   * 
   *    
   * 
   * 
   */
  public MGridBoundedDeformable(MPolygon polygon,int density){
    
    
    
  }

}
