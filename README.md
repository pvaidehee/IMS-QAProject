Coverage: 34%
# Inventory Management System (IMS)

An IMS that an end user can interact with via cmd Interface. The application supports coustomer, item and order entities. Functionality is included for costomer-level usability and an administator.

## Getting Started

Clone this Repo (it's easier that way, without any issues)

### Prerequisites

Up-to-date version of Java installed on end-user's machine.

Ensure the ```db.properties``` file located at ```src/main/resources``` matches the correct url and password for the desired database instance that is to be interacted with. ```ims``` should be appended to the end of the url.

eg. ```jdbc:mysql://localhost:3306/ims```

```initialdb.properties``` should contain the exact same url but without the name of the database to be created, this is to ensure the correct database is created if it does not already exist.

eg. ```jdbc:mysql://localhost:3306/```

Changing the database instance will mean the application will need to be repackaged with the Maven command ```mvn clean package```.


### Running the system

From the command line navigate to the root folder of the cloned git repository.

Next, run the following command:

```
java -jar ims-0.0.1-jar-with-dependencies.jar
```

Once the system is running from the Command-Line Interface an end-user will be prompted to choose their access level.

An administrator is able to access CRUD functionality for all 3 entities. The password for the administrator access level is root.

A customer is able to choose from a list of options:

A. Change their customer information.

B. View all available items.

C. View their orders.

D. Create an order.

E. Update one of their orders.

F. Delete an order.


## Testing

### Unit Tests 

Unit testing is done through JUnit and Mockito.

The system is broadly based on the Data-Access-Object (DAO) Pattern, a structural pattern used to isolate the business layer from the persistence layer. This model has the general form of:

* Controller
* Service
* Data Access </br>

Unit testing is done in accordance with this model. So we have three general groups of unit tests:

* Controlllers
* Domain
* DAO </br>

The interactions between groups are mocked using Mockito.

An example of a unit test for the Order Controller method:

```
@Mock
private OrderDAO orderDAO;
	
@InjectMocks
private OrderController orderController;

@Test
public void testReadAll() {
	List<Order> orders = new ArrayList<>();
	orders.add(new Order(1L, 1L, 25.73, "03/07/21"));
	Mockito.when(orderDAO.readAll()).thenReturn(orders);

	assertEquals(orders, this.orderController.readAll());

	Mockito.verify(this.orderDAO, Mockito.times(1)).readAll();
}
```

### And coding style tests

Static code analysis is conducted through Sonarqube.

To run the static code analysis, install Sonarqube, then navigate to the src folder and run the following command:

```
mvn sonar:sonar - Dsonar.host.url=http://localhost:9000 -Dsonar.login=admin - Dsonar.password=admin
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Chris Perrins** - *Initial work* - [christophperrins](https://github.com/christophperrins)

## License

This project is licensed under the MIT license - see the [LICENSE.md](LICENSE.md) file for details 

*For help in [Choosing a license](https://choosealicense.com/)*

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
