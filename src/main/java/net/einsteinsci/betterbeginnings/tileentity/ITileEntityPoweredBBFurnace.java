package net.einsteinsci.betterbeginnings.tileentity;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;

public interface ITileEntityPoweredBBFurnace extends IEnergyReceiver
{
	IEnergyStorage getBattery();

	int getEnergyScaled(int px);

	void setEnergy(int rf);

	public void updateNetwork();
}
