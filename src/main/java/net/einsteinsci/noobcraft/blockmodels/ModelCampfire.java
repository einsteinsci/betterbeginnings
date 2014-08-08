// Date: 8/8/2014 4:16:30 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package net.einsteinsci.noobcraft.blockmodels;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCampfire extends ModelBase
{
  //fields
    ModelRenderer Stone1;
    ModelRenderer Stone2;
    ModelRenderer Stone3;
    ModelRenderer Stone4;
    ModelRenderer Stone5;
    ModelRenderer Stone6;
    ModelRenderer Stone7;
    ModelRenderer Stone8;
    ModelRenderer Stone9;
    ModelRenderer Stone10;
    ModelRenderer Stone11;
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
  
  public ModelCampfire()
  {
    textureWidth = 32;
    textureHeight = 32;
    
      Stone1 = new ModelRenderer(this, 16, 0);
      Stone1.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone1.setRotationPoint(0F, 24F, -4F);
      Stone1.setTextureSize(32, 32);
      Stone1.mirror = true;
      setRotation(Stone1, 0.669215F, 1.301251F, -1.226894F);
      Stone2 = new ModelRenderer(this, 8, 0);
      Stone2.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone2.setRotationPoint(-2F, 24F, -4F);
      Stone2.setTextureSize(32, 32);
      Stone2.mirror = true;
      setRotation(Stone2, 0.2974289F, 1.412787F, -1.375609F);
      Stone3 = new ModelRenderer(this, 0, 0);
      Stone3.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone3.setRotationPoint(2.5F, 24F, -2.5F);
      Stone3.setTextureSize(32, 32);
      Stone3.mirror = true;
      setRotation(Stone3, 0.8179294F, 1.003822F, -1.375609F);
      Stone4 = new ModelRenderer(this, 0, 0);
      Stone4.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone4.setRotationPoint(3F, 24F, 0F);
      Stone4.setTextureSize(32, 32);
      Stone4.mirror = true;
      setRotation(Stone4, 1.412787F, 0.8922867F, -1.784573F);
      Stone5 = new ModelRenderer(this, 8, 0);
      Stone5.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone5.setRotationPoint(3.4F, 24F, -1F);
      Stone5.setTextureSize(32, 32);
      Stone5.mirror = true;
      setRotation(Stone5, 0.9294653F, 0.9294653F, -1.449966F);
      Stone6 = new ModelRenderer(this, 8, 0);
      Stone6.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone6.setRotationPoint(1F, 24F, 2F);
      Stone6.setTextureSize(32, 32);
      Stone6.mirror = true;
      setRotation(Stone6, 1.152537F, 0.7807508F, -1.784573F);
      Stone7 = new ModelRenderer(this, 0, 0);
      Stone7.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone7.setRotationPoint(-2F, 24F, 2F);
      Stone7.setTextureSize(32, 32);
      Stone7.mirror = true;
      setRotation(Stone7, 0.8551081F, 0.6320364F, -1.561502F);
      Stone8 = new ModelRenderer(this, 16, 0);
      Stone8.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone8.setRotationPoint(-3F, 24F, 1F);
      Stone8.setTextureSize(32, 32);
      Stone8.mirror = true;
      setRotation(Stone8, 1.412787F, 0.4833219F, -1.747395F);
      Stone9 = new ModelRenderer(this, 8, 0);
      Stone9.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone9.setRotationPoint(-3.5F, 24F, -1F);
      Stone9.setTextureSize(32, 32);
      Stone9.mirror = true;
      setRotation(Stone9, 1.561502F, 0.2974289F, -1.673038F);
      Stone10 = new ModelRenderer(this, 0, 0);
      Stone10.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone10.setRotationPoint(-2F, 24F, -3F);
      Stone10.setTextureSize(32, 32);
      Stone10.mirror = true;
      setRotation(Stone10, 2.267895F, 0.1858931F, -1.710216F);
      Stone11 = new ModelRenderer(this, 16, 0);
      Stone11.addBox(0F, 0F, 0F, 1, 3, 3);
      Stone11.setRotationPoint(0F, 24F, -4F);
      Stone11.setTextureSize(32, 32);
      Stone11.mirror = true;
      setRotation(Stone11, 2.862753F, 0.1487144F, -1.729941F);
      Shape1 = new ModelRenderer(this, 0, 24);
      Shape1.addBox(0F, 0F, 0F, 7, 1, 7);
      Shape1.setRotationPoint(-4F, 23.6F, -4F);
      Shape1.setTextureSize(32, 32);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
      Shape2 = new ModelRenderer(this, 0, 7);
      Shape2.addBox(0F, 0F, 0F, 1, 1, 3);
      Shape2.setRotationPoint(-0.5F, 21F, 0F);
      Shape2.setTextureSize(32, 32);
      Shape2.mirror = true;
      setRotation(Shape2, -1.59868F, 0.1115358F, 0.5576792F);
      Shape3 = new ModelRenderer(this, 0, 12);
      Shape3.addBox(0F, 0F, 0F, 1, 1, 4);
      Shape3.setRotationPoint(1F, 21F, -1F);
      Shape3.setTextureSize(32, 32);
      Shape3.mirror = true;
      setRotation(Shape3, -1.635859F, 3.141593F, 0.5576792F);
      Shape4 = new ModelRenderer(this, 0, 12);
      Shape4.addBox(0F, 0F, 0F, 1, 1, 4);
      Shape4.setRotationPoint(0F, 20.5F, -1F);
      Shape4.setTextureSize(32, 32);
      Shape4.mirror = true;
      setRotation(Shape4, -1.784573F, -0.9852332F, 0.4089647F);
      Shape5 = new ModelRenderer(this, 0, 12);
      Shape5.addBox(0F, 0F, 0F, 1, 1, 4);
      Shape5.setRotationPoint(1F, 21F, 0.5F);
      Shape5.setTextureSize(32, 32);
      Shape5.mirror = true;
      setRotation(Shape5, -1.524323F, 1.61727F, 0.669215F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Stone1.render(f5);
    Stone2.render(f5);
    Stone3.render(f5);
    Stone4.render(f5);
    Stone5.render(f5);
    Stone6.render(f5);
    Stone7.render(f5);
    Stone8.render(f5);
    Stone9.render(f5);
    Stone10.render(f5);
    Stone11.render(f5);
    Shape1.render(f5);
    Shape2.render(f5);
    Shape3.render(f5);
    Shape4.render(f5);
    Shape5.render(f5);
  }
  
  public void renderModel(float f) {
	    Stone1.render(f);
	    Stone2.render(f);
	    Stone3.render(f);
	    Stone4.render(f);
	    Stone5.render(f);
	    Stone6.render(f);
	    Stone7.render(f);
	    Stone8.render(f);
	    Stone9.render(f);
	    Stone10.render(f);
	    Stone11.render(f);
	    Shape1.render(f);
	    Shape2.render(f);
	    Shape3.render(f);
	    Shape4.render(f);
	    Shape5.render(f);
		
	}
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5,Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }



}
