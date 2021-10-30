# Swagger Generator
## What is Swagger Generator?
 Swagger Generator is a java based tool to generate swagger from a predefined Excel.
 Steps to generate the Swagger file

## Installation

Swagger Generator requires [Java 8] (https://www.oracle.com/java/technologies/downloads/) 1.8 or above to run.
- Check out the maven project from github repo
 - Update file.path properties in the application.properties
```sh
mvn clean install
```

start the server...

```sh
java -jar Swaggergenerator-api-<version>.jar
```
## How to Generate Swagger?
- Download the sample excel template from repo
- Make a post call to
```sh
http://localhost:8080/generateswagger
inputs : 
Key details
{
  "title": "API tile",
  "description": "API Description",
  "apiVersion": "1.3.0",
  "apiPath": "/apiPath",
  "apiDesc": "API Description",
  "apiSummary": "Api to fetch vehicle invoices\n",
  "serverUrl": "server Url",
  "requestMethod": "get",
  "contactName": "Contact Name ",
  "contactEmail": "email"
}
```
update the sample excel downloaded
- The response from API is json.
- Copy it to [Online Swagger editor] (https://editor.swagger.io/) to convert to YAML files.

###### please note there are some assumptions made on the API request and response structure and its a Beta version
