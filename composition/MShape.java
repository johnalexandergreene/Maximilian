package org.fleen.maximilian.composition;

import java.util.ArrayList;
import java.util.List;

import org.fleen.util.tag.TagManager;
import org.fleen.util.tag.Tagged;
import org.fleen.util.tree.TreeNode;

/*
 * abstract class upon which MPolygon and MYard are based
 */
public abstract class MShape extends MTreeNode implements Tagged{
  
  private static final long serialVersionUID=3945977644698006389L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public MShape(int chorusindex){
    this.chorusindex=chorusindex;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  /*
   * create a new grid node based on this shape's geometry
   * set it as a child of this node
   * return a reference 
   */
  public MGrid createGrid(int density){
    MPolygon polygon;
    MGrid grid;
    if(this instanceof MPolygon){
      polygon=(MPolygon)this;
      grid=new MGridBoundedDeformable(polygon,density);
    }else{
      polygon=((MYard)this).polygons.get(0);
      grid=new MGridBoundedDeformable(polygon,density);}
    grid.setParent(this);
    setChild(grid);
    return grid;}
  
  /*
   * ################################
   * TREE STUFF
   * ################################
   */
  
  /*
   * traversing the tree we get rootgrid > rootshape (polygon) > grid > shape > grid > shape ... etc  
   */
  public MShape getShapeGrandparent(){
    if(getParent().isRoot())return null;
    return (MShape)getAncestor(2);}
  
  /*
   * a shape has 0 or 1 child
   * if it has a child then it's a grid
   * that grid has 1..n children, shapes
   * return those shapes if they exist, return null otherwise
   */
  public List<MShape> getShapeGrandchildren(){
    //if this shape is a leaf
    if(isLeaf())return null;
    //refer to this polygons's grid child, refer to that grid's children
    List<? extends TreeNode> children=getChild().getChildren();
    //gather those old nodes into a list
    List<MShape> shapes=new ArrayList<MShape>(children.size());
    for(TreeNode n:children)
      shapes.add((MShape)n);
    return shapes;}
  
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
   * Yards get index -1
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
  
  public void addTags(List<String> tags){
    tagmanager.addTags(tags);}
  
  public void removeTags(List<String> tags){
    tagmanager.removeTags(tags);}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    return getClass().getSimpleName()+"["+hashCode()+"]"+tagmanager;}

}
