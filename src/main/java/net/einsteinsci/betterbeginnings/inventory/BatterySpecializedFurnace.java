package net.einsteinsci.betterbeginnings.inventory;

import cofh.api.energy.IEnergyStorage;

public class BatterySpecializedFurnace implements IEnergyStorage
{
	private int maxEnergy;
	private int energyStored;

	public BatterySpecializedFurnace(int maxRF)
	{
		maxEnergy = maxRF;
	}
	public BatterySpecializedFurnace(int maxRF, int startRF)
	{
		this(maxRF);
		energyStored = startRF;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		if (energyStored + maxReceive <= maxEnergy)
		{
			if (!simulate)
			{
				energyStored += maxReceive;
			}

			return maxReceive;
		}
		else
		{
			int received = maxEnergy - energyStored;
			if (!simulate)
			{
				energyStored = maxEnergy;
			}

			return received;
		}
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		if (energyStored >= maxExtract)
		{
			if (!simulate)
			{
				energyStored -= maxExtract;
			}

			return maxExtract;
		}
		else
		{
			int extracted = energyStored;
			if (!simulate)
			{
				energyStored = 0;
			}

			return extracted;
		}
	}

	@Override
	public int getEnergyStored()
	{
		return energyStored;
	}

	@Override
	public int getMaxEnergyStored()
	{
		return maxEnergy;
	}

	public void setEnergyStored(int rf)
	{
		energyStored = rf;
	}
}
