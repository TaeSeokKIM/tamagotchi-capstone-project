package com.tamaproject.gameobjects;

import android.graphics.Bitmap;

import com.tamaproject.GameObject;

public class Item extends GameObject
{
    private String name;
    private String description;
    private int health, hunger, sickness, xp;

    public Item(Bitmap bitmap, int x, int y)
    {
	super(bitmap, x, y);
	setDefault();
    }

    public Item(Bitmap bitmap)
    {
	super(bitmap, 0, 0);
	setDefault();
    }

    public Item(Bitmap bitmap, String name, String description, int health, int hunger,
	    int sickness, int xp)
    {
	super(bitmap, 0, 0);
	this.name = name;
	this.description = description;
	this.health = health;
	this.hunger = hunger;
	this.sickness = sickness;
	this.xp = xp;
    }

    public Item(Bitmap bitmap, String name, int health, int hunger, int sickness, int xp)
    {
	super(bitmap, 0, 0);
	this.name = name;
	this.description = "Item name: " + name + "\nHealth: " + health + "\nHunger: " + hunger + "\nSickness: " + sickness + "\nExperience: " + xp;
	this.health = health;
	this.hunger = hunger;
	this.sickness = sickness;
	this.xp = xp;
    }

    private void setDefault()
    {
	this.name = "Dummy";
	this.description = "This is a dummy item.";
	this.health = 0;
	this.hunger = 0;
	this.sickness = 0;
	this.xp = 0;
    }

    public String getName()
    {
	return name;
    }

    public int getHealth()
    {
	return health;
    }

    public int getHunger()
    {
	return hunger;
    }

    public int getSickness()
    {
	return sickness;
    }

    public int getXp()
    {
	return xp;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public void setHealth(int health)
    {
	this.health = health;
    }

    public void setHunger(int hunger)
    {
	this.hunger = hunger;
    }

    public void setSickness(int sickness)
    {
	this.sickness = sickness;
    }

    public void setXp(int xp)
    {
	this.xp = xp;
    }

    public String getDescription()
    {
	return description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

}