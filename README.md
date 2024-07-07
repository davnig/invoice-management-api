# Invoice Management API

## Features

### Pagination and sorting

Pagination and sorting are both supported. Clients that want to limit the result list of data items may use
the `page`, `sort` and `limit` query parameters.

- `page`: the page index.
- `sort`: a field name. To indicate the sorting direction, the field can be prefixed with either `asc-` (ascending)
  or `desc-` (descending). By default, an ascending order will be userd.
- `limit`: the maximum number or items per page that needs to be included in the result list.

Check the [example section](#pagination-and-sorting-examples) for more details.

### Partial responses via filtering

In order to reduce payload size and reduce network bandwith need, the API supports partial responses via filtering.
Clients can explicitly determine the subset of fields they want to receive via the `fields` query parameter.

Check the [example section](#partial-responses-examples) for more details.

### Embedding of sub-resources

Clients that know upfront that they need related resources, can instruct the API to prefetch that data eagerly using
the `embed` query parameter. This type of request is optimized on the server, i.e. the database query is dinamically
constructed at runtime based on the value of the `embed` query parameter.

Check the [example section](#sub-resource-embedding-examples) for more details.

### Advanced query language

The API supports advanced query language based on query parameters. To apply an advance filter on the result list of
data items, clients may use the `search` query parameter.

Check the [example section](#advanced-query-language-examples) for more details.

### Examples

#### Pagination and sorting examples

```http request
GET /invoices?limit=3&page=1&sort=desc-date
```
```json
{
  "totalElements": 7,
  "totalPages": 3,
  "first": false,
  "last": false,
  "size": 3,
  "content": [
    {
      "number": 7,
      "date": "2022-02-27 17:21:00.0",
      "idVendor": 1,
      "idClient": 3
    },
    {
      "number": 1,
      "date": "2021-03-20 10:01:00.0",
      "idVendor": 1,
      "idClient": 1
    },
    {
      "number": 6,
      "date": "2021-01-21 16:10:00.0",
      "idVendor": 1,
      "idClient": 2
    }
  ],
  "number": 1,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 3,
  "pageable": {
    "pageNumber": 1,
    "pageSize": 3,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 3,
    "paged": true,
    "unpaged": false
  },
  "empty": false
}
```

#### Partial responses examples

```http request
GET /taxable-subjects?fields=piva
```
```json
{
  "totalElements": 4,
  "totalPages": 2,
  "first": true,
  "last": false,
  "size": 3,
  "content": [
    {
      "piva": "0764352056C"
    },
    {
      "piva": "398G1380921"
    },
    {
      "piva": "740947406B5"
    }
  ],
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 3,
  "pageable": {
    "pageNumber": 0,
    "pageSize": 3,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "empty": false
}
```

#### Sub-resource embedding examples

```http request
GET /invoices/7?embed=client
```
```json
{
  "number": 7,
  "date": "2022-02-27 17:21:00.0",
  "idVendor": 1,
  "client": {
    "taxCode": "BSCRFA92D01C149G",
    "name": "Arif Buscato",
    "address": "via Trieste 2"
  }
}
```

#### Advanced query language examples

```http request
GET /invoices?search=idClient:3
```
```json
{
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "size": 3,
  "content": [
    {
      "number": 7,
      "date": "2022-02-27 17:21:00.0",
      "idVendor": 1,
      "idClient": 3
    }
  ],
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 1,
  "pageable": {
    "pageNumber": 0,
    "pageSize": 3,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "empty": false
}
```

## ToDo

- [ ] Extend advanced query search language
- [ ] Add tests
- [ ] Add support for `embed` and `fields` everywhere



