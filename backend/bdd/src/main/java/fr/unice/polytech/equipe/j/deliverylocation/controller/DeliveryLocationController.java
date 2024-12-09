package fr.unice.polytech.equipe.j.deliverylocation.controller;

import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.deliverylocation.dao.DeliveryLocationDAO;
import fr.unice.polytech.equipe.j.deliverylocation.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.deliverylocation.entities.DeliveryLocationEntity;
import fr.unice.polytech.equipe.j.deliverylocation.mapper.DeliveryLocationMapper;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;

import java.util.List;
import java.util.UUID;

@Controller("/api/delivery-locations")
public class DeliveryLocationController {
    @Route(value = "", method = HttpMethod.GET)
    public HttpResponse getDeliveryLocations() {
        List<DeliveryLocationEntity> deliveryLocationDTOS = DeliveryLocationDAO.getAllDeliveryLocations();
        List<DeliveryLocationDTO> deliveryLocationEntities = deliveryLocationDTOS.stream().map(DeliveryLocationMapper::toDTO).toList();
        return new HttpResponse(HttpCode.HTTP_200, deliveryLocationEntities);
    }

    @Route(value = "/{deliveryLocationId}", method = HttpMethod.GET)
    public HttpResponse getDeliveryLocation(@PathParam("deliveryLocationId") UUID deliveryLocationId) {
        DeliveryLocationEntity deliveryLocationEntity = DeliveryLocationDAO.getDeliveryLocationById(deliveryLocationId);
        if (deliveryLocationEntity == null) {
            return new HttpResponse(HttpCode.HTTP_404, "Delivery location not found");
        }
        DeliveryLocationDTO deliveryLocationDTO = DeliveryLocationMapper.toDTO(deliveryLocationEntity);
        return new HttpResponse(HttpCode.HTTP_200, deliveryLocationDTO);
    }
}
