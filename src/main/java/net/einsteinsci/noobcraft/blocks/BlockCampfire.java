package net.einsteinsci.noobcraft.blocks;

import java.util.Random;

import net.einsteinsci.noobcraft.ModMain;
import net.einsteinsci.noobcraft.config.NoobcraftConfig;
import net.einsteinsci.noobcraft.gui.NoobCraftGuiHandler;
import net.einsteinsci.noobcraft.register.RegisterBlocks;
import net.einsteinsci.noobcraft.tileentity.TileEntityCampfire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCampfire extends BlockContainer {

	private static boolean isKindling;
	private static boolean keepInventory;
	private static boolean isLit;
	private static boolean isKindling2;
	private static boolean isLit2;
	private final Random random = new Random();
	private static int maxKindle = NoobcraftConfig.maxKindleTime;
	
	public BlockCampfire(boolean lit, boolean kindling) {
		super(Material.rock);
		this.setResistance(1.0F);
		this.setHardness(1.0F);
		this.setBlockTextureName(ModMain.MODID + ":CampfireImg");
		if(!lit && !kindling){
			this.setCreativeTab(ModMain.tabNoobCraft);
			this.setBlockName("Campfire");
		}else if(!kindling && lit){
			this.setBlockName("CampfireLit");
		}else{
			this.setBlockName("CampfireKindling");
		}
		isLit = lit;
		isKindling = kindling;
	}

	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {

	}
	
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		TileEntityCampfire tileEntityCampfire = (TileEntityCampfire)world.getTileEntity(x, y, z);
		if (!isLit)
		{
			if (tileEntityCampfire != null)
			{
				for (int i = 0; i < tileEntityCampfire.getSizeInventory(); i++)
				{
					ItemStack stack = tileEntityCampfire.getStackInSlot(i);
					
					if (stack != null)
					{
						float velX = random.nextFloat() * 0.6f;
						float velY = random.nextFloat() * 0.6f + 0.2f;
						float velZ = random.nextFloat() * 0.6f;
						
						while (stack.stackSize > 0)
						{
							int j = random.nextInt(21) + 10;
							
							j = Math.min(j, stack.stackSize);
							
							stack.stackSize -= j;
							EntityItem entityItem =
								new EntityItem(world, x + velX, y + velY, z + velZ, new ItemStack(stack.getItem(), j,
									stack.getItemDamage()));
							
							if (stack.hasTagCompound())
							{
								entityItem.getEntityItem()
								.setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
							}
							
							float f3 = 0.025f;
							entityItem.motionX = (float)random.nextGaussian() * f3;
							entityItem.motionY = (float)random.nextGaussian() * f3 + 0.1f;
							entityItem.motionX = (float)random.nextGaussian() * f3;
							world.spawnEntityInWorld(entityItem);
						}
					}
				}
				
				world.func_147453_f(x, y, z, block);
			}
		}
		

		
		super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX,
		float clickY, float clickZ)
	{
		if(!world.isRemote){
			FMLNetworkHandler.openGui(player, ModMain.modInstance, NoobCraftGuiHandler.CAMPFIRE_ID, world, x, y, z);
		}
		return true;
		
	}
	
	public int getRenderType(){
		return -1;
	}
	
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return  p_149742_1_.getBlock(p_149742_2_, p_149742_3_, p_149742_4_).isReplaceable(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_) && World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_);
    }
	
	public boolean isOpaqueCube(){
		return false;
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityCampfire();
	}

	public static void updateCampfireState(boolean lit, boolean kindling, World world, int x, int y, int z)
	{
		int dir = world.getBlockMetadata(x, y, z);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		if (lit)
		{
			world.setBlock(x, y, z, RegisterBlocks.campfireLit);
		}
		else if(!lit && !kindling)
		{
			world.setBlock(x, y, z, RegisterBlocks.campfire);
		}
		else
		{
			world.setBlock(x, y, z, RegisterBlocks.campfireKindling);
		}
		
		world.setBlockMetadataWithNotify(x, y, z, dir, 2);
		
		if (tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);
		}
	}
	
	public boolean hasComparatorInputOverride(){
		return true;
	}
	
	public int getComparatorInputOverride(World world, int x, int y, int z, int i){
		return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(x, y, z));
		
	}
	
	@Override
	public Item getItemDropped(int par1, Random rand, int par3)
	{
		return Item.getItemFromBlock(RegisterBlocks.campfire);
	}
	
	@Override
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(RegisterBlocks.campfire);
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		boolean lit = isLit2;
		boolean kindling = isKindling2;
		System.out.println("Campfire: " + lit + "," + kindling);
		
		
		if (lit)
		{
			
			float f = x + 0.5f + random.nextFloat() * 4.0F / 16.0F;
			float f1 = y + random.nextFloat() * 6.0f / 16.0f;
			float f2 = z + 0.5f + random.nextFloat() * 4.0F / 16.0F;
			
			float f3 = 0.5f;
			float f4 = random.nextFloat() * 0.6f - 0.2f;
			
			world.spawnParticle("smoke", f, f1 + f3 - 0.2F, f2, 0.0F, 0.03F, 0.0F);
			world.spawnParticle("flame", f, f1 + f3 - 0.2F, f2, 0.0F, 0.03F, 0.0F);
			world.spawnParticle("smoke", f, f1 + f3 - 0.2F, f2, 0.0F, 0.03F, 0.0F);
			world.spawnParticle("flame", f, f1 + f3 - 0.2F, f2, 0.0F, 0.03F, 0.0F);
		}
		if(kindling)
		{
			float f = x + 0.5f + random.nextFloat() * 4.0F / 16.0F;
			float f1 = y + random.nextFloat() * 6.0f / 16.0f;
			float f2 = z + 0.5f + random.nextFloat() * 4.0F / 16.0F;
			
			float f3 = 0.5f;
			float f4 = random.nextFloat() * 0.6f - 0.2f;
			
			world.spawnParticle("smoke", f, f1 + f3 - 0.2F, f2, 0.0F, 0.03F, 0.0F);
			world.spawnParticle("smoke", f, f1 + f3 - 0.2F, f2, 0.0F, 0.04F, 0.0F);
			world.spawnParticle("smoke", f, f1 + f3 - 0.2F, f2, 0.0F, 0.04F, 0.0F);
			world.spawnParticle("smoke", f, f1 + f3 - 0.2F, f2, 0.0F, 0.03F, 0.0F);
		}
	}

}
