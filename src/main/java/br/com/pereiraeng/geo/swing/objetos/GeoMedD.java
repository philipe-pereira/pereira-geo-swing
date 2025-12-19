package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Graphics2D;
import java.awt.Point;

import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.geo.objetos.GeoMed;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public abstract class GeoMedD extends GeoMed implements GeoD {
	private static final long serialVersionUID = -5542276310368155133L;

	public GeoMedD(double longitude, double latitude) {
		super(longitude, latitude);
	}
	
	public GeoMedD(GeoCoordinate gc) {
		super(gc);
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
