package com.dewadatta.test.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Slf4j
@Data
public class Barang {
    @Data
    public static class RequestPatch{
        @JsonProperty("nomorRegister")
        private int nomorRegister;
        @JsonProperty("jumlahBarang")
        private String jumlahBarang;
    }
    @Data
    public static class Request{
        @JsonProperty("namaBarang")
        private String namaBarang;
        @JsonProperty("keteranganPemesanan")
        private String keteranganPemesanan;
        @JsonProperty("jumlahBarang")
        private String jumlahBarang;
        @JsonProperty("namaPemesan")
        private String namaPemesan;
        @JsonProperty("alamatPemesan")
        private String alamatPemesan;
        @JsonProperty("tanggalPemesanan")
        private Date tanggalPemesanan;
    }
    @Data
    public static class Response {
        private String responseMessage;
    }

    @Data
    @Entity
    @Table(name = "pemesanan_barang")
    public static class PemesananBarang{
        @Id
        @Column(name = "id")
        private String id;
        @Column(name = "id_seq")
        private int idSeq;
        @Column(name = "nama_barang")
        private String namaBarang;
        @Column(name = "keterangan_pemesanan")
        private String keteranganPemesanan;
        @Column(name = "jumlah_barang")
        private String jumlahBarang;
        @Column(name = "nama_pemesan")
        private String namaPemesan;
        @Column(name = "alamat_pemesan")
        private String alamatPemesan;
        @Column(name = "tanggal_pemesanan")
        private Date tanggalPemesanan;


    }
    @Data
    public class DTO {
        @JsonProperty("namaBarang")
        private String namaBarang;
        @JsonProperty("keteranganPemesanan")
        private String keteranganPemesanan;
        @JsonProperty("jumlahBarang")
        private String jumlahBarang;
        @JsonProperty("namaPemesan")
        private String namaPemesan;
        @JsonProperty("alamatPemesan")
        private String alamatPemesan;
        @JsonProperty("tanggalPemesanan")
        private Date tanggalPemesanan;
    }
}
