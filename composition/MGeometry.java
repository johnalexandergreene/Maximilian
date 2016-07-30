package org.fleen.maximilian.composition;

import org.fleen.util.tag.TagManager;

/*
 * abstract class upon which MPolygon, MBagel and MSponge are based
 */
public abstract class MGeometry extends MTreeNode{
  
  private static final long serialVersionUID=3945977644698006389L;
  
  /*
   * ################################
   * CHORUS INDEX AND GEOMETRY CONTEXT SIGNATURE
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
  private MGeometrySignature signature=null;
  
  public int getChorusIndex(){
    return chorusindex;}
  
  public MGeometrySignature getSignature(){
    if(signature==null)signature=new MGeometrySignature(this);
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
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    return getClass().getSimpleName()+"["+hashCode()+"]"+tagmanager;}

}
