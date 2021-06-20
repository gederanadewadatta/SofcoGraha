package com.dewadatta.test.repository;

import com.dewadatta.test.entity.Barang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PemesananRepository extends JpaRepository<Barang.PemesananBarang,String> {
    @Query(nativeQuery = true,value = "select max(id_seq) from pemesanan_barang")
    int maxIdSeq();
    @Query(nativeQuery = true,value = "select nextval('pemesanan_barang_id_seq_seq')")
    int nextIdSeq();
    @Modifying
    @Query(nativeQuery = true,value = "update pemesanan_barang set jumlah_barang = :jumlahBarang where id_seq = :nomorRegister")
    void  patchPemesananBarang(@Param("jumlahBarang") int jumlahBarang,@Param("nomorRegister") int nomorRegister);
    Barang.PemesananBarang findAllByIdSeq(int idSeq);
    @Query(nativeQuery = true,value = "select * from pemesanan_barang where nama_pemesan=:name")
    List<Barang.PemesananBarang> findByNamaPemesan(@Param("name") String name);

}
