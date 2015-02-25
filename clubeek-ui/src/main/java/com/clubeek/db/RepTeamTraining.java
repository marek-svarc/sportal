package com.clubeek.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.TeamTraining;

public class RepTeamTraining implements Repository<TeamTraining> {

	/** Nazev tabulky */
	public static final String tableName = "team_training";

	/** Identifikatory sloupcu tabulky */
	public enum TableColumn {
		ID("id"), START("start"), END("end"), PLACE("place"), CLUB_TEAM_ID("club_team_id");

		private TableColumn(String dbColumnName) {
			this.name = dbColumnName;
		}

		@Override
		public String toString() {
			return name;
		}

		public final String name;
	}

	public static RepTeamTraining getInstance() {
		return trainingDb;
	}

	// DML Insert

	/**
	 * Vlozi a inicializuje radek v tabulce "training".
	 * 
	 * @throws SQLException
	 */
	public static int insert(TeamTraining training) throws SQLException {
		return insert(training.getStart(), training.getEnd(), training.getPlace(), training.getClubTeamId());
	}

	/**
	 * Vlozi a inicializuje radek v tabulce "training".
	 * 
	 * @throws SQLException
	 */
	public static int insert(Date start, Date end, String place, int clubTeamId) throws SQLException {
		// sestaveni sql prikazu
		String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES ( ?, ?, ?, ?)", tableName, TableColumn.START.name,
				TableColumn.END.name, TableColumn.PLACE.name, TableColumn.CLUB_TEAM_ID.name);
		// provedeni transakce
		return Admin.update(sql, new ColumnData[] { new ColumnData(start, false), new ColumnData(end, false),
				new ColumnData(place), new ColumnData(clubTeamId) }, true);
	}

	// DML Update

	/**
	 * Modifikuje radek v tabulce "training"
	 * 
	 * @throws SQLException
	 */
	public static void update(TeamTraining training) throws SQLException {
		update(training.getId(), training.getStart(), training.getEnd(), training.getPlace(), training.getClubTeamId());
	}

	/**
	 * Modifikuje radek v tabulce "training"
	 * 
	 * @throws SQLException
	 */
	public static void update(int id, Date start, Date end, String place, int clubTeamId) throws SQLException {
		// sestaveni sql prikazu
		String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = %d", tableName,
				TableColumn.START.name, TableColumn.END.name, TableColumn.PLACE.name, TableColumn.CLUB_TEAM_ID.name,
				TableColumn.ID.name, id);
		// provedeni transakce
		Admin.update(sql, new ColumnData[] { new ColumnData(start, false), new ColumnData(end, false), new ColumnData(place),
				new ColumnData(clubTeamId) });
	}

	// DML delete

	/**
	 * Maze radek urceny primarnim klicem v tabulce "training"
	 * 
	 * @throws SQLException
	 */
	public static void delete(int id) throws SQLException {
		Admin.delete(tableName, TableColumn.ID.name, id);
	}

	// SQL Select

	/**
	 * Vraci radky z tabulky "training" dle prislusnosti k tymu pouzitim klice
	 * club_team_id
	 * 
	 * @throws SQLException
	 */
	public static List<TeamTraining> selectByClubTeamId(int clubTeamId, TableColumn[] columns) throws SQLException {
		columns = getColumns(columns);
		return Admin.query(TeamTraining.class, String.format("SELECT %s FROM %s WHERE %s = %d ORDER BY %s DESC",
				Admin.createSelectParams(columns), tableName, TableColumn.CLUB_TEAM_ID.name, clubTeamId, TableColumn.START.name),
				columns, RepTeamTraining.getInstance());
	}

	// implementation of Repository<Training>

	@Override
	public void updateRow(TeamTraining value) throws SQLException {
		update(value);
	}

	@Override
	public void insertRow(TeamTraining value) throws SQLException {
		insert(value);
	}

	@Override
	public void deleteRow(int id) throws SQLException {
		delete(id);
	}

	@Override
	public void readValue(ResultSet result, int resultsColumnId, TeamTraining data, Object dataColumnId) throws SQLException {
		switch ((RepTeamTraining.TableColumn) dataColumnId) {
		case ID:
			data.setId(result.getInt(resultsColumnId));
			break;
		case START:
			data.setStart(new Date(result.getTimestamp(resultsColumnId).getTime()));
			break;
		case END:
			data.setEnd(new Date(result.getTimestamp(resultsColumnId).getTime()));
			break;
		case PLACE:
			data.setPlace(result.getString(resultsColumnId));
			break;
		case CLUB_TEAM_ID:
			data.setClubTeamId(result.getInt(resultsColumnId));
			break;
		}
	}

	/* PRIVATE */

	private RepTeamTraining() {
	}

	private static RepTeamTraining.TableColumn[] getColumns(RepTeamTraining.TableColumn[] columns) {
		return columns != null ? columns : TableColumn.values();
	}

	private static RepTeamTraining trainingDb = new RepTeamTraining();

}
