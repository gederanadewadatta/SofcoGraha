package com.dewadatta.test.repository;

import com.dewadatta.test.entity.Barang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PemesananRepository extends JpaRepository<Barang.PemesananBarang,String> {
    Barang.PemesananBarang findTopByOrderByIdSeqDesc();
    @Query(nativeQuery = true,value = "select nextval('pemesanan_barang_id_seq_seq')")
    int nextIdSeq();
    @Query(nativeQuery = true,value = "update pemesanan_barang set jumlah_barang = :jumlahBarang where id_seq = :nomorRegister")
    void patchPemesananBarang(@Param("jumlahBarang") String jumlahBarang,@Param("nomorRegister") int nomorRegister);
    Barang.PemesananBarang findAllByIdSeq(int idSeq);
    List<Barang.DTO> findAllByName(String name);
}
