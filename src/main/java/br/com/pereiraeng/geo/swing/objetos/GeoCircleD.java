package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Graphics2D;
import java.awt.Point;

import br.com.pereiraeng.geo.objetos.GeoCircle;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public class GeoCircleD extends GeoCircle implements GeoD {
	private static final long serialVersionUID = -5963164234463752964L;

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

	@Override
	public void drawGeo(Graphics2D g, int x, int y) {
		Point gr = LeafOM.getTranformedPoint(0f, 0f, (float) this.getRadius(), 0f, this.wl);
		g.drawOval(x - gr.x, y - gr.x, 2 * gr.x, 2 * gr.x);
	}
}