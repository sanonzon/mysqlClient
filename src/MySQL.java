import java.sql.*;
import java.util.ArrayList;

/**
 * Created by kaffefe on 2016-01-01.
 */
public class MySQL {
    /**
     * Insert all the info into database.
     * From the
     */
    public String fixSytnax(String s) {
        if (s.trim().equals("PRI")) {
            return "PRIMARY KEY";
        } else if (s.trim().equals("NO")) {
            return "";
        } else
            return s;

    }

    public ArrayList<Object> getDatabaseList(String url, String username, String password) throws SQLException {
        ArrayList<Object> arrayList = new ArrayList<>();
        Connection c = null;

        c = DriverManager.getConnection(url, username, password);

        DatabaseMetaData meta = c.getMetaData();
        ResultSet rs = meta.getCatalogs();
        while (rs.next()) {
            String db = rs.getString("TABLE_CAT");
            arrayList.add(db);
        }

        c.close();


        return arrayList;
    }

    public ArrayList<Object> getTables(String url, String username, String password) throws SQLException {
        ArrayList<Object> arrayList = new ArrayList<>();

        Connection c = null;
        Statement stmt = null;
        c = DriverManager.getConnection(url, username, password);
        c.setAutoCommit(false);


        DatabaseMetaData md = c.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            arrayList.add(rs.getObject(3));
        }
        // Commit changes
        c.commit();

        // Close all the connections

        c.close();

        return arrayList;
    }

    public ArrayList<ArrayList<Object>> getSchema(String tableName, String url, String username, String password) throws SQLException {
        ArrayList<ArrayList<Object>> result = new ArrayList<>();

        Connection c = null;
        Statement stmt = null;
        c = DriverManager.getConnection(url, username, password);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "DESCRIBE " + tableName;


        ResultSet resultSet = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            ArrayList<Object> temp = new ArrayList<>();
            for (int i = 1; i < columnsNumber; i++) {
                if (resultSet.getObject(i) != null) {
                    // System.out.println(resultSet.getObject(i));
                    // FIX ABBREVIATIONS TO PROPER SQL SYNTAX
                    String s1 = fixSytnax(resultSet.getObject(i).toString());
                    temp.add(s1);
                }

            }
            result.add(temp);

        }


        // Commit changes
        c.commit();

        // Close all the connections
        stmt.close();
        c.close();
        return result;
    }


    public ArrayList<ArrayList<Object>> Select(String tableName, String url, String username, String password) throws SQLException {
        Connection c = null;
        Statement stmt = null;

        c = DriverManager.getConnection(url, username, password);
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = "SELECT * FROM " + tableName; // PRE-loop of column names
        ResultSet rs = stmt.executeQuery(sql);

        ArrayList<ArrayList<Object>> fetched = new ArrayList<>();
        /**
         *  Saves collected data to temporary ArrayList and adds that ArrayList to the collection.
         *  Returns collection on completion
         */
        while (rs.next()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            if (fetched.size() == 0) {
                // Get column names
                ArrayList<Object> temp = new ArrayList<>();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    temp.add(rsmd.getColumnName(i));
                }

                fetched.add(temp);
            }

            ArrayList<Object> temp = new ArrayList<>();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                temp.add(rs.getObject(i));
            }

            fetched.add(temp);
        }


        // Commit changes
        c.commit();

        // Close all the connections
        stmt.close();
        c.close();

        return fetched;
    }

}
