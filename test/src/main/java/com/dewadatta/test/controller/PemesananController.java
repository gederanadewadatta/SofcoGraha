package com.dewadatta.test.controller;

import com.dewadatta.test.entity.Barang;
import com.dewadatta.test.exception.GlobalExceptionHandler;
import com.dewadatta.test.service.PemesananService;
import com.dewadatta.test.util.Response;
import com.dewadatta.test.util.RestApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pemesanan")
public class PemesananController {

    @Value("${spring.application.name}")
    String applicationName;

    @Autowired
    private PemesananService pemesananService;

    @Autowired
    RestApiUtil restApiUtil;
    @PostMapping("/create")
    public Response<Object> createPemesananBarang(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) throws Exception {
        try {
            Barang.Request requestData = restApiUtil.requestToObject(httpRequest, Barang.Request.class);
            pemesananService.createPemesananBarang(requestData);
            int latestNomorRegistrasi = pemesananService.getLatestIdSeq();
            Barang.Response responseData = new Barang.Response();
            responseData.setResponseMessage("Pesanan telah diterima dengan nomor register "+latestNomorRegistrasi);
                    log.debug("Create Pemesanan Barang Response: "+responseData);
            return Response.status(httpServletResponse, HttpStatus.OK, responseData);
        }catch (Exception e){
            log.error("Error Process Create Pemesanan Barang : " + e, e);
            return GlobalExceptionHandler.ChekingExceptionGlobal(applicationName, httpServletResponse, e);
        }
    }

    @PatchMapping("/patch/")
    public Response<Object> patchPemesananBarang(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) throws Exception {
        try {
            Barang.RequestPatch requestData = restApiUtil.requestToObject(httpRequest, Barang.RequestPatch.class);

             pemesananService.patchPemesananBarang(requestData);
            Barang.Response  responseData = new Barang.Response();
            responseData.setResponseMessage("Jumlah Barang dengan nomor registrasi "+ requestData.getNomorRegister()+" telah diubah");
            log.debug("Patch Pemesanan Barang Response: "+responseData);
            return Response.status(httpServletResponse, HttpStatus.OK, responseData);
        }catch (Exception e){
            log.error("Error Process Patch Pemesanan Barang : " + e, e);
            return GlobalExceptionHandler.ChekingExceptionGlobal(applicationName, httpServletResponse, e);

        }
    }

    @GetMapping("/get/{name}")
    public Response<Object> getPemesananBarangByName(@PathVariable("name") String name, HttpServletResponse httpServletResponse) throws Exception {
        try {
            List<Barang.DTO> responseData = pemesananService.getPemesananBarangByName(name);
            log.debug("Get Pemesanan Barang Response: "+responseData);
            return Response.status(httpServletResponse, HttpStatus.OK, responseData);
        }catch (Exception e){
            log.error("Error Process Get Pemesanan Barang : " + e, e);
            return GlobalExceptionHandler.ChekingExceptionGlobal(applicationName, httpServletResponse, e);

        }
    }
}
