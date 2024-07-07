# Invoice Management API

## Features

### Pagination and sorting

Pagination and sorting are both supported. Clients that want to limit the result list of data items may use
the `page`, `sort` and `limit` query parameters.

- `page`: the page index.
- `sort`: a field name. To indicate the sorting direction, the field can be prefixed with either `+` (ascending)
  or `-` (descending).
- `limit`: the maximum number or items per page that needs to be included in the result list.

### Partial responses via filtering

In order to reduce payload size and reduce network bandwith need, the API supports partial responses via filtering.
Clients can explicitly determine the subset of fields they want to receive via the `fields` query parameter.

### Embedding of sub-resources

Clients that know upfront that they need related resources, can instruct the API to prefetch that data eagerly using
the `embed` query parameter. This type of request is optimized on the server, i.e. the database query is dinamically
constructed at runtime based on the value of the `embed` query parameter.

### Advanced query language

The API supports advanced query language based on query parameters. To apply an advance filter on the result list of
data items, clients may use the `search` query parameter.




