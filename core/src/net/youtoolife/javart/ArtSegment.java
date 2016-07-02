package net.youtoolife.javart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class ArtSegment {
	
	ArtObject ao = null;
	ShapeRenderer sr = null;

	public Color cl1 = new Color(1.f, 1.f, 1.f, 1.f);
	public Color cl2 = new Color(1.f, 1.f, 1.f, 1.f);
	
	public Color cl21 = new Color(1.f, 1.f, 1.f, 1.f);
	public Color cl22 = new Color(1.f, 1.f, 1.f, 1.f);
	
	public Vector2 pos = new Vector2(0,0);
	
	public int type = 0;
	public int index = 0;
	public float radius = 0;
	public boolean filled = false;
	public boolean drawLine = true;
	
	public boolean remove = false;
	
	
	public ArtSegment(ArtObject ao) {
		this.ao = ao;
		this.sr = ao.sr;
		radius = ao.size;
	}
	
	public ArtSegment(int index, ArtObject ao) {
		this(ao);
		this.index = index;
	}
	
	private void move(float delta) {
		
		if (ao.fig.size != ao.vecSeg.size)
			return;
		
		if (!ao.anim)
			return;
		
		if (!ao.move)
			return;
		
		if (!ao.multySeg) {
			if (ao.curSeg != index)
				return;
		}
		
		drawLine = ao.drawLine;
		filled = ao.filled;
		cl1 = ao.cl1;
		cl21 = ao.cl2;
		type = ao.type;
		
		float speed = ao.speed;
		Vector2 vec = ao.vecSeg.get(index);
		
		if (Math.abs(vec.x-pos.x) < 3.3f && Math.abs(vec.y-pos.y) < 3.3f) {
			if (Math.abs(vec.x-pos.x) > 1.f || Math.abs(vec.y-pos.y) > 1.f) {
				speed = 1;
			}
			if (ao.lineAct)
				if ((index+1) != ao.fig.size) {
					ao.curSeg++;
					if (ao.curSeg >= ao.fig.size)
						ao.curSeg = 0;
					System.out.println("run:"+
					ao.curSeg+"|"+(ao.actions.size>0?ao.actions.first():"null"));
					if (ao.actions.size == 0)
						ao.lineAct = false;
				}
				else
					if (ao.runAction)
						ao.runAction = false;
		}
		if (Math.abs(vec.x-pos.x) > 1.f || Math.abs(vec.y-pos.y) > 1.f) {
			if (vec.x > pos.x)
				pos.x += speed*delta;
			else
				pos.x -= speed*delta;
			
			if (vec.y > pos.y)
				pos.y += speed*delta;
			else
				pos.y -= speed*delta;
		}
		if (Math.abs(vec.x-pos.x) <= 1.f || Math.abs(vec.y-pos.y) <= 1.f) {
			if (remove)
				ao.fig.removeValue(this, false);
		}
		
	}
	
	public void update(float delta) {
		move(delta);
	}
	
	
	public void draw() {
		
		update(Gdx.graphics.getDeltaTime());
		
		float x = ao.pos.x, y = ao.pos.y;
		
		
		if (filled) {
			cl1.set(Color.RED);
			cl1.a = 0.15f;
			sr.setColor(cl1);
			sr.begin(ShapeType.Filled);
			if (type == 0) {
				sr.circle(x+pos.x, y+pos.y, radius);
				sr.circle(x+pos.x-1.5f, y+pos.y+1.5f, radius);
				sr.circle(x+pos.x+1.5f, y+pos.y+1.5f, radius);
				sr.circle(x+pos.x-1.5f, y+pos.y-1.5f, radius);
				sr.circle(x+pos.x+1.5f, y+pos.y-1.5f, radius);
			}
			else
				sr.rect(x+pos.x, y+pos.y, radius, radius);
			sr.end();
		}
		if (filled) {
			cl21.set(Color.WHITE);
			cl21.a = 0.75f;
			sr.setColor(cl21);
			sr.begin(ShapeType.Filled);
			if (type == 0) {
				sr.circle(x+pos.x, y+pos.y, radius);
				/*sr.circle(x+pos.x-1.f, y+pos.y+1.f, radius);
				sr.circle(x+pos.x+1.f, y+pos.y+1.f, radius);
				sr.circle(x+pos.x-1.f, y+pos.y-1.f, radius);
				sr.circle(x+pos.x+1.f, y+pos.y-1.f, radius);*/
			}
			else
				sr.rect(x+pos.x, y+pos.y, radius, radius);
			sr.end();
		}
		
		if (drawLine) {
			cl1.a = 0.1f;
			sr.setColor(cl1);
			sr.begin(ShapeType.Line);
			if (type == 0){
				sr.circle(x+pos.x, y+pos.y, radius);
				sr.circle(x+pos.x-1.f, y+pos.y+1.f, radius);
				sr.circle(x+pos.x+1.f, y+pos.y+1.f, radius);
				sr.circle(x+pos.x-1.f, y+pos.y-1.f, radius);
				sr.circle(x+pos.x+1.f, y+pos.y-1.f, radius);
			}
			else
				sr.rect(x+pos.x, y+pos.y, radius, radius);
			sr.end();
		}
		
	}
}
