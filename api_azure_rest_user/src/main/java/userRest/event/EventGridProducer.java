package userRest.event;

import java.time.OffsetDateTime;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import userRest.model.User;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;


public class EventGridProducer {
    
    private final EventGridPublisherClient<EventGridEvent> eventGridPublisherClient;
    private final String eventGridTopicEndpoint = "https://g20-eventgrid-duocuc.eastus2-1.eventgrid.azure.net/api/events";
    private final String eventGridTopicKey = "4QLrThROPNgPIAuVW6QvaPkJuFl8F9SWkjjtU1pT04gES7FpzZaQJQQJ99BDACHYHv6XJ3w3AAABAZEGfNYs";
    // Constructor de la clase que configura el cliente de Event Grid
    public EventGridProducer() {
        
        // Crear el cliente de Event Grid usando el TokenCredential y el endpoint de Event Grid
        this.eventGridPublisherClient = new EventGridPublisherClientBuilder()
            .credential(new AzureKeyCredential(eventGridTopicKey))
            .endpoint(eventGridTopicEndpoint)  // Coloca tu Event Grid endpoint
            .buildEventGridEventPublisherClient();
    }

    // Método para enviar eventos a Event Grid
    public void sendEvent(String eventType, String resource, String userName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Convertir los datos del evento a un mapa
            Map<String, String> eventData = Map.of(
                "id", resource,
                "name", userName
            );
            
            // Convertir el mapa a JSON utilizando Jackson
            String eventDataJson = objectMapper.writeValueAsString(eventData);
            
            // Crear el BinaryData a partir del JSON generado
            BinaryData data = BinaryData.fromString(eventDataJson);

            // Crear el evento de EventGrid con los parámetros necesarios
            EventGridEvent event = new EventGridEvent(
                "User Event: " + eventType,  // subject
                eventType,                   // eventType
                data,                        // data (en formato BinaryData)
                "1.0"                        // dataVersion
            );
            
            event.setEventTime(OffsetDateTime.now()); // Establecer la hora del evento
            
            // Enviar el evento a Event Grid
            eventGridPublisherClient.sendEvent(event);
            System.out.println("Evento enviado a Event Grid: " + eventType);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al enviar el evento a Event Grid.");
        }
    }

    //Metodo para enviar datos de usuario por el event grid
    public void sendEvent(String eventType, User user) {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Convertir objeto User a JSON
        String eventDataJson = objectMapper.writeValueAsString(user);
        BinaryData data = BinaryData.fromString(eventDataJson);

        EventGridEvent event = new EventGridEvent(
            "User Event: " + eventType,
            eventType,
            data,
            "1.0"
        );

        event.setEventTime(OffsetDateTime.now());
        eventGridPublisherClient.sendEvent(event);
        System.out.println("Evento enviado a Event Grid: " + eventType);
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error al enviar el evento a Event Grid.");
    }
}
}
