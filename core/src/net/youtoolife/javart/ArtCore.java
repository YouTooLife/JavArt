package net.youtoolife.javart;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ArtCore {
	
	public static byte[][] heart = {
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
	
	public static Array<Vector2> createFigure(byte[][] mfig, float size, float tab) {
		Array<Vector2> fig = new Array<Vector2>();
		for (int i = 0; i < mfig.length; i++) {
			for (int j = 0; j < mfig[0].length; j++) {
				if (mfig[i][j] == 1) {
					fig.add(new Vector2((tab+size*2)*j
							- (tab+size*2)*mfig[0].length/2, 
							-(tab+size*2)*i + (size*2+tab)*mfig.length/2));
				}
			}
		}
		return fig;
	}
	
	public static Array<Vector2> getBox(float a, int count) {
		Array<Vector2> box = new Array<Vector2>();
		for (int i = 0; i < count; i++) {
			float p = (float)Math.sqrt((a)*(a)*2);
			float t = p + 4*p/(count-1)*i;
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
		return box;
	}

}
