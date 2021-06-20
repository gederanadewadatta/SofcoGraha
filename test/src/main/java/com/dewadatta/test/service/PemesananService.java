package com.dewadatta.test.service;

import com.dewadatta.test.entity.Barang;
import com.dewadatta.test.repository.PemesananRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class PemesananService {
    @Autowired
    private PemesananRepository pemesananRepository;



    public void createPemesananBarang(Barang.Request requestData) {
        try{
            pemesananRepository.save(toDBObject(requestData));
        } catch (Exception e){
            log.error("Error   Create Pemesanan Barang Process : "+e,e);
            throw e;
        }
        log.info("Success insert Pemesanan Barang with nama barang:"+requestData.getNamaBarang());
    }

    private Barang.PemesananBarang toDBObject(Barang.Request requestData){
        Barang.PemesananBarang data = new Barang.PemesananBarang();
        data.setId(UUID.randomUUID().toString());
        data.setIdSeq(pemesananRepository.nextIdSeq());
        data.setAlamatPemesan(requestData.getAlamatPemesan());
        data.setJumlahBarang(requestData.getJumlahBarang());
        data.setKeteranganPemesanan(requestData.getKeteranganPemesanan());
        data.setNamaBarang(requestData.getNamaBarang());
        data.setNamaPemesan(requestData.getNamaPemesan());
        data.setTanggalPemesanan(requestData.getTanggalPemesanan());
        log.info("Data Save:"+data);
        return data;
    }


    public void patchPemesananBarang(Barang.RequestPatch requestData) {

        try{
             pemesananRepository.patchPemesananBarang(requestData.getJumlahBarang(),requestData.getNomorRegister());
//            Barang.PemesananBarang latestData= pemesananRepository.findAllByIdSeq(requestData.getNomorRegister());
            log.info("Pesanan telah diubah dengan nomor register "+requestData.getNomorRegister());
        } catch (Exception e){
            log.error("Error Patching Pemesanan Barang Process : "+e,e);
            throw e;
        }

    }

    public List<Barang.DTO> getPemesananBarangByName(String name) {

        List<Barang.PemesananBarang>  listBarang = null;
        try{
            listBarang = pemesananRepository.findByNamaPemesan(name);
            log.info("Daftar Barang untuk:"+name+" adalah :" +listBarang.toString());
        }catch (Exception e){
            log.error("Data Not Found: "+e,e);
            throw e;
        }
        return toDTO(listBarang);
    }

    private List<Barang.DTO> toDTO(List<Barang.PemesananBarang> listBarang) {
        Barang.DTO dtoResponse = new Barang.DTO();
        List<Barang.DTO> dtoList = new ArrayList<Barang.DTO>();
        for (int i = 0; i < listBarang.size(); i++) {
            dtoResponse.setNomorRegistrasi(listBarang.get(i).getIdSeq());
            dtoResponse.setNamaBarang(listBarang.get(i).getNamaBarang());
            dtoResponse.setJumlahBarang(listBarang.get(i).getJumlahBarang());
            dtoResponse.setNamaPemesan(listBarang.get(i).getNamaPemesan());
            dtoResponse.setAlamatPemesan(listBarang.get(i).getAlamatPemesan());
            dtoResponse.setTanggalPemesanan(listBarang.get(i).getTanggalPemesanan());
            dtoResponse.setKeteranganPemesanan(listBarang.get(i).getKeteranganPemesanan());
            dtoList.add(dtoResponse);
        }
        return dtoList;
    }

    public int getLatestIdSeq() {
        return pemesananRepository.maxIdSeq();
    }
}
