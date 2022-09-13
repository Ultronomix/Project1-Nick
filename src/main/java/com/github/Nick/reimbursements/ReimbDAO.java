package com.github.Nick.reimbursements;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.Nick.common.datasource.ConnectionFactory;

public class ReimbDAO {

    private final String select = "SELECT * FROM ers_reimbursements";

    public List<Reimb> getAllReimb () {

        List<Reimb> allreimbs = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = ((java.sql.Statement) stmt).executeQuery(select);

            allreimbs = mapResultSet(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
        }

        return allreimbs;
    }
    

    private List<Reimb> mapResultSet(ResultSet rs) throws SQLException {

        List<Reimb> reimbs = new ArrayList<>();

        while (rs.next()) {
            Reimb reimb = new Reimb();
            reimb.setReimb_id(rs.getString("reimb_id"));
            reimb.setAmount(rs.getInt("amount"));
            reimb.setSubmitted(rs.getString("submitted"));
            reimb.setResolved(rs.getString("resolved"));
            reimb.setDescription(rs.getString("description"));
            reimb.setPayment_id(rs.getString("payment_id"));
            reimb.setAuthor_id("author_id");
            reimb.setResolver_id("resolver_id");
            reimb.setStatus_id("status_id");
            reimb.setType_id("type_id");
            reimbs.add(reimb);
        }

        return reimbs;
    }
}
