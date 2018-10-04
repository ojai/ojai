from abc import ABCMeta, abstractmethod
from future.utils import with_metaclass


class DocumentStore(with_metaclass(ABCMeta, object)):
    @abstractmethod
    def find_by_id(self, _id, field_paths=None, condition=None, result_as_document=False):
        """Returns the document with the specified `_id` or None if the document with that `_id` either doesn't exist
        in this DocumentStore or does not meet the specified condition.
        When 'result_as_document' flag is set to False (default), the document is returned as Python dictionary
        and when True, the document is returned as an object of OJAI Document class.
        :param _id: Document id. Type may be str, Value.
        :param field_paths: list of fields that should be returned in the read document.
        :param condition: query condition to test the document
        :param result_as_document: if True, the document is returned as object of OJAI Document class.
        :raises StoreError
        :return an OJAI document with the specified _id"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def find(self, query, options=None):
        """Returns a QueryResult with all the documents from this
        DocumentStore that match the specified query criteria.
        When 'ojai.mapr.query.result-as-document' flag is set to False
        (default), the documents are returned as Python dictionaries and when
        True, the documents are returned as objects of OJAI Document class.
        :param query: OJAI Query.
        :param options: set of options for find method.
        :raises StoreError
        :return Method returns an object of QueryResult containing OJAI documents"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def insert_or_replace(self, doc=None, _id=None, field_as_key=None, doc_stream=None):
        """Inserts or replaces a new document in this DocumentStore with the given _id.
        :param doc: the Document or json dictionary to be inserted or replaced in the DocumentStore.
        :param _id: value to be used as the _id for this document.
        :param field_as_key: document's field to be used as the key when an id is not passed in and the
        document doesn't have an "_id" field or a different field is desired to be used as _id.
        :param doc_stream: the DocumentStream to read the documents from.
        :raises StoreError"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def update(self, _id, mutation):
        """Applies a mutation on the document identified by the document id.
        All updates specified by the mutation object should be applied atomically,
        and consistently meaning either all of the updates in mutation are applied
        or none of them is applied and a partial update should not be visible to an
        observer.
        :param _id: document id. Type may be str, Value.
        :param mutation: a mutation object specifying the mutation operations on the document. Type DocumentMutation.
        :raises StoreError"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def delete(self, doc=None, _id=None, field_as_key=None, doc_stream=None):
        """Deletes a document with the given id or set of documents represent by stream.
        This operation is successful even when the document with the given id doesn't exist.
        :param doc: the Document or json dictionary to be deleted.
        :param _id: document id.
        :param field_as_key: document's field to be used as the key when an id is not
        passed in and a document doesn't have an "_id" field.
        If the parameter field_as_key is provided, its value will be used as
        the "_id" to delete the document.
        :param doc_stream: DocumentStream.
        :raises StoreError"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def insert(self, doc=None, _id=None, field_as_key=None, doc_stream=None):
        """Inserts a document with the given id or documents represent by the DocumentStream.
        This operation is successful only when the document with the given id doesn't exist.
        If "_id" already existed in the document, then an error will be thrown.
        :param doc: the Document or json dictionary to be inserted in the DocumentStore.
        :param _id: to be used as the key for the document.
        :param field_as_key: document's field to be used as the key when the id is not
        passed in and document doesn't have an "_id" field.
        :param doc_stream: DocumentStream.
        :raises StoreError"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def replace(self, doc=None, _id=None, field_as_key=None, doc_stream=None):
        """Replaces a document or set of documents represented by the DocumentStream in the DocumentStore.
        The document id is either explicitly specified as parameter "id" or it is implicitly specified as the
        field "_id" in the passed document. If the document id is explicitly passed then the document should
        not contain "_id" field or its value should be the same as the explicitly
        specified id; otherwise, the operation will  fail.
        :param doc: the Document or json dictionary to be replaced in the DocumentStore.
        :param _id: to be used as the key for the document.
        :param field_as_key: document's field to be used as the key when the id is not
        passed in and document doesn't have an "_id" field.
        :param doc_stream: DocumentStream.
        :raises StoreError"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def increment(self, _id, field, inc):
        """Atomically applies an increment to a given field (in dot separated notation)
        of the given document id. If the field doesn't exist on the server
        then it will be created with the type of the incremental value.
        The increment operation can be applied on any of the numeric
        types, such as byte, int, long or float,
        of a field. The operation will fail if the increment is applied to a
        field that is of a non-numeric type.
        :param _id: document id.
        :param field: the field name in dot separated notation.
        :param inc: increment to apply to a field. Can be positive or negative."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def check_and_update(self, _id, query_condition, mutation):
        """Atomically evaluates the condition on a given document and if the
        condition holds true for the document then a mutation is applied on the document.
        :param _id: document id.
        :param query_condition: the condition to evaluate on the document.
        :param mutation: mutation to apply on the document."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def check_and_delete(self, _id, condition):
        """Atomically evaluates the condition on given document and if the
        condition holds true for the document then it is atomically deleted.
        :param _id: document id.
        :param condition: the condition to evaluate on the document."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def check_and_replace(self, _id, condition, doc):
        """ Atomically evaluates the condition on the given document and if the
        condition holds true for the document then it atomically replaces the document
        with the given document.
        :param _id: document id.
        :param condition: the condition to evaluate on the document."""
        raise NotImplementedError("Should have implemented this")
