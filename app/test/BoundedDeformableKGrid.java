package org.fleen.maximilian.app.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;
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
    //get edge vertices (reticulate)
    //TODO for easier checking interior-cross we should mark all these edge vertices for what side-seg they are on
    //a corner vertex is on 2 adjoining sides of course
    
    KPolygon edgevertices=kpolygon.getReticulation();
    //get interior seg pairs
    Set<SegPair> segpairs=getSegPairs(edgevertices);
    
    
    
  }
  
  Set<SegPair> getSegPairs(KPolygon edgevertices){
    Set<SegPair> sp=new HashSet<SegPair>();
    for(KVertex v0:edgevertices){
      for(KVertex v1:edgevertices){
        if(!v0.equals(v1))
          sp.add(new SegPair(v0,v1));}}
    return sp;}
  
  /*
   * 2 vertices on the edge of a polygon
   * coaxial
   * ideally, cross the interior and (if the polygon is concave) not cross the exterior  
   */
  class SegPair{
    
    SegPair(KVertex v0,KVertex v1){
      this.v0=v0;
      this.v1=v1;}
    
    KVertex v0,v1;
    
    public int hashCode(){
      return v0.hashCode()+v1.hashCode();}
    
    public boolean equals(Object a){
      SegPair p0=(SegPair)a;
      if((p0.v0.equals(v0)&&p0.v1.equals(v1))||
        (p0.v0.equals(v1)&&p0.v1.equals(v0)))
        return true;
      return false;}
    
  }
  
  /*
   * returns the fully reticulated form of this polygon
   * That is, all traversed vertices are represented. 
   *   Not just the corners, the vertices in-between the corners too. If any.
   *    
   * We also record which vertices are on which side
   *   corner vertices are on 2 adjoining sides
   * We use this for testing for interior/exterior crossing
   *   if 2 vertices are not on the same side then they cross the interior or exterior, right?
   */
  public KPolygon getEdgeVertices(){
    List<KVertex> vertices=new ArrayList<KVertex>();
    int s=size(),i0,i1;
    KVertex c0,c1,v;
    int d;
    for(i0=0;i0<s;i0++){//i0 is also side index
      i1=i0+1;
      if(i1==s)i1=0;
      c0=get(i0);
      c1=get(i1);
      d=c0.getDirection(c1);
      v=c0;
      while(!v.equals(c1)){
        vertices.add(v);
        v=v.getVertex_Adjacent(d);}}
    return new KPolygon(vertices);}

}
