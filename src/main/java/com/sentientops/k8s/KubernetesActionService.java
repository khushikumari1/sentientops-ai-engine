package com.sentientops.k8s;

import com.sentientops.exception.ApiException;
import com.sentientops.model.dto.K8sActionRequest;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class KubernetesActionService {

    public Map<String, Object> executeAction(K8sActionRequest request) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            return switch (request.getAction().toUpperCase()) {
                case "RESTART_POD" -> restartPod(client, request);
                case "SCALE_DEPLOYMENT" -> scaleDeployment(client, request);
                default -> throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Unknown action: " + request.getAction());
            };
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Kubernetes action failed: {}", e.getMessage());
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Kubernetes action failed: " + e.getMessage());
        }
    }

    private Map<String, Object> restartPod(KubernetesClient client, K8sActionRequest request) {
        var pods = client.pods()
                .inNamespace(request.getNamespace())
                .withLabel("app", request.getResourceName())
                .list().getItems();

        if (pods.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "No pods found for: " + request.getResourceName());
        }

        // Delete first pod to trigger restart
        String podName = pods.get(0).getMetadata().getName();
        client.pods().inNamespace(request.getNamespace()).withName(podName).delete();
        log.info("Restarted pod {} in namespace {}", podName, request.getNamespace());

        return Map.of("action", "RESTART_POD", "pod", podName, "status", "restarting");
    }

    private Map<String, Object> scaleDeployment(KubernetesClient client, K8sActionRequest request) {
        int replicas = request.getReplicas() != null ? request.getReplicas() : 2;

        client.apps().deployments()
                .inNamespace(request.getNamespace())
                .withName(request.getResourceName())
                .scale(replicas);

        log.info("Scaled deployment {} to {} replicas", request.getResourceName(), replicas);
        return Map.of("action", "SCALE_DEPLOYMENT", "deployment", request.getResourceName(),
                "replicas", replicas, "status", "scaled");
    }
}
