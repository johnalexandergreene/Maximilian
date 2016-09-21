package org.fleen.maximilian;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.fleen.forsythia.grammar.FMetagon;
import org.fleen.forsythia.grammar.ForsythiaGrammar;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.maximilian.jig.MJigServer;
import org.fleen.util.tree.TreeNode;
import org.fleen.util.tree.TreeNodeIterator;

/*
 * A tree of polygons and yards
 * some convenient methods for init and node-access
 * reference to
 *   a forsythia grammar 
 *   a jig server
 */
public class MComposition implements Maximilian{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public MComposition(){}
  
  public MComposition(ForsythiaGrammar forsythiagrammar){
    setForsythiaGrammar(forsythiagrammar);}
  
  /*
   * ################################
   * FORSYTHIA GRAMMAR
   * ################################
   */
  
  private ForsythiaGrammar forsythiagrammar=null;
  
  public void setForsythiaGrammar(ForsythiaGrammar forsythiagrammar){
    this.forsythiagrammar=forsythiagrammar;}
  
  public ForsythiaGrammar getForsythiaGrammar(){
    if(forsythiagrammar==null)
      throw new IllegalArgumentException("null forsythia grammar");
    return forsythiagrammar;}
  
  /*
   * ################################
   * METAGONS
   * A metagon is the abstracted operands in our shape-grammarly dance
   * ################################
   */
  
  private List<MMetagon> metagons=null;
  
  public MMetagon getMMetagon(KMetagon km){
    if(metagons==null)initMMetagons();
    for(MMetagon mm:metagons)
      if(mm.equals(km))
        return mm;
    throw new IllegalArgumentException("MMetagon with the specified KMetagon not found");}
  
  public List<MMetagon> getMMetagons(){
    if(metagons==null)initMMetagons();
    return new ArrayList<MMetagon>(metagons);}
  
  private void initMMetagons(){
    metagons=new ArrayList<MMetagon>();
    Iterator<FMetagon> i=getForsythiaGrammar().getMetagonIterator();
    FMetagon fm;
    MMetagon mm;
    while(i.hasNext()){
      fm=i.next();
      mm=new MMetagon(fm);
      metagons.add(mm);}
    ((ArrayList<MMetagon>)metagons).trimToSize();}
  
  /*
   * ################################
   * JIG SERVER
   * A jig is the operator in our shape-grammarly dance
   * ################################
   */
  
  private MJigServer jigserver=null;
  
  public void setMJigServer(MJigServer jigserver){
    this.jigserver=jigserver;}
  
  /*
   * return this composition's jig server
   * if the jig server is null then init it to an instance of MJigServer_Basic
   */
  public MJigServer getJigServer(){
//    if(jigserver==null)jigserver=new MJigServer_Basic();
    return jigserver;}
  
  /*
   * ################################
   * SHAPE TREE
   * ################################
   */
  
  public static final String[] ROOT_METAGON_ACCQUIREMENT_TAG={"root"};
  MPolygon root;
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INIT
   * ++++++++++++++++++++++++++++++++
   */
  
  public void initTree(MPolygon rootpolygon){
    root=rootpolygon;}

  public void initTree(){
    initTree(ROOT_METAGON_ACCQUIREMENT_TAG);}
  
  /*
   * get the metagon with the specified tag/s and make a shape from that
   * if you want a random shape from the grammar then just use a tag that none of the metagons use, like "random" or "flippynips".
   */
  public void initTree(String... tags){
    List<MMetagon> 
      yestag=new ArrayList<MMetagon>(),
      notag=new ArrayList<MMetagon>();
    Iterator<MMetagon> i=getMMetagons().iterator();
    MMetagon m=null;
    while(i.hasNext()){
      m=i.next();
      if(m.hasTags(tags))
        yestag.add(m);
      else
        notag.add(m);}
    //
    if(!yestag.isEmpty()){
      m=yestag.get(new Random().nextInt(yestag.size()));
    }else if(!notag.isEmpty()){
      m=notag.get(new Random().nextInt(notag.size()));
    }else{
      throw new IllegalArgumentException("this forsythia grammar appears to have no metagons in it");}
    root=new MPolygon(m);
    root.addTags("egg");}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * NODE ACCESS
   * ++++++++++++++++++++++++++++++++
   */
  
  public MPolygon getRoot(){
    return root;}

  public TreeNodeIterator getShapeIterator(){
    return new TreeNodeIterator(root);}
  
  public List<MShape> getShapes(){
    List<MShape> shapes=new ArrayList<MShape>();
    TreeNodeIterator i=getShapeIterator();
    while(i.hasNext())
      shapes.add((MShape)i.next());
    return shapes;}
  
  public TreeNodeIterator getLeafIterator(){
    return new LeafIterator(root);}
  
  public List<MShape> getLeafShapes(){
    List<MShape> shapes=new ArrayList<MShape>();
    TreeNodeIterator i=getLeafIterator();
    while(i.hasNext())
      shapes.add((MShape)i.next());
    return shapes;}
  
  private class LeafIterator extends TreeNodeIterator{

    public LeafIterator(MShape root){
      super(root);}

    protected boolean skip(TreeNode node){
      return !(node.isLeaf());}}
 
}
