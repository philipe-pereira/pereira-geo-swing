package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Graphics2D;
import java.awt.Point;

import br.com.pereiraeng.geo.objetos.GeoPoint;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public abstract class GeoPointD extends GeoPoint implements GeoD {
	private static final long serialVersionUID = 4678647322426476798L;

	public GeoPointD(double longitude, double latitude) {
		super(longitude, latitude);
	}

	protected transient WL wl;

	@Override
	public void setWL(WL wl) {
		this.wl = wl;
	}

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public void setDrawable(boolean drawable) {
	}

	@Override
	public boolean wasDrawn() {
		return true;
	}

	@Override
	public void drawObject(Graphics2D g) {
		Point p = LeafOM.getTranformedPoint(x, y, wl);
		drawGeo(g, p.x, p.y);
	}
}
