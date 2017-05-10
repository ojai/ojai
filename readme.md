# Open JSON Application Interface

OJAI is a general-purpose JSON access layer that sits on databases, file systems, and message streams which enables access to structured, semi-structured and unstructured data using a common API.

## Documents

The OJAI API specification is centered around documents that follow a JSON-like data model. Documents can describe entities such as products, people, and places, much more easily and in greater detail than can tables in relational databases. The greater flexibility and richness are due to the lack of schemas in documents, the ability to nest data within other data, the ability to create arrays, and in general the ability to range from simple to very complex data within a single document.

An OJAI document is a tree of fields. Each field has a type and a value, and also has either a name or an array index. Field names are strings. The root of each document is a map.

For example, an online retailer of sports equipment might have this OJAI document for storing data about a set of bicycle pedals:

    {
      "_id" : "2DT3201",
      "product_ID" : "2DT3201",
	  "name" : " Allegro SPD-SL 6800",
	  "brand" : "Careen",
	  "category" : "Pedals",
	  "type" : "Components,
	  "price" : 112.99,

	  "features" : [
		"Low-profile design",
		"Floating SH11 cleats included"
	  ],

	  "specifications" : {
		"weight_per_pair" : "260g",
		"color" : "black"
	  }
    }
The structure of each document, called the document's schema, is easy to change. Simply add new fields.    

For more about documents, see [Documents](https://github.com/ojai/ojai/wiki/1.-Documents) in the OJAI wiki.

## Interfaces for Working with Documents
The OJAI specification provides two types of APIs for creating, reading, and updating documents within the context of a client application: Document Object Model (DOM)-based APIs and event-driven APIs. 

### Document Object Model-based APIs

When a client application calls one of these APIs in the context of a particular `Document` object, a model of the document represented by the `Document` object is created in memory on the client. 

For example, suppose you had a document with this schema:

<pre>
{
  "a" : int,
  "b" : {
    "d" {
      "g" : "string",
      "h" : short,
      "i" : [long, long, long]
    },
    "e" : "string"
  },
  "c" : {
    "f" : "string"
  }
}
</pre>

The document, which itself is a map, contains a map at field `b`, a map at field `d`, and another map at field `c`. The document also contains an array at field `i`. 

An application calls a `set()` method to change the value of one of the elements in the array at `i`. The implementation can then locate the element and change its value.

Represented as a tree, the document would look like this, where the pairs of curly braces indicate that the node contains a map and the pair of straight braces indicate that the node contains an array:

![DOM tree](https://github.com/mutton4/mutton4.github.io/blob/master/dom_3.png)

When you use APIs that are based on document object models, applications insert fields and set values by using `set()` methods, and retrieve values by using `get()` methods. Applications specify fields in these methods by providing FieldPath objects. A field path is the name of each field in sequence that leads to the particular field that a `set()` or `get()` method to act on. The field names are separated by periods in a syntax called dotted notation.

For example, the following table lists the fields of this document and their field paths:

Field | Field Path 
------------ | ------------- 
a | a  | 
b | b  | 
c | c |
d | b.d |
e | b.e |
f | c.f |
g | b.d.g |
h | b.d.h |
i | b.d.i<br>Values in i:<br> b.d.i[0]<br> b.d.i[1]<br>b.d.i[2]|

Suppose the document were in a `Document` object named `exampleDoc`. To set the value of `g`, an application could call a `set()` method like this:

    exampleDoc.set("b.d.g", "new value");

There are two `set()` methods defined for every supported data type: one takes the field path as a `String` and the other takes the field path as a `FieldPath` object.

To retrieve the value of `g`, the application could call this `get()` method:

    exampleDoc.getString("b.d.g");

As with the `set()` methods, there are two `get()` methods defined for every supported data type: one takes the field path as a `String` and the other takes the field path as a `FieldPath` object. 

These DOM-based APIs are defined in the `Document` interface. Because they require document object models to be built in memory, they are useful for small or medium-sized documents.

### Event-driven APIs

Whereas the DOM-based APIs require a document to be represented as a tree in client memory, these APIs bypass that requirement by treating each document as a series of events. To illustrate this concept, let's look at the each of the two interfaces where these APIs are defined: `DocumentBuilder` and `DocumentReader`

#### `DocumentBuilder`

Rather than using `set()` methods to build a `Document` object and passing a field path into each `set()` method, applications that use a `DocumentBuilder` object create the fields in a `Document` object sequentially.

For example, let's look again at the sample document that we used to describe the DOM-based APIs:

<pre>
{
  "a" : int,
  "b" : {
    "d" {
      "g" : "string",
      "h" : short,
      "i" : [long, long, long]
    },
    "e" : "string"
  },
  "c" : {
    "f" : "string"
  }
}
</pre>

With a `DocumentBuilder`, an application would create this document in a `Document` object by following these steps:

1. Add a new map. The whole of any document is contained in a map.
2. Put the new field `a` and give it an integer value.
3. Put the new field `b` and make it a map.
4. Put the new field `d` and make it a map.
5. Put the new field `g` and give it a string value.
6. Put the new field `h` and give it a short value.
7. Put the new field `i` and make it an array.
8. Add three long values to the array.
9. End the array.
10. End the map that starts at field `d`.
11. Put the new field `e` and give it a string value.
12. End the map that starts at field `b`.
13. Put the new field `c` and make it a map.
14. Put the new field `f` and give it a string value.
15. End the map that starts at field `c`.
16. End the map that contains the document.

Because each method is called within the context of the current position in the document, OJAI implementations do not need to represent documents in memory as trees and applications do not need to provide field paths. 

#### `DocumentReader`

Rather than using get() methods to read from a `Document` object and passing a field path into each `get()` method, applications can move through documents with `DocumentReader` objects. As with `DocumentBuilder` objects, context of the current position in a document matters for `DocumentReader` objects. To a `DocumentReader`, each field:value pair, each start and end to a map, and each start and end to an array is an event. As an application uses a `DocumentReader` to read a document, the application moves from event to event by calling the `next()` method.

These are the different types of events that a `DocumentReader` can encounter:

Event Type | Constant 
------------ | ------------- 
Reading a node that has a scalar value | BINARY <br>BOOLEAN <br>BYTE <br>DATE <br>DECIMAL <br>DOUBLE <br>FLOAT <br>INT <br>INTERVAL <br>LONG <br>NULL <br>SHORT <br>STRING <br>TIME <br>TIMESTAMP   | 
Reading the start or end of an array | START_ARRAY<br>END_ARRAY  | 
Reading the start or end of a map | START_MAP<br>END_MAP |

For example, the following diagram is based on the same document that is used in the example for DOM-based APIs. On the left is a representation of the document as a document object model, while on the right is a representation of the document as a sequence of events. Remember that the representation on the right is only an illustration of the concept of event-driven APIs. OJAI implementations do not need to construct such representations in memory. Only one event at a time is held in memory. 

![Comparison](https://github.com/mutton4/mutton4.github.io/blob/master/events_7.png)

Suppose that an application attempts to read the first value that is in the array at `i`. If the application were to use a `get()` method (a DOM-based API) in a `Document` object, the OJAI implementation would create the document object model in memory. The application would pass the field path `b.d.i[0]` in the `get()` method. The implementation would then traverse the tree to the first element in the array and then return the value.

If the application were to use a `DocumentReader`, it would follow these steps:

1. Call `next()` to enter the document. The implementation would return the event at the first node in the document. That event is START_MAP.
2. Call `next()` to move to the next node. The implementation would return the field name and the event.
3. Repeat step 2 until arriving at the node that represents the first element in the array. Because the application would be tracking its location in the document on the basis of the events is has encountered and the names of the fields, the application would stop moving through the document after the implementation returns the value of the array element.

In this second scenario, the document is not constructed in memory ahead of time. Therefore, the events that appear in the diagram after the first array element never take place. The diagram represents all of the events that would arise if an application were to use a `DocumentReader` to read the entire document.

### `DocumentStream`

Document streams can contain one or more documents. There are three main ways that client applications can use them:

* Client applications can add documents to them as Document objects or by using `DocumentBuilder` objects, and then pass the loaded `DocumentStream` objects to document stores, where the documents can be inserted, replace existing documents, or be deleted. 
* Client applications receive results of queries in `DocumentStream` objects from document stores. To access the results, applications can call either the `DocumentStream.iterator()` method to get an `Iterator` of `Document` objects or the `DocumentStream.documentReaders()` method to get an `Iterable` of `DocumentReader` objects.
* Client applications can stream documents into them from input streams, using document listeners to receive documents that become available.

## Interfaces for Working with Document Stores
These interfaces define APIs that are run against document stores.

### `DocumentStore`

This interface defines APIs for inserting, replacing, querying, and deleting documents that are in document stores. It also defines some APIs for incrementing values in documents and one API for performing mutations on documents.

### `QueryCondition`

Several of the `find()` methods in `DocumentStore`, used for querying documents, take `QueryCondition` objects. The `QueryCondition` interface defines APIs that you can use to create search criteria for queries. `QueryCondition` objects are built from one condition, two or more conditions grouped into an AND block, two or more conditions grouped into an OR block, or a combination of these three elements.

A number of the APIs take a constant that defines the comparison between the value of a specified field and another value. These constants are defined by the enum `QueryCondition.Op`.

For example, a set of conditions could look similar to this one:

<pre>
is(<i>fieldpath</i>, EQUAL, <i>value</i>)
and()
     is(<i>fieldpath</i>, LESS_OR_EQUAL, <i>value</i>)
     like(<i>fieldpath</i>, <i>string</i>)
close()
</pre>

### `DocumentMutation`

This interface defines APIs for building mutations to perform on documents that are already in document stores. For example, it defines APIs to set values, set or replace values, append values, merge values, and increment values. These objects can then be passed to `DocumentStore.checkAndMutate()` and `DocumentStore.update()` to carry out the changes to specified documents.

## More Information
To learn how to compile applications that use the OJAI API specification and to get started with the reference implementation, see the OJAI [wiki](https://github.com/ojai/ojai/wiki)
