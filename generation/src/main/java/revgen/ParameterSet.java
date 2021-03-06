package revgen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * A set of parameters to run the simulation with
 */
public class ParameterSet {

    public long id = 0; // database id
    public long run_id; // corresponding run id
    public double theta_s; // proportion of cases female
    public double[] theta_i; // proportion of [0] male cases or [1] female cases with implant of interest
    public double[][] alpha; // weibull alpha for [sex][implant]
    public double[][] beta; // weibull beta for [sex][implant]
    public double study_length; // Length of study. Not currently used
    public int numCases; // Number of cases per simulation

    // Insert the parameter set into the database
    public void insert_db(Connection con) {

        // Unprepared SQL query
        String query = "insert into parameter_sets values (default,default,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setLong(1, run_id);
            stmt.setDouble(2, theta_s);
            stmt.setDouble(3, theta_i[0]);
            stmt.setDouble(4, theta_i[1]);
            stmt.setDouble(5, alpha[0][0]);
            stmt.setDouble(6, alpha[0][1]);
            stmt.setDouble(7, alpha[1][0]);
            stmt.setDouble(8, alpha[1][1]);
            stmt.setDouble(9, beta[0][0]);
            stmt.setDouble(10, beta[0][1]);
            stmt.setDouble(11, beta[1][0]);
            stmt.setDouble(12, beta[1][1]);
            stmt.setDouble(13, study_length);
            stmt.setInt(14, numCases);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println("1: " + e);
        }
    }

    public ParameterSet(long crun_id, double ctheta_s, double ctheta_i0, double ctheta_i1, 
            double calpha00, double calpha01, double calpha10, double calpha11,
            double cbeta00, double cbeta01, double cbeta10, double cbeta11, 
            double cstudy_length, int cnumCases) {

        run_id = crun_id;
        theta_s = ctheta_s;
        theta_i = new double[] {ctheta_i0, ctheta_i1};
        alpha = new double[][] {{calpha00,calpha01},{calpha10,calpha11}};
        beta = new double[][] {{cbeta00,cbeta01},{cbeta10,cbeta11}};
        study_length = cstudy_length;
        numCases = cnumCases;
    }

}
