package org.fleen.maximilian.composition;

import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.DCircle;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_2D.IncircleCalculator;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KGrid;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.maximilian.grammar.MMetagon;
import org.fleen.util.tag.TagManager;
import org.fleen.util.tag.Tagged;
import org.fleen.util.tree.TreeNode;

/*
 * A polygon defined by an underlying grid, a metagon, an anchor and a chorus index
 * the grid is this polygon's parent
 * the anchor gives us the first 2 vertices in the polygon, scale and orientation
 * the polygon points chirality (clockwise or counterclockwise) gives us twist 
 * chorus index is for local child grouping 
 * real 2d points are derived from the compounded local grid-geometry, which is derived from this node's ancestry
 */
public class MPolygon extends MTreeNode implements Tagged{
  
  private static final long serialVersionUID=7403675520824450721L;
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public MPolygon(MMetagon metagon,KAnchor anchor,int chorusindex,String[] tags){
    this.metagon=metagon;
    this.anchor=anchor;
    this.chorusindex=chorusindex;
    
    //get tags from metagon and jig section
    initTags(metagon.getTags());
    tagmanager.addTags(tags);
    
    initVertices();}
  
  /*
   * returns a default form for use in the Quasar editing tool or whatever
   */
  public MPolygon(MMetagon metagon){
    this.metagon=metagon;
    KPolygon p=metagon.getPolygon();
    anchor=new KAnchor(p.get(0),p.get(1),true);
    chorusindex=0;
    initVertices();}
  
  /*
   * for test
   */
  public MPolygon(MMetagon metagon,KAnchor anchor){
    this.metagon=metagon;
    this.anchor=anchor;
    chorusindex=0;
    initVertices();}
  
  /*
   * ################################
   * TREE STRUCTURE STUFF
   * ################################
   */
  
  /*
   * if this polygon's parent is the root grid then this is the root polygon and its depth is 1
   */
  public boolean isRootPolygon(){
    return getDepth()==1;}
  
  public MPolygon getPolygonParent(){
    if(isRootPolygon())return null;
    return (MPolygon)getAncestor(2);}
  
  public List<MPolygon> getPolygonChildren(){
    //if this polygon is a leaf then we have no polygon children
    if(isLeaf())return new ArrayList<MPolygon>(0);
    //refer to this polygons's grid child, refer to that grid's children
    List<? extends TreeNode> children=getChild().getChildren();
    //gather those old nodes into a list
    List<MPolygon> polygons=new ArrayList<MPolygon>(children.size());
    for(TreeNode n:children)
      polygons.add((MPolygon)n);
    return polygons;}
  
  /*
   * ################################
   * GEOM
   * ################################
   */
  
  public MMetagon metagon;
  public KAnchor anchor;
  
  /*
   * ++++++++++++++++++++++++++++++++
   * KVERTICES
   * ++++++++++++++++++++++++++++++++
   */
  
  private KVertex[] vertices;
  
  public KVertex[] getVertices(){
    return vertices;}
  
  public int getVertexCount(){
    return vertices.length;}
  
  /*
   * vertex intervals
   * specified in the case of (v0,v1)
   * in the other cases they are (modelinterval[n]/modelinterval[0])*distance(v0,v1) 
   */
  private void initVertices(){
    int vectorcount=metagon.vectors.length;
    vertices=new KVertex[vectorcount+2];
    double localbaseinterval=getLocalBaseInterval(),distance;
    vertices[0]=anchor.v0;
    vertices[1]=anchor.v1;
    int direction=getLocalBaseForeward(),delta;
    for(int i=0;i<vectorcount;i++){
      distance=metagon.vectors[i].relativeinterval*localbaseinterval;
      delta=metagon.vectors[i].directiondelta;
      if(!anchor.twist)delta*=-1;
      direction=(direction+delta+12)%12;
      vertices[i+2]=new KVertex(GK.getVertex_VertexVector(vertices[i+1].coors,direction,distance));
      if(vertices[i+2].coors[3]==-1)
        throw new IllegalArgumentException(
          "BAD GEOMETRY. "+vertices[i+1]+" , direction:"+direction+" distance:"+distance);}}
  
  public int getLocalBaseForeward(){
    int f=GK.getDirection_VertexVertex(
      anchor.v0.getAnt(),anchor.v0.getBat(),anchor.v0.getCat(),anchor.v0.getDog(),
      anchor.v1.getAnt(),anchor.v1.getBat(),anchor.v1.getCat(),anchor.v1.getDog());
    if(f==GK.DIRECTION_NULL)
      throw new IllegalArgumentException("local foreward is direction null "+anchor.v0+" "+anchor.v1);
    return f;}
  
  public double getLocalBaseInterval(){
    return anchor.v0.getDistance(anchor.v1);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * DPOLYGON
   * 2d polygon
   * This FPolygon's KVertex geometry translated into 2D geometry
   * we derive any foreward, scale and translation context from ancestry
   * ++++++++++++++++++++++++++++++++
   */
  
  private DPolygon dpolygon=null;
  
  public DPolygon getDPolygon(){
    if(dpolygon==null)
      initDPolygon();
    dpolygon.object=this;//because it is very handy
    return dpolygon;}
  
  private void initDPolygon(){
    KVertex[] v=getVertices();
    int s=v.length;
    dpolygon=new DPolygon(s);
    //get the local kgrid
    //if there is none it's because were doing some kind of test thing
    //  in that case create a test grid. the default.
    MGrid fag=getFirstAncestorGrid();
    KGrid grid;
    if(fag!=null){
      grid=fag.getLocalKGrid();
    }else{
      grid=new KGrid();}
    //
    for(int i=0;i<s;i++)
      dpolygon.add(new DPoint(grid.getPoint2D(v[i])));}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * DETAIL SIZE
   * The diameter of the incircle
   * TODO this chould be in DPolygon
   * ++++++++++++++++++++++++++++++++
   */
  
  private Double detailsize=null;
  
  public double getDetailSize(){
    if(detailsize==null)
      detailsize=IncircleCalculator.getIncircle(getDPolygon()).r*2;
    return detailsize;}
  

  

  


}
