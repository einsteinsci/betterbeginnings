package net.einsteinsci.betterbeginnings.config.json;

import net.einsteinsci.betterbeginnings.util.RegistryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

// allows for damage values and stack sizes
public class JsonLoadedItemStack
{
	private String itemName;
	private boolean isOreDictionary;
	private boolean isWildcard;
	private int damage;
	private int stackSize;

	public JsonLoadedItemStack(String name, int amount, int dmg)
	{
		itemName = name;
		damage = dmg;
		stackSize = amount;
		isOreDictionary = false;
		isWildcard = (dmg == OreDictionary.WILDCARD_VALUE);
	}

	public JsonLoadedItemStack(String name, int amount)
	{
		this(name, amount, 0);
	}

	public JsonLoadedItemStack(String name, int amount, boolean wildcard)
	{
		itemName = name;
		damage = 0;
		stackSize = amount;
		isWildcard = wildcard;
		isOreDictionary = false;
	}

	public JsonLoadedItemStack(String name, boolean wildcard)
	{
		this(name, 1, wildcard);
	}

	public JsonLoadedItemStack(String name)
	{
		this(name, 1, 0);
	}

	public static JsonLoadedItemStack makeOreDictionary(String entry, int amount)
	{
		JsonLoadedItemStack res = new JsonLoadedItemStack(entry, amount);
		res.isOreDictionary = true;
		return res;
	}

	public static JsonLoadedItemStack makeOreDictionary(String entry)
	{
		return makeOreDictionary(entry, 1);
	}

	public JsonLoadedItemStack(ItemStack stack)
	{
		this(RegistryUtil.getForgeName(stack), stack.stackSize, stack.getMetadata());
	}

	public List<ItemStack> getItemStacks()
	{
		if (isOreDictionary)
		{
			return OreDictionary.getOres(itemName);
		}

		Item item = RegistryUtil.getItemFromRegistry(itemName);

		List<ItemStack> single = new ArrayList<>();
		single.add(new ItemStack(item, stackSize, getDamage()));
		return single;
	}

	public ItemStack getFirstItemStackOrNull()
	{
		List<ItemStack> stacks = getItemStacks();
		if (stacks.isEmpty())
		{
			return null;
		}

		return stacks.get(0);
	}

	// not sure if this will be useful in the future...
	public String getMinetweakerName()
	{
		if (isOreDictionary)
		{
			return "<ore:" + itemName + ">";
		}

		String dmgStr = ":" + damage;
		if (isWildcard)
		{
			dmgStr = ":-1";
		}
		else if (damage == 0)
		{
			dmgStr = "";
		}

		String amountStr = " * " + stackSize;
		if (stackSize == 1)
		{
			amountStr = "";
		}

		return "<" + itemName + dmgStr + ">" + amountStr;
	}

	public String getItemName()
	{
		return itemName;
	}

	public boolean isOreDictionary()
	{
		return isOreDictionary;
	}

	public boolean isWildcard()
	{
		return isWildcard;
	}

	public int getStackSize()
	{
		return stackSize;
	}

	public int getDamage()
	{
		if (isWildcard)
		{
			return OreDictionary.WILDCARD_VALUE;
		}

		return damage;
	}

	public int getDamageNoWildcard()
	{
		return damage;
	}
}
