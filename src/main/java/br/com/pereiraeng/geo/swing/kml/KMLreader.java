package br.com.pereiraeng.geo.swing.kml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import br.com.pereiraeng.geo.Brasil.Estado;
import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.geo.objetos.Geo;
import br.com.pereiraeng.geo.objetos.GeoMark;
import br.com.pereiraeng.geo.objetos.GeoStringMark;
import br.com.pereiraeng.geo.objetos.GeoValue;
import br.com.pereiraeng.geo.swing.obj.Curso;
import br.com.pereiraeng.geo.swing.obj.Frontier;
import br.com.pereiraeng.geo.swing.obj.Rio;
import br.com.pereiraeng.geo.swing.obj.Town;
import br.com.pereiraeng.geo.swing.obj.TrechoVia;
import br.com.pereiraeng.geo.swing.obj.Via;
import br.com.pereiraeng.geo.swing.objetos.GeoD;
import br.com.pereiraeng.geo.swing.objetos.GeoMultiStringD;
import br.com.pereiraeng.geo.swing.objetos.GeoValueD;
import br.com.pereiraeng.io.IOutils;
import br.com.pereiraeng.xml.XMLadapter;

public class KMLreader extends XMLadapter {

	public enum TypeGeo {
		FRONTIER, WAY, RIVER, TOWN, VALUE;
	}

	private TypeGeo type;

	private boolean regiao = false;

	private List<GeoD> geos;

	private int freq = 1;

	public void setType(TypeGeo type) {
		this.type = type;
	}

	public void setRegiao(boolean regiao) {
		this.regiao = regiao;
	}

	public List<GeoD> getGeos() {
		return geos;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	/**
	 * <p>
	 * Função que faz a fusão de objetos geográficos da lista quando isso for
	 * possível.
	 * </p>
	 * 
	 * <p>
	 * Como exemplo de objetos que podem ser reunidos, tem-se:
	 * </p>
	 * 
	 * <ul>
	 * <li>{@link TrechoVia trechos} podem ser agrupados em {@link Via vias};</i>
	 * <li>{@link Curso cursos} podem ser agrupados em {@link Rio rios}.</i>
	 * </ul>
	 */
	public void merge() {
		List<GeoD> newList = new LinkedList<>();
		HashMap<String, Integer> name2position = new HashMap<>();
		int position = 0;
		for (GeoD g : geos) {
			String name = null;
			if (g instanceof Curso) {
				Curso c = (Curso) g;
				name = c.getNome();
				c.setNome(null);
			} else if (g instanceof TrechoVia) {
				TrechoVia t = (TrechoVia) g;
				name = t.getSigla();
				t.setSigla(null);
			}

			if (!"".equals(name)) {
				Integer pos = name2position.get(name);

				GeoMultiStringD<?> geoMultiString = null;
				if (pos != null) {
					// já existe na tabela
					geoMultiString = (GeoMultiStringD<?>) newList.get(pos);

					if (g instanceof Curso) {
						Curso c = (Curso) g;
						Rio r = (Rio) geoMultiString;
						r.add(c);
						geoMultiString = r;
					} else if (g instanceof TrechoVia) {
						TrechoVia t = (TrechoVia) g;
						Via v = (Via) geoMultiString;
						v.add(t);
						geoMultiString = v;
					}
				} else {
					// não existe
					if (g instanceof Curso) {
						Curso c = (Curso) g;
						Rio r = new Rio(name);
						r.add(c);
						geoMultiString = r;
					} else if (g instanceof TrechoVia) {
						TrechoVia t = (TrechoVia) g;
						Via v = new Via(name);
						v.add(t);
						geoMultiString = v;
					}

					newList.add(geoMultiString);
					name2position.put(name, position++);
				}
			}
		}
		this.geos = newList;
	}

	// --------------------------------------------------------------------

	/**
	 * Função que carrega as cidades de um ou mais estados brasileiros
	 * 
	 * @param folder diretório com os arquivos geográficos
	 * @param states par de letras que designa o estado (ver {@link Estado})
	 * @return lista de cidades
	 */
	public List<Town> getTowns(String folder, String... states) {
		if (states.length == 0)
			return null;

		setType(TypeGeo.TOWN);

		List<Town> out = new LinkedList<>();
		for (int i = 0; i < states.length; i++) {
			// para cada estado
			parse(String.format("%s\\%2$s\\sede_%2$s.kml", folder, states[i]));
			List<GeoD> temp = getGeos();
			for (GeoD g : temp)
				out.add((Town) g);
		}

		return out;
	}

	public List<GeoValue> getValues(String filename) {
		setType(TypeGeo.VALUE);

		parse(filename);
		List<GeoD> temp = getGeos();

		// cast e repassar para o conjunto
		List<GeoValue> out = new ArrayList<>(temp.size());
		for (GeoD g : temp)
			out.add((GeoValue) g);
		return out;
	}

	public List<GeoValue> getValues(InputStream is) {
		setType(TypeGeo.VALUE);

		parse(is);
		List<GeoD> temp = getGeos();

		// cast e repassar para o conjunto
		List<GeoValue> out = new ArrayList<>(temp.size());
		for (GeoD g : temp)
			out.add((GeoValue) g);
		return out;
	}

	// =============================== LEITURA ===============================

	private transient int counter = 0;

	private transient GeoD geo;

	private transient String data;

	private transient boolean pointFlag = false, polylineFlag = false;
	private transient boolean coordFlag = false, idFlag = false, descFlag = false, nameFlag = false;

	private transient Map<String, String> extendedData;
	private transient String key;

	@Override
	public void startDocument() throws SAXException {
		this.geos = new LinkedList<>();
	}

	@Override
	public void startElement(String qName, Attributes atts) {
		switch (qName) {
		case "Placemark":
			switch (type) {
			case TOWN:
				this.geo = new Town();
				break;
			case VALUE:
				this.geo = new GeoValueD();
				break;
			case FRONTIER:
				this.geo = new Frontier();
				break;
			case RIVER:
				this.geo = new Curso();
				break;
			case WAY:
				this.geo = new TrechoVia();
				break;
			}

			// se o ID estiver como atributo do campo raiz do lugar
			String id = atts.getValue("id");
			if (id != null)
				this.geo.setId(id);
			break;
		case "Polygon":
		case "LineString": // segmentos de reta ou polígono
			this.polylineFlag = true;
			break;
		case "Point": // ponto
			this.pointFlag = true;
			break;
		case "SimpleData": // informações suplementares do KML
			String dataName = atts.getValue("name");

			switch (dataName) {
			case "MESOREGIAO":
			case "MICROREGIA":
				if (regiao)
					this.nameFlag = true;
				break;
			case "NOME":
			case "SIGLA":
				this.nameFlag = true;
				break;
			case "ID":
			case "FID":
			case "GEOCODIGO":
				this.idFlag = true;
				break;
			case "CORPODAGUA":
			case "NOME_RODOV":
				this.descFlag = true;
				break;
			}
			break;
		case "name": // campo do nome explícito
			this.nameFlag = true;
			break;
		case "coordinates": // coordenada
			this.coordFlag = true;
			break;
		case "description": // descrição
			this.descFlag = true;
			break;
		case "ExtendedData":
			extendedData = new HashMap<>();
			break;
		case "Data":
			key = atts.getValue("name");
			break;
		}
	}

	@Override
	public void characters(String s) {
		if (nameFlag || idFlag || descFlag || coordFlag || key != null)
			this.data = s;
	}

	@Override
	public void endElement(String qName) {
		switch (qName) {
		case "Placemark":
			if (polylineFlag) // segmentos de reta ou polígono
				this.polylineFlag = false;
			else if (pointFlag) // ponto
				this.pointFlag = false;

			// coloca o objeto na tabela de dispersão e libera a memória
			this.geos.add(this.geo);
			this.geo = null;
			break;
		case "SimpleData": // informações suplementares do KML
			if (this.nameFlag) {
				if (this.geo instanceof GeoMark)
					((GeoMark) this.geo).setName(this.data);
				else if (this.geo instanceof GeoStringMark)
					((GeoStringMark) this.geo).setName(this.data);
				else if (this.geo instanceof Curso)
					((Curso) this.geo).setNome(this.data);
				else if (this.geo instanceof TrechoVia)
					((TrechoVia) this.geo).setSigla(this.data);
				this.nameFlag = false;
			} else if (this.idFlag) {
				this.geo.setId(this.data);
				this.idFlag = false;
			} else if (this.descFlag) {
				if (this.geo instanceof Curso)
					((Curso) this.geo).setCorpo(this.data);
				else if (this.geo instanceof TrechoVia)
					((TrechoVia) this.geo).setNome(this.data);
				this.descFlag = false;
			}
			this.data = null;
			break;
		case "name": // campo do nome explícito
			if (this.geo instanceof GeoMark)
				((GeoMark) this.geo).setName(this.data);
			else if (this.geo instanceof GeoStringMark)
				((GeoStringMark) this.geo).setName(this.data);
			this.data = null;
			this.nameFlag = false;
			break;
		case "description": // campo descrição
			if (this.geo instanceof GeoMark)
				((GeoMark) this.geo).setDescription(this.data);
			else if (this.geo instanceof GeoStringMark)
				((GeoStringMark) this.geo).setDescription(this.data);
			this.data = null;
			this.descFlag = false;
			break;
		case "coordinates": // coordenada
			this.counter = parseCoordinate(this.data, this.geo, this.polylineFlag, this.freq, this.counter);
			this.data = null;
			this.coordFlag = false;
			break;
		case "value":
			extendedData.put(key, this.data);
			break;
		case "Data":
			key = null;
			break;
		case "ExtendedData":
			if (this.geo instanceof GeoValue)
				((GeoValue) this.geo).setValue(extendedData);
			extendedData = null;
			break;
		}
	}

	@Override
	public void endDocument() throws SAXException {
	}

	// ============================= AUXILIARES ==============================

	public static void parseCoordinate(String coordinates, GeoD geo) {
		parseCoordinate(coordinates, geo, false, -1, -1);
	}

	public static int parseCoordinate(String coordinates, Geo geo, boolean polylineFlag, int freq, int counter) {
		String[] c = coordinates.replaceAll("[\t\n\f\r]+", ",").split(",");
		if (c.length % 3 == 0) { // se houver trincas, 'parseia'
			for (int i = 0; i < c.length; i += 3) {
				if (polylineFlag && freq != 1 ? counter++ % freq == 0 : true) {
					GeoCoordinate cd = new GeoCoordinate(Float.parseFloat(c[i]), Float.parseFloat(c[i + 1]));
					if (!"0".equals(c[i + 2]))
						cd.setAltitude(Float.parseFloat(c[i + 2]));
					geo.addCoordinate(cd);
				}
			}
		} else
			throw new IllegalArgumentException("As coordenadas deveriam vir em trincas.");
		return counter;
	}

	private static final Pattern COORDS = Pattern.compile("<coordinates>(\\d|\\p{Punct})+?</coordinates>"),
			NAME = Pattern.compile("<name>.+?</name>");

	/**
	 * Função que converte um arquivo KML em um arquivo de texto. A estrutura é que
	 * em cada linha há o nome do objeto seguido das coordenadas
	 * 
	 * @param pathFileKml caminho do arquivo KML
	 * @param pathFileTxt caminho do arquivo TXT
	 */
	public static void kml2txt(String pathFileKml, String pathFileTxt) {
		String txt = "";
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(pathFileKml)), "UTF8"));

			// baixa o arquivo primeiro
			String s, kml = "";
			while ((s = br.readLine()) != null)
				kml += s;

			// começo da região
			Matcher start = Pattern.compile("<Placemark>").matcher(kml);
			while (start.find()) {
				// fim da região
				Matcher end = Pattern.compile("</Placemark>").matcher(kml);
				end.region(start.start(), kml.length());
				end.find();

				// nome
				Matcher nomeM = NAME.matcher(kml);
				nomeM.region(start.start(), end.end());
				nomeM.find();
				String nome = nomeM.group();

				// coordenada
				Matcher coorM = COORDS.matcher(kml);
				coorM.region(start.start(), end.end());
				coorM.find();
				String[] coor = coorM.group().split(",");

				txt += nome.substring(6, nome.length() - 7) + "\t" + coor[0].substring(13) + "\t" + coor[1] + "\n";
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		IOutils.writeFile(pathFileTxt, txt);
	}
}
