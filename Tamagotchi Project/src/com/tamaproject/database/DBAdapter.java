package com.tamaproject.database;

import com.tamaproject.andengine.entity.Item;
import com.tamaproject.andengine.entity.Tamagotchi;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	
	public static final String colID = "_id";
	public static final String colCurHealth = "curHealth";
	public static final String colMaxHealth = "maxHealth";
	public static final String colCurHunger = "curHunger";
	public static final String colMaxHunger = "maxHunger";
	public static final String colCurXP = "curXP";
	public static final String colMaxXP = "maxXp";
	public static final String colCurSickness = "curSickness";
	public static final String colMaxSickness = "maxSickness";
	public static final String colPoop = "poop";
	public static final String colBattleLevel = "battleLevel";
	public static final String colStatus = "status";
	public static final String colBirthday = "birthday";
	private static final String TamaTable = "Tamagotchi";
	
	public static final String colIPOID = "_id";
	public static final String colXCoord = "xCoord";
	public static final String colYCoord = "yCoord";
	public static final String colItem = "itemName";
	private static final String IPOTable = "InPlayObjects";
	
	public static final String colItemID = "_id";
	public static final String colCategory = "category";
	public static final String colItemName = "itemName";
	public static final String colHealth = "health";
	public static final String colHunger = "hunger";
	public static final String colSickness = "sickness";
	public static final String colXP = "xp";
	public static final String colDescription = "description";
	private static final String ItemTable = "Items";
	
	public static final String colItemID2 = "_id";
	public static final String colQuantity = "quantity";
	private static final String BackpackTable = "Backpack";
	
	private static final String dbName = "TamagotchiProject";
	private static final int dbVersion = 1;
	private static final String tag = "DBAdapter";
	
	private static final String createTamaTable = "create table "+TamaTable+" (_id integer primary key autoincrement, curHealth integer" +
			"not null, maxHealth integer not null, curHunger integer not null, maxHunger integer not null, curXP integer not null, " +
			"maxXP integer not null, curSickness integer not null, maxSickness integer not null, poop integer not null, battleLevel" +
			"integer not null, status integer not null, birthday integer not null, filePath text);";
	
	private static final String createIPOTable = "create table "+IPOTable+" (_id integer primary key autoincrement, xCoord float" +
			"not null, yCoord float not null, itemName text not null);";
	
	private static final String createItemTable = "create table "+ItemTable+" (category text," +
			"itemName text unique not null, health integer not null, hunger integer not null, sickness integer not null, xp integer not null," +
			"description text not null, filePath);";
	
	private static final String createBackpackTable = "create table "+BackpackTable+" (itemName text unique not null, quantity integer" +
			"not null);";
	
	private final Context context;
	
	private static DatabaseHelper DBHelper;
	private static SQLiteDatabase db;
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		/**
		 * Constructor
		 * 
		 * @param context
		 */
		DatabaseHelper(Context context) {
			super(context, dbName, null, dbVersion);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(createTamaTable);
			db.execSQL(createIPOTable);
			db.execSQL(createItemTable);
			db.execSQL(createBackpackTable);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(tag, "Upgrading database from version " +
					oldVersion + "to " + newVersion + ", which will destroy all old data");
			db.execSQL("drop table if exists Tamagotchi");
			db.execSQL("drop table if exists InPlayObjects");
			db.execSQL("drop table if exists Items");
			db.execSQL("drop table if exists Backpack");
			onCreate(db);
		}
		
		/**
		 * Opens the database
		 * 
		 * @return
		 * @throws SQLException
		 */
		public DatabaseHelper open() throws SQLException {
			db = DBHelper.getWritableDatabase();
			return this;
		}
		
		/**
		 * Closes the database
		 */
		public void close() {
			DBHelper.close();
		}
		
		/**
		 * Inserts the Tamagotchi attributes into the database for the first time
		 * 
		 * @param t
		 * @return
		 */
		public long insertTama(Tamagotchi t) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(colID, t.getID());
			initialValues.put(colCurHealth, t.getCurrentHealth());
			initialValues.put(colMaxHealth, t.getMaxHealth());
			initialValues.put(colCurHunger, t.getCurrentHunger());
			initialValues.put(colMaxHunger, t.getMaxHunger());
			initialValues.put(colCurXP, t.getCurrentXP());
			initialValues.put(colMaxXP, t.getMaxXP());
			initialValues.put(colCurSickness, t.getCurrentSickness());
			initialValues.put(colMaxSickness, t.getMaxSickness());
			/*initialValues.put(colPoop, );*/
			initialValues.put(colBattleLevel, t.getBattleLevel());
			initialValues.put(colStatus, t.getStatus());
			initialValues.put(colBirthday, t.getBirthday());
			return db.insert(TamaTable, null, initialValues);
		}
		
		/**
		 * Saves the Tamagotchi attributes into the database after the initial save
		 * 
		 * @param t
		 * @return
		 */
		public boolean savaTama(Tamagotchi t) {
			
			ContentValues args = new ContentValues();
			args.put(colID, t.getID());
			args.put(colCurHealth, t.getCurrentHealth());
			args.put(colMaxHealth, t.getMaxHealth());
			args.put(colCurHunger, t.getCurrentHunger());
			args.put(colMaxHunger, t.getMaxHunger());
			args.put(colCurXP, t.getCurrentXP());
			args.put(colMaxXP, t.getMaxXP());
			args.put(colCurSickness, t.getCurrentSickness());
			args.put(colMaxSickness, t.getMaxSickness());
			/*args.put(colPoop, poop);*/
			args.put(colBattleLevel, t.getBattleLevel());
			args.put(colStatus, t.getStatus());
			args.put(colBirthday, t.getBirthday());
			return db.update(TamaTable, args, colID+"="+id, null) > 0;
		}
		
		/**
		 * Inserts the In Play Objects into the database table for the first time
		 * 
		 * @param id
		 * @param xCoord
		 * @param yCoord
		 * @param itemName
		 * @return
		 */
		public long insertIPOs(int id, float xCoord, float yCoord, String itemName) {
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(colIPOID, id);
			initialValues.put(colXCoord, xCoord);
			initialValues.put(colYCoord, yCoord);
			initialValues.put(colItem, itemName);
			return db.insert(IPOTable, null, initialValues);
		}
		
		/**
		 * Updates the in play objects in the database after the initial save
		 * 
		 * @param id
		 * @param xCoord
		 * @param yCoord
		 * @param itemName
		 * @return
		 */
		public boolean saveIPOs(int id, float xCoord, float yCoord, String itemName) {
			
			ContentValues args = new ContentValues();
			args.put(colIPOID, id);
			args.put(colXCoord, xCoord);
			args.put(colYCoord, yCoord);
			args.put(colItem, itemName);
			return db.update(IPOTable, args, colIPOID+"="+id, null) > 0;
		}
		
		/**
		 * Inserts the items data into the database for the first time
		 * @param i
		 * @return
		 */
		public long insertItems(Item i) {
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(colCategory, null);
			initialValues.put(colItemName, i.getName());
			initialValues.put(colHealth, i.getHealth());
			initialValues.put(colHunger, i.getHunger());
			initialValues.put(colSickness, i.getSickness());
			initialValues.put(colXP, i.getXp());
			initialValues.put(colDescription, i.getDescription());
			return db.insert(ItemTable, null, initialValues);
		}
		
		/**
		 * Inserts the items that the user currently has in the game for the first time
		 * 
		 * @param id
		 * @param quantity
		 * @return
		 */
		public long insertBackpack(int id, int quantity) {
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(colItemID2, id);
			initialValues.put(colQuantity, quantity);
			return db.insert(BackpackTable, null, initialValues);
		}
		
		public long insertBackpack(ArrayList<Item> item) {
			Iterator itr = new Iterator();
			while(itr.hasNext()) {
				item.
			}
		}
		
		/**
		 * Updates the backpack in the database after the initial save
		 * 
		 * @param id
		 * @param quantity
		 * @return
		 */
		public boolean saveBackpack(int id, int quantity) {
			
			ContentValues args = new ContentValues();
			args.put(colItemID2, id);
			args.put(colQuantity, quantity);
			return db.update(BackpackTable, args, colItemID2+"="+id, null) > 0;
		}
		
		/**
		 * Retrieves the last saved attributes of the Tamagotchi
		 * 		
		 * @param id
		 * @return
		 */
		public Cursor retreatTama(int id) {
			Cursor mCursor =  db.query(true, TamaTable, new String[] {colID,colCurHealth, colMaxHealth, 
					colCurHunger, colMaxHunger, colCurXP, colMaxXP, colCurSickness, 
					colMaxSickness, colPoop, colBattleLevel, colStatus, colBirthday}, colID+"="+id, null, null, null, null, null);
			if(mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
			
		}
		
		public Cursor retreatIPO() {
			return db.query(IPOTable, new String[] {colIPOID, colXCoord, colYCoord, colItem}, null, null, null, null, null);
		}
		
		public Cursor retreatItems() {
			return db.query(ItemTable, new String[] {colItemID, colCategory, colItemName, colHealth, colHunger, 
					colHunger, colSickness, colXP, colDescription}, null, null, null, null,  null);
		}
		
		public Cursor retreatBackpack() {
			return db.query(BackpackTable, new String[] {colItemID2, colQuantity}, null, null, null, null, null);
		}
	}
}