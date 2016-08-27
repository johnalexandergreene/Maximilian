package org.fleen.maximilian.boundedDeformableKGrid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
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
  
  public BoundedDeformableKGrid(MShape shape,int density){
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
    doEdgeVertices(kpolygon,mpolygon.dpolygon);
    //get interior seg pairs
//    Set<SegPair> segpairs=getSegPairs(edgevertices);
//    //cull noncrossing
//    //noncrossing means that the seg described by the segpair does not cross over the interior or exterior of the polygon
//    //  that is, the seg is entirely on one side of the polygon
//    cullNoncrossingSegPairs(segpairs);
//    //if the polygon is concave, cull the segpairs that describe a seg that 
//    //crosses the outside of the polygon
//    if(kpolygon.getDefaultPolygon2D().isConcave())
//      cullOutsideCrossingPairs(segpairs); 
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
   * ################################
   * EDGE VERTICES
   * 
   * Given our param kpolygon
   * 
   * Get all of the edge vertices. Not just the corners, the vertices between the corners too, if any
   * 
   * Get the side-index too. hold it in a SideInfo object. A corner belongs to 2 sides.
   * We use this for testing for interior/exterior crossing
   *   if 2 vertices are not on the same side then they cross the interior or exterior, right?
   *   
   * Create KVertexPointDefinitions for the edge vertices too 
   * ################################
   */
  
  public KPolygon edgevertices;
  public Map<KVertex,SideInfo> edgevertexsideinfo=new HashMap<KVertex,SideInfo>();
  public Map<KVertex,KVertexPointDefinition> edgevertexpointdefinitions=new HashMap<KVertex,KVertexPointDefinition>();
  
  void doEdgeVertices(KPolygon kpolygon,DPolygon dpolygon){
    edgevertices=kpolygon.getReticulation();
    doSideInfoAndPointDefsForCorners(kpolygon,dpolygon);
    createEdgeVerticesAndDoSideInfoAndPointDefsForMids(kpolygon);}
  
  void doSideInfoAndPointDefsForCorners(KPolygon kpolygon,DPolygon dpolygon){
    int s=kpolygon.size();
    KVertex kvertex;
    DPoint dpoint;
    int iprior;
    for(int i=0;i<s;i++){
      iprior=i-1;
      if(iprior==-1)iprior=s-1;
      kvertex=kpolygon.get(i);
      dpoint=dpolygon.get(i);
      edgevertexsideinfo.put(kvertex,new SideInfo(iprior,i));
      edgevertexpointdefinitions.put(kvertex,new KVertexPointDefinition(dpoint));}}
  
  void createEdgeVerticesAndDoSideInfoAndPointDefsForMids(KPolygon kpolygon){
    int s=kpolygon.size(),ifirst,ilast;
    edgevertices=new KPolygon();
    KVertex vfirst,vlast,vmid;
    int dir;
    for(ifirst=0;ifirst<s;ifirst++){//ifirst is also side index
      ilast=ifirst+1;
      if(ilast==s)ilast=0;
      //
      vfirst=kpolygon.get(ifirst);
      vlast=kpolygon.get(ilast);
      dir=vfirst.getDirection(vlast);
      vmid=vfirst;
      while(!vmid.equals(vlast)){
        edgevertices.add(vmid);
        if(vmid!=vfirst){
          doKVertexPointDefinitionForSideMidVertex(vfirst,vmid,vlast);
          edgevertexsideinfo.put(vmid,new SideInfo(ifirst));}//because side index is same as prior vertex index
        vmid=vmid.getVertex_Adjacent(dir);}}}
  
  /*
   * get the KVertexPointDefinition for vmid
   */
  private void doKVertexPointDefinitionForSideMidVertex(KVertex vfirst,KVertex vmid,KVertex vlast){
    DPoint 
      pfirst=vfirst.getBasicPoint2D(),
      pmid=vmid.getBasicPoint2D(),
      plast=vlast.getBasicPoint2D();
    double 
      d0=pfirst.getDistance(plast),
      d1=pfirst.getDistance(pmid),
      offset=d1/d0;
    KVertexPointDefinition
      deffirst=edgevertexpointdefinitions.get(vfirst),
      deflast=edgevertexpointdefinitions.get(vlast),
      defmid=new KVertexPointDefinition(deffirst,deflast,offset);
    edgevertexpointdefinitions.put(vmid,defmid);}
  
  //we use this in debug, maybe elsewhere
  public boolean isCorner(KVertex v){
    SideInfo i=edgevertexsideinfo.get(v);
    if(i==null)return false;
    return(i.side1!=-1);}
  
  /*
   * a corner belongs to 2 sides, all others belong to one
   */
  private class SideInfo{
    
    SideInfo(int side0){
      this.side0=side0;}
    
    SideInfo(int side0,int side1){
      this.side0=side0;
      this.side1=side1;}
    
    int side0,side1=-1;}

}
