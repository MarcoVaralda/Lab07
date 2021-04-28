package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<PowerOutage> getPowerOutages(Nerc nerc, int maxOre) {
		
		String sql = "SELECT p.id, p.date_event_began, p.date_event_finished, p.customers_affected, (UNIX_TIMESTAMP(p.date_event_finished)-UNIX_TIMESTAMP(p.date_event_began))/3600 AS diff "
				+ "FROM poweroutages p "
				+ "WHERE p.nerc_id=? AND (UNIX_TIMESTAMP(p.date_event_finished)-UNIX_TIMESTAMP(p.date_event_began))/3600<=? "
				+ "ORDER BY p.date_event_began ASC";
		
		List<PowerOutage> elenco = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			st.setInt(2, maxOre);
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				PowerOutage p = new PowerOutage(res.getInt("id"),res.getTimestamp("date_event_began").toLocalDateTime(),res.getTimestamp("date_event_finished").toLocalDateTime(),res.getInt("customers_affected"));				
			    p.setDifferenza(res.getInt("diff"));
				elenco.add(p);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException("Errore nella lettura dei PowerOutages",e);
		}
		
		return elenco;
	}

}
