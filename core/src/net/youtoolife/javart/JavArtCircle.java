package net.youtoolife.javart;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class JavArtCircle extends ApplicationAdapter {
	
    float width, height;
	
	OrthographicCamera guiCam;
	
	SpriteBatch batch;
	ShapeRenderer sr;
	
	Array<Vector2> circle, box, vBox, vBoxv;
	Array<Vector2> aheart, vheart, vheartv;
	Array<Vector2> afig, vfig, vfigv;
	
	float r = 200.f;
	float a = 180f;
	
	float alpha = 0;
	int mode = 0, mode2 = 0;
	boolean drawLine = false;
	
	int iter = 0;
	
	byte[][] heart = {
			{0,0,1,1,1,1,0,0,0,1,1,1,1,0,0},
			{0,1,0,0,0,0,1,0,1,0,0,0,0,1,0},
			{1,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0},
			{0,0,1,0,0,0,0,0,0,0,0,0,1,0,0},
			{0,0,0,1,1,0,0,0,0,0,1,1,0,0,0},
			{0,0,0,0,0,1,0,0,0,1,0,0,0,0,0},
			{0,0,0,0,0,0,1,1,1,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0}
			};
	
	ArtObject ao;
	
	private Vector2 rotVect2(float alpha, Vector2 vec) {
		alpha = (float)Math.PI/180*alpha;
		return new Vector2((float)(vec.x*Math.cos(alpha)-vec.y*Math.sin(alpha)), 
				(float)(vec.x*Math.sin(alpha)+vec.y*Math.cos(alpha)));
	}
	
	private void rotateBox(float alpha) {
		for (int i = 0; i < box.size; i++) {
			Vector2 vec = box.get(i);
			box.set(i, rotVect2(alpha, vec));
		}
	}
	
	private void rotateCircle(float alpha) {
		for (int i = 0; i < circle.size; i++) {
			Vector2 vec = circle.get(i);
			circle.set(i, rotVect2(alpha, vec));
		}
	}
	
	private void rotateFigure(Array<Vector2> fig, float alpha) {
		for (int i = 0; i < fig.size; i++) {
			Vector2 vec = fig.get(i);
			fig.set(i, rotVect2(alpha, vec));
		}
	}
	
	private Array<Vector2> createFigure(byte[][] mfig, float size) {
		Array<Vector2> fig = new Array<Vector2>();
		for (int i = 0; i < mfig.length; i++) {
			for (int j = 0; j < mfig[0].length; j++) {
				if (mfig[i][j] == 1) {
					fig.add(new Vector2((6+size)*j
							- (6+size)*mfig[0].length/2, 
							-(6+size)*i + 12*mfig.length/2));
				}
			}
		}
		return fig;
	}
	
	private void reBox(Array<Vector2> cBox) {
		Array<Vector2> box2 = new Array<Vector2>();
		for (int i = 0; i < circle.size; i++) {
			boolean flag = false;
			for (int al = 0; al < 3600; al++) {
				float alpha = (float)Math.PI/180*360/3600*al;
				float x = (float)Math.cos(alpha)*r;
				float y = (float)Math.sin(alpha)*r;
				for(Vector2 vec:cBox) {
					if (vec.y*y >= 0 && vec.x*x >= 0)
						if ( Math.abs(vec.x*y/x - vec.y) < Math.E) {
						//System.out.println(vec.x*y/x+"|=|"+vec.y);
						box2.add(vec);
						cBox.removeValue(vec, false);
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
		}
		cBox.addAll(box2);
	}
	
	private void moveBox(float delta) {
		float speed = 55;
		for (int i = 0; i < vBox.size; i++) {
			if (vBoxv.get(i).x > vBox.get(i).x)
				vBox.get(i).x += speed*delta;
			else
				vBox.get(i).x -= speed*delta;
			if (vBoxv.get(i).y > vBox.get(i).y)
				vBox.get(i).y += speed*delta;
			else
				vBox.get(i).y -= speed*delta;
		}
	}
	
	private void moveFig(Array<Vector2> fig,Array<Vector2> vfig, float delta) {
		float speed = 55;
		for (int i = 0; i < fig.size; i++) {
			if (vfig.get(i).x > fig.get(i).x)
				fig.get(i).x += speed*delta;
			else
				fig.get(i).x -= speed*delta;
			if (vfig.get(i).y > fig.get(i).y)
				fig.get(i).y += speed*delta;
			else
				fig.get(i).y -= speed*delta;
		}
	}
		
	@Override
	public void create () {
		width = Gdx.graphics.getWidth(); 
	    height = Gdx.graphics.getHeight();
		guiCam = new OrthographicCamera(width, height);
		
		Boolean b = Boolean.parseBoolean("1");
		System.out.println(b);
		
		ao = new ArtObject(ArtCore.heart, 6, 6, 
				new Color(0.5f, 0.5f, 0.075f, 1.f),
				new Color(0.5f, 0.5f, 0.75f,1.f));
		//ao.type = 0;
		//sr = new ShapeRenderer();
		
		/*circle = new Array<Vector2>();
		for (int i = 0; i < 36; i++) {
			float alpha = (float)Math.PI/180*((360/36)*i - 3f);
			float x = (float)Math.cos(alpha)*r;
			float y = (float)Math.sin(alpha)*r;
			circle.add(new Vector2(x, y));
		}
		
		box = new Array<Vector2>();
		vBox = new Array<Vector2>();
		vBoxv = new Array<Vector2>();
		for (int i = 0; i < 36; i++) {
			float p = (float)Math.sqrt((a)*(a)*2);
			float t = p + 4*p/35*i;
			int a1 = (1/(int)(t/p))*(int)(t/p); 
			int a2 = (2/(int)(t/p))*(int)(((int)(t/p))/2); 
			int a3 = (3/(int)(t/p))*(int)(((int)(t/p))/3);
			int a4 = (4/(int)(t/p))*(int)(((int)(t/p))/4); 
			float a5 = t - p*(int)(t/p);
			float l = (float)Math.PI/4;
			float  x = -a/2.f + ((a2+a3)*a5 + a4*p)*(float)Math.cos(l);
			float  y = -a/2.f + ((a1+a4)*a5 + a3*p)*(float)Math.sin(l);
			box.add(new Vector2(x, y));
		}
		//rotateBox(2);
		reBox(box);
		vBox.addAll(box);
		vBoxv.addAll(vBox);
		
		
		aheart = createFigure(heart,12);
		reBox(aheart);
		vheart = new Array<Vector2>();
		vheart.addAll(aheart);
		vheartv = new Array<Vector2>();
		vheartv.addAll(vheart);
		
		afig = new Array<Vector2>();
		vfig = new Array<Vector2>();
		vfigv = new Array<Vector2>();
		afig.addAll(box);
		vfig.addAll(vBox);
		vfigv.addAll(vBoxv);
		*/
	}
	
	private void drawFig(Array<Vector2> fig) {
		sr.setProjectionMatrix(guiCam.combined);
		sr.setColor(0.5f, 0.5f, 0.075f, 1.f);
		sr.begin(ShapeType.Filled);
		for (Vector2 vec:fig)
			sr.circle(vec.x, vec.y, 3.f);
		sr.end();
		
		if (drawLine) {
		sr.setColor(0.5f, 0.5f, 0.75f, 1.f);
		sr.begin(ShapeType.Line);
		for (int i = 0; i < fig.size; i++)
			sr.line(circle.get(i), fig.get(i));
		sr.end();
		}		
	}

	@Override
	public void render () {
		GL20 gl = Gdx.gl;
		
		gl.glEnable(GL20.GL_BLEND);
		gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClearColor(0, 0, 0.075f, 1.f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//sr.setProjectionMatrix(guiCam.combined);
		//sr.setColor(0.5f, 0.5f, 0.075f, 1.f);
		
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			ao.lineAct = true;
			ao.addAct("setSpeed:50");
			ao.addAct("drawLine:false");
			ao.addAct("setType:0");
			ao.addAct("setColor:4f0014");
			ao.addAct("filledColor:true");
			ao.addAct("multySeg:true");
			ao.addAct("rotateBy:10");
			ao.addAct("rotateBy:-90");
			ao.addAct("loadFigure:box");
			ao.addAct("drawLine:false");
			ao.addAct("setType:0");
			ao.addAct("setColor:4f0014");
			ao.addAct("filledColor:true");
			ao.addAct("rotateBy:10");
			ao.addAct("rotateBy:-90");
			ao.addAct("moveTo:100:0");
			ao.addAct("loadFigure:heart");
			ao.addAct("moveTo:0:0");
		}
		
		
		ao.sr.setProjectionMatrix(guiCam.combined);
		ao.draw();
		
		
		
		/*rotateFigure(aheart,50*Gdx.graphics.getDeltaTime());
		vheartv.clear();
		vheartv.addAll(aheart);
		reBox(vheartv);
		moveFig(vheart,vheartv,Gdx.graphics.getDeltaTime());
		drawFig(vheart);*/
		
		/*if (Gdx.input.isKeyJustPressed(Keys.E)) {
			mode++;
			afig.clear();
			vfig.clear();
			vfigv.clear();
			if (mode%2==0) {
				afig.addAll(box);
				vfig.addAll(vBox);
				vfigv.addAll(vBoxv);
			}
			if (mode%2==1) {
				afig.addAll(aheart);
				vfig.addAll(vheart);
				vfigv.addAll(vheartv);
			}
		}
		if (Gdx.input.isKeyJustPressed(Keys.Q)) {
			mode2++;
			circle.clear();
			for (int i = 0; i < 36; i++) {
				float alpha = (float)Math.PI/180*((360/36)*i - 3f);
				float x = (float)Math.cos(alpha)*r;
				float y = (float)Math.sin(alpha)*r;
				circle.add(new Vector2(x, y));
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			afig.clear();
			vfig.clear();
			vfigv.clear();
			afig.addAll(box);
			vfig.addAll(vBox);
			vfigv.addAll(vBoxv);
		}
		if (Gdx.input.isKeyJustPressed(Keys.D)) {
			drawLine = true;
		}
		vfigv.clear();
		vfigv.addAll(afig);
		reBox(vfigv);
		if (mode2%4==2) {
			rotateFigure(afig, 50*Gdx.graphics.getDeltaTime());
			moveFig(vfig,vfigv,Gdx.graphics.getDeltaTime());
		}
		if (mode2%4==1) {
			rotateCircle(-10*Gdx.graphics.getDeltaTime());
		}
		if (mode2%4==3) {
			rotateFigure(afig, 50*Gdx.graphics.getDeltaTime());
			moveFig(vfig,vfigv,Gdx.graphics.getDeltaTime());
			rotateCircle(-40*Gdx.graphics.getDeltaTime());
		}
		drawFig(vfig);*/
		
		/*sr.setColor(0.5f, 0.5f, 0.75f, 1.f);
		sr.begin(ShapeType.Line);
		for (int i = 0; i < vBox.size; i++)
			sr.line(circle.get(i), vBox.get(i));
		sr.end();
		
		
		rotateBox(50*Gdx.graphics.getDeltaTime());
		vBoxv.clear();
		vBoxv.addAll(box);
		reBox(vBoxv);
		moveBox(Gdx.graphics.getDeltaTime());*/
		//rotateCircle(Gdx.graphics.getDeltaTime());
	}
}