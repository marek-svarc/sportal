package com.clubeek.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.ClubRival;

public class RepClubRival implements Repository<ClubRival> {

	/** Nazev tabulky */
	public static final String tableName = "club_rival";

	/** Identifikatory sloupcu tabulky */
	public enum TableColumn {
		ID("id"), NAME("name"), WEB("web"), GPS("gps"), STREET("street"), CITY("city"), CODE("code"), ICON("icon");

		private TableColumn(String dbColumnName) {
			this.name = dbColumnName;
		}

		@Override
		public String toString() {
			return name;
		}

		public final String name;
	}

	public static RepClubRival getInstance() {
		return clubDb;
	}

	// DML insert

	/**
	 * Vlozi a inicializuje radek v tabulce "club".
	 * 
	 * @param club
	 *            data klubu, ktera budou zapsana do databaze
	 * @throws SQLException
	 */
	public static int insert(ClubRival club) throws SQLException {
		return insert(club.getName(), club.getWeb(), club.getGPS(), club.getStreet(), club.getCity(), club.getCode(),
				club.getIcon());
	}

	/**
	 * Vlozi a inicializuje radek v tabulce "club".
	 * 
	 * @throws SQLException
	 */
	public static int insert(String name, String web, String gps, String street, String city, String code, byte[] icon)
			throws SQLException {
		// sestaveni sql prikazu
		String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES ( ?, ?, ?, ?, ?, ?, ?)", tableName,
				TableColumn.NAME.name, TableColumn.WEB.name, TableColumn.GPS.name, TableColumn.STREET.name,
				TableColumn.CITY.name, TableColumn.CODE.name, TableColumn.ICON.name);
		// provedeni transakce
		return Admin.update(sql, new ColumnData[] { new ColumnData(name), new ColumnData(web), new ColumnData(gps),
				new ColumnData(street), new ColumnData(city), new ColumnData(code), new ColumnData(icon) }, true);
	}

	// DML Update

	/**
	 * Modifikuje radek v tabulce "club"
	 * 
	 * @param club
	 *            data klubu, ktera budou zapsana do databaze
	 * @throws SQLException
	 */
	public static void update(ClubRival club) throws SQLException {
		update(club.getId(), club.getName(), club.getWeb(), club.getGPS(), club.getStreet(), club.getCity(), club.getCode(),
				club.getIcon());
	}

	/**
	 * Modifikuje radek v tabulce "club"
	 * 
	 * @param id
	 *            index modifikovane radky tabulky
	 * @throws SQLException
	 */
	public static void update(int id, String name, String web, String gps, String street, String city, String code,
			byte[] small_icon) throws SQLException {
		// sestaveni sql prikazu
		String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = %d",
				tableName, TableColumn.NAME.name, TableColumn.WEB.name, TableColumn.GPS.name, TableColumn.STREET.name,
				TableColumn.CITY.name, TableColumn.CODE.name, TableColumn.ICON.name, TableColumn.ID.name, id);
		// provedeni transakce
		Admin.update(sql, new ColumnData[] { new ColumnData(name), new ColumnData(web), new ColumnData(gps),
				new ColumnData(street), new ColumnData(city), new ColumnData(code), new ColumnData(small_icon) });
	}

	// DML delete

	/**
	 * Maze radek urceny primarnim klicem v tabulce "club"
	 * 
	 * @param id
	 *            unikatni identifikator radky tabulky
	 * @throws SQLException
	 */
	public static void delete(int id) throws SQLException {
		Admin.delete(tableName, TableColumn.ID.name, id);
	}

	// SQL Select

	/**
	 * Vraci vsechny radky a vsechny sloupce tabulky "club"
	 * 
	 * @return seznam vsech radek tabulky
	 * @throws SQLException
	 */
	public static List<ClubRival> selectAll(TableColumn[] columns) throws SQLException {
		columns = getColumns(columns);
		return Admin.query(ClubRival.class, String.format("SELECT %s FROM %s ORDER BY %s ASC", Admin.createSelectParams(columns),
				tableName, TableColumn.NAME), columns, RepClubRival.getInstance());
	}

	/**
	 * Vraci klub z tabulky "club" dle primarniho klice
	 * 
	 * @return data clena klubu
	 * @throws SQLException
	 */
	public static ClubRival selectById(int id, TableColumn[] columns) throws SQLException {
		columns = getColumns(columns);
		List<ClubRival> clubList = Admin.query(ClubRival.class, String.format("SELECT %s FROM %s WHERE %s = %d ORDER BY %s ASC",
				Admin.createSelectParams(columns), tableName, TableColumn.ID, id, TableColumn.NAME), columns, getInstance());
		return (clubList != null) && (clubList.size() == 1) ? clubList.get(0) : null;
	}

	// interface Globals.SqlExtension<Club>

	@Override
	public void updateRow(ClubRival value) throws SQLException {
		update(value);
	}

	@Override
	public void insertRow(ClubRival value) throws SQLException {
		insert(value);
	}

	@Override
	public void deleteRow(int id) throws SQLException {
		delete(id);
	}

	@Override
	public void readValue(ResultSet result, int resultsColumnId, ClubRival data, Object dataColumnId) throws SQLException {
		switch ((RepClubRival.TableColumn) dataColumnId) {
		case ID:
			data.setId(result.getInt(resultsColumnId));
			break;
		case NAME:
			data.setName(result.getString(resultsColumnId));
			break;
		case WEB:
			data.setWeb(result.getString(resultsColumnId));
			break;
		case GPS:
			data.setGPS(result.getString(resultsColumnId));
			break;
		case STREET:
			data.setStreet(result.getString(resultsColumnId));
			break;
		case CITY:
			data.setCity(result.getString(resultsColumnId));
			break;
		case CODE:
			data.setCode(result.getString(resultsColumnId));
			break;
		case ICON:
			data.setIcon(result.getBytes(resultsColumnId));
			break;
		}
	}

	/* PRIVATE */

	private RepClubRival() {
	}

	private static RepClubRival.TableColumn[] getColumns(RepClubRival.TableColumn[] columns) {
		return columns != null ? columns : TableColumn.values();
	}

	private static RepClubRival clubDb = new RepClubRival();
}
