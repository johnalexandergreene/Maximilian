package org.fleen.maximilian.boundedDeformableKGrid;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.GD;

/*
 * defines the 2d point of a KVertex 
 * explicitly : specify 2d coors
 * implicitly : refer to a position on a seg, the endpoints of which are defined by 2 KVertexPointDefinitions
 * A KVertexPointDefinition ultimately refers to an explicit definition, directly or indirectly.
 * Thus we can define the noncorner edge points and interior points of a deformable polygon-bounded 
 * grid in terms of the polygon's corner points.
 */
public class KVertexPointDefinition{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  //explicit
  KVertexPointDefinition(DPoint p){
    this.p=p;}
  
  //implicit
  KVertexPointDefinition(KVertexPointDefinition d0,KVertexPointDefinition d1,double offset){
    this.d0=d0;
    this.d1=d1;
    this.offset=offset;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  //def for explicit
  DPoint p=null;
  //def for implicit
  //2 points and a proportional offset range (0,1)
  KVertexPointDefinition d0,d1;
  double offset;
  
  public DPoint getPoint(){
    if(isExplicit())
      return p;
    else{
      DPoint p0=d0.getPoint(),p1=d1.getPoint();
      double[] a=GD.getPoint_Between2Points(p0.x,p0.y,p1.x,p1.y,offset);
      return new DPoint(a);}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * TYPE
   * ++++++++++++++++++++++++++++++++
   */
  
  public static final int 
    TYPE_EXPLICIT=0,
    TYPE_IMPLICIT=1;

  public int getType(){
    if(p!=null)
      return TYPE_EXPLICIT;
    else
      return TYPE_IMPLICIT;}
  
  public boolean isImplicit(){
    return p==null;}
  
  public boolean isExplicit(){
    return p!=null;}
  
}
