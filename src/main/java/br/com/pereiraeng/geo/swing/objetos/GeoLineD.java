package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.geo.objetos.GeoLine;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public abstract class GeoLineD extends GeoLine implements GeoD {

	public GeoLineD(GeoCoordinate c1, GeoCoordinate c2) {
		super(c1, c2);
	}

	// ------------------------------- DRAWER -------------------------------

	protected transient WL wl;

	@Override
	public void setWL(WL wl) {
		this.wl = wl;
	}

	@Override
	public void drawObject(Graphics2D g) {
		drawGeo(g, -1, -1);
	}

	@Override
	public void drawGeo(Graphics2D g, int x, int y) {
		g.setColor(getColor());
		Point p1 = LeafOM.getTranformedPoint((float) c1.getX(), (float) c1.getY(), wl);
		Point p2 = LeafOM.getTranformedPoint((float) c2.getX(), (float) c2.getY(), wl);
		g.drawLine(p1.x, p1.y, p2.x, p2.y);

		if (c1 instanceof GeoPointD) {
			GeoPointD g1 = (GeoPointD) c1;
			g1.setWL(wl);
			g1.drawObject(g);
		}

		if (c2 instanceof GeoPointD) {
			GeoPointD g1 = (GeoPointD) c2;
			g1.setWL(wl);
			g1.drawObject(g);
		}
	}

	protected abstract Color getColor();

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public boolean wasDrawn() {
		return true;
	}

	@Override
	public void setDrawable(boolean drawable) {
	}
}
