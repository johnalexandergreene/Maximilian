package org.fleen.maximilian.composition;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_Kisrhombille.KGrid;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * NODE GRID TRANSFORM
 * Given an MShape (defined in terms of its parent's grid), define a grid
 * get 
 *   origin : p0 or (in the case of a yard) outer polygon p0
 *   forward : dir(p0,p1)
 *   twist : polygon chirality : cw=true, ccw=false
 *   6 fishes : one for each axis
 *     we can handle only deformations that are uniform over each axis
 *     we derive fish by getting the distance between 2 coaxial points across the polygon and dividing it by the undeformed distance 
 *     (we could get scale that way too, or whatever) 
 * 
 */
public class MGridBoundedDeformable extends MGrid{
  
  private static final long serialVersionUID=-220936433111379307L;
  
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
  
  
  
  /*
   * ################################
   * ORIGIN
   * ################################
   */
  
  public DPoint origin;
  
  private void initOrigin(){
    
  }
  
  
  //a kvertex relative to origin of the uptree kgrid 
  public KVertex origintransform;
  //a direction offset in terms of the foreward and twist of the uptree kgrid 
  public int forewardtransform;
  //twist relative to twist of uptree kgrid.
  //either same twist as prior element (true, aka 'positive') or opposite (false, aka 'negative').
  public boolean twisttransform;
  //a factor. Simply multiply by fish of the uptree kgrid
  public double fishtransform;
  
  /*
   * ################################
   * GRID
   * ################################
   */
  
  protected KGrid initLocalKGrid(){
    KGrid priorgrid=getFirstAncestorGrid().getLocalKGrid();
    //
    double[] gridorigin=priorgrid.getPoint2D(origintransform);
    //
    double gridforeward=priorgrid.getDirection2D(forewardtransform);
    //
    boolean gridtwist=priorgrid.getTwist();
    if(!twisttransform)gridtwist=!gridtwist;
    //
    double gridfish=priorgrid.getFish()*fishtransform;
    //
    return new KGrid(
      gridorigin,
      gridforeward,
      gridtwist,
      gridfish);}

}
