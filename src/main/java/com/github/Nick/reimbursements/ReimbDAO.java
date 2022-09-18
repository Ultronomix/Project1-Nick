package com.github.Nick.reimbursements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.Nick.common.datasource.ConnectionFactory;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;

public class ReimbDAO {

    private static Logger logger = LogManager.getLogger(ReimbDAO.class);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final String select = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, " +
                                "er.description, er.payment_id, er.author_id, er.resolver_id, " +
                                "ers.status, ert.type " +
                                "FROM ers_reimbursements er " +
                                "JOIN ers_reimbursement_statuses ers ON er.status_id = ers.status_id " +
                                "JOIN ers_reimbursement_types ert ON er.type_id = ert.type_id ";

    public List<Reimb> getAllReimb () {

        logger.info("Getting all reimbursement at {}", LocalDateTime.now().format(format));
        List<Reimb> allreimbs = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);

            allreimbs = mapResultSet(rs);
            
            return allreimbs;

        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public Optional<Reimb> getReimbById (String id) {

        logger.info("Getting Reimbursements by author id at {}", LocalDateTime.now().format(format));
        String sqlId = select + "WHERE er.author_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlId);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();
            
        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public Optional<Reimb> getReimbByReimbId (String reimbid) {

        logger.info("Getting Reimbursement by reimbursement id");
        String sqlId = select + "WHERE er.reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlId);
            pstmt.setString(1, reimbid);
            ResultSet rs = pstmt.executeQuery();

            
            return mapResultSet(rs).stream().findFirst();
            
        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public List<Reimb> getReimbByStatus (String status) {
        
        logger.info("Getting reimbursement by status at {}", LocalDateTime.now().format(format));
        String sqlStatus = select + "WHERE ers.status = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            
            PreparedStatement pstmt = conn.prepareStatement(sqlStatus);
            pstmt.setString(1, status.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs);
            
        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public List<Reimb> getReimbByUserID (String userId) {

        logger.info("Getting all reimbursement by user id at {}", LocalDateTime.now().format(format));
        String sqlId = select + "WHERE er.author_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlId);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs);
         
        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }
    
    public List<Reimb> getReimbByType (String type) {

        logger.info("Getting all reimbursement type at {}", LocalDateTime.now().format(format));
        String sqlType = select + "WHERE ert.type = ?";
        List<Reimb> reimbsType = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            
            PreparedStatement pstmt = conn.prepareStatement(sqlType);
            pstmt.setString(1, type.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            reimbsType = mapResultSet(rs);

            return reimbsType;
            
        } catch (Exception e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public String updateRequestStatus (String status, String reimb_id, String resolver_id) {

        logger.info("Updating request status at {}", LocalDateTime.now().format(format));
        String updateSql = "UPDATE ers_reimbursements SET status_id = ?, resolved = ?, resolver_id = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, status);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, resolver_id);
            pstmt.setString(4, reimb_id);
            
            pstmt.executeUpdate();

            return "Updated status";
            
        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }

    }

    public String updateUserAmount (String reimbId, double newAmount) {

        logger.info("Updating request amount at {}", LocalDateTime.now().format(format));
        String updateAmountSql = "UPDATE ers_reimbursements SET amount = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateAmountSql);
            pstmt.setDouble(1, newAmount);
            pstmt.setString(2, reimbId);
            
            pstmt.executeUpdate();

            return "Amount ";

        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public String updateUserDescription (String reimbId, String description) {

        logger.info("Updating request description at {}", LocalDateTime.now().format(format));
        String updateAmountSql = "UPDATE ers_reimbursements SET description = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateAmountSql);
            pstmt.setString(1, description);
            pstmt.setString(2, reimbId);
            System.out.println(pstmt);
            pstmt.executeUpdate();
            
            return "Description ";

        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }
    
    public String updateUserType (String reimbId, String type_id) {

        logger.info("Updating request type at {}", LocalDateTime.now().format(format));
        String updateAmountSql = "UPDATE ers_reimbursements SET type_id = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateAmountSql);
            pstmt.setString(1, type_id);
            pstmt.setString(2, reimbId);
            System.out.println(pstmt);
            pstmt.executeUpdate();
            
            return "Type ";
        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public boolean isPending (String reimbId) {

        logger.info("Checking if request is pending at {}", LocalDateTime.now().format(format));
        try {
            Optional<Reimb> reimb = getReimbByReimbId(reimbId);

            if (reimb.get().getStatus().equals("PENDING")) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            logger.warn("Request not found at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new ResourceNotFoundException();
        }
    }

    public String newRequest (Reimb reimb, String user_id) {

        logger.info("Creating a new request at {}", LocalDateTime.now().format(format));
        String requestSql = "INSERT INTO ers_reimbursements " +
                            "(reimb_id, amount, submitted, description, payment_id, author_id, status_id, type_id) " +
                            "VALUES " +
                            "(?, ?, current_date, ?, ?, ?, 100002, ?)";
                            
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement ptsmt = conn.prepareStatement(requestSql);
            ptsmt.setString(1, reimb.getReimb_id());
            ptsmt.setDouble(2, reimb.getAmount());
            ptsmt.setString(3, reimb.getDescription());
            ptsmt.setString(4, reimb.getPayment_id());
            ptsmt.setString(5, user_id);
            ptsmt.setString(6, reimb.getType());

            ptsmt.executeUpdate();

            return "Request Created ID: " + reimb.getReimb_id();
            
        } catch (SQLException e) {
            logger.warn("DataSourceException at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
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
