# ws-rs-explorer

Jakarta RESTful Web Services Explorer.  
A generic and dynamic endpoint for basic **CRUD** *(Create, Read, Update, Delete)* operation.  

## Showcase

For example, you want an endpoint for a `Customer` entity.  
This project provide full *CRUD* operation.  
So, you have dynamically this endpoint:

~~~
GET     <your-path>/entity/customer
GET     <your-path>/entity/customser/{id}
POST    <your-path>/entity/customer
PUT     <your-path>/entity/customer/{id}
DELETE  <your-path>/entity/customer/{id}
~~~

### Filter

~~~
GET     <your-path>/entity/customer
~~~

This endpoint provides an advance filter query with multiple operator.  
It's following this principe:  

~~~
attributName[operator]=value
~~~

* The attributName should be present in **DTO** *(Data Transfer Object)* class.
* The logical operator should be in bracket.
  * See `com.github.ws.rs.explorer.persistence.Operator` for more details.
* The result is a pagination **JSON** *(JavaScrip Object Notation)*.

Example:
~~~
GET     <your-path>/entity/customer?email[li]=linux.org&familyName[eq]=MINT
Content-Type: application/json

# Result:
{
  "data": [...],
  "size": 42,
  "pageSize": 10,
  "pageNumber": 1,
  "pageCount, 5
}
~~~

The `data` attribut contains *JSON* objects based on the query parameters.  
The result is the *JSON* object of the entity.  

### Find

~~~
GET     <your-path>/entity/customer/{id}
Content-Type: application/json

# Result:
{
 ...
}
~~~

This endpoint provides a simple find by unique identifier.
By default, the identifier should be a **UUID** *(Universal Unique IDentifier)*.

### Create

~~~
POST    <your-path>/entity/customer
Content-Type: application/json

# Body:
{
 ...
}

# Result:
201: Created /entity/{id}
~~~

This endpoint provides a simple create operation.
The unique identifier attribut don't need to be defined.
The *JSON* object of the entity is in the body of the request.  
The result contains the location of the new resource.  

### Update

~~~
PUT    <your-path>/entity/customer/{id}
Content-Type: application/json

# Body:
{
 ...
}

# Result:
204: No content
~~~

This endpoint provides a simple update operation.
The unique identifier should be defined in the path parameter.  
The *JSON* object of the entity is in the body of the request.
The result contains nothing.  

### Delete

~~~
DELETE    <your-path>/entity/customer/{id}
Content-Type: application/json

# Result:
204: No content
~~~

This endpoint provides a simple delete operation.
The unique identifier should be defined in the path parameter.
The result contains nothing.

## Setup

Example for a customer entry point: `Customer`.
You need a *Jakarta 9.1* web project.  
See `ws-rs-exeplorer-example` for more details.

### Step 1: Create your table in database

Create your table.
See `src/main/resources/derby-init.sql`.

### Step 2: Model your class

Create your entity class using **JPA** *(Java Persistence API)*
See `com.github.ws.rs.explorer.example.customer.CustomerEntity`.

Create your data class using **JSON-B** *(JSON Binding)*
See `com.github.ws.rs.explorer.example.customer.CustomerDTO`.

Create your mapper class between entity and data class.
See `com.github.ws.rs.explorer.example.customer.CustomerMapper`.

> Simplify writing Java beans with [Lombok](https://projectlombok.org/).
> You can generate mappers using [Mapstruct](https://mapstruct.org/).

### Step 3: Register your entry point

Register your classes for publish your entry point.
See `com.github.ws.rs.explorer.example.StartUp`.

___

> This project is writing in **Java 17** with **Jakarta 9.1**.
> It uses **Glassfish 6.X** as standard implementation.
> Build with **Maven 3.8.X**.

