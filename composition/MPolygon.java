package org.fleen.maximilian.composition;

import java.util.List;

import org.fleen.geom_2D.DPolygon;
import org.fleen.maximilian.grammar.MMetagon;

/*
 * A polygon defined by an underlying grid, a metagon, an anchor and a chorus index
 * the grid is this polygon's parent
 * the anchor gives us the first 2 vertices in the polygon, scale and orientation
 * the polygon points chirality (clockwise or counterclockwise) gives us twist 
 * chorus index is for local child grouping 
 * real 2d points are derived from the compounded local grid-geometry, which is derived from this node's ancestry
 */
public class MPolygon extends MShape{
  
  private static final long serialVersionUID=7403675520824450721L;
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public MPolygon(DPolygon dpolygon,MMetagon mmetagon,int chorusindex,List<String> tags){
    super(chorusindex,tags);
    this.dpolygon=dpolygon;
    this.mmetagon=mmetagon;}
  
  /*
   * ################################
   * GEOM
   * ################################
   */
  
  public DPolygon dpolygon;
  public MMetagon mmetagon;
  
  

}
