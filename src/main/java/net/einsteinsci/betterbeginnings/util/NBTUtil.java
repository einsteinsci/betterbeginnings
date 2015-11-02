package net.einsteinsci.betterbeginnings.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

public class NBTUtil
{
	public static final byte TAG_END = 0;
	public static final byte TAG_BYTE = 1;
	public static final byte TAG_SHORT = 2;
	public static final byte TAG_INT = 3;
	public static final byte TAG_LONG = 4;
	public static final byte TAG_FLOAT = 5;
	public static final byte TAG_DOUBLE = 6;
	public static final byte TAG_BYTE_ARRAY = 7;
	public static final byte TAG_STRING = 8;
	public static final byte TAG_LIST = 9;
	public static final byte TAG_COMPOUND = 10;
	public static final byte TAG_INT_ARRAY = 11;

	public static NBTBase makeByType(byte typeCode, Object data)
	{
		switch (typeCode)
		{
			case 0:
				return new NBTTagEnd();
			case 1:
				return new NBTTagByte((byte)data);
			case 2:
				return new NBTTagShort((short)data);
			case 3:
				return new NBTTagInt((int)data);
			case 4:
				return new NBTTagLong((long)data);
			case 5:
				return new NBTTagFloat((float)data);
			case 6:
				return new NBTTagDouble((double)data);
			case 7:
				return new NBTTagByteArray((byte[])data);
			case 8:
				return new NBTTagString((String)data);
			case 9:
				return new NBTTagList();
			case 10:
				return new NBTTagCompound();
			case 11:
				return new NBTTagIntArray((int[])data);
			default:
				return null;
		}
	}

	public static void addBookEnchantment(ItemStack stack, Enchantment ench, short level)
	{
		if (stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}

		if (!stack.getTagCompound().hasKey("ench", 9))
		{
			stack.getTagCompound().setTag("StoredEnchantments", new NBTTagList());
		}

		NBTTagList enchList = stack.getTagCompound().getTagList("StoredEnchantments", TAG_COMPOUND);
		NBTTagCompound enchTag = new NBTTagCompound();
		enchTag.setShort("id", (short)ench.effectId);
		enchTag.setShort("lvl", level);
		enchList.appendTag(enchTag);
	}
}
