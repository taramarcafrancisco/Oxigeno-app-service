package com.oxigeno.portal.jobs;

import com.oxigeno.portal.services.UsuarioPlanService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPlanScheduler {

    private final UsuarioPlanService usuarioPlanService;

    public UsuarioPlanScheduler(UsuarioPlanService usuarioPlanService) {
        this.usuarioPlanService = usuarioPlanService;
    }

//    @Scheduled(cron = "0 0 1 * * *")
//    public void desactivarPlanesVencidos() {
//        usuarioPlanService.actualizarPlanesVencidos();
//    }
}