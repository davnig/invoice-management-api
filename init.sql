-- SCHEMA --

CREATE TABLE t_profitability_coefficient
(
    id          serial,
    coefficient decimal NOT NULL,
    description varchar(256) NULL,
    CONSTRAINT PK_t_profitability_coefficient PRIMARY KEY (id)
);

CREATE TABLE t_social_security
(
    id   serial,
    name varchar(128) NOT NULL,
    rate decimal      NOT NULL,
    CONSTRAINT PK_t_social_security PRIMARY KEY (id)
);

CREATE TABLE t_flat_rate_scheme
(
    id   serial,
    name varchar(128) NOT NULL,
    rate decimal      NOT NULL,
    CONSTRAINT PK_t_flat_rate_scheme PRIMARY KEY (id)
);

CREATE TABLE t_tax_profile
(
    id                      serial,
    id_prof_coefficient     int NULL,
    id_social_security_fund int NULL,
    id_flat_rate_scheme     int NULL,
    CONSTRAINT PK_t_tax_profile PRIMARY KEY (id),
    CONSTRAINT FK_t_tax_profile_id_prof_coefficient FOREIGN KEY (id_prof_coefficient) REFERENCES t_profitability_coefficient (id),
    CONSTRAINT FK_t_tax_profile_id_social_security_fund FOREIGN KEY (id_social_security_fund) REFERENCES t_social_security (id),
    CONSTRAINT FK_t_tax_profile_id_flat_rate_scheme FOREIGN KEY (id_flat_rate_scheme) REFERENCES t_flat_rate_scheme (id)
);

CREATE TABLE t_taxable_subject
(
    id             serial,
    p_iva          varchar(11)  NOT NULL,
    first_name     varchar(128) NOT NULL,
    last_name      varchar(128) NOT NULL,
    tax_code       varchar(16) NULL,
    id_tax_profile int          NOT NULL,
    CONSTRAINT PK_t_taxable_subject PRIMARY KEY (id),
    CONSTRAINT FK_t_taxable_subject_id_tax_profile FOREIGN KEY (id_tax_profile) REFERENCES t_tax_profile (id)
);

CREATE TABLE t_client
(
    id       serial,
    tax_code varchar(16)  NOT NULL,
    name     varchar(128) NOT NULL,
    address  varchar(255) NOT NULL,
    CONSTRAINT PK_t_client PRIMARY KEY (id)
);

CREATE TABLE t_invoice
(
    id            serial,
    number        int       NOT NULL,
    emission_date timestamp NOT NULL,
    id_vendor     int       NOT NULL,
    id_client     int       NOT NULL,
    CONSTRAINT PK_t_invoice PRIMARY KEY (id),
    CONSTRAINT FK_t_invoice_id_vendor FOREIGN KEY (id_vendor) REFERENCES t_taxable_subject (id),
    CONSTRAINT FK_t_invoice_id_client FOREIGN KEY (id_client) REFERENCES t_client (id)
);

CREATE TABLE t_invoice_item_type
(
    id   serial,
    name varchar(255) NOT NULL,
    CONSTRAINT PK_t_invoice_item_type PRIMARY KEY (id)
);

CREATE TABLE t_invoice_item
(
    id          serial,
    description varchar(255) NOT NULL,
    quantity    int          NOT NULL,
    price       int          NOT NULL,
    id_type     int          NOT NULL,
    id_invoice  int          NOT NULL,
    CONSTRAINT PK_t_invoice_item PRIMARY KEY (id),
    CONSTRAINT FK_t_invoice_item_id_type FOREIGN KEY (id_type) REFERENCES t_invoice_item_type (id),
    CONSTRAINT FK_t_invoice_item_id_invoice FOREIGN KEY (id_invoice) REFERENCES t_invoice (id)
);

-- DATA --

INSERT INTO t_social_security
VALUES (default, 'gestione separata INPS', 0.2623),
       (default, 'inarcassa', 0.1450);

INSERT INTO t_profitability_coefficient
VALUES (default, 0.40,
        'alloggio e ristorazione, commercio al dettaglio e all''ingrosso, commercio di alimenti e bevande, industrie alimentari e delle bevande'),
       (default, 0.54, 'commercio ambulante non alimentare'),
       (default, 0.62, 'intermediari del commercio'),
       (default, 0.67, 'altre attività economiche'),
       (default, 0.78,
        'attività professionali, scientifiche, tecniche, sanitarie di istruzione, servizi finanziari e assicurativi'),
       (default, 0.86, 'costruzione e attività immobiliari');

INSERT INTO t_flat_rate_scheme
VALUES (default, 'ordinario', 0.15),
       (default, 'startup', 0.05);

INSERT INTO t_tax_profile
VALUES (default, 4, 1, 2);

INSERT INTO t_taxable_subject
VALUES (default, '0764352056C', 'Davide', 'Rossi', 'DVDRSS91A01L424G', 1),
       (default, '398G1380921', 'Marco', 'Stussi', 'STSMRC91B20A074D', 1),
       (default, '740947406B5', 'Giovanni', 'Rosato', 'RSTGNN91P20L716V', 1),
       (default, '17476T80843', 'Federica', 'Amabile', 'MBLFRC92E51H502W', 1);

INSERT INTO t_invoice_item_type
VALUES (default, 'Servizio'),
       (default, 'Prodotto');

INSERT INTO t_client
VALUES (default, 'MLSRRP70B01E256X', 'Ottica S.p.a.', 'via Milano 4'),
       (default, 'RSTPRZ09L41A588Q', 'Maria Luisa Ristagno', 'via Roma 5'),
       (default, 'BSCRFA92D01C149G', 'Arif Buscato', 'via Trieste 2'),
       (default, 'SVDMLR33P01M003C', 'Emanuele Rocco Savreda', 'via Bologna 1'),
       (default, 'GNLMLN50S41F898M', 'Marlene Gioanola', 'via Sturari 12');

INSERT INTO t_invoice
VALUES (default, 1, '2021-03-20 10:01', 1, 1),
       (default, 2, '2022-04-21 12:21', 1, 1),
       (default, 3, '2022-05-21 13:24', 1, 1),
       (default, 4, '2022-09-21 16:32', 1, 1),
       (default, 5, '2021-01-21 12:09', 1, 2),
       (default, 6, '2021-01-21 16:10', 1, 2),
       (default, 7, '2022-02-27 17:21', 1, 3);

INSERT INTO t_invoice_item
VALUES (default, 'Consulenza informatica', 1, 500, 1, 1),
       (default, 'Collaborazione Aprile 2022', 1, 3500, 1, 2),
       (default, 'Collaborazione Marzo 2022', 1, 3250, 1, 2),
       (default, 'Collaborazione Febbraio 2022', 1, 3300, 1, 2),
       (default, 'Progetto sito web', 1, 5000, 1, 3),
       (default, 'Progetto API', 1, 8200, 1, 4),
       (default, 'Revisione sito web', 1, 600, 1, 5),
       (default, 'Realizzazione sito web', 1, 1500, 1, 6),
       (default, 'Configurazione applicativo', 1, 200, 1, 7);
