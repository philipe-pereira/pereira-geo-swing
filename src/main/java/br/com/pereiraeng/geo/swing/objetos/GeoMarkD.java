package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Graphics2D;
import java.awt.Point;

import br.com.pereiraeng.geo.objetos.GeoMark;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public abstract class GeoMarkD extends GeoMark implements GeoD {
	private static final long serialVersionUID = -3408102939921833895L;

	public GeoMarkD(double longitude, double latitude) {
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
