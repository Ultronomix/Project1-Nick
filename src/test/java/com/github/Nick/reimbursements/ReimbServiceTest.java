package com.github.Nick.reimbursements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;

public class ReimbServiceTest {

    ReimbService sut;
    ReimbDAO mockReimbDAO;

    @BeforeEach
    public void setUp() {
        mockReimbDAO = Mockito.mock(ReimbDAO.class);
        sut = new ReimbService(mockReimbDAO);
    }

    @AfterEach
    public void cleanUp () {
        Mockito.reset(mockReimbDAO);
    }

    @Test
    public void testGetAllReimb() {

        Reimb reimb1 = new Reimb();
        reimb1.setReimb_id("1");
        reimb1.setAmount(300);
        reimb1.setSubmitted("time");
        reimb1.setResolved("resolved");
        reimb1.setDescription("description");
        reimb1.setPayment_id("payment_id");
        reimb1.setAuthor_id("author_id");
        reimb1.setResolver_id("resolver_id");
        reimb1.setStatus("status");
        reimb1.setType("type");

        Reimb reimb2 = new Reimb();
        reimb2.setReimb_id("2");
        reimb2.setAmount(300);
        reimb2.setSubmitted("time");
        reimb2.setResolved("resolved");
        reimb2.setDescription("description");
        reimb2.setPayment_id("payment_id");
        reimb2.setAuthor_id("author_id");
        reimb2.setResolver_id("resolver_id");
        reimb2.setStatus("status");
        reimb2.setType("type");

        List<ReimbResponse> results = new ArrayList<>();
        List<Reimb> reimbs = new ArrayList<>();

        reimbs.add(reimb1);
        reimbs.add(reimb2);
        
        
        for (Reimb reimb : reimbs) {
            results.add(new ReimbResponse(reimb));
        }

        when(mockReimbDAO.getAllReimb()).thenReturn(reimbs);

        List<ReimbResponse> expected = results;

        List<ReimbResponse> actual = sut.getAllReimb();
        
        assertNotNull(results);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetReimbById() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbById("");
        });

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbById(null);
        });

        Reimb reimb = new Reimb();
        when(mockReimbDAO.getReimbById(anyString())).thenReturn(Optional.of(reimb));

        ReimbResponse actual = sut.getReimbById("2");
        ReimbResponse expected = new ReimbResponse(reimb);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetReimbByUserId() {

        Reimb reimb1 = new Reimb();
        reimb1.setReimb_id("1");
        reimb1.setAmount(300);
        reimb1.setSubmitted("time");
        reimb1.setResolved("resolved");
        reimb1.setDescription("description");
        reimb1.setPayment_id("payment_id");
        reimb1.setAuthor_id("author_id");
        reimb1.setResolver_id("resolver_id");
        reimb1.setStatus("status");
        reimb1.setType("type");

        List<ReimbResponse> results = new ArrayList<>();
        List<Reimb> reimbs = new ArrayList<>();

        reimbs.add(reimb1);

        for (Reimb reimb : reimbs) {
            results.add(new ReimbResponse(reimb));
        }

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbByUserId("2");
        });

        when(mockReimbDAO.getReimbByUserID("1")).thenReturn(reimbs);

        List<ReimbResponse> actual = sut.getReimbByUserId("1");
        List<ReimbResponse> expected = results;

        assertEquals(expected, actual);
    }

    @Test
    public void testGetReimbByStatus() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByStatus(null);
        });

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByStatus("status");
        });

        List<ReimbResponse> result = new ArrayList<>();
        List<Reimb> reimbs = new ArrayList<>();

        Reimb reimb1 = new Reimb();
        reimb1.setReimb_id("1");
        reimb1.setAmount(300);
        reimb1.setSubmitted("time");
        reimb1.setResolved("resolved");
        reimb1.setDescription("description");
        reimb1.setPayment_id("payment_id");
        reimb1.setAuthor_id("author_id");
        reimb1.setResolver_id("resolver_id");
        reimb1.setStatus("PENDING");
        reimb1.setType("type");

        reimbs.add(reimb1);

        for (Reimb reimb : reimbs) {
            result.add(new ReimbResponse(reimb));
        }

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbByStatus("Approved");
        });

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbByStatus("Denied");
        });

        when(mockReimbDAO.getReimbByStatus(anyString())).thenReturn(reimbs);

        List<ReimbResponse> actual = sut.getReimbByStatus("Pending");
        List<ReimbResponse> expected = result;

        assertEquals(expected, actual);
    }

    @Test
    public void testGetReimbByType() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByType(null);
        });

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByType("type");
        });

        List<ReimbResponse> result = new ArrayList<>();
        List<Reimb> reimbs = new ArrayList<>();

        Reimb reimb1 = new Reimb();
        reimb1.setReimb_id("1");
        reimb1.setAmount(300);
        reimb1.setSubmitted("time");
        reimb1.setResolved("resolved");
        reimb1.setDescription("description");
        reimb1.setPayment_id("payment_id");
        reimb1.setAuthor_id("author_id");
        reimb1.setResolver_id("resolver_id");
        reimb1.setStatus("PENDING");
        reimb1.setType("FOOD");

        reimbs.add(reimb1);

        for (Reimb reimb : reimbs) {
            result.add(new ReimbResponse(reimb));
        }

        when(mockReimbDAO.getReimbByType(anyString())).thenReturn(reimbs);

        List<ReimbResponse> actualF = sut.getReimbByType("Food");
        List<ReimbResponse> expectedF = result;

        assertEquals(expectedF, actualF);

        List<ReimbResponse> actualL = sut.getReimbByType("Lodging");
        List<ReimbResponse> expectedL = result;

        assertEquals(expectedL, actualL);

        List<ReimbResponse> actualT = sut.getReimbByType("Travel");
        List<ReimbResponse> expectedT = result;

        assertEquals(expectedT, actualT);

        List<ReimbResponse> actualO = sut.getReimbByType("Other");
        List<ReimbResponse> expectedO = result;

        assertEquals(expectedO, actualO);
    }
}
