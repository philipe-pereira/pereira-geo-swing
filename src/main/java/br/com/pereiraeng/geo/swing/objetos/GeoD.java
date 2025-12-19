package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Graphics2D;

import br.com.pereiraeng.swing.interfaces.DesM;

/**
 * Interface dos objetos geográficos desenháveis sobre um painel
 */
public interface GeoD extends DesM {

	public void drawGeo(Graphics2D g, int x, int y);
}
