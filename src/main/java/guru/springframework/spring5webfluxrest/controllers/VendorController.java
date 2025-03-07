package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping(BASE_URL)
    public Flux<Vendor> list(){
        return vendorRepository.findAll();
    }

    @GetMapping(BASE_URL + "/{id}")
    public Mono<Vendor> getById(@PathVariable String id){
        return vendorRepository.findById(id);
    }


    @PostMapping(BASE_URL)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream){
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping(BASE_URL + "/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping(BASE_URL + "/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor){

        Vendor foundVendor = vendorRepository.findById(id).block();

        if (!foundVendor.getFirstName().equals(vendor.getFirstName())) {
            foundVendor.setFirstName(vendor.getFirstName());

            return vendorRepository.save(foundVendor);
        }
        return Mono.just(foundVendor);
    }
}
