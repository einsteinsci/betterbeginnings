package net.einsteinsci.betterbeginnings.util;

public class MathUtil
{
	// just one function for now. There might be more in the future.
	public static int roundUp(float num)
	{
		if (num == (int)num)
		{
			return (int)num;
		}

		return (int)num + 1;
	}
}
