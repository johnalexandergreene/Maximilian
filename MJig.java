package org.fleen.maximilian;

import java.util.List;


/*
 * Maximilian Jig
 * given an MShape, create new MShapes
 * like cutting a board into puzzle pieces
 * 
 * We use real 2d geometry for our shapes
 *   
 * The 2d shapes are based on kpolygons and kisrhombille geometry.
 * 
 * The jig is the operator in our shape grammar
 *    
 */
public interface MJig{
  
  
  /*
   * ################################
   * SHAPE PRODUCTION
   * 
   * Create 2..n new shapes using the target shape as param
   * Link the new shapes to the target treewise. Target is parent, new shapes are children. 
   * Return a reference to the new shapes
   * ################################
   */
  
  List<MShape> createShapes(MShape target);

  /*
   * ################################
   * DETAIL SIZE PREVIEW
   * 
   * This tells us the size of the shapes that this jig will generate before we actually generate them
   * Because we filter jigs by this. 
   * Because we try to control detail size in our compositions.
   * 
   * detail size is the smallest incircle diameter of the generated polygons, scaled to local fish 
   * that is : smallestincirclerad*localfish
   * controlling detail size helps us control the fine detail level of the composition
   * the composition logic goes like this :
   *   if the detail size is below our specified limit then the proposed 
   *   polygons are too finely detailed, so we reject the jig
   *   
   * The detail size of this jig can be described as a ratio (basedetailsize) of the target fish value.
   * basedetailsize is constant throughout the lifetime of this jig
   * so we need only calculate it once. 
   * ################################
   */
  
  double getDetailSizePreview(MShape target);
  
}
