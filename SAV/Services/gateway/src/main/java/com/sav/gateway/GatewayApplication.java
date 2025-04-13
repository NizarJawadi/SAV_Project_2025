package com.sav.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.WebFluxConfigurer;



@SpringBootApplication
public class GatewayApplication implements WebFluxConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				// Route pour /auth/**
				.route("authentification",
						r -> r.path("/auth/**")
								.or()
								.path("/client/**")
								.or()
								.path("/technicien/**")
								.or()
								.path("/ResponsableSav/**")

								//.filters(f -> f.addRequestHeader("Access-Control-Allow-Origin", "*"))
						.uri("http://localhost:9090")
				)


				.route("Reclamtion-service", r -> r.path("/reclamations/**")
						.uri("http://localhost:8060")
				)


				.route("PieceDeRechange-service", r -> r.path("/pieces/**")
						.uri("http://localhost:8110")
				)


				.route("interventions-service", r -> r.path("/interventions/**")
						.uri("http://localhost:8050")
				)
				// Route pour /produits/**
				.route("produit-service", r -> r.path("/produits/**")
						.uri("http://localhost:9999")
				)

				// Route pour /historique/**
				.route("HistoriqueAchat", r -> r.path("/historique/**")
						.uri("http://localhost:8070")
				)

				// Route pour /categories/**
				.route("categorie-service", r -> r.path("/categories/**")
						.uri("http://localhost:9988")
				)

				.build();
	}


}
