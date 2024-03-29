package com.tamaproject.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.anddev.andengine.entity.sprite.BaseSprite;

/**
 * Tamagotchi class. Holds all of the stats for Tamagotchi.
 * 
 * @author Jonathan
 * 
 */
public class Tamagotchi
{
    public static final int MAX_BATTLE_LEVEL = 100;
    private int currentHealth, maxHealth;
    private int currentHunger, maxHunger;
    private int currentXP, maxXP;
    private int currentSickness, maxSickness;
    private int battleLevel;
    private int status;
    private long birthday;
    private long age;
    private int id;
    private Item equippedItem;
    private int money;

    private BaseSprite sprite;
    private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private Calendar calendar = Calendar.getInstance();

    public final static int ALIVE = 1, DEAD = 0, LEVEL_UP = 2;

    public Tamagotchi()
    {
	setDefault();
	this.calendar.setTimeInMillis(birthday);
    }

    public Tamagotchi(int currentHealth, int maxHealth, int currentHunger, int maxHunger,
	    int currentXP, int maxXP, int currentSickness, int maxSickness, int battleLevel,
	    int status, long birthday, Item equippedItem, long age, int id, int money)
    {
	this.currentHealth = currentHealth;
	this.maxHealth = maxHealth;
	this.currentHunger = currentHunger;
	this.maxHunger = maxHunger;
	this.currentXP = currentXP;
	this.maxXP = maxXP;
	this.currentSickness = currentSickness;
	this.maxSickness = maxSickness;
	this.battleLevel = battleLevel;
	this.status = status;
	this.birthday = birthday;
	this.equippedItem = equippedItem;
	this.calendar.setTimeInMillis(birthday);
	this.age = age;
	this.id = id;
	this.setMoney(money);
    }

    /**
     * Default stats for when Tamagotchi is first born
     */
    public void setDefault()
    {
	this.currentHealth = 100;
	this.maxHealth = 100;
	this.currentHunger = 10;
	this.maxHunger = 100;
	this.currentXP = 10;
	this.maxXP = 100;
	this.currentSickness = 0;
	this.maxSickness = 100;
	this.battleLevel = 1;
	this.status = Tamagotchi.ALIVE;
	this.birthday = System.currentTimeMillis();
	this.age = 0;
	this.id = 1;
	this.money = 100;
    }

    public Tamagotchi(int currentHealth, int maxHealth, int currentHunger, int maxHunger,
	    int currentXP, int maxXP, int currentSickness, int maxSickness, int battleLevel,
	    int status)
    {
	this.currentHealth = currentHealth;
	this.maxHealth = maxHealth;
	this.currentHunger = currentHunger;
	this.maxHunger = maxHunger;
	this.currentXP = currentXP;
	this.maxXP = maxXP;
	this.currentSickness = currentSickness;
	this.maxSickness = maxSickness;
	this.battleLevel = battleLevel;
	this.status = status;
    }

    /**
     * Applies item to Tamagotchi
     * 
     * @param item
     *            Item to be applied
     * @return DEAD, ALIVE, or LEVEL_UP
     */
    public int applyItem(Item item)
    {
	this.currentHealth += item.getHealth();
	this.currentHunger += item.getHunger();
	this.currentSickness += item.getSickness();
	this.currentXP += item.getXp();

	return checkStats();
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
     * Checks the stats of Tamagotchi
     * 
     * @return Tamagotchi.DEAD, LEVEL_UP, or ALIVE
     */
    public int checkStats()
    {
	if (this.currentHealth > this.maxHealth)
	{
	    this.currentHealth = this.maxHealth;
	}
	else if (this.currentHealth < 0)
	{
	    this.currentHealth = 0;
	}

	if (this.currentHunger < 0)
	{
	    this.currentHunger = 0;
	}
	else if (this.currentHunger > this.maxHunger)
	{
	    this.currentHunger = this.maxHunger;
	}

	if (this.currentSickness < 0)
	{
	    this.currentSickness = 0;
	}
	else if (this.currentSickness > this.maxSickness)
	{
	    this.currentSickness = this.maxSickness;
	}

	// check if tama is dead
	if (isDead())
	    return Tamagotchi.DEAD;

	// check if tama leveled up

	if (levelUp())
	    return Tamagotchi.LEVEL_UP;

	return Tamagotchi.ALIVE;

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
	else if (currentHealth <= 0)
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
    public long getAge()
    {
	return age / (1000 * 60 * 60 * 24);
    }

    public void addToAge(long time)
    {
	age += time;
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

    public String getFormattedBirthday()
    {
	return formatter.format(calendar.getTime());
    }

    public void setBirthday(long birthday)
    {
	this.birthday = birthday;
    }

    public int getID()
    {
	return id;
    }

    public Item getEquippedItem()
    {
	return equippedItem;
    }

    public String getEquippedItemName()
    {
	if (equippedItem == null)
	    return "None";
	else
	    return equippedItem.getName();
    }

    public BaseSprite getSprite()
    {
	return sprite;
    }

    public void setEquippedItem(Item equippedItem)
    {
	this.equippedItem = equippedItem;
    }

    public void setSprite(BaseSprite sprite)
    {
	this.sprite = sprite;
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

    public String getStats()
    {
	String s = "Age: " + getAge() + " days old \nHealth: " + currentHealth + "/" + maxHealth + "\nSickness: " + currentSickness + "/" + maxSickness + "\nHunger: " + currentHunger + "/" + maxHunger + "\nExperience: " + currentXP + "/" + maxXP + "\nBattle Level: " + battleLevel + "\nBirthday: " + getFormattedBirthday() + "\nMoney: $" + money;
	if (equippedItem != null)
	    s += "\n \nEquipped Item: \n" + equippedItem.getInfo();
	return s;
    }

    public void setBattleLevel(int battleLevel)
    {
	this.battleLevel = battleLevel;
    }

    public void setStatus(int status)
    {
	this.status = status;
    }

    public int getMoney()
    {
	return money;
    }

    public void setMoney(int money)
    {
	this.money = money;
    }
}
