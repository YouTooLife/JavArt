package net.youtoolife.javart;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ArtObject {
	
	public ShapeRenderer sr = new ShapeRenderer();
	
	public Vector2 pos = new Vector2(-100, 0);
	public float size = 0;
	
	public Array<Vector2> vecSeg = null;
	public byte[][] bSeg;
	Array<ArtSegment> fig = new Array<ArtSegment>();
	
	public Color cl1 = new Color(1.f, 1.f, 1.f, 1.f);
	public Color cl2 = new Color(1.f, 1.f, 1.f, 1.f);
	
	public boolean filled = false;
	public boolean drawLine = true;
	
	public boolean anim = true;
	public boolean move = true;
	public boolean multySeg = false;
	public int speed = 70;
	public int type = 0;
	
	public int curSeg = 0;
	
	
	public Array<String> actions = new Array<String>();
	public boolean lineAct = true;
	public boolean runAction = false;
	
	public ArtObject(byte[][] segments) {
		bSeg = segments;
		vecSeg = ArtCore.createFigure(bSeg, 12, 6);
	}
	
	public ArtObject(byte[][] segments, float size, float tab) {
		bSeg = segments;
		vecSeg = ArtCore.createFigure(bSeg, size, tab);
		sortByCirclePos();
		this.size = size;
		for (int i = 0; i < vecSeg.size; i++) {
			ArtSegment seg = new ArtSegment(i, this);
			seg.index = i;
			seg.pos.set(vecSeg.get(i));
			fig.add(seg);
		}
	}
	
	public ArtObject(byte[][] segments, float size, float tab, Color cl1, Color cl2) {
		bSeg = segments;
		vecSeg = ArtCore.createFigure(bSeg, size, tab);
		sortByCirclePos();
		this.size = size;
		for (int i = 0; i < vecSeg.size; i++) {
			ArtSegment seg = new ArtSegment(i, this);
			seg.index = i;
			seg.pos.set(vecSeg.get(i));
			seg.cl1 = cl1;
			seg.cl21 = cl2;
			//seg.filled = true;
			fig.add(seg);
		}
	}
	
	public void  addSegment(int id, boolean remove, Vector2 vec) {
		ArtSegment seg = new ArtSegment(id, this);
		seg.index = id;
		seg.pos.set(vec);
		seg.cl1 = cl1;
		seg.cl21 = cl2;
		seg.remove = remove;
		fig.add(seg);
	}
	
	public ArtObject(byte[][] segments, float size, float tab, Vector2 pos) {
		this(segments, size, tab);
		this.pos = pos;
	}
	
	public void sortByCirclePos() {
		Array<Vector2> box2 = new Array<Vector2>();
		for (int i = 0; i < vecSeg.size; i++) {
			boolean flag = false;
			for (int al = 0; al < 3600; al++) {
				float alpha = (float)Math.PI/180*360/3600*al;
				float x = (float)Math.cos(alpha)*size;
				float y = (float)Math.sin(alpha)*size;
				for(Vector2 vec:vecSeg) {
					if (vec.y*y >= 0 && vec.x*x >= 0)
						if ( Math.abs(vec.x*y/x - vec.y) < Math.E) {
						//System.out.println(vec.x*y/x+"|=|"+vec.y);
						box2.add(vec);
						vecSeg.removeValue(vec, false);
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
		}
		vecSeg.addAll(box2);
	}
	
	private Vector2 rotVect2(float alpha, Vector2 vec) {
		alpha = (float)Math.PI/180*alpha;
		return new Vector2((float)(vec.x*Math.cos(alpha)-vec.y*Math.sin(alpha)), 
				(float)(vec.x*Math.sin(alpha)+vec.y*Math.cos(alpha)));
	}
	
	public void rotateFigure(float alpha) {
		
		for (int i = 0; i < vecSeg.size; i++) {
			Vector2 vec = vecSeg.get(i);
			vecSeg.set(i, rotVect2(alpha, vec));
		}
		curSeg = 0;
		//sortByCirclePos();
	}
	
	private Vector2 moveToVector(Vector2 old, Vector2 vec) {
		return new Vector2(vec.x+old.x, vec.y+old.y);
	}
	
	public void moveFigToVector(Vector2 point) {
		
		for (int i = 0; i < vecSeg.size; i++) {
			Vector2 vec = vecSeg.get(i);
			vecSeg.set(i, moveToVector(vec, point));
		}
		curSeg = 0;
	}
	
	public void parseCommand(String[] act) {
		if (act[0].equals("rotateBy")) {
			rotateFigure(Float.parseFloat(act[1]));
		}
		if (act[0].equals("setSpeed")) {
			speed = Integer.parseInt(act[1]);
		}
		if (act[0].equals("multySeg")) {
			multySeg = Boolean.parseBoolean(act[1]);
		}
		if (act[0].equals("setColor")) {
			cl2 = Color.valueOf(act[1]);
		}
		if (act[0].equals("setFilledColor")) {
			cl1 = Color.valueOf(act[1]);
		}
		if (act[0].equals("filledColor")) {
			filled = Boolean.parseBoolean(act[1]);
		}
		if (act[0].equals("drawLine")) {
			drawLine = Boolean.parseBoolean(act[1]);
		}
		if (act[0].equals("setType")) {
			type = Integer.parseInt(act[1]);
		}
		if (act[0].equals("loadFigure")) {
			vecSeg.clear();
			if (act[1].equals("heart"))
				vecSeg.addAll(ArtCore.createFigure(ArtCore.heart, 6, 6));
			if (act[1].equals("box"))
				vecSeg.addAll(ArtCore.getBox(200.f, 45));
			sortByCirclePos();	
		}
		if (act[0].equals("moveTo")) {
			Vector2 vec = new Vector2(Float.parseFloat(act[1]), 
					Float.parseFloat(act[2]));
			moveFigToVector(vec);
		}
	}
	
	public void addAct(String act) {
		//curSeg = 0;
		actions.add(act);
	}
	
	private Vector2 getRandVec() {
		Random rand = new Random();
		Vector2 vec = new Vector2(0, 0);
		
		int way = rand.nextInt(4);
		if (way == 0 || way == 2) {
			vec.set(rand.nextInt(Gdx.graphics.getWidth()), 
					way==0?Gdx.graphics.getHeight():0);
		}
		if (way == 1 || way == 3) {
			vec.set(way==1?Gdx.graphics.getWidth():0, 
					rand.nextInt(Gdx.graphics.getHeight()));
		}
		return vec;
		
	}
	
	public void update() {
		
		if (vecSeg.size > fig.size || vecSeg.size < fig.size) {
			
			for (int i = fig.size; i < vecSeg.size; i++) {
				addSegment(i, false, getRandVec());
			}
			Array<Vector2> vecs = new Array<Vector2>();
			for (int i = fig.size-1; i >= (vecSeg.size); i--) {
				vecs.add(getRandVec());
				fig.get(i).remove = true;
			}
			vecSeg.addAll(vecs);
		}
		
		if (actions.size > 0)
		if (lineAct) {
			
			if (runAction)
				return;
			
			curSeg = 0;
			String action = actions.get(0);
			String[] act = action.split(":");
			runAction = true;
			parseCommand(act);
			actions.removeIndex(0);
		}
		
	}
	
	public void draw() {
		update();
		for (ArtSegment seg: fig) {
			//System.out.println(seg.pos.x +":"+seg.pos.y);
			seg.draw();
		}
	}
}
