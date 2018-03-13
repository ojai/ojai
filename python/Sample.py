from ojai.document.Document import Document
from ojai.document.DocumentStore import DocumentStore
from ojai.store.ConnectionManager import ConnectionManager

"""Following example works with Python Client"""
"""Create a connection, get store, insert new document into store"""
# create a connection using path:user@password
connection = ConnectionManager.get_connection(url="hostname:port:user@password")

# Get a store and assign it as a DocumentStore object
store = connection.get_store(store_name="/test_name")

# Json string or json dictionary
json_string = '{"name": "Joe", "age": 50, "address": {"street": "555 Moon Way", "city": "Gotham"}}'

# Create new document from json_document
new_document = connection.new_document(json_string)

# Insert new document into the store
store.insert_or_replace(new_document)

# close
store.close()
connection.close()


"""Create a connection, get store, find document by id"""
# create a connection using path:user@password
connection = ConnectionManager.get_connection(url="hostname:port:user@password")

# Get a store and assign it as a DocumentStore object
store = connection.get_store(store_name="/test_name")

# Fetch document from store by id
json_document = store.find_by_id("id_005")

# close
store.close()
connection.close()


"""Create a connection, get store, create a query, execute find query and access through document stream"""
# create a connection using path:user@password
connection = ConnectionManager.get_connection(url="hostname:port:user@password")

# Get a store and assign it as a DocumentStore object
store = connection.get_store(store_name="/test_name")

# Build query
query = connection.new_query().select("_id", "city").build()

# fetch all document from store by query
document_stream = store.find_query(query)

# go to fetched records and print them
for document in document_stream.iterator():
    print(document)


# close
store.close()
connection.close()



