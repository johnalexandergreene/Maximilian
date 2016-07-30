package org.fleen.maximilian.composition;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.fleen.maximilian.Maximilian;
import org.fleen.util.tree.TreeNode;
import org.fleen.util.tree.TreeNodeIterator;
import org.fleen.util.tree.TreeNodeServices;

/*
 * FORSYTHIA TREE NODE
 * Nodes come in 3 types : Grid, Bubble and Lake
 * Grids comes in 2 subtypes : Root and Transform
 * See the Forsythia interface for details.
 */
public abstract class MTreeNode implements TreeNode,Serializable,Maximilian{

  private static final long serialVersionUID=6049549726585045831L;
  
  /*
   * ################################
   * TREENODE
   * ################################
   */
  
  public TreeNodeServices treenodeservices=new TreeNodeServices();
  
  /*
   * ++++++++++++++++++++++++++++++++
   * IMPLEMENTATION OF TreeNode INTERFACE
   */
  
  public TreeNode getParent(){
    return treenodeservices.getParent();}
  
  public void setParent(TreeNode node){
    treenodeservices.setParent(node);}
  
  public List<? extends TreeNode> getChildren(){
    return treenodeservices.getChildren();}
  
  public TreeNode getChild(){
    return treenodeservices.getChild();}
  
  public void setChildren(List<? extends TreeNode> nodes){
    treenodeservices.setChildren(nodes);}
  
  public void setChild(TreeNode node){
    treenodeservices.setChild(node);}
  
  public void addChild(TreeNode node){
    treenodeservices.addChild(node);}
  
  public int getChildCount(){
    return treenodeservices.getChildCount();}
  
  public boolean hasChildren(){
    return treenodeservices.hasChildren();}
  
  public void clearChildren(){
    treenodeservices.clearChildren();}
  
  public void removeChildren(Collection<? extends TreeNode> children){
    treenodeservices.removeChildren(children);}
  
  public boolean isRoot(){
    return treenodeservices.isRoot();}
  
  public boolean isLeaf(){
    return treenodeservices.isLeaf();}
  
  public int getDepth(){
    return treenodeservices.getDepth(this);}
  
  public TreeNode getRoot(){
    return treenodeservices.getRoot(this);}
  
  public TreeNode getAncestor(int levels){
    return treenodeservices.getAncestor(this,levels);}
  
  public List<TreeNode> getSiblings(){
    return treenodeservices.getSiblings(this);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * MAXIMILIAN SPECIFIC TREE STUFF
   */
  
  /**
   * @return The number of polygons encountered when traversing the tree from this node to the root.
   */
  public int getPolygonDepth(){
    int c=0;
    MTreeNode n=this;
    while(n!=null){
      n=n.getFirstAncestorGeometry();
      if(n!=null)c++;}
    return c;}
  
  /**
   * @return The number of grids encountered when traversing the tree from this node to the root.
   */
  public int getGridDepth(){
    int c=0;
    MTreeNode n=this;
    while(n!=null){
      n=n.getFirstAncestorGrid();
      if(n!=null){
        c++;}}
    return c;}
  
  public MGrid getFirstAncestorGrid(){
    TreeNode n=treenodeservices.getParent();
    while(!(n instanceof MGrid)){
      if(n==null)return null;
      n=n.getParent();}
    return (MGrid)n;}
  
  /*
   * returns the first ancestor of this node that is an MGeometry class object
   */
  public MGeometry getFirstAncestorGeometry(){
    TreeNode n=treenodeservices.getParent();
    while(!(n instanceof MGeometry)){
      if(n==null)return null;
      n=n.getParent();}
    return (MGeometry)n;}
  
  /**
   * @return Leaves of the branch rooted at this node
   */
  public TreeNodeIterator getLeafPolygonIterator(){
    return new TreeNodeIterator(this){
      //if it isn't a leaf AND a polygon then skip it
      public boolean skip(TreeNode node){
        return !((node instanceof MPolygon)&&(node.isLeaf()));}};}
  
  /**
   * @return nodes in the branch rooted at this node
   */
  public TreeNodeIterator getNodeIterator(){
    return new TreeNodeIterator(this);}
  
  /*
   * ################################
   * GENERAL PURPOSE OBJECT
   * ################################
   */
  
  public Object gpobject;
  
  
}
