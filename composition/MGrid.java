package org.fleen.maximilian.composition;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * maximilian grid node
 * comes in 2 flavors
 *   defined explicitly in construction params
 *   derived from MShape
 *   
 * Unlike the KGrid each axis gets its own fish, so we can do simple deformations. 
 *   Like the kind of deformation that happens when we squeeze a polygon inward to create a border
 *   Like we do in boiler and crusher jigs
 *     This is the whole point of Maximilian vs Kisrhombille. In Maximilian we can do these deformations. We also use yards.
 *     So we can do some fancier geometry, including proper split-boil-crush composition. 
 */
public class MGrid extends MTreeNode{
  
  private static final long serialVersionUID=-2082766167662596276L;
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  //explicit simple default, used in root or whatever
  public MGrid(){
    initSimple(ORIGIN_DEFAULT,FOREWARD_DEFAULT,TWIST_DEFAULT,FISH_DEFAULT);}
  
  //explicit simple, used in foaming operations maybe
  public MGrid(DPoint origin,double forward,boolean twist,double fish){
    initSimple(origin,forward,twist,fish);}
  
  //explicit, not sure where we'll use this, maybe in testing
  public MGrid(DPoint origin,double forward,boolean twist,double[] fish){
    init(origin,forward,twist,fish);}
  
  //derived from MShape
  public MGrid(MShape shape){
    init(shape);}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  private static final DPoint ORIGIN_DEFAULT=new DPoint(0,0);
  private static final double FOREWARD_DEFAULT=0;
  private static final boolean TWIST_DEFAULT=true;
  private static final double FISH_DEFAULT=1.0;
  
  private DPoint origin;
  private double forward;
  private boolean twist;
  private double[] fish=new double[GK.AXISCOUNT];//axiscount = 6 fishes
  
  private void initSimple(DPoint origin,double forward,boolean twist,double fish){
    this.origin=origin;
    this.forward=forward;
    this.twist=twist;
    for(int i=0;i<GK.AXISCOUNT;i++)
      this.fish[i]=fish;}
  
  private void init(DPoint origin,double forward,boolean twist,double[] fish){
    this.origin=origin;
    this.forward=forward;
    this.twist=twist;
    this.fish=fish;}
  
  private void init(MShape mshape){
    if(mshape instanceof MPolygon)
      init((MPolygon)mshape);
    else
      init(((MYard)mshape).polygons.get(0));}
  
  private void init(MPolygon mpolygon){
    DPolygon dpolygon=mpolygon.getDPolygon();
    DPoint 
      p0=dpolygon.get(0),
      p1=dpolygon.get(1);
    origin=new DPoint(p0);
    forward=GD.getDirection_PointPoint(p0.x,p0.y,p1.x,p1.y);
    //derive twist from grandparent (grid)
    //if this mpolygon is counterclockwise then reverse that twist
    twist=((MGrid)getParent().getParent()).twist;
    if(!dpolygon.getTwist())twist=!twist;
    //
    initFishes(mpolygon);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INIT FISHES FOR POLYGON-DERIVED GRID
   * ++++++++++++++++++++++++++++++++
   */
  
  /*
   * first we need to have all of the axes represented
   * create a new polygon with added points (increased density, basically) to get that
   * then get 6 vertex pairs, one for each axis
   * for each pair, divide the distance between them by the distance between them for a default undeformed unscaled grid
   *   this gives us a factor by which we can multiply the corrosponding grandparent grid fish
   *   this gives us the scaled fish for that axis (?)
   */
  private void initFishes(MPolygon mpolygon){
    
  }
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GET DPOINT
   * Given a KVertex get a real DPoint
   * ++++++++++++++++++++++++++++++++
   */
  
  public DPoint getDPoint(KVertex kvertex){
    
  }
  
  
  
  

  

}
