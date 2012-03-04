package com.tamaproject.gameobjects;

import java.util.ArrayList;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.tamaproject.GameObject;

public class Tamagotchi extends GameObject
{
    private int currentHealth, maxHealth;
    private int currentHunger, maxHunger;
    private int currentXP, maxXP;
    private int currentSickness, maxSickness;
    private int poop;
    private int battleLevel;
    private int status;
    private long birthday;
    private Item equippedItem;

    public final static int ALIVE = 1, DEAD = 0;

    public Tamagotchi(Bitmap bitmap, int x, int y)
    {
	super(bitmap, x, y);
	setDefault();
    }

    public Tamagotchi(String bitmapFileLocation, AssetManager assetManager, int x, int y)
    {
	super(bitmapFileLocation, assetManager, x, y);
	setDefault();
    }

    /**
     * For testing purposes. Sets the Tamagotchi to a default state if none has been specified.
     */
    private void setDefault()
    {
	this.currentHealth = 100;
	this.maxHealth = 150;
	this.currentHunger = 10;
	this.maxHunger = 50;
	this.currentXP = 10;
	this.maxXP = 50;
	this.currentSickness = 10;
	this.maxSickness = 50;
	this.poop = 6;
	this.battleLevel = 5;
	this.status = Tamagotchi.ALIVE;
	this.birthday = 1325376000000l;
    }

    public Tamagotchi(Bitmap bitmap, int x, int y, int currentHealth, int maxHealth,
	    int currentHunger, int maxHunger, int currentXP, int maxXP, int currentSickness,
	    int maxSickness, int poop, int battleLevel, int status)
    {
	super(bitmap, x, y);
	this.currentHealth = currentHealth;
	this.maxHealth = maxHealth;
	this.currentHunger = currentHunger;
	this.maxHunger = maxHunger;
	this.currentXP = currentXP;
	this.maxXP = maxXP;
	this.currentSickness = currentSickness;
	this.maxSickness = maxSickness;
	this.poop = poop;
	this.battleLevel = battleLevel;
	this.status = status;
    }

    /**
     * Applies an item to the Tamagotchi
     * 
     * @param item
     * @return true if item successfully applied, false if not
     */
    public boolean applyItem(Item item)
    {
	this.currentHealth += item.getHealth();
	this.currentHunger += item.getHunger();
	this.currentSickness += item.getSickness();
	this.currentXP += item.getXp();

	checkStats();
	return true;
    }

    /**
     * Equips an item to Tamagotchi and returns the previously equipped item.
     * 
     * @param item
     *            Item to be equipped.
     * @return Previously equipped item, null if nothing was equipped.
     */
    public Item equipItem(Item item)
    {
	if (equippedItem != null)
	{
	    Item oldItem = equippedItem;
	    equippedItem = item;
	    return oldItem;
	}
	else
	{
	    equippedItem = item;
	    return null;
	}
    }

    /**
     * Makes sure that the Tamagotchi's stats are legitimate.
     */
    private void checkStats()
    {
	if (this.currentHealth > this.maxHealth)
	{
	    this.currentHealth = this.maxHealth;
	}

	if (this.currentHunger < 0)
	{
	    this.currentHunger = 0;
	}

	if (this.currentSickness < 0)
	{
	    this.currentSickness = 0;
	}

	// check if tama leveled up
	levelUp();

	// check if tama is dead
	isDead();
    }

    /**
     * Checks to see if Tamagotchi is dead
     * 
     * @return true if dead, false if alive
     */
    public boolean isDead()
    {
	if (status == Tamagotchi.DEAD)
	{
	    return true;
	}
	else if (currentHunger > maxHunger || currentSickness > maxSickness || currentHealth < 0)
	{
	    status = Tamagotchi.DEAD;
	    return true;
	}

	return false;
    }

    /**
     * Checks to see if Tamagotchi has gained a level
     * 
     * @return true if gained a level, false if not
     */
    private boolean levelUp()
    {
	boolean leveled = false;
	while (this.currentXP > this.maxXP)
	{
	    this.battleLevel++;

	    this.currentXP = this.currentXP - this.maxXP;
	    this.maxXP *= 2;

	    this.maxHealth += this.maxHealth / 2;
	    this.currentHealth = this.maxHealth;

	    this.maxHunger += this.maxHunger / 4;

	    this.maxSickness += this.maxSickness / 4;
	    leveled = true;
	}
	return leveled;
    }

    /**
     * Gets the Tamagotchi's age by subtracting its birthday from the current time.
     * 
     * @return The tama's age in days.
     */
    public int getAge()
    {
	int age = (int) ((System.currentTimeMillis() - birthday) / (24L * 60 * 60 * 1000));
	return age;
    }

    public int getCurrentHealth()
    {
	return currentHealth;
    }

    public int getMaxHealth()
    {
	return maxHealth;
    }

    public int getCurrentHunger()
    {
	return currentHunger;
    }

    public int getMaxHunger()
    {
	return maxHunger;
    }

    public int getCurrentXP()
    {
	return currentXP;
    }

    public int getMaxXP()
    {
	return maxXP;
    }

    public int getCurrentSickness()
    {
	return currentSickness;
    }

    public int getMaxSickness()
    {
	return maxSickness;
    }

    public int getPoop()
    {
	return poop;
    }

    public int getBattleLevel()
    {
	return battleLevel;
    }

    public int getStatus()
    {
	return status;
    }

    public long getBirthday()
    {
	return birthday;
    }

    public void setCurrentHealth(int currentHealth)
    {
	this.currentHealth = currentHealth;
    }

    public void setMaxHealth(int maxHealth)
    {
	this.maxHealth = maxHealth;
    }

    public void setCurrentHunger(int currentHunger)
    {
	this.currentHunger = currentHunger;
    }

    public void setMaxHunger(int maxHunger)
    {
	this.maxHunger = maxHunger;
    }

    public void setCurrentXP(int currentXP)
    {
	this.currentXP = currentXP;
    }

    public void setMaxXP(int maxXP)
    {
	this.maxXP = maxXP;
    }

    public void setCurrentSickness(int currentSickness)
    {
	this.currentSickness = currentSickness;
    }

    public void setMaxSickness(int maxSickness)
    {
	this.maxSickness = maxSickness;
    }

    public void setPoop(int poop)
    {
	this.poop = poop;
    }

    public void setBattleLevel(int battleLevel)
    {
	this.battleLevel = battleLevel;
    }

    public void setStatus(int status)
    {
	this.status = status;
    }

    public void setBirthday(long birthday)
    {
	this.birthday = birthday;
    }
}