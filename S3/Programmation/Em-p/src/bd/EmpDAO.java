package db;

import java.sql.*;
import java.util.Vector;

public class EmpDAO {

    // Méthode qui retourne toutes les lignes de EMP sous forme de Vector<Vector<Object>>
    public static Vector<Vector<Object>> getAllEmployees() throws SQLException {
        String sql = "SELECT empno, ename, job, sal, deptno FROM emp";
        Vector<Vector<Object>> data = new Vector<>();

        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("empno"));
                row.add(rs.getString("ename"));
                row.add(rs.getString("job"));
                row.add(rs.getDouble("sal"));
                row.add(rs.getInt("deptno"));
                data.add(row);
            }
        }
        return data;
    }

    // Méthode pour récupérer les noms de colonnes
    public static Vector<String> getColumnNames() {
        Vector<String> colNames = new Vector<>();
        colNames.add("EMPNO");
        colNames.add("ENAME");
        colNames.add("JOB");
        colNames.add("SAL");
        colNames.add("DEPTNO");
        return colNames;
    }
}
