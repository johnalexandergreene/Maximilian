package org.fleen.maximilian;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.fleen.util.tag.TagManager;
import org.fleen.util.tag.Tagged;
import org.fleen.util.tree.TreeNode;
import org.fleen.util.tree.TreeNodeIterator;
import org.fleen.util.tree.TreeNodeServices;

/*
 * abstract class upon which MPolygon and MYard are based
 */
public abstract class MShape implements TreeNode,Serializable,Maximilian,Tagged{
  
  private static final long serialVersionUID=3945977644698006389L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public MShape(int chorusindex,List<String> tags){
    this.chorusindex=chorusindex;
    addTags(tags);}
  
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
  
  /**
   * @return nodes in the branch rooted at this node
   */
  public TreeNodeIterator getNodeIterator(){
    return new TreeNodeIterator(this);}
  
  /*
   * ################################
   * CHORUS INDEX AND GEOMETRIC CONTEXT SIGNATURE
   * 
   * Similar geometry with similiar context gets the same chorus index
   * Stuff that differs gets differing chorus index
   * 
   * The chorus indices of a geometry object and its ancestors describes a context-unique signature
   * 
   * By cultivating geometry with identical signatures identically we get symmetry
   * 
   * It's also just useful info.
   * 
   * Polygons get index 0..n
   * Yards get a constant. probably MAXINT
   * ################################
   */
  
  private int chorusindex;
  private MShapeSignature signature=null;
  
  public int getChorusIndex(){
    return chorusindex;}
  
  public MShapeSignature getSignature(){
    if(signature==null)signature=new MShapeSignature(this);
    return signature;}
  
  /*
   * ################################
   * TAGS
   * 
   * We can get tags from the jig that was used to create this geometry. 
   *   In the case of a polygon that's a tag from a jig section. 
   *   In the case of a yard we have tags denoting bagel and sponge
   * 
   * If this is a polygon we can also get tags from the polygon's metagon.
   * 
   * We can also add tags in the composition process. However we like.
   * 
   * ################################
   */
  
  private TagManager tagmanager=new TagManager();
  
  public String[] getTags(){
    return tagmanager.getTags();}
  
  public void initTags(String[] tags){
    tagmanager.setTags(tags);}
  
  public boolean hasTag(String tag){
    return tagmanager.hasTag(tag);}
  
  public boolean hasTags(String...tags){
    return tagmanager.hasTags(tags);}
  
  public void addTags(List<String> tags){
    tagmanager.addTags(tags);}
  
  public void removeTags(List<String> tags){
    tagmanager.removeTags(tags);}
  
  /*
   * ################################
   * GENERAL PURPOSE OBJECT
   * ################################
   */
  
  public Object gpobject;
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    return getClass().getSimpleName()+"["+hashCode()+"]"+tagmanager;}

}
