package com.tamaproject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import com.tamaproject.gameobjects.*;
import com.tamaproject.util.GameObjectUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    private static final String TAG = GameView.class.getSimpleName();

    private GameLoopThread thread;

    private int startX = 50, startY = 50;
    private Context context = null;
    public final String PREFS_NAME = "GRAPHICS";
    private SharedPreferences settings;
    private ArrayList<GameObject> inPlayObjects = new ArrayList<GameObject>();
    private Hashtable<Integer, Bitmap> bitmapTable = new Hashtable<Integer, Bitmap>();
    private Display display = null;
    private int height = -1, width = -1;
    private int playTopBound, playBottomBound;

    private final String BACKPACK_LABEL = "Backpack";

    private Backpack bp; // backpack with items
    private Tamagotchi tama; // our tamagotchi
    private InPlayObjects ipo;

    public GameView(Context context)
    {
	super(context);
	// adding the callback (this) to the surface holder to intercept events
	getHolder().addCallback(this);

	this.context = context;

	settings = context.getSharedPreferences(PREFS_NAME, 0);

	// load last location of tama
	LoadPreferences();

	// initialize the height, width, display variables
	initDisplay();

	// initialize bitmaps
	initBitmaps();

	// create dummy items

	ArrayList<Item> items = new ArrayList<Item>();
	for (int i = 1; i <= 15; i++)
	{
	    items.add(new Item(bitmapTable.get(R.drawable.treasure)));
	}

	bp = new Backpack(items, display);
	tama = new Tamagotchi(bitmapTable.get(R.drawable.tama), display.getWidth() / 2, display.getHeight() / 2);
	ipo = new InPlayObjects();

	if (isDead(tama))
	{
	    Toast.makeText(this.context, "Your Tamagotchi is DEAD", Toast.LENGTH_SHORT).show();
	}

	initPoop(tama.getPoop());

	initInterface();

	// create the game loop thread
	thread = new GameLoopThread(getHolder(), this);

	// make the GamePanel focusable so it can handle events
	setFocusable(true);
    }

    private boolean isDead(Tamagotchi t)
    {
	return t.isDead();
    }

    // gets the width and height of the screen
    public void initDisplay()
    {
	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	display = wm.getDefaultDisplay();
	this.height = display.getHeight();
	this.width = display.getWidth();
	this.playTopBound = height / 5;
	this.playBottomBound = height / 3 * 2 - 50;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
	// at this point the surface is created and
	// we can safely start the game loop
	thread.setRunning(true);
	thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
	Toast.makeText(this.context, tama.toString(), Toast.LENGTH_SHORT).show();
	SavePreferences("x", tama.getX() + "");
	SavePreferences("y", tama.getY() + "");
	Log.d(TAG, "Surface is being destroyed");
	try
	{
	    thread.setRunning(false);
	} catch (Exception e)
	{

	}
	Log.d(TAG, "Thread was shut down cleanly");
    }

    // loads up the sprites and bitmaps
    private void initBitmaps()
    {
	bitmapTable.put(R.drawable.kuro, BitmapFactory.decodeResource(getResources(), R.drawable.kuro));
	bitmapTable.put(R.drawable.tama, BitmapFactory.decodeResource(getResources(), R.drawable.tama));
	bitmapTable.put(R.drawable.treasure, BitmapFactory.decodeResource(getResources(), R.drawable.treasure));
	bitmapTable.put(R.drawable.poop, BitmapFactory.decodeResource(getResources(), R.drawable.poop));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
	int ex = (int) event.getX();
	int ey = (int) event.getY();

	if (event.getAction() == MotionEvent.ACTION_DOWN)
	{
	    Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());

	    if (!bp.handleActionDown(ex, ey))
	    {
		if (!bp.isBackpackOpen())
		{
		    //tama.handleActionDown(ex, ey);
		    ipo.handleActionDown(ex, ey);
		}
	    }

	    // close any open pop ups
	    if (popUp != null)
		popUp.dismiss();

	    // region to open backpack
	    if (event.getY() > getHeight() - 50 && event.getX() > getWidth() - 50)
	    {
		bp.setBackpackOpen(!bp.isBackpackOpen());
		if (!bp.isBackpackOpen())
		{
		    bp.refreshItems();
		}
	    }
	}
	if (event.getAction() == MotionEvent.ACTION_MOVE)
	{
	    // the tama was picked up and is being dragged
	    //tama.handleActionMove(ex, ey);
	    Item temp = bp.handleActionMove(ex, ey);
	    if (temp == null)
	    {
		ipo.handleActionMove(ex, ey, playTopBound, playBottomBound);
	    }

	    if (temp != null && bp.isBackpackOpen())
	    {
		bp.setBackpackOpen(false);
		bp.refreshItems();
	    }

	    if (GameObjectUtil.isTouching(temp, tama))
	    {
		tama.setBitmap(bitmapTable.get(R.drawable.kuro));
	    }
	    else
	    {
		tama.setBitmap(bitmapTable.get(R.drawable.tama));
	    }

	}
	if (event.getAction() == MotionEvent.ACTION_UP)
	{
	    //tama.handleActionUp();
	    Item temp = bp.handleActionUp();

	    if (temp != null)
	    {
		if (temp.isMoved())
		{
		    temp.setTouched(false);
		    giveItem(tama, temp);
		}
		else
		// if user tapped item
		{
		    showItemDescription(temp);
		}
	    }
	    else
	    {
		ipo.handleActionUp();
	    }

	    bp.refreshItems();
	}
	return true;
    }

    private PopupWindow popUp;
    private LinearLayout layout;

    private void showItemDescription(Item i)
    {
	popUp = new PopupWindow(context);
	layout = new LinearLayout(context);
	TextView tv = new TextView(context);
	Button but = new Button(context);
	ImageView iv = new ImageView(context);
	LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	layout.setOrientation(LinearLayout.HORIZONTAL);
	params.setMargins(10, 10, 10, 10);
	tv.setText(i.getDescription() + "\n");
	iv.setImageBitmap(i.getBitmap());
	but.setText("Close");
	but.setOnClickListener(new OnClickListener()
	{
	    public void onClick(View v)
	    {
		popUp.dismiss();
	    }

	});

	layout.addView(iv, params);
	layout.addView(tv, params);
	layout.addView(but, params);
	popUp.setContentView(layout);

	popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
	popUp.update(width, height / 4);
	popUp.setFocusable(true);
    }

    private void initPoop(int numPoop)
    {
	Random r = new Random();
	int count = 0;
	while (count < numPoop)
	{
	    int x = r.nextInt(width);
	    int y = r.nextInt(playBottomBound - playTopBound) + playTopBound;
	    GameObject go = new GameObject(bitmapTable.get(R.drawable.poop), x, y);
	    go.setGroup("poop");
	    if (!GameObjectUtil.isTouching(go, tama))
	    {
		ipo.add(go);
		count++;
	    }
	}
    }

    // this method is to demonstrate collisions
    protected boolean giveItem(GameObject tama, Item item)
    {
	if (tama != null && item != null)
	{
	    tama.setBitmap(bitmapTable.get(R.drawable.tama));
	    if (GameObjectUtil.isTouching(tama, item))
	    {
		bp.removeItem(item);
		bp.refreshItems();
		return true;
	    }
	}

	return false;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
	// fills the canvas with black
	if (canvas != null)
	{
	    canvas.drawColor(Color.BLACK);
	    if (bp.isBackpackOpen())
	    {
		bp.drawAllItems(canvas);
	    }
	    else
	    {
		drawInterface(canvas);
		bp.draw(canvas);
		ipo.draw(canvas);
		tama.draw(canvas); // always draw tama last
	    }
	}
    }

    private Paint paint = new Paint();
    private Rect bpRectangle;
    private Rect cHealth, mHealth, cHunger, mHunger, cSick, mSick;

    protected void drawInterface(Canvas canvas)
    {
	paint.setColor(Color.WHITE);
	paint.setStyle(Style.STROKE);
	paint.setStrokeWidth(1);
	canvas.drawRect(bpRectangle, paint);

	paint.setStyle(Style.FILL_AND_STROKE);
	paint.setTextSize(20);
	paint.setAntiAlias(true);
	canvas.drawText(BACKPACK_LABEL + " (" + bp.numItems() + "/" + bp.maxSize() + ")", 5, height / 3 * 2 + 25, paint);

    }

    protected void initInterface()
    {
	this.bpRectangle = new Rect(1, height / 3 * 2, width - 1, height - 1);
    }

    private void SavePreferences(String key, String value)
    {
	SharedPreferences.Editor editor = settings.edit();
	editor.putString(key, value);
	editor.commit();
    }

    private void LoadPreferences()
    {
	try
	{
	    this.startX = Integer.parseInt(settings.getString("x", ""));
	    this.startY = Integer.parseInt(settings.getString("y", ""));
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

}