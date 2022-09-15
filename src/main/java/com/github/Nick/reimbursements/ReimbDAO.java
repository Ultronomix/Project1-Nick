package com.github.Nick.reimbursements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.github.Nick.common.datasource.ConnectionFactory;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;

public class ReimbDAO {

    private final String select = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, " +
                                "er.description, er.payment_id, er.author_id, er.resolver_id, " +
                                "ers.status, ert.type " +
                                "FROM ers_reimbursements er " +
                                "JOIN ers_reimbursement_statuses ers ON er.status_id = ers.status_id " +
                                "JOIN ers_reimbursement_types ert ON er.type_id = ert.type_id ";

    public List<Reimb> getAllReimb () {

        List<Reimb> allreimbs = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);

            allreimbs = mapResultSet(rs);
            
            return allreimbs;

        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }

    }

    public Optional<Reimb> getReimbById (String id) {

        // TODO add log
        String sqlId = select + "WHERE er.author_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlId);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();
            // TODO add log
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }

    }

    public Optional<Reimb> getReimbByReimbId (String reimbid) {

        // TODO add log
        String sqlId = select + "WHERE er.reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlId);
            pstmt.setString(1, reimbid);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();
            // TODO add log
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }

    }

    public List<Reimb> getReimbByStatus (String status) {
        
        // TODO add log
        String sqlStatus = select + "WHERE ers.status = ?";
        List<Reimb> reimbsStatus = new  ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            
            PreparedStatement pstmt = conn.prepareStatement(sqlStatus);
            pstmt.setString(1, status.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            reimbsStatus = mapResultSet(rs);

            return reimbsStatus;
            // TODO add log
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }
    }
    
    public List<Reimb> getReimbByType (String type) {

        // TODO add log
        String sqlType = select + "WHERE ert.type = ?";
        List<Reimb> reimbsType = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            
            PreparedStatement pstmt = conn.prepareStatement(sqlType);
            pstmt.setString(1, type.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            reimbsType = mapResultSet(rs);

            return reimbsType;
            // TODO add log
        } catch (Exception e) {
            // TODO add log
            throw new DataSourceException(e);
        }
    }

    public String updateRequestStatus (String status, String reimb_id, String resolver_id) {

        //TODO add log
        String updateSql = "UPDATE ers_reimbursements SET status_id = ?, resolved = ?, resolver_id = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, status);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, resolver_id);
            pstmt.setString(4, reimb_id);
            // ResultSet rs =
            pstmt.executeUpdate();
            return "Updated status";
            //TODO add log
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }

    }

    public String updateUserAmount (String reimbId, double newAmount) {

        // TODO add log
        String updateAmountSql = "UPDATE ers_reimbursements SET amount = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateAmountSql);
            pstmt.setDouble(1, newAmount);
            pstmt.setString(2, reimbId);
            
            pstmt.executeUpdate();

            return "Amount ";
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }
    }

    public String updateUserDescription (String reimbId, String description) {

        // TODO add log
        String updateAmountSql = "UPDATE ers_reimbursements SET description = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateAmountSql);
            pstmt.setString(1, description);
            pstmt.setString(2, reimbId);
            System.out.println(pstmt);
            pstmt.executeUpdate();
            
            return "Description ";
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }
    }
    
    public String updateUserType (String reimbId, String type_id) {

        // TODO add log
        String updateAmountSql = "UPDATE ers_reimbursements SET type_id = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateAmountSql);
            pstmt.setString(1, type_id);
            pstmt.setString(2, reimbId);
            System.out.println(pstmt);
            pstmt.executeUpdate();
            
            return "Type ";
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }
    }

    public boolean isPending (String reimbId) {

        try {
            Optional<Reimb> reimb = getReimbByReimbId(reimbId);

            if (reimb.get().getStatus().equals("PENDING")) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            // TODO add log
            throw new ResourceNotFoundException();
        }
    }

    private List<Reimb> mapResultSet(ResultSet rs) throws SQLException {

        List<Reimb> reimbs = new ArrayList<>();

        while (rs.next()) {
            Reimb reimb = new Reimb();
            reimb.setReimb_id(rs.getString("reimb_id"));
            reimb.setAmount(rs.getDouble("amount"));
            reimb.setSubmitted(rs.getString("submitted"));
            reimb.setResolved(rs.getString("resolved"));
            reimb.setDescription(rs.getString("description"));
            reimb.setPayment_id(rs.getString("payment_id"));
            reimb.setAuthor_id(rs.getString("author_id"));
            reimb.setResolver_id(rs.getString("resolver_id"));
            reimb.setStatus(rs.getString("status"));
            reimb.setType(rs.getString("type"));
            reimbs.add(reimb);
        }

        return reimbs;
    }
}
