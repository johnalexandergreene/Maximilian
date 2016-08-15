package org.fleen.maximilian.composition;

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
  
  public MGridBoundedDeformable(MShape shape,int density){
    
    
    
  }
  
  /*
   * ################################
   * TRANSFORM PARAMS
   * 
   * ################################
   */
  
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
