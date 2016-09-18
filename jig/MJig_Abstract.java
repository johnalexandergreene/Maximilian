package org.fleen.maximilian.jig;

import java.util.List;

import org.fleen.maximilian.MShape;
import org.fleen.util.tag.TagManager;

public abstract class MJig_Abstract implements MJig{
  
  /*
   * ################################
   * SHAPE CREATION
   * ################################
   */
  
  public List<MShape> createAndLinkShapes(MShape target){
    List<MShape> newshapes=createShapes(target).getList();
    for(MShape s:newshapes)
      s.setParent(target);
    target.setChildren(newshapes);
    return newshapes;}
  
  protected abstract CreatedShapes createShapes(MShape target);
  
  /*
   * ################################
   * DETAIL SIZE PREVIEW
   * ################################
   */
  
  public double getDetailSizePreview(MShape target){
    List<MShape> a=createShapes(target).getList();
    double smallest=Double.MAX_VALUE,test;
    for(MShape b:a){
      test=b.getDetailSize();
      if(test<smallest)
        smallest=test;}
    return smallest;}

  /*
   * ################################
   * DISTORTION LEVEL PREVIEW
   * ################################
   */
  
  public double getDistortionLevelPreview(MShape target){
    List<MShape> a=createShapes(target).getList();
    double largest=Double.MIN_VALUE,test;
    for(MShape b:a){
      test=b.getDistortionLevel();
      if(test>largest)
        largest=test;}
    return largest;}
  
  /*
   * ################################
   * TAGS
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

}
