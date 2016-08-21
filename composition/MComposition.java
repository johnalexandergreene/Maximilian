package org.fleen.maximilian.composition;

import java.util.ArrayList;
import java.util.List;

import org.fleen.maximilian.Maximilian;
import org.fleen.maximilian.grammar.MMetagon;
import org.fleen.maximilian.grammar.MGrammar;
import org.fleen.util.tree.TreeNode;
import org.fleen.util.tree.TreeNodeIterator;

/*
 * A tree of polygons and yards
 * some convenient methods for init and node-access
 */
public class MComposition implements Maximilian{
  
  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  protected MGrammar grammar=null;
  
  public void setGrammar(MGrammar grammar){
    this.grammar=grammar;}
  
  public MGrammar getGrammar(){
    return grammar;}
  
  /*
   * ################################
   * INIT
   * ################################
   */
  
  //create a root grid. We use the default.
  //create a root polygon from the specified metagon. We use the default.
  //set parent-child relationships
  public void initTree(MMetagon rootpolygonmetagon){
    root=new MGridBasic();
    MPolygon p=new MPolygon(rootpolygonmetagon);
    root.setChild(p);
    p.setParent(root);}
  
  //create a root grid. We use the default.
  //set parent-child relationships
  public void initTree(MPolygon rootpolygon){
    root=new MGridBasic();
    root.setChild(rootpolygon);
    rootpolygon.setParent(root);}
  
  public void initTree(MGridBasic grid,MPolygon rootpolygon){
    root=grid;
    root.setChild(rootpolygon);
    rootpolygon.setParent(root);}
  
  /*
   * ################################
   * TREE NODES
   * The root is a grid
   * The nodes are grids and polygons
   * ################################
   */
  
  public MGridBasic root;

  public MGridBasic getRoot(){
    return root;}
  
  public void setRoot(MGridBasic root){
    this.root=root;}

  public TreeNodeIterator getNodeIterator(){
    return new TreeNodeIterator(root);}
  
  public List<MTreeNode> getNodes(){
    List<MTreeNode> nodes=new ArrayList<MTreeNode>();
    TreeNodeIterator i=getNodeIterator();
    while(i.hasNext())
      nodes.add((MTreeNode)i.next());
    return nodes;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GRIDS
   * ++++++++++++++++++++++++++++++++
   */
  
  public TreeNodeIterator getGridIterator(){
    return new GridNodeIterator(root);}
  
  private class GridNodeIterator extends TreeNodeIterator{

    public GridNodeIterator(MTreeNode root){
      super(root);}

    protected boolean skip(TreeNode node){
      return !(node instanceof MGrid);}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * POLYGONS
   * ++++++++++++++++++++++++++++++++
   */
  
  public MPolygon getRootPolygon(){
    MPolygon a=(MPolygon)root.getChild();
    return a;}

  public TreeNodeIterator getPolygonIterator(){
    return new PolygonIterator(root);}
  
  public List<MPolygon> getPolygons(){
    List<MPolygon> polygons=new ArrayList<MPolygon>();
    TreeNodeIterator i=getPolygonIterator();
    while(i.hasNext())
      polygons.add((MPolygon)i.next());
    return polygons;}
  
  public TreeNodeIterator getLeafPolygonIterator(){
    return new LeafPolygonIterator(root);}
  
  public List<MPolygon> getLeafPolygons(){
    List<MPolygon> polygons=new ArrayList<MPolygon>();
    TreeNodeIterator i=getLeafPolygonIterator();
    while(i.hasNext())
      polygons.add((MPolygon)i.next());
    return polygons;}
  
  private class PolygonIterator extends TreeNodeIterator{

    public PolygonIterator(TreeNode root){
      super(root);}

    protected boolean skip(TreeNode node){
      return(!(node instanceof MPolygon));}}
 
  private class LeafPolygonIterator extends TreeNodeIterator{

    public LeafPolygonIterator(MTreeNode root){
      super(root);}

    protected boolean skip(TreeNode node){
      return !(node instanceof MPolygon&&node.isLeaf());}}
 
}
