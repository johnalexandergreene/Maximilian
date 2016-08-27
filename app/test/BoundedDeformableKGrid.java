package org.fleen.maximilian.app.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
    //create edgevertices polygon
    //we hold the side of each edge vertex too : edgevertexsideinfo
    //a corner vertex is on 2 adjoining sides
    doEdgeVertices(kpolygon);
    //get interior seg pairs
    Set<SegPair> segpairs=getSegPairs(edgevertices);
    //cull noncrossing
    //noncrossing means that the seg described by the segpair does not cross over the interior or exterior of the polygon
    //  that is, the seg is entirely on one side of the polygon
    cullNoncrossingSegPairs(segpairs);
    //if the polygon is concave, cull the segpairs that describe a seg that 
    //crosses the outside of the polygon
    if(kpolygon.getDefaultPolygon2D().isConcave())
      cullOutsideCrossingPairs(segpairs); 
    //create interior strands
    //a strand is a straight procession of vertices
    
    
    
    
  }
  
  private void createInteriorStrands(){
    
  }
  
  //TODO
  private void cullOutsideCrossingPairs(Set<SegPair> segpairs){
    
  }
  
  private void cullNoncrossingSegPairs(Set<SegPair> segpairs){
    Iterator<SegPair> i=segpairs.iterator();
    SegPair sp;
    while(i.hasNext()){
      sp=i.next();
      if(!crosses(sp))
        i.remove();}}
  
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
   * tests a segpair for crossing
   * if the seg crosses the interior or the exterior of the param polygon
   * if the vertices in the pair are on the same side then the segpair doesn't cross
   *   otherwise it does 
   */
  boolean crosses(SegPair sp){
    SideInfo 
      si0=edgevertexsideinfo.get(sp.v0),
      si1=edgevertexsideinfo.get(sp.v1);
    if(
      (si0.side0==si1.side0)||
      (si0.side1==si1.side0)||
      (si0.side0==si1.side1)||
      (si0.side1==si1.side1))
      return false;
    return true;}
  
  /*
   * create an edge-vertices polygon
   * that is, the fully reticulated form of our param polygon
   * all traversed vertices are represented. Not just the corners, the vertices in-between the corners too. If any.
   *    
   * We also record which vertices are on which side
   *   corner vertices are on 2 adjoining sides
   * We use this for testing for interior/exterior crossing
   *   if 2 vertices are not on the same side then they cross the interior or exterior, right?
   */
  
  void doEdgeVertices(KPolygon kpolygon){
    edgevertices=new KPolygon(kpolygon.size());
    int s=kpolygon.size(),i0,i1;
    KVertex c0,c1,v;
    int d;
    for(i0=0;i0<s;i0++){//i0 is also side index
      i1=i0+1;
      if(i1==s)i1=0;
      c0=kpolygon.get(i0);
      c1=kpolygon.get(i1);
      d=c0.getDirection(c1);
      v=c0;
      while(!v.equals(c1)){
        addSideInfo(v,i0);
        edgevertices.add(v);
        v=v.getVertex_Adjacent(d);}}}
  
  KPolygon edgevertices;
  
  Map<KVertex,SideInfo> edgevertexsideinfo=new HashMap<KVertex,SideInfo>();
  
  void addSideInfo(KVertex v,int side){
    SideInfo si=edgevertexsideinfo.get(v);
    if(si==null){
      si=new SideInfo();
      si.side0=side;
      edgevertexsideinfo.put(v,si);
    }else{
      si.side1=side;}}
  
  class SideInfo{
    int side0,side1=-1;}

}
