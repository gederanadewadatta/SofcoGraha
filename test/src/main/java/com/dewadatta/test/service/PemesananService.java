package com.dewadatta.test.service;

import com.dewadatta.test.entity.Barang;
import com.dewadatta.test.repository.PemesananRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class PemesananService {

    @Autowired
    PemesananRepository pemesananRepository;
    public Barang.Response createPemesananBarang(Barang.Request requestData) {
        Barang.Response dataResponse = null;
        try{
            pemesananRepository.save(toDBObject(requestData));
            Barang.PemesananBarang latestData= pemesananRepository.findTopByOrderByIdSeqDesc();
            dataResponse.setResponseMessage("Pesanan telah diterima dengan nomor register "+latestData.getIdSeq());
        } catch (Exception e){
            log.error("Error   Create Pemesanan Barang Process : "+e,e);
            throw e;
        }
        return dataResponse;
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
        return data;
    }


    public Barang.Response patchPemesananBarang(Barang.RequestPatch requestData) {
        Barang.Response dataResponse = null;
        try{
            pemesananRepository.patchPemesananBarang(requestData.getJumlahBarang(),requestData.getNomorRegister());
            Barang.PemesananBarang latestData= pemesananRepository.findAllByIdSeq(requestData.getNomorRegister());
            dataResponse.setResponseMessage("Pesanan telah diubah dengan nomor register "+latestData.getIdSeq());
        } catch (Exception e){
            log.error("Error Patching Pemesanan Barang Process : "+e,e);
            throw e;
        }
        return dataResponse;
    }

    public List<Barang.DTO> getPemesananBarangByName(String name) {
        List<Barang.DTO> listBarang = null;
        try{
            listBarang = pemesananRepository.findAllByName(name);
        }catch (Exception e){
            log.error("Data Not Found: "+e,e);
            throw e;
        }
        return listBarang;
    }
}
