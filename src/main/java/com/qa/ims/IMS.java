package com.qa.ims;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.controller.Action;
import com.qa.ims.controller.CrudController;
import com.qa.ims.controller.CustomerAction;
import com.qa.ims.controller.CustomerController;
import com.qa.ims.controller.ItemController;
import com.qa.ims.controller.OrderController;
import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.dao.OrderItemDAO;
import com.qa.ims.persistence.domain.Access;
import com.qa.ims.persistence.domain.Domain;
import com.qa.ims.utils.DBUtils;
import com.qa.ims.utils.PrintUtils;
import com.qa.ims.utils.Utils;

public class IMS {

	public static final Logger LOGGER = LogManager.getLogger();

	private final CustomerController customers;
	private final ItemController items;
	private final OrderController orders; 
	private final Utils utils;

	public IMS() {
		this.utils = new Utils();
		final CustomerDAO custDAO = new CustomerDAO();
		final ItemDAO itemDAO = new ItemDAO();
		final OrderDAO orderDAO = new OrderDAO();
		final OrderItemDAO orderitemDAO = new OrderItemDAO();
		this.customers = new CustomerController(custDAO, utils);
		this.items = new ItemController(itemDAO, utils);
		this.orders = new OrderController(custDAO, orderDAO, itemDAO, orderitemDAO, utils);
	}
	
	public void imsSystem() {
		LOGGER.info("Welcome to the Inventory Management System!");
		PrintUtils.printDottedLine();
		DBUtils db = DBUtils.connect("initialdb.properties");
		db.init("src/main/resources/sql-schema.sql");
		db.init("src/main/resources/sql-data.sql");
		DBUtils.connect();
		Access access = null;
		do {
			LOGGER.info("Which access level would you like to use?");
			PrintUtils.printLine();
			Access.printAccess();
			access = Access.getAccess(utils);
			accessAction(access);
		} while (access != Access.EXIT);
	} 

	public void imsAdministrator() {
		Domain domain = null;
		do {
			LOGGER.info("Which entity would you like to use?");
			PrintUtils.printLine();
			Domain.printDomains();
			domain = Domain.getDomain(utils);
			domainAction(domain);
		} while (domain != Domain.RETURN);
	}

	private void accessAction(Access access) {
		switch (access) {
		case CUSTOMER:
			customerDomainAction();
			break;
		case ADMINISTRATOR:
			String password = restrictAccess(utils);
			if(password.equals("root")) {
				imsAdministrator();
			}else {
				LOGGER.info("PASSWORD INCORRECT");
			}
			break;
		case EXIT:
			return;
		default:
			break;
		}
	}
	
	private void customerDomainAction() {
		boolean changeDomain = false;
		do {
			LOGGER.info("Welcome to the customers' menu. Please choose a selection:");
			PrintUtils.printDottedLine();
			CustomerAction.printActions();
			CustomerAction customerAction = CustomerAction.getAction(utils);
			if (customerAction == CustomerAction.RETURN) {
				changeDomain = true;
			} else {
				doCustomerAction(customerAction);
			}
		} while (!changeDomain);
	}

	private void domainAction(Domain domain) {
		boolean changeDomain = false;
		LOGGER.info(domain);
		do {

			CrudController<?> active = null;
			switch (domain) {
			case CUSTOMER:
				active = this.customers;
				break;
			case ITEM:
				active = this.items;
				break;
			case ORDER:
				active = this.orders;
				break;
			case RETURN:
				return;
			default:
				break;
			}

			LOGGER.info(() -> "What would you like to do with " + domain.name().toLowerCase() + ":");
			PrintUtils.printLine();
			Action.printActions(domain);
			Action action = Action.getAction(utils);

			if (action == Action.RETURN) {
				changeDomain = true;
			} else {
				doAction(active, action);
			}
		} while (!changeDomain);
	}

	public void doCustomerAction(CustomerAction customerAction) {
		switch (customerAction) {
		case A:
			customers.update();
			break;
		case B:
			items.readAll();
			break;
		case C:
			LOGGER.info("Please enter a customer ID");
			Long customer_id = utils.getLong();
			orders.read(customer_id);
			break;
		case D:
			orders.create();
			break;
		case E:
			orders.update();
			break;
		case F:
			orders.delete();
			break;
		case RETURN:
			break;
		default:
			break;
		}
	}
	
	public String restrictAccess(Utils utils) {
		LOGGER.info("ADMINISTRATOR ACCESS RESTRICTED");
		PrintUtils.printDottedLine();
		LOGGER.info("Please enter password:");
		return utils.getString();
	}
	
	public void doAction(CrudController<?> crudController, Action action) {
		switch (action) {
		case CREATE:
			crudController.create();
			break;
		case READ:
			crudController.readAll();
			break;
		case UPDATE:
			crudController.update();
			break;
		case DELETE:
			crudController.delete();
			break;
		case RETURN:
			break;
		default:
			break;
		}
	}

	public Utils getUtils() {
		return this.utils;
	}
}
