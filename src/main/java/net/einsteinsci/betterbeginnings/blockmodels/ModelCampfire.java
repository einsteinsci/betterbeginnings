package net.einsteinsci.betterbeginnings.blockmodels;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCampfire extends ModelBase
{
  //fields
    ModelRenderer Log1;
    ModelRenderer Log2;
    ModelRenderer Log3;
    ModelRenderer Log4;
    ModelRenderer Log5;
    ModelRenderer Log6;
  
  public ModelCampfire()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      Log1 = new ModelRenderer(this, 0, 0);
      Log1.addBox(0F, 0F, 0F, 2, 2, 8);
      Log1.setRotationPoint(-1F, 18F, 1.8F);
      Log1.setTextureSize(64, 64);
      Log1.mirror = true;
      setRotation(Log1, -0.7807508F, 0F, 0F);
      Log2 = new ModelRenderer(this, 0, 0);
      Log2.addBox(0F, 0F, 0F, 2, 2, 8);
      Log2.setRotationPoint(1F, 18F, -1F);
      Log2.setTextureSize(64, 64);
      Log2.mirror = true;
      setRotation(Log2, -0.7807508F, 2.658271F, 0F);
      Log3 = new ModelRenderer(this, 0, 0);
      Log3.addBox(0F, 0F, 0F, 2, 2, 8);
      Log3.setRotationPoint(1F, 18F, 2F);
      Log3.setTextureSize(64, 64);
      Log3.mirror = true;
      setRotation(Log3, -0.7807508F, 1.161832F, 0F);
      Log4 = new ModelRenderer(this, 0, 10);
      Log4.addBox(0F, 0F, 0F, 2, 2, 8);
      Log4.setRotationPoint(-2F, 18F, 0F);
      Log4.setTextureSize(64, 64);
      Log4.mirror = true;
      setRotation(Log4, -0.7807508F, -1.050296F, 0F);
      Log5 = new ModelRenderer(this, 0, 10);
      Log5.addBox(0F, 0F, 0F, 2, 2, 8);
      Log5.setRotationPoint(-1F, 18F, -1.5F);
      Log5.setTextureSize(64, 64);
      Log5.mirror = true;
      setRotation(Log5, -0.7807508F, -2.398021F, 0F);
      Log6 = new ModelRenderer(this, 0, 10);
      Log6.addBox(0F, 0F, 0F, 2, 2, 8);
      Log6.setRotationPoint(2F, 18F, 0.5F);
      Log6.setTextureSize(64, 64);
      Log6.mirror = true;
      setRotation(Log6, -0.7807508F, 1.87752F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Log1.render(f5);
    Log2.render(f5);
    Log3.render(f5);
    Log4.render(f5);
    Log5.render(f5);
    Log6.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

  public void renderModel(float f)
  {
	    Log1.render(f);
	    Log2.render(f);
	    Log3.render(f);
	    Log4.render(f);
	    Log5.render(f);
	    Log6.render(f);
  }
}
