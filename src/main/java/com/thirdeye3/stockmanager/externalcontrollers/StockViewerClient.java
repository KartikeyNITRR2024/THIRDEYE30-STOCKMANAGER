package com.thirdeye3.stockmanager.externalcontrollers;

import com.thirdeye3.stockmanager.LatestStockData;
import com.thirdeye3.stockmanager.dtos.Response;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockViewerClient {

    private final DiscoveryClient discoveryClient;
    private final WebClient webClient;

    public StockViewerClient(DiscoveryClient discoveryClient, WebClient.Builder webClientBuilder) {
        this.discoveryClient = discoveryClient;
        this.webClient = webClientBuilder.build();
    }

    public Response<List<LatestStockData>> getStockHistory(Long stockId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("THIRDEYE30-STOCKVIEWER");
        
        ParameterizedTypeReference<Response<List<LatestStockData>>> typeRef = 
                new ParameterizedTypeReference<>() {};

        return Flux.fromIterable(instances)
                .flatMap(instance -> {
                    String url = instance.getUri().toString() + "/sv/processing/details/" + stockId;
                    return webClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(typeRef)
                            .filter(res -> res != null && res.isSuccess() && res.getResponse() != null)
                            .onErrorResume(e -> Mono.empty()); 
                })
                .collectList() 
                .map(allResponses -> {
                    if (allResponses.isEmpty()) {
                        return new Response<List<LatestStockData>>(false, 404, "Stock history not found on any instance", null);
                    }

                    List<LatestStockData> mergedAndSortedList = allResponses.stream()
                            .flatMap(res -> res.getResponse().stream())
                            .sorted(Comparator.comparing(LatestStockData::getCurrentTime))
                            .collect(Collectors.toList());
                    return new Response<List<LatestStockData>>(true, 0, null, mergedAndSortedList);
                })
                .block(); 
    }
}
