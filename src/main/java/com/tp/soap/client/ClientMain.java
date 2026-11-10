package com.tp.soap.client;

import com.tp.soap.client.generated.Produit;
import com.tp.soap.client.generated.StockService;
import com.tp.soap.client.generated.StockService_Service;
import com.tp.soap.security.SecurityClientHandler;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.Handler;

import java.util.List;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("=== Initialisation du Client SOAP ===");

        // Instantiation of the generated service locator/proxy
        StockService_Service locator = new StockService_Service();
        StockService proxy = locator.getStockServiceImplPort();

        BindingProvider bp = (BindingProvider) proxy; // (replace 'proxy' with whatever your service variable is named)
        List<Handler> handlerChain = bp.getBinding().getHandlerChain();
        handlerChain.add(new SecurityClientHandler());
        bp.getBinding().setHandlerChain(handlerChain);

        // 1. Nominal operation test (Existing Product ID = 10)
        System.out.println("\n[Test 1] Recherche Produit existant (ID = 10) :");
        try {
            Produit p = proxy.chercherProduit(10L);
            if (p != null) {
                System.out.println("Produit : " + p.getNom() + " | Prix : " + p.getPrix() + " EUR");
            }
        } catch (Exception e) {
            System.err.println("Échec inattendu : " + e.getMessage());
        }

        // 2. Operation test triggering a SOAP Fault (Non-existent ID = 99)
        System.out.println("\n[Test 2] Recherche Produit inexistant (ID 99) :");
        try {
            proxy.chercherProduit(99L);
        } catch (Exception e) {
            System.out.println("Exception SOAP interceptée avec succès : " + e.getMessage());
        }


    }
}