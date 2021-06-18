CREATE TABLE public.pemesanan_barang
(
    id character varying(36)  ,
    id_seq int4 DEFAULT nextval('pemesanan_barang_id_seq_seq'::regclass),
    nama_barang character varying(255)  ,
    keterangan_pemesanan character varying(255) ,
    jumlah_barang int,
    nama_pemesan character varying(255) ,
    alamat_pemesan character varying(255)  ,
    tanggal_pemesanan timestamptz  ,
    CONSTRAINT pemesanan_barang _pkey PRIMARY KEY (id)


);