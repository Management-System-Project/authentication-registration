package store.glacies.authentication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import store.glacies.authentication.sevices.AuthenticationService;

@Controller
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/authenticate")
    public void authenticate(@RequestBody String code) {
        service.loginWithFederatedProvider(code);
    }


    @PostMapping("/seller/register")
    public void registerSellerWithFederatedProviders(@RequestBody String code) {
        service.registerOptimized(code, "Sellers");
    }

    @PostMapping("/customer/register")
    public void registerCustomerWithFederatedProviders(@RequestBody String code) {
        service.registerOptimized(code, "Customers");
    }
}
