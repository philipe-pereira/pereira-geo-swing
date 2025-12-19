package br.com.pereiraeng.geo.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;

import br.com.pereiraeng.geo.swing.objetos.GeoD;
import br.com.pereiraeng.geo.objetos.Geo;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.Click;
import br.com.pereiraeng.swing.interfaces.WL;

/**
 * Painel sobre o qual serão desenhado um mapa, como diferentes objetos
 * geográficos.
 * 
 * @author Philipe Pereira
 * 
 */
public class GeoMap<K> extends LeafOM<GeoD> {
	private static final long serialVersionUID = 1L;

	private LinkedHashMap<K, GeoD> geos;
	private GeoGrid grid;

	/**
	 * Construtor do objeto gráfico que desenha mapas
	 * 
	 * @param background cor de fundo
	 * @param width      comprimento, em pixels
	 * @param height     comprimento, em pixels
	 * @param grid       grade com indição das posições
	 */
	public GeoMap(Color background, int width, int height, GeoGrid grid) {
		super(background, width, height, true, true, true, true);
		this.grid = grid;
		this.geos = new LinkedHashMap<K, GeoD>();
	}

	// -------------- MÉTODOS DE DESENHO --------------

	@Override
	protected void drawBackground(Graphics2D g, WL wl) {
		if (grid != null)
			grid.drawGrid(g, this);
	}

	@Override
	protected void drawForeground(Graphics2D g, WL wl) {
	}

	@Override
	protected float getMinDx() {
		return 0.001f;
	}

	@Override
	protected float getMaxDx() {
		return 180f;
	}

	@Override
	protected boolean isDragable() {
		return false;
	}

	@Override
	protected void setDragPostion(Click c, int x, int y) {
	}

	// -------------- MÉTODOS DE INTERFACEAMENTO --------------

	@Override
	public Collection<GeoD> getList() {
		return this.geos.values();
	}

	public GeoD get(K key) {
		return this.geos.get(key);
	}

	public int getCount() {
		return this.geos.size();
	}

	/**
	 * Associates the specified value with the specified key in this map (optional
	 * operation). If the map previously contained a mapping for the key, the old
	 * value is replaced by the specified value. (A map m is said to contain a
	 * mapping for a key k if and only if <code>m.containsKey(k)</code> would return
	 * <code>true</code>.)
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param geo value to be associated with the specified key
	 */
	public void put(K key, GeoD geo) {
		put(key, geo, true);
	}

	/**
	 * Copies all of the mappings from the specified map to this map (optional
	 * operation). The effect of this call is equivalent to that of calling
	 * {@link #put(String, Geo)} on this map once for each mapping from key k to
	 * value v in the specified map. The behavior of this operation is undefined if
	 * the specified map is modified while the operation is in progress.
	 * 
	 * @param geos mappings to be stored in this map
	 * 
	 */
	public void putAll(Map<? extends K, ? extends GeoD> geos) {
		for (Entry<? extends K, ? extends GeoD> e : geos.entrySet())
			this.put(e.getKey(), e.getValue(), false);
		repaint();
	}

	private void put(K key, GeoD geo, boolean repaint) {
		this.geos.put(key, geo);
		calculateView(geo);
		if (repaint)
			repaint();
	}

	/**
	 * Removes all of the mappings from this map (optional operation).The map will
	 * be empty after this call returns.
	 */
	public void clear() {
		this.geos.clear();
	}

	public Iterator<Entry<K, GeoD>> getIterator() {
		return this.geos.entrySet().iterator();
	}
}
