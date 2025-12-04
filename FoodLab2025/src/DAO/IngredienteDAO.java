package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Ingrediente;

public class IngredienteDAO extends Ingrediente 
{
	public IngredienteDAO(int idRicetta, String nome, int quantita) {
		super(idRicetta, nome, quantita);
		// TODO Auto-generated constructor stub
	}

	public static boolean insertIngrediente(Ingrediente ingrediente) throws SQLException
	{
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		String query = "INSERT INTO \"Ingrediente\" (\"ID_Ricetta\", \"NomeIngrediente\", \"Quantita\") values (?, ?, ?)";
		PreparedStatement st = dataBase.prepareStatement(query);
		st.setInt(1, ingrediente.getIdRicetta());
		st.setString(2, ingrediente.getNome());
		st.setInt(3, ingrediente.getQuantita());
		
		int rowsAffected = st.executeUpdate();
		st.close();
		dataBase.close();
		
		return rowsAffected > 0;
	}
}
