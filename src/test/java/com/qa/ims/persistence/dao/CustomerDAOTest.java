package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.utils.DBUtils;

public class CustomerDAOTest {

	private final CustomerDAO DAO = new CustomerDAO();

	@BeforeClass
	public static void setup() {
		DBUtils.connect();
		DBUtils.getInstance().init("src/test/resources/sql-schema.sql");
		DBUtils.getInstance().init("src/test/resources/sql-data.sql");
	} 

	@Test
	public void testCreate() {
		final Customer created = new Customer(2L, "chris", "perrins");
		assertEquals(created, DAO.create(created));
	}

	@Test
	public void testReadAll() {
		List<Customer> expected = new ArrayList<>();
		expected.add(new Customer(1L, "jordan", "harrison"));
		assertEquals(expected, DAO.readAll());
	}

	@Test
	public void testReadLatest() {
		assertEquals(new Customer(1L, "jordan", "harrison"), DAO.readLatest());
	}

	@Test
	public void testRead() {
		final long ID = 1L;
		assertEquals(new Customer(ID, "jordan", "harrison"), DAO.read(ID));
	}
	
	@Test
	public void testUpdate() {
		final Customer updated = new Customer(2L, "chris", "perrins");
		assertEquals(updated, DAO.update(updated));
	}
	
	@Test
	public void testUpdateInvalid() {
		final Customer updated = new Customer(50L, "chris", "perrins");
		assertEquals(null, DAO.update(updated));
	}
	
	@Test
	public void testDelete() {
		assertEquals(1, DAO.delete(1));
	}
	@Test
	public void testDeleteInvalid() {
		assertEquals(0, DAO.delete(-2000));
	}
	@AfterClass
	public static void deleteAll() {
		DBUtils.getInstance().init("src/test/resources/sql-dropall.sql");
	}
}