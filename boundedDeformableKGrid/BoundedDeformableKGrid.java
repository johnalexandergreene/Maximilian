package org.fleen.maximilian.boundedDeformableKGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    System.out.println("density ="+density);
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
   * get the interior strands
   *   an interior strand is a sequence of vertices that extends from one 
   *   edge vertex to another, crossing the interior, and not crossing to the outside.
   * get the interior vertices
   *   create definitions for interior vertices. 
   *     Use strand end points (which are also edge points) as references for KVertexPointDefinitions.
   *   each interior point has 1..12 definitions.
   *     when reconstructing the interior points using our vertex definitions we use averages when necessary
   *   
   * now we have a model of the interior points in terms of the edge points
   *   and the edge points are either corner points or defined in terms of the corner points
   *   
   * get real polygon corner points, assumedly describing a deformed FPolygon
   *   derive real edge and interior points from that
   *   put all these real points on in a map keyed by kvertex
   */
  private void init(MPolygon mpolygon,int density){
    boolean twist=mpolygon.dpolygon.getTwist();
    KPolygon kpolygon=mpolygon.mmetagon.getPolygon(density,twist);
    doEdgeVertices(kpolygon,mpolygon.dpolygon);
    initEdgeVertexAdjacents(); 
    doInteriorVertices();}
  
  /*
   * ################################
   * DO INTERIOR VERTICES
   * 
   * for each vertex
   * test against each of the other strands in the polygon
   * get coaxial pairs
   * 
   * cull duplicated coaxial pairs with a set.
   * 
   * for each coaxial pair
   * traverse vertices from the first in the pair to the second
   *   if the first vertex we hit is either of the edge vertices adjacent to 
   *     the first edge vertex in the coaxial pair then the strand fails
   *   if we hit another edge vertex before we hit the second of the pair of 
   *     vertices then the strand is invalid
   * discard the invalid ones
   * what's left is the interior strands
   * 
   * for each interior strand
   *   for each of the points that isn't an endpoint
   *     create a point definition for that vertex and add it to the list of points definitions for that vertex
   *     
   * use those definitions for getting real 2d points for our interior vertices.
   * we might cache
   * ################################
   */
  
  public Map<KVertex,List<KVertexPointDefinition>> interiorvertexpointdefinitions=new HashMap<KVertex,List<KVertexPointDefinition>>();
  public Set<KVertex> interiorvertices=new HashSet<KVertex>();
  
  private void doInteriorVertices(){
    Set<CoaxialPair> coaxialpairs=getCoaxialPairs();
    List<InteriorStrand> interiorstrands=createInteriorStrands(coaxialpairs);
    createInteriorVertexPointDefinitions(interiorstrands);
    for(KVertex v:interiorvertices)
      initInteriorVertexPoint(v);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INTERIOR VERTEX POINTS ACCESS
   * ++++++++++++++++++++++++++++++++
   */
  
  public Map<KVertex,DPoint> interiorvertexpoints=new HashMap<KVertex,DPoint>();
  
  public DPoint getInteriorPoint(KVertex vertex){
    DPoint p=interiorvertexpoints.get(vertex);
    return p;}
  
  void initInteriorVertexPoint(KVertex vertex){
    List<KVertexPointDefinition> pointdefs=interiorvertexpointdefinitions.get(vertex);
    DPoint p;
    if(pointdefs.size()==1)
      p=pointdefs.get(0).getPoint();
    else{
      double xsum=0,ysum=0;
      for(KVertexPointDefinition def:pointdefs){
        p=def.getPoint();
        xsum+=p.x;
        ysum+=p.y;}
      double defscount=pointdefs.size();
      xsum/=defscount;
      ysum/=defscount;
      p=new DPoint(xsum,ysum);}
    interiorvertexpoints.put(vertex,p);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * CREATE INTERIOR VERTEX POINT DEFINITIONS
   * ++++++++++++++++++++++++++++++++
   */
  
  private void createInteriorVertexPointDefinitions(List<InteriorStrand> interiorstrands){
    for(InteriorStrand strand:interiorstrands)
      createInteriorVertexPointDefinitions(strand);}
  
  private void createInteriorVertexPointDefinitions(InteriorStrand strand){
    int s=strand.size();
    if(s==2)return;//exit on no interior vertices
    KVertex 
      vfirst=strand.get(0),
      vlast=strand.get(s-1),
      v;
    DPoint 
      pfirst=vfirst.getBasicPoint2D(),
      plast=vlast.getBasicPoint2D(),
      p;
    double dtotal=pfirst.getDistance(plast),d,offset;
    KVertexPointDefinition 
      deffirst=edgevertexpointdefs.get(vfirst),
      deflast=edgevertexpointdefs.get(vlast),
      def;
    List<KVertexPointDefinition> defs;
    //exclude first and last, they are edge points
    for(int i=1;i<s-1;i++){
      v=strand.get(i);
      interiorvertices.add(v);
      p=v.getBasicPoint2D();
      d=pfirst.getDistance(p);
      offset=d/dtotal;
      def=new KVertexPointDefinition(deffirst,deflast,offset);
      //
      defs=interiorvertexpointdefinitions.get(v);
      if(defs==null){
        defs=new ArrayList<KVertexPointDefinition>();
        interiorvertexpointdefinitions.put(v,defs);}
      defs.add(def);}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * COAXIAL PAIRS
   * ++++++++++++++++++++++++++++++++
   */
  
  Set<CoaxialPair> getCoaxialPairs(){
    Set<CoaxialPair> sp=new HashSet<CoaxialPair>();
    for(KVertex v0:edgevertices){
      for(KVertex v1:edgevertices){
        if((!v0.equals(v1))&&(v0.isColinear(v1)))//TODO change colinear to coaxial
          sp.add(new CoaxialPair(v0,v1));}}
    return sp;}
  
  /*
   * 2 vertices on the edge of a polygon
   * coaxial
   * ideally, cross the interior and (if the polygon is concave) not cross the exterior  
   */
  class CoaxialPair{
    
    CoaxialPair(KVertex v0,KVertex v1){
      this.v0=v0;
      this.v1=v1;}
    
    KVertex v0,v1;
    
    //pairs with the same vertices are equal, order doesn't matter
    public int hashCode(){
      return v0.hashCode()+v1.hashCode();}
    
    public boolean equals(Object a){
      CoaxialPair p0=(CoaxialPair)a;
      if((p0.v0.equals(v0)&&p0.v1.equals(v1))||
        (p0.v0.equals(v1)&&p0.v1.equals(v0)))
        return true;
      return false;}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INTERIOR STRANDS
   * ++++++++++++++++++++++++++++++++
   */
  
  //expose strands for debug
  public List<InteriorStrand> strands;
  
  private List<InteriorStrand> createInteriorStrands(Set<CoaxialPair> coaxialpairs){
//    List<InteriorStrand> strands=new ArrayList<InteriorStrand>();
    strands=new ArrayList<InteriorStrand>();
    
    InteriorStrand strand;
    for(CoaxialPair pair:coaxialpairs){
      strand=getInteriorStrand(pair);
      if(strand!=null)
        strands.add(strand);}
    
    //expose strands for debug
    
    return strands;}
  
  private InteriorStrand getInteriorStrand(CoaxialPair pair){
    InteriorStrand strand=new InteriorStrand();
    int dir=pair.v0.getDirection(pair.v1);
    KVertex v=pair.v0;
    //do the second vertex in the sequence. test it
    strand.add(v);
    v=v.getVertex_Adjacent(dir);
    //if the second vertex in the sequence is either of the edge vertex 
    //polygon adjacents for pair.v0 then we are just running along the edge of the polygon 
    //with this strand. The strand fails 
    if(v.equals(getEdgeAdjacentPrior(pair.v0))||v.equals(getEdgeAdjacentNext(pair.v0)))
      return null;
    //if the second vertex in the sequence is the second vertex in the pair then 
    //we're done. We have a 2-vertex strand
    if(v.equals(pair.v1)){
      return null;}
    //ok, the second vertex checks out ok so far. continue the sequence
    //if we hit and edge vertex before we hit pair.v1 then the strand fails
    while(!v.equals(pair.v1)){
      if(edgevertices.contains(v))
        return null;
      strand.add(v);
      v=v.getVertex_Adjacent(dir);}
    //we have a strand
    strand.add(pair.v1);
    System.out.println("strand size = "+strand.size());
    //if the strand is of length 2 then it is composed of only end points and does not cross the interior and is therefor invalid
    if(strand.size()==2)return null;
    //
    return strand;}
  
  @SuppressWarnings("serial")
  private class InteriorStrand extends ArrayList<KVertex>{
    InteriorStrand(){
      super();}}
  
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
  public Map<KVertex,KVertexPointDefinition> edgevertexpointdefs=new HashMap<KVertex,KVertexPointDefinition>();
  
  void doEdgeVertices(KPolygon kpolygon,DPolygon dpolygon){
    edgevertices=kpolygon.getReticulation();
    doPointDefsForCorners(kpolygon,dpolygon);
    createEdgeVerticesAndDoPointDefsForMids(kpolygon);}
  
  private void doPointDefsForCorners(KPolygon kpolygon,DPolygon dpolygon){
    int s=kpolygon.size();
    KVertex kvertex;
    DPoint dpoint;
    int iprior;
    for(int i=0;i<s;i++){
      iprior=i-1;
      if(iprior==-1)iprior=s-1;
      kvertex=kpolygon.get(i);
      dpoint=dpolygon.get(i);
      edgevertexpointdefs.put(kvertex,new KVertexPointDefinition(dpoint));}}
  
  public void createEdgeVerticesAndDoPointDefsForMids(KPolygon kpolygon){
    System.out.println("kpolygon size ="+kpolygon.size());
    edgevertices=new KPolygon();
    int s=kpolygon.size(),i0,i1;
    KVertex c0,c1,v;
    int d;
    for(i0=0;i0<s;i0++){
      i1=i0+1;
      if(i1==s)i1=0;
      c0=kpolygon.get(i0);
      c1=kpolygon.get(i1);
      d=c0.getDirection(c1);
      v=c0;
      while(!v.equals(c1)){
        edgevertices.add(v);
        if(v!=c0)
          doKVertexPointDefinitionForSideMidVertex(c0,v,c1);
        v=v.getVertex_Adjacent(d);}}}
  
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
      deffirst=edgevertexpointdefs.get(vfirst),
      deflast=edgevertexpointdefs.get(vlast),
      defmid=new KVertexPointDefinition(deffirst,deflast,offset);
    edgevertexpointdefs.put(vmid,defmid);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * EDGE VERTEX POINT ACCESS
   * ++++++++++++++++++++++++++++++++
   */
  
  public DPoint getEdgePoint(KVertex v){
    KVertexPointDefinition d=edgevertexpointdefs.get(v);
    if(d==null)return null;//this vertex is not on the edge of our polygon
    return d.getPoint();}
  
  /*
   * ################################
   * GENERAL POINTS ACCESS
   * given a point look in the edge and interior maps
   * ################################
   */
  
  public DPoint getPoint(KVertex v){
    DPoint p=getEdgePoint(v);
    if(p==null)
      p=getInteriorPoint(v);
    return p;}
  
  /*
   * ################################
   * EDGE VERTEX POLYGON PRIOR AND NEXT ADJACENTS
   * a kvertex mapped to its prior and next adjacent in the index-forward-wise traversal of the edge vertices polygon
   * it makes life easier
   * ################################
   */
  
  private Map<KVertex,KVertex> 
    prioradjacent=new HashMap<KVertex,KVertex>(),
    nextadjacent=new HashMap<KVertex,KVertex>();
  
  private void initEdgeVertexAdjacents(){
    int s=edgevertices.size(),iprior,inext;
    KVertex v,vprior,vnext;
    for(int i=0;i<s;i++){
      iprior=s-1;
      if(iprior==-1)iprior=s-1;
      inext=i+1;
      if(inext==s)inext=0;
      //
      v=edgevertices.get(i);
      vprior=edgevertices.get(iprior);
      vnext=edgevertices.get(inext);
      prioradjacent.put(v,vprior);
      nextadjacent.put(vnext,vnext);}}
  
  private KVertex getEdgeAdjacentPrior(KVertex v){
    KVertex p=prioradjacent.get(v);
    if(p==null)
      throw new IllegalArgumentException("NULL PRIOR");
    return p;}
  
  private KVertex getEdgeAdjacentNext(KVertex v){
    KVertex n=nextadjacent.get(v);
    if(n==null)
      throw new IllegalArgumentException("NULL NEXT");
    return n;}
  
  
    
  

}
