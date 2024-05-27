# happi-explorer

**H**yper **A**pplication **P**owerful **P**rogramming **I**nterface - **E**xplorer
A generic and dynamic endpoint for basic **CRUD** *(Create, Read, Update, Delete)* operation.  

It provides Docker images for each runtime environment based on **GNU/Linux Alpine**.  

## Requirement

This project is written in **Java 21 LTS** with **Jakarta 10**.  
It builds with **Maven 3.8.X**.  

## Runtime environment

Available Java web server: 
  * [x] Eclipse Glassfish 7.X
    * Reference web server.
  * [x] Payara 6.2024.X
    * Fork of Glassfish.
  * [ ] Apache Tomcat 10.1.X
    * **WARNING** It is a prototype using extra dependencies from a Jakarta platform implementations. 
  * [ ] Apache TomEE 10.X
    * **WARNING** Work in progress.
  * [x] Wildfly 32.X
    * RedHat Community web server.

## Basic Maven commands

**Building the whole project.**  
~~~
cd happi-explorer-parent
mvn clean install -U -DskipITs -DskipITs
~~~

**Running integration tests.**  
~~~
cd happi-explorer-parent/runtime/<web-server-runtime>
mvn clean verify -U
~~~

**Running web server.**  
~~~
cd happi-explorer-parent/runtime/<web-server-runtime>
mvn mvn clean install -U -DskipITs -DskipITs cargo:run
~~~

## Showcase

For example, you want an endpoint for a `Customer` entity.  
This project provides full *CRUD* operation.  
So, you have dynamically this endpoint:  

~~~
GET     <your-path>/entity/customer
GET     <your-path>/entity/customser/{id}
POST    <your-path>/entity/customer
PUT     <your-path>/entity/customer/{id}
DELETE  <your-path>/entity/customer/{id}

OPTIONS <your-path>/entity/customer
OPTIONS <your-path>/entity/customer/{id}
~~~

### Filter

~~~
GET     <your-path>/entity/customer
~~~

This endpoint provides an advance filter query with a multiple operator.  
It's following this principe:  

~~~
attributName[operator]=value
~~~

* The attributName should be present in **DTO** *(Data Transfer Object)* and **Entity** classes.  
* The logical operator should be in bracket.  
  * See `Operator` enumeration for more details.  
  * Logical operator `OR` can be used with **pipe** in value (e.g. `familyName[eq]=MINT|DOE`)
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

The `data` attribute contains *JSON* objects based on the query parameters.  
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

This endpoint provides a simple created operation.  
The unique identifier attribute doesn't need to be defined.  
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

This endpoint provides a simple deleted operation.  
The unique identifier should be defined in the path parameter.  
The result contains nothing.  

## Setup

Example for a customer entry point: `Customer`.  
You need a *Jakarta 10* web project.  
See `happi-exeplorer-example` for more details.  

### Step 1: Create your table in a database

Create your database schema.  
See `src/main/resources/derby-init.sql`.  

### Step 2: Model your class

Create your entity class using **JPA** *(Java Persistence API)*  
See `CustomerEntity` class.  

Create your data class using **JSON-B** *(JSON Binding)*  
See `CustomerDTO` class.  

Create your mapper class between entity and data class.  
See `CustomerMapper` class.  

> **Optional :**  
> Simplify writing Java beans with [Lombok](https://projectlombok.org/).  
> You can generate mappers using [Mapstruct](https://mapstruct.org/).  

### Step 3: Register your entry point

Register your classes for publish your entry point.  
See `StartUp` class.  

~~~
@ApplicationScoped
public class StartUp {

    @Inject
    private ExplorerManager explorerManager;
    
    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {
        this.explorerManager.register(new DynamicEntry<>(
                "customer",
                Map.of(
                        Action.FILTER, HappiSecurityManager.PERMIT_ALL,
                        Action.FIND, HappiSecurityManager.PERMIT_ALL,
                        Action.CREATE, Roles.CUSTOMER_MANAGER,
                        Action.UPDATE, Roles.CUSTOMER_MANAGER,
                        Action.DELETE, Roles.CUSTOMER_MANAGER
                ),
                CustomerEntity.class,
                CustomerDTO.class,
                CustomerMapper.class,
                BasicExplorerService.class
        ));
    }
}
~~~

> **Note :**  
> Special roles :   
> `HappiSecurityManager.PUBLIC` : Full access, no authentication needed.  
> `HappiSecurityManager.PERMIT_ALL` : Full access for authenticated user, no role needed.  
> `HappiSecurityManager.DENY_ALL` : Unauthorized operation.  
> By default, if an action is not specified, the role is `HappiSecurityManager.DENY_ALL`.  
> **Semantic :** `role`, `group` and `permission` are synonym.  

## Extra features

### Manager endpoint

Enable manager endpoint feature in `StartUp` class :  

~~~
@ApplicationScoped
public class StartUp {

    @Inject
    private HappiSecurityManager securityManager;
    
    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {
      this.securityManager.putConfiguration(
                  HappiSecurityManager.Configuration.MANAGER_ENDPOINT, 
                  "true"
      );
    }
}
~~~

This endpoint will show all registered entries in `ExplorerManager` class.  

~~~
GET    <your-path>/manager/entry
Content-Type: application/json

# Result:
[
  {
    "path":"customer",
    "entity":"CustomerEntity",
    "data":"CustomerDTO",
    "mapper":"CustomerMapper",
    "service":"BasicExplorerService",
    "actions":[
      { "action":"UPDATE", "role":"customer-manager" },
      { "action":"DELETE", "role":"customer-manager" },
      { "action":"FIND",   "role":"@PermitAll" },
      { "action":"CREATE", "role":"customer-manager" },
      { "action":"FILTER", "role":"@PermitAll" }]
    }
]
~~~

Use `@DeclareRoles` annotation in your web configuration class:   

~~~
@DeclareRoles({ "customer-manager" })
@ApplicationPath("api")
public class WebConfiguration extends Application { }
~~~

And register this configuration class by `HappiSecurityManager` class in `StartUp` class:  

~~~
@ApplicationScoped
public class StartUp {

    @Inject
    private HappiSecurityManager securityManager;
    
    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {
      this.securityManager.scanRoleClassConfiguration(WebConfiguration.class);
    }
}
~~~

Now this endpoint will show all declared roles.

~~~
GET    <your-path>/manager/role
Content-Type: application/json

# Result:
[ "customer-manager" ]
~~~

## Discovery

Enable discovery endpoint feature in `StartUp` class :

~~~
@ApplicationScoped
public class StartUp {

    @Inject
    private HappiSecurityManager securityManager;
    
    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {
      this.securityManager.putConfiguration(
                  HappiSecurityManager.Configuration.DISCOVERY_ENDPOINT, 
                  "true"
      );
    }
}
~~~

Now this endpoint will show all declared data classes used by entry points.

~~~
GET    <your-path>/discovery
Content-Type: application/json

# Result:
[
  {
    "type": "CustomerDTO",
    "attributes": [
      {
        "name": "email",
        "type": "String",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.Email",
            "specifications": {
              "regexp": ".*",
              "flags": []
            }
          },
          {
            "name": "jakarta.validation.constraints.NotBlank",
            "specifications": {}
          },
          {
            "name": "jakarta.validation.constraints.Size",
            "specifications": {
              "min": 0,
              "max": 255
            }
          }
        ]
      },
      {
        "name": "gender",
        "type": "GenderDTO",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.NotNull",
            "specifications": {}
          }
        ]
      },
      {
        "name": "givenName",
        "type": "String",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.NotBlank",
            "specifications": {}
          },
          {
            "name": "jakarta.validation.constraints.Size",
            "specifications": {
              "min": 0,
              "max": 255
            }
          }
        ]
      },
      {
        "constraints": [],
        "name": "id",
        "type": "String"
      },
      {
        "name": "phoneNumber",
        "type": "String",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.NotBlank",
            "specifications": {}
          },
          {
            "name": "jakarta.validation.constraints.Size",
            "specifications": {
              "min": 0,
              "max": 255
            }
          }
        ]
      },
      {
        "name": "familyName",
        "type": "String",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.NotBlank",
            "specifications": {}
          },
          {
            "name": "jakarta.validation.constraints.Size",
            "specifications": {
              "min": 0,
              "max": 255
            }
          }
        ]
      }
    ]
  }
]
~~~


___
