package wercsmik.spaghetticodingclub.global;

import io.awspring.cloud.core.config.AwsPropertySource;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathRequest;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ParameterStorePropertySource extends AwsPropertySource<ParameterStorePropertySource, SsmClient> {

    private final Map<String, Object> properties = new LinkedHashMap<>();
    private String prefix;

    public ParameterStorePropertySource(String name, SsmClient source) {
        super(name, source);
    }

    private void getParameters(GetParametersByPathRequest paramsRequest) {
        GetParametersByPathResponse paramsResult = ((SsmClient) this.source).getParametersByPath(paramsRequest);

        for (Parameter parameter : paramsResult.parameters()) {
            String key = parameter.name();
            String propertyKey = this.prefix != null ? this.prefix + key : key;
            this.properties.put(propertyKey, parameter.value());
        }

        if (paramsResult.nextToken() != null) {
            this.getParameters((GetParametersByPathRequest) paramsRequest.toBuilder().nextToken(paramsResult.nextToken()).build());
        }

    }
}