package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;

import boundary.DataField;
import entity.ReportMensile;

public class ReportMensileDAO extends ReportMensile
{

	public ReportMensileDAO(int corsiTotali, int sessioniOnlineTotali, int sessioniPraticheTotali,
			int minimoSessioniPratiche, int massimoSessioniPratiche, int mediaSessioniPratiche) 
	{
		super(corsiTotali, sessioniOnlineTotali, sessioniPraticheTotali, minimoSessioniPratiche, massimoSessioniPratiche,
				mediaSessioniPratiche);
		// TODO Auto-generated constructor stub
	}

	public static ReportMensile getReportMensile(String idChef) throws SQLException
	{
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		int maxDay = DataField.getMaxMonthDay(currentYear, currentMonth);
		
		String query = "select * from \"reportCorsiChef\"(?, ?, ?)";
		PreparedStatement st = dataBase.prepareStatement(query);
		st.setString(1, idChef);
		st.setDate(2, Date.valueOf(LocalDate.of(currentYear, currentMonth, 1))) ;
		st.setDate(3, Date.valueOf(LocalDate.of(currentYear, currentMonth, maxDay))) ;
		ResultSet rs = st.executeQuery();
		
		if(rs.next() == false)
			return null;
		
		if(rs.getString(4) == null)
			return new ReportMensile(
				Integer.parseInt(rs.getString(1)),
				Integer.parseInt(rs.getString(2)),
				Integer.parseInt(rs.getString(3)), 0, 0 ,0
			); 
		
		
		return new ReportMensile(
			Integer.parseInt(rs.getString(1)),
			Integer.parseInt(rs.getString(2)),
			Integer.parseInt(rs.getString(3)),
			Integer.parseInt(rs.getString(4)),
			Integer.parseInt(rs.getString(5)),
			Integer.parseInt(rs.getString(6))
		);
	}
}
